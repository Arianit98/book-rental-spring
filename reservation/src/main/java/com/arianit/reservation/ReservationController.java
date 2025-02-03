package com.arianit.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<?> getReservations() {
        List<Reservation> reservations = reservationService.getReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.getReservation(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation newReservation) {
        Reservation reservation = reservationService.createReservation(newReservation);
        if (reservation == null) {
            return ResponseEntity.badRequest().body("Costumer not found or book is not available");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateReservation(@PathVariable("id") Long id, @RequestBody Reservation newReservation) {
        Reservation reservation = reservationService.updateReservation(id, newReservation);
        if (reservation == null) {
            return ResponseEntity.badRequest().body("Costumer not found or book is not available");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
