# Virtual Power Plant API

This is a Spring Boot REST API developed to manage battery data for a virtual power plant. It allows clients to register battery entries, fetch aggregated statistics based on postcode, query batteries by status, and initialize the database with sample data.

---

## Features

- Register new batteries with name, postcode, and capacity.
- Get statistics (total and average capacity) of batteries in a given postcode range.
- Retrieve batteries by postcode or status (ACTIVE/INACTIVE).
- Automatic initial data load from JSON file on first run.
- Clean code architecture: controller-service-repository layers.
- Swagger UI for interactive API testing.
- Unit and integration tests using JUnit and H2 database.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **MySQL 8** (production)
- **H2** (testing)
- **Lombok**
- **JaCoCo Report**
- **Swagger/OpenAPI**
- **JUnit 5 + AssertJ**

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL installed and running
- Git

---

## Once the app is running, go to:

http://localhost:8080/swagger-ui/index.html

## To generate the JaCoCo test coverage report, run the following command:

mvn clean test jacoco:report

## Architectural Decisions

Layered Architecture: The application follows a classic layered architecture pattern—dividing responsibilities across Controller, Service, and Repository layers. This promotes separation of concerns, maintainability, and scalability.

Spring Boot Framework: Chosen for rapid application development, dependency injection, and out-of-the-box support for RESTful services, data validation, and exception handling.

Database Choice: MySQL is used for production-like persistence, while H2 is used for isolated, in-memory testing, ensuring fast and reliable test execution.

DTOs and Mappers: Data Transfer Objects (DTOs) are used to decouple internal models from API payloads. This adds flexibility to evolve internal logic without impacting external interfaces.

Validation: Jakarta Bean Validation is integrated to ensure input integrity and prevent invalid data from entering the system.

API Documentation: OpenAPI (via springdoc) is used to auto-generate interactive API documentation, improving developer experience and reducing onboarding time.

Test Strategy: Unit and integration tests are written using JUnit, Spring Boot Test, and AssertJ to validate both individual components and end-to-end behavior.

Code Coverage: JaCoCo is integrated to ensure thorough test coverage and to identify untested areas in the codebase.
