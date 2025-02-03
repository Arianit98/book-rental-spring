package com.arianit.reservation;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableWireMock({
        @ConfigureWireMock(
                name = "costumer-service",
                port = 8080),
        @ConfigureWireMock(
                name = "book-service",
                port = 8081)
})
class ReservationControllerTest {

    @LocalServerPort
    private Integer port;

    private static Integer reservationId;

    @InjectWireMock("costumer-service")
    private WireMockServer mockCostumerService;

    @InjectWireMock("book-service")
    private WireMockServer mockBookService;

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
        mockCostumerService.stubFor(get("/api/v1/costumers/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \" \"}")
                ));

        mockBookService.stubFor(get("/api/v1/books/1/checkAvailability")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                ));

        mockBookService.stubFor(get("/api/v1/books/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"title\": \"Book Title\", \"author\": \"Author Name\", \"reservedNr\": 0, \"stockNr\": 1, \"year\": 2021}")
                ));

        mockBookService.stubFor(put("/api/v1/books/1")
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"title\": \"Book Title\", \"author\": \"Author Name\", \"reservedNr\": 1, \"stockNr\": 1, \"year\": 2021}")
                ));

        reservationId = given()
                .contentType("application/json")
                .body("{\"costumerId\": 1, \"bookId\": 1, \"durationInDays\": 7, \"createdDate\": \"2021-10-01\"}")
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
        mockCostumerService.stubFor(get("/api/v1/costumers/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \" \"}")
                ));

        mockBookService.stubFor(get("/api/v1/books/1/checkAvailability")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                ));

        mockBookService.stubFor(get("/api/v1/books/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"title\": \"Book Title\", \"author\": \"Author Name\", \"reservedNr\": 0, \"stockNr\": 1, \"year\": 2021}")
                ));

        mockBookService.stubFor(put("/api/v1/books/1")
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"title\": \"Book Title\", \"author\": \"Author Name\", \"reservedNr\": 1, \"stockNr\": 1, \"year\": 2021}")
                ));

        given()
                .contentType("application/json")
                .body("{\"costumerId\": 1, \"bookId\": 1, \"durationInDays\": 7, \"createdDate\": \"2021-10-01\"}")
                .when()
                .put("api/v1/reservations/" + reservationId)
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Delete reservation")
    @Order(4)
    void deleteReservation() {
        mockBookService.stubFor(get("/api/v1/books/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"title\": \"Book Title\", \"author\": \"Author Name\", \"reservedNr\": 1, \"stockNr\": 1, \"year\": 2021}")
                ));

        mockBookService.stubFor(put("/api/v1/books/1")
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"title\": \"Book Title\", \"author\": \"Author Name\", \"reservedNr\": 0, \"stockNr\": 1, \"year\": 2021}")
                ));

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
        mockCostumerService.stubFor(get("/api/v1/costumers/100")
                .willReturn(aResponse()
                        .withStatus(204)
                ));

        mockBookService.stubFor(get("/api/v1/books/1/checkAvailability")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                ));

        given()
                .contentType("application/json")
                .body("{\"costumerId\": 100, \"bookId\": 1, \"durationInDays\": 7, \"createdDate\": \"2021-10-01\"}")
                .when()
                .post("api/v1/reservations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create new reservation with invalid book")
    @Order(6)
    void createReservationWithInvalidBook() {
        mockCostumerService.stubFor(get("/api/v1/costumers/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \" \"}")
                ));

        mockBookService.stubFor(get("/api/v1/books/100/checkAvailability")
                .willReturn(aResponse()
                        .withStatus(204)
                ));

        given()
                .contentType("application/json")
                .body("{\"costumerId\": 1, \"bookId\": 100, \"durationInDays\": 7, \"createdDate\": \"2021-10-01\"}")
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
        mockCostumerService.stubFor(get("/api/v1/costumers/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \" \"}")
                ));

        mockBookService.stubFor(get("/api/v1/books/1/checkAvailability")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                ));

        mockBookService.stubFor(get("/api/v1/books/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"title\": \"Book Title\", \"author\": \"Author Name\", \"reservedNr\": 0, \"stockNr\": 1, \"year\": 2021}")
                ));

        mockBookService.stubFor(put("/api/v1/books/1")
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"title\": \"Book Title\", \"author\": \"Author Name\", \"reservedNr\": 1, \"stockNr\": 1, \"year\": 2021}")
                ));

        given()
                .contentType("application/json")
                .body("{\"costumerId\": 1, \"bookId\": 1}")
                .when()
                .put("api/v1/reservations/100")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Create new reservation with invalid book and customer")
    @Order(10)
    void createReservationWithInvalidBookAndCustomer() {
        mockCostumerService.stubFor(get("/api/v1/costumers/100")
                .willReturn(aResponse()
                        .withStatus(204)
                ));

        mockBookService.stubFor(get("/api/v1/books/100/checkAvailability")
                .willReturn(aResponse()
                        .withStatus(204)
                ));

        given()
                .contentType("application/json")
                .body("{\"costumerId\": 100, \"bookId\": 100}")
                .when()
                .post("api/v1/reservations")
                .then()
                .statusCode(400);
    }
}