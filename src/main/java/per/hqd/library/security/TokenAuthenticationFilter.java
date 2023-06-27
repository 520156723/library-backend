package per.hqd.library.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 17:15
 */
@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String accessToken = request.getHeader("access-token");
        if (Objects.isNull(accessToken)) {
            Cookie cookie = WebUtils.getCookie(request, "access-token");
            if (Objects.isNull(cookie)) {
                accessToken = request.getParameter("access-token");
                if (StringUtils.isEmpty(accessToken)) {
                    chain.doFilter(request, response);
                    return;
                }
            } else {
                accessToken = cookie.getValue();
            }
        }

        if (Objects.isNull(accessToken)) {
            chain.doFilter(request, response);
            return;
        }

        String username = null;
        try {
            // 校验 token
            username = tokenManager.getUsernameFromToken(accessToken);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        if (Objects.isNull(username)) {
            chain.doFilter(request, response);
            return;
        }

        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                log.error("userDetailsService.loadUserByUsername({}) error.", username);
            }
            if (Objects.isNull(userDetails)) {
                chain.doFilter(request, response);
                return;
            }
            if (tokenManager.validateToken(username, accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        request.setAttribute(ALREADY_FILTERED_SUFFIX, true);
        chain.doFilter(request, response);
    }
}
