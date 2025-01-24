# Book Rental Spring

Book rental is a simple application for renting books. It is written in Java using Spring Boot. This project follows the
microservice architecture.

## Build

To build the entire code, you need to run the following commands from the root project

```shell
mvn clean install
```

## Running the application locally

Since this application is seperated in three services. All of them need to be started separately.

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method
from your IDE for each of the services.

Alternatively you can use
the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html)
like so:

* In a new terminal, run the costumer service:

```shell
cd costumer
mvn spring-boot:run
```

* In a new terminal, run the book service:

```shell
cd book
mvn spring-boot:run
```

* In a new terminal, run the reservation service:

```shell
cd reservation
mvn spring-boot:run
```

## Ports

* **costumer service** *(running on port 8080)*
* **book service** *(running on port 8081)*
* **reservation service** *(running on port 8082)*