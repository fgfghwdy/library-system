package lxx.library.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lxx.library.entity.Book;
import lxx.library.entity.BorrowRecord;
import lxx.library.mapper.BookMapper;
import lxx.library.mapper.BorrowRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书控制器 - 可立即还书版本
 */
@RestController
public class BookController {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    /**
     * 获取所有图书，并返回当前用户是否借了这本书
     */
    @GetMapping("/books/{userId}")
    public List<BookWithBorrowStatus> getBooksWithStatus(@PathVariable Integer userId) {
        List<Book> books = bookMapper.selectList(null);
        List<BookWithBorrowStatus> result = new ArrayList<>();
        for (Book b : books) {
            boolean isBorrowed = borrowRecordMapper.selectOne(
                    new QueryWrapper<BorrowRecord>()
                            .eq("book_id", b.getId())
                            .eq("user_id", userId)
                            .isNull("return_time")
            ) != null;

            result.add(new BookWithBorrowStatus(b, isBorrowed));
        }
        return result;
    }

    /**
     * 添加新书
     */
    @PostMapping("/books")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        if (book.getStock() == null) {
            book.setStock(1);
        }
        bookMapper.insert(book);
        return ResponseEntity.ok("图书添加成功");
    }

    /**
     * 借书
     */
    @PostMapping("/books/{bookId}/borrow/{userId}")
    public ResponseEntity<String> borrowBook(@PathVariable Integer bookId,
                                             @PathVariable Integer userId) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("图书不存在");
        }
        if (book.getStock() <= 0) {
            return ResponseEntity.badRequest().body("库存不足，借阅失败");
        }

        // 库存减1
        book.setStock(book.getStock() - 1);
        bookMapper.updateById(book);

        // 借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setBookId(bookId);
        record.setUserId(userId);
        record.setBorrowTime(LocalDateTime.now());
        borrowRecordMapper.insert(record);

        return ResponseEntity.ok("借书成功");
    }

    /**
     * 还书
     */
    /**
     * 按书名或作者模糊查询书籍（读者用）
     */
    @GetMapping("/books/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookMapper.selectList(
                new QueryWrapper<Book>()
                        .like("title", keyword)
                        .or()
                        .like("author", keyword)
        );
    }

    @PostMapping("/books/{bookId}/return/{userId}")
    public ResponseEntity<String> returnBook(@PathVariable Integer bookId,
                                             @PathVariable Integer userId) {
        BorrowRecord record = borrowRecordMapper.selectOne(
                new QueryWrapper<BorrowRecord>()
                        .eq("book_id", bookId)
                        .eq("user_id", userId)
                        .isNull("return_time")
        );

        if (record == null) {
            // 防止空指针 → 返回友好提示
            return ResponseEntity.badRequest().body("归还失败：该用户没有借阅该书");
        }

        // 设置归还时间
        record.setReturnTime(LocalDateTime.now());
        borrowRecordMapper.updateById(record);

        // 库存加1
        Book book = bookMapper.selectById(bookId);
        if (book != null) {
            book.setStock(book.getStock() + 1);
            bookMapper.updateById(book);
        }

        return ResponseEntity.ok("归还成功");
    }

    /**
     * 内部类 - 带借阅状态的图书返回对象
     */
    public static class BookWithBorrowStatus {
        private Book book;
        private boolean isBorrowedByCurrentUser;

        public BookWithBorrowStatus(Book book, boolean isBorrowedByCurrentUser) {
            this.book = book;
            this.isBorrowedByCurrentUser = isBorrowedByCurrentUser;
        }

        public Book getBook() {
            return book;
        }

        public boolean isBorrowedByCurrentUser() {
            return isBorrowedByCurrentUser;
        }
    }
}
