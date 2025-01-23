package com.arianit.reservation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${costumer.client.name}", url = "${costumer.client.url}")
public interface CostumerClient {

    @GetMapping
    ResponseEntity<?> getCostumers();

    @GetMapping("/{id}")
    ResponseEntity<?> getCostumer(@PathVariable("id") Long id);
}
