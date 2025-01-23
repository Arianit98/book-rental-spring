package com.arianit.reservation;

import com.arianit.reservation.client.Book;
import com.arianit.reservation.client.BookClient;
import com.arianit.reservation.client.CostumerClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return reservationRepository.findById(id).orElseThrow();
    }

    public Reservation createReservation(Reservation reservation) {
        ResponseEntity<?> customerResponse;
        try {
            customerResponse = costumerClient.getCostumer(reservation.getCostumerId());
            if (customerResponse.getBody() == null) {
                logger.warn("Costumer with id: {} not found", reservation.getCostumerId());
                return null;
            }
            ResponseEntity<Boolean> bookResponse = bookClient.checkAvailability(reservation.getBookId());
            if (Boolean.FALSE.equals(bookResponse.getBody())) {
                logger.warn("Book with id: {} not available", reservation.getBookId());
                return null;
            }
            Book book = bookClient.getBook(reservation.getBookId()).getBody();
            assert book != null;
            book.setReservedNr(book.getReservedNr() + 1);
            bookClient.updateBook(reservation.getBookId(), book);
        } catch (FeignException e) {
            logger.warn("createReservation() => {}", e.getMessage());
            return null;
        }

        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = getReservation(id);
        if (reservation == null) {
            logger.warn("Reservation with id: {} not found", id);
            return;
        }
        Book book = bookClient.getBook(reservation.getBookId()).getBody();
        assert book != null;
        book.setReservedNr(book.getReservedNr() - 1);
        reservationRepository.deleteById(id);
        bookClient.updateBook(reservation.getBookId(), book);
    }

    public Reservation updateReservation(Long id, Reservation newReservation) {
        //TODO: update book reserved number accordingly
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setCostumerId(newReservation.getCostumerId());
                    reservation.setBookId(newReservation.getBookId());
                    reservation.setCreatedDate(newReservation.getCreatedDate());
                    reservation.setDurationInDays(newReservation.getDurationInDays());
                    return reservationRepository.save(reservation);
                })
                .orElseGet(() -> reservationRepository.save(newReservation));
    }
}
