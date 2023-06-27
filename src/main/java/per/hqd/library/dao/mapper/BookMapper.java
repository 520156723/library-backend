package per.hqd.library.dao.mapper;

import io.mybatis.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import per.hqd.library.dao.entity.Book;

import java.util.List;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 17:15
 */
public interface BookMapper extends Mapper<Book, Long> {

    List<Book> selectByPage(@Param("from") Integer from, @Param("size") Integer size);

    List<Book> selectByName(@Param("name") String name);

    Integer createBooks(@Param("books") List<Book> books);

    Integer deleteBooksByIds(@Param("ids") List<Long> ids);

    Integer updateBooks(@Param("books") List<Book> books);

    Integer modifyBooksCount(@Param("books") List<Book> books);
}
