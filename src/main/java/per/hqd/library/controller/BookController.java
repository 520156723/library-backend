package per.hqd.library.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import per.hqd.library.dao.entity.Book;
import per.hqd.library.service.BookService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 16:07
 */
@RestController
@RequestMapping("/book")
@Slf4j
@Api(tags = "图书相关接口")
public class BookController {

    @Resource
    private BookService bookService;

    @ApiOperation(value = "浏览图书", notes="分页查看图书")
    @GetMapping("/get/books")
    public List<Book> getBooks(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return bookService.selectByPage(page, pageSize);
    }

    @GetMapping("/search/books")
    @ApiOperation(value = "查找图书", notes="根据书名 name 模糊查询，有10分钟缓存")
    public List<Book> searchBooks(@RequestParam(value = "name") String name) {
        return bookService.selectByName(name);
    }

    @PostMapping("/create/books")
    @ApiOperation(value = "创建图书", notes="图书名 name 、数量 count 必填")
    public Integer createBooks(@RequestBody List<Book> books) {
        books = books.stream().filter(b -> !StringUtils.isEmpty(b.getName()) && b.getCount() > 0).collect(Collectors.toList());
        return bookService.createBooks(books);
    }

    @PostMapping("/delete/books")
    @ApiOperation(value = "删除图书", notes="输入图书 id")
    public Integer deleteBooks(@RequestBody List<Long> bookIds) {
        return bookService.deleteBooks(bookIds);
    }

    @PostMapping("/update/books")
    @ApiOperation(value = "更新图书", notes="可更改图书名，图书 id 和书名 name 必填")
    public Integer updateBooks(@RequestBody List<Book> books) {
        books = books.stream()
                .filter(b -> Objects.nonNull(b.getId()) && !StringUtils.isEmpty(b.getName()))
                .collect(Collectors.toList());
        return bookService.updateBooks(books);
    }

    @PostMapping("/modify/books")
    @ApiOperation(value = "变更库存", notes="增加或减少对应图书数量，图书 id 和 count 必填，count表示增加减少多少库存")
    public Integer modifyBooksCount(@RequestBody List<Book> books) {
        books = books.stream().filter(b -> Objects.nonNull(b.getId()) && b.getCount() != 0).collect(Collectors.toList());
        return bookService.modifyBooksCount(books);
    }
}
