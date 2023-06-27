package per.hqd.library.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 520156723@qq.com
 * @date 2023/6/27 00:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReq {
    private String userName;
    private String password;
    private String nickName;
    private String phone;
}
