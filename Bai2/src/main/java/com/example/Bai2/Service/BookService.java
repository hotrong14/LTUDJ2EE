package com.example.Bai2.Service;

import java.util.List;
import java.util.ArrayList;
import com.example.Bai2.Model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private List<Book> books = new ArrayList<>(List.of(
        new Book(1, "Dế Mèn Phiêu Lưu Ký", "Tô Hoài"),
        new Book(2, "Tắt Đèn", "Ngô Tất Tố"),
        new Book(3, "Mắt Biếc", "Nguyễn Nhật Ánh"),
        new Book(4, "Đất Rừng Phương Nam", "Đoàn Giỏi"),
        new Book(5, "Tuổi Thơ Dữ Dội", "Phùng Quán")
    ));

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBookById(int id) {
        return books.stream()
                    .filter(book -> book.getId() == id)
                    .findFirst()
                    .orElse(null);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void updateBook(int id, Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                books.set(i, updatedBook);
                return;
            }
        }
    }

    public void deleteBook(int id) {
        books.removeIf(book -> book.getId() == id);
    }
}