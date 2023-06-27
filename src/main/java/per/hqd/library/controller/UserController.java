package per.hqd.library.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import per.hqd.library.dao.entity.SysUser;
import per.hqd.library.dao.mapper.SysUserMapper;
import per.hqd.library.req.RegisterReq;
import per.hqd.library.security.LibraryUserDetail;
import per.hqd.library.security.TokenManager;
import per.hqd.library.utils.Result;
import per.hqd.library.utils.ResultUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 16:07
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关接口")
public class UserController {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private TokenManager tokenManager;

    @Resource
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    @ApiOperation(value = "登录", notes="根据用户名、密码判断该用户是否存在。存在登录成功获取token")
    public Result login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        Optional<SysUser> optional = sysUserMapper.selectOne(SysUser.builder().email(userName).build());
        if (!optional.isPresent()) {
            return ResultUtil.error(-1, "用户不存在!");
        }
        if (!passwordEncoder.matches(password, optional.get().getPassword())) {
            return ResultUtil.error(-2, "密码不正确!");
        }
        // 获取登录授权信息, 生成 Access Token
        UserDetails userDetails = new LibraryUserDetail(optional.get());
        String accessToken = tokenManager.generateToken(userDetails);
        return ResultUtil.success(accessToken);
    }

    @PostMapping("/signup")
    @ApiOperation(value = "注册", notes="输入用户名(邮箱)、密码、手机号、昵称")
    public Result register(@RequestBody RegisterReq req) {
        SysUser user = new SysUser();
        user.setEmail(req.getUserName());
        user.setNickName(req.getNickName());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        int num = sysUserMapper.insertSelective(user);
        return ResultUtil.success(num);
    }

    @GetMapping("/sign/in")
    @ApiOperation(value = "未登录跳转接口")
    public Result signIn(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ResultUtil.error(-1, "尚未登录，请登录!");
    }

}
