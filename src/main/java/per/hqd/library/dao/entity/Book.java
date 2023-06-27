package per.hqd.library.dao.entity;

import io.mybatis.provider.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 17:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity.Table(value = "book", remark = "图书表", autoResultMap = true)
public class Book {
    @Entity.Column(id = true, remark = "主键", updatable = false, insertable = false)
    private Long id;
    @Entity.Column(value = "name", remark = "书名")
    private String name;
    @Entity.Column(value = "name", remark = "库存")
    private Integer count;
    @Entity.Column(value = "create_time")
    private Date createTime;
    @Entity.Column(value = "update_time")
    private Date updateTime;
    @Entity.Column(value = "is_delete")
    private Byte isDelete;
}
