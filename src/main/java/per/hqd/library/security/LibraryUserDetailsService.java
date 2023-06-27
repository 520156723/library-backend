package per.hqd.library.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import per.hqd.library.dao.entity.SysUser;
import per.hqd.library.dao.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author henry
 * @date 2021/8/9
 */
@Slf4j
@Service("userDetailsService")
public class LibraryUserDetailsService implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SysUser> user = sysUserMapper.selectOne(SysUser.builder().email(username).build());
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        return new LibraryUserDetail(user.get());
    }
}
