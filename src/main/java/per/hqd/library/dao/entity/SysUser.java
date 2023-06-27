package per.hqd.library.dao.entity;

import io.mybatis.provider.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 23:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity.Table(value = "sys_user", remark = "系统用户", autoResultMap = true)
public class SysUser {
    @Entity.Column(id = true, remark = "主键", updatable = false, insertable = false)
    private Integer id;
    @Entity.Column(value = "email")
    private String email;
    @Entity.Column(value = "password")
    private String password;
    @Entity.Column(value = "phone")
    private String phone;
    @Entity.Column(value = "nick_name")
    private String nickName;
    @Entity.Column(value = "create_time")
    private Date createTime;
    @Entity.Column(value = "update_time")
    private Date updateTime;
}
