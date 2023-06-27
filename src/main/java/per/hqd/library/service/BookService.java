package per.hqd.library.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import per.hqd.library.dao.entity.Book;
import per.hqd.library.dao.mapper.BookMapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 520156723@qq.com
 * @date 2023/6/26 16:57
 */
@Service
@Slf4j
public class BookService {

    @Resource
    private BookMapper bookMapper;

    Cache<String, List<Book>> cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    public List<Book> selectByPage(Integer page, Integer pageSize) {
        return bookMapper.selectByPage((page - 1) * pageSize, pageSize);
    }

    public List<Book> selectByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return Collections.emptyList();
        }
        // 简单本地缓存
        List<Book> books = cache.getIfPresent(name);
        if (CollectionUtils.isEmpty(books)) {
            books = bookMapper.selectByName("%" + name + "%");
            cache.put(name, books);
        }

        return books;
    }

    public Integer createBooks(List<Book> books) {
        if (CollectionUtils.isEmpty(books)) {
            return 0;
        }
        return bookMapper.createBooks(books);
    }

    public Integer deleteBooks(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        return bookMapper.deleteBooksByIds(ids);
    }

    public Integer updateBooks(List<Book> books) {
        if (CollectionUtils.isEmpty(books)) {
            return 0;
        }
        return bookMapper.updateBooks(books);
    }

    public Integer modifyBooksCount(List<Book> books) {
        if (CollectionUtils.isEmpty(books)) {
            return 0;
        }
        return bookMapper.modifyBooksCount(books);
    }
}


