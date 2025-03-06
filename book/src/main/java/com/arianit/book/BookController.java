package com.arianit.book;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<?> getBooks() {
        List<Book> books = bookService.getBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable("id") Long id) {
        Book book = bookService.getBook(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book newBook) {
        newBook = bookService.createBook(newBook);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newBook.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping()
    public ResponseEntity<?> updateBook(@RequestBody Book newBook) {
        newBook = bookService.updateBook(newBook);
        if (newBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/checkAvailability")
    public ResponseEntity<?> isAvailable(@PathVariable("id") Long id) {
        boolean isAvailable = bookService.checkAvailability(id);
        return ResponseEntity.ok(isAvailable);
    }


}
