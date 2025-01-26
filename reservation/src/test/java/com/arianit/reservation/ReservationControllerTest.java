package com.arianit.reservation;


import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationControllerTest {

    @LocalServerPort
    private int port;

    private static Integer reservationId;
    private static Integer costumerId;
    private static Integer bookId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Get all reservations")
    @Order(0)
    void getAllReservations() {
        given()
                .when()
                .get("api/v1/reservations")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Create new reservation")
    @Order(1)
    void createReservation() {

        costumerId = given()
                .contentType("application/json")
                .body("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"phone\": \"123456789\", \"address\": \"123 Main St\", \"age\": 30}")
                .when()
                .post("api/v1/costumers")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        bookId = given()
                .contentType("application/json")
                .body("{\"title\": \"The Great Gatsby\", \"author\": \"F. Scott Fitzgerald\", \"stockNr\": 10, \"year\": 1925, \"reservedNr\": 0}")
                .when()
                .post("api/v1/books")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        reservationId = given()
                .contentType("application/json")
                .body("{\"customerId\":" + costumerId + ", \"bookId\":" + bookId + ", \"durationInDays\": 7, \"createdDate\": \"2021-10-01\"}")
                .when()
                .post("api/v1/reservations")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Get reservation by ID")
    @Order(2)
    void getReservationById() {
        given()
                .when()
                .get("api/v1/reservations/" + reservationId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Update reservation")
    @Order(3)
    void updateReservation() {
        given()
                .contentType("application/json")
                .body("{\"customerId\": 1, \"bookId\": 1}")
                .when()
                .put("api/v1/reservations/" + reservationId)
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Delete reservation")
    @Order(4)
    void deleteReservation() {
        given()
                .when()
                .delete("api/v1/reservations/" + reservationId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Create new reservation with invalid customer")
    @Order(5)
    void createReservationWithInvalidCustomer() {
        given()
                .contentType("application/json")
                .body("{\"customerId\": 100, \"bookId\": 1}")
                .when()
                .post("api/v1/reservations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create new reservation with invalid book")
    @Order(6)
    void createReservationWithInvalidBook() {
        given()
                .contentType("application/json")
                .body("{\"customerId\": 1, \"bookId\": 100}")
                .when()
                .post("api/v1/reservations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Get non-existing reservation")
    @Order(7)
    void getNonExistingReservation() {
        given()
                .when()
                .get("api/v1/reservations/100")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Delete non-existing reservation")
    @Order(8)
    void deleteNonExistingReservation() {
        given()
                .when()
                .delete("api/v1/reservations/100")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Update non-existing reservation")
    @Order(9)
    void updateNonExistingReservation() {
        given()
                .contentType("application/json")
                .body("{\"customerId\": 1, \"bookId\": 1}")
                .when()
                .put("api/v1/reservations/100")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Create new reservation with invalid book and customer")
    @Order(10)
    void createReservationWithInvalidBookAndCustomer() {
        given()
                .contentType("application/json")
                .body("{\"customerId\": 100, \"bookId\": 100}")
                .when()
                .post("api/v1/reservations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create new reservation with invalid book and valid customer")
    @Order(11)
    void createReservationWithInvalidBookAndValidCustomer() {
        given()
                .contentType("application/json")
                .body("{\"customerId\": 1, \"bookId\": 100}")
                .when()
                .post("api/v1/reservations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create new reservation with valid book and invalid customer")
    @Order(12)
    void createReservationWithValidBookAndInvalidCustomer() {
        given()
                .contentType("application/json")
                .body("{\"customerId\": 100, \"bookId\": 1}")
                .when()
                .post("api/v1/reservations")
                .then()
                .statusCode(400);
    }


}