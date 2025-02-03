package com.arianit.reservation;

import com.arianit.reservation.client.Book;
import com.arianit.reservation.client.BookClient;
import com.arianit.reservation.client.CostumerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CostumerClient costumerClient;
    private final BookClient bookClient;

    Logger logger = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, CostumerClient costumerClient, BookClient bookClient) {
        this.reservationRepository = reservationRepository;
        this.costumerClient = costumerClient;
        this.bookClient = bookClient;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public Reservation createReservation(Reservation reservation) {
        if (!costumerExists(reservation.getCostumerId()))
            return null;
        if (!isBookAvailable(reservation.getBookId()))
            return null;
        Reservation savedReservation = reservationRepository.save(reservation);
        increaseBookReservedNr(reservation.getBookId());
        return savedReservation;
    }

    public void deleteReservation(Long id) {
        Reservation reservation = getReservation(id);
        if (reservation == null) {
            logger.warn("Reservation with id: {} not found", id);
            return;
        }
        reservationRepository.deleteById(id);
        decreaseBookReservedNr(reservation.getBookId());
    }

    public Reservation updateReservation(Long id, Reservation newReservation) {
        if (!costumerExists(newReservation.getCostumerId()))
            return null;
        if (!isBookAvailable(newReservation.getBookId()))
            return null;
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setCostumerId(newReservation.getCostumerId());
                    reservation.setBookId(newReservation.getBookId());
                    reservation.setCreatedDate(newReservation.getCreatedDate());
                    reservation.setDurationInDays(newReservation.getDurationInDays());
                    Reservation savedReservation = reservationRepository.save(reservation);
                    if (!reservation.getBookId().equals(newReservation.getBookId())) {
                        decreaseBookReservedNr(reservation.getBookId());
                        increaseBookReservedNr(newReservation.getBookId());
                    }
                    return savedReservation;
                })
                .orElseGet(() -> {
                    Reservation savedReservation = reservationRepository.save(newReservation);
                    increaseBookReservedNr(newReservation.getBookId());
                    return savedReservation;
                });
    }

    public void increaseBookReservedNr(Long bookId) {
        Book book = bookClient.getBook(bookId).getBody();
        assert book != null;
        book.setReservedNr(book.getReservedNr() + 1);
        bookClient.updateBook(bookId, book);
    }

    public void decreaseBookReservedNr(Long bookId) {
        Book book = bookClient.getBook(bookId).getBody();
        assert book != null;
        book.setReservedNr(book.getReservedNr() - 1);
        bookClient.updateBook(bookId, book);
    }

    public boolean isBookAvailable(Long bookId) {
        ResponseEntity<Boolean> bookResponse = bookClient.checkAvailability(bookId);
        if (bookResponse.getStatusCode() != HttpStatus.OK) {
            logger.warn("Book with id: {} not found", bookId);
            return false;
        }
        if (Boolean.FALSE.equals(bookResponse.getBody())) {
            logger.warn("Book with id: {} is not available", bookId);
            return false;
        }
        return true;
    }

    public boolean costumerExists(Long costumerId) {
        ResponseEntity<?> customerResponse = costumerClient.getCostumer(costumerId);
        if (customerResponse.getStatusCode() != HttpStatus.OK) {
            logger.warn("Costumer with id: {} not found", costumerId);
            return false;
        }
        return true;
    }
}
