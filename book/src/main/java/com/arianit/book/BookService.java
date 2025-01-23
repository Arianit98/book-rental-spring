package com.arianit.book;

import org.springframework.stereotype.Service;

import java.util.List;

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

    public Book updateBook(Long id, Book newBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setAuthor(newBook.getAuthor());
                    book.setYear(newBook.getYear());
                    book.setStockNr(newBook.getStockNr());
                    book.setReservedNr(newBook.getReservedNr());
                    return bookRepository.save(book);
                })
                .orElseGet(() -> bookRepository.save(newBook));
    }

    public boolean checkAvailability(Long id) {
        Book book = getBook(id);
        if (book == null) {
            return false;
        }
        return book.getStockNr() > book.getReservedNr();
    }
}
