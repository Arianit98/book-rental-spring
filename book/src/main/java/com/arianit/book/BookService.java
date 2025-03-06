package com.arianit.book;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(Book book) {
        Optional<Book> entityOpt = bookRepository.findById(book.getId());
        if (entityOpt.isEmpty()) {
            return null;
        }
        Book entity = entityOpt.get();
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setYear(book.getYear());
        entity.setStockNr(book.getStockNr());
        entity.setReservedNr(book.getReservedNr());
        return bookRepository.save(entity);
    }

    public boolean checkAvailability(Long id) {
        Book book = getBook(id);
        if (book == null) {
            return false;
        }
        return book.getStockNr() > book.getReservedNr();
    }
}
