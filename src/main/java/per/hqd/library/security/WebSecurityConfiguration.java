package per.hqd.library.security;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.io.PrintWriter;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 17:15
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private LibraryUserDetailsService userDetailsService;

    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Resource
    TokenManager tokenManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/signup").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/user/sign/in").loginProcessingUrl("/user/login").permitAll()
                .successHandler((request, response, authentication) -> {
                    String accessToken = tokenManager.generateToken((UserDetails) authentication.getPrincipal());
                    response.setContentType("application/json;charset=utf-8");
                    response.setHeader("access-token", accessToken);
                    PrintWriter out = response.getWriter();
                    out.write(JSON.toJSONString(accessToken));
                    out.flush();
                    out.close();
                })
                .and()
                .logout().logoutUrl("/user/logout").logoutSuccessUrl("/")
                .and()
                .csrf().disable()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
