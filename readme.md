# SpringBootLibraryManagement

## Overview

SpringBootLibraryManagement is a production-ready Spring Boot application for managing a library's core operations. It provides REST APIs and a simple web UI (if bundled) to manage books, students, and transactions (issue/return). The project is intended as a starting point for small library systems and demos.

## Key Features

- Book CRUD: create, read, update, delete books with metadata (title, author, ISBN, copies, etc.)
- Student management: register and manage student records
- Issue / Return: map issued books to students and track return dates and fines
- Filtering & Sorting: support for paging, filtering, and sorting when listing books and students
- REST API: endpoints for programmatic access; Postman collection included under `src/main/resources/PostmanCollection`
- Security: basic authentication and role-based access (Spring Security integration)
- Profiles: separate application properties for dev/qa/test/preprod environments

## Project structure (important folders)

- `src/main/java/.../controller` - REST controllers
- `src/main/java/.../services` - business logic
- `src/main/java/.../repositories` - Spring Data repositories
- `src/main/java/.../entities` - JPA entities
- `src/main/resources` - application properties and Postman collection

## Technology Stack

- Java 11+ (project targets Java 11 or higher)
- Spring Boot (MVC, Data JPA, Security)
- Maven for build and dependency management
- MySQL (or any JDBC-compatible DB) as the default datasource
- Swagger / Springfox (if configured) for API docs

## Prerequisites

- JDK 11 or newer installed and JAVA_HOME configured
- Maven 3.6+ installed
- A running database (MySQL/Postgres) or you can use an in-memory DB for testing

## Quickstart — run locally

1. Clone the repository

    ```bash
    git clone https://github.com/lkumarra/SpringBootLibraryManagement.git
    cd SpringBootLibraryManagement
    ```

2. Configure your database in `src/main/resources/application.properties` or use one of the profile-specific files (`application-dev.properties`, etc.). Example:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/library
    spring.datasource.username=root
    spring.datasource.password=password
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update
    ```

3. Build and run with Maven

    ```bash
    mvn clean package
    mvn spring-boot:run
    ```

   By default the app runs on `http://localhost:8080` unless overridden in `application.properties`.

## Running with Docker / Docker Compose

This repo contains `Dockerfile` and `dockercompose.yml` (top-level). To run using Docker Compose:

```bash
docker compose up --build
```

Adjust DB configuration if you want to run a DB container alongside the app.

## API documentation & Postman

- A Postman collection is included at `src/main/resources/PostmanCollection/LibraryManagementSystem.postman_collection.json`.
- If Swagger/OpenAPI is enabled, API docs are usually available at `/swagger-ui.html` or `/swagger-ui/index.html` when the app is running.

## Configuration & Profiles

This project includes multiple Spring profiles with separate property files:
- `application.properties` (defaults)
- `application-dev.properties`
- `application-qa.properties`
- `application-preprod.properties`
- `application-test.properties`

Activate a profile via environment variable or JVM arg:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
# or
java -jar -Dspring.profiles.active=dev target/*.jar
```

## Tests

Run unit and integration tests with Maven:

```bash
mvn test
```

## Contributing

Feel free to open issues or submit pull requests. Keep changes focused and add tests for new behavior. Follow existing code style and package structure.

## Author

- Name: L KUMAR RAJPUT
- GitHub: https://github.com/lkumarra
- Contact: (add your email or preferred contact info here)

Note: I inferred the author from the repository owner (`lkumarra`) and your local username path (`lkumarrajput`). If you want a different display name, email, or contact details in the README, tell me and I will update it.

## License

This project is distributed under the MIT License. See the `LICENSE` file for the full license text.

## Acknowledgements

- Based on Spring Boot and common open-source libraries. See `pom.xml` for full dependency list.

---

If you'd like any additional sections (architecture diagrams, CI/CD steps, or detailed API reference) added to the README, tell me which ones and I will add them.
