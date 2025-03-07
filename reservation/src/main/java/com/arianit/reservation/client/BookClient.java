package com.arianit.reservation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "${book.client.name}", url = "${book.client.url}")
public interface BookClient {

    @GetMapping("/{id}")
    ResponseEntity<Book> getBook(@PathVariable("id") Long id);

    @PutMapping
    ResponseEntity<Book> updateBook(Book book);

    @GetMapping("/{id}/checkAvailability")
    ResponseEntity<Boolean> checkAvailability(@PathVariable("id") Long id);

}
