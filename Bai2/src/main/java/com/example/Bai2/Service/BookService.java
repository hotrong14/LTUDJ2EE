package com.example.Bai2.Service;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import com.example.Bai2.Model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private List<Book> books = new ArrayList<>(List.of(
        new Book(1L, "Dế Mèn Phiêu Lưu Ký", "Tô Hoài"),
        new Book(2L, "Tắt Đèn", "Ngô Tất Tố"),
        new Book(3L, "Mắt Biếc", "Nguyễn Nhật Ánh"),
        new Book(4L, "Đất Rừng Phương Nam", "Đoàn Giỏi"),
        new Book(5L, "Tuổi Thơ Dữ Dội", "Phùng Quán")
    ));


    public List<Book> getAllBooks() {
        return books;
    }

    public void addBook(Book book) {

        java.util.Set<Long> usedIds = new java.util.HashSet<>();
        for (Book b : books) {
            usedIds.add(b.getId());
        }
        long id = 1L;
        while (usedIds.contains(id)) {
            id++;
        }
        book.setId(id);
        books.add(book);
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst();
    }

    public void updateBook(Book updatedBook) {
        books.stream()
            .filter(book -> book.getId().equals(updatedBook.getId()))
            .findFirst()
            .ifPresent(book -> {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
            });
    }

    public void deleteBook(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }
}