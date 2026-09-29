# SchoolApp 10 (SSR)

A server-rendered web application for managing teachers and user access for the Coding Factory central service. The application uses Spring MVC and Thymeleaf for its web interface, Spring Data JPA for persistence, MySQL for storage, and Spring Security for authentication and authorization.

## Features

- Browse teachers in a paginated list.
- Create, edit, and soft-delete teacher records, with input validation and region selection.
- Register users and authenticate with a custom login page.
- Enforce role and capability-based access to teacher and user management.
- Create and update the database schema with Flyway migrations.
- Display the interface in English and Greek.

## Technology

- Java 21 (Amazon Corretto toolchain)
- Spring Boot 4.1.1
- Gradle 9.7.1 (included wrapper)
- MySQL
- Thymeleaf, Spring Security, Spring Data JPA, and Flyway

## Prerequisites

- Java 21. The Gradle toolchain configuration targets Amazon Corretto.
- A MySQL server and a database for the application.

## Setup and run

1. Clone the repository and create a local environment file from the example:

   ```powershell
   Copy-Item .env.example .env
   ```

   On macOS or Linux:

   ```sh
   cp .env.example .env
   ```

2. Edit `.env` and provide the connection details for your MySQL database:

   ```properties
   MYSQL_HOST=localhost
   MYSQL_PORT=3306
   MYSQL_DB=schoolapp
   MYSQL_USER=your_mysql_user
   MYSQL_PASSWORD=your_mysql_password
   ```

   Create the database and ensure the configured MySQL user has permission to use it. Keep `.env` local; it is excluded from version control.

3. Start the application:

   ```powershell
   .\gradlew.bat bootRun
   ```

   On macOS or Linux:

   ```sh
   ./gradlew bootRun
   ```

   The default development profile reads `.env`, and the application is available at [http://localhost:8080](http://localhost:8080). Flyway applies the SQL migrations in `src/main/resources/db/migration` on startup.

## Tests

Run the test suite with the Gradle wrapper:

```powershell
.\gradlew.bat test
```

On macOS or Linux, use `./gradlew test`.

## Main pages

| Path | Purpose |
| --- | --- |
| `/` | Home page |
| `/login` | Sign in |
| `/users/register` | User registration |
| `/teachers` | Paginated teacher list |
| `/teachers/insert` | Add a teacher |
| `/teachers/edit/{uuid}` | Edit a teacher |

Access to management pages depends on the signed-in user's role and capabilities. Initial roles and capabilities are seeded by the Flyway migrations; the `ADMIN` role has teacher-management capabilities, while `EMPLOYEE` is seeded with view access.

## Project layout

```text
src/main/java/.../controller/       Spring MVC controllers
src/main/java/.../service/          Application services
src/main/java/.../repository/       Spring Data repositories
src/main/java/.../model/            JPA entities
src/main/java/.../authentication/   Spring Security configuration
src/main/resources/templates/       Thymeleaf pages
src/main/resources/static/          CSS and images
src/main/resources/db/migration/    Flyway SQL migrations
src/test/                            Automated tests
```

## Configuration

The default profile is `dev`. Profile-specific settings are in `application-dev.properties`, `application-staging.properties`, and `application-prod.properties`; the common settings are in `application.properties`. Database connection values are supplied through the `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_DB`, `MYSQL_USER`, and `MYSQL_PASSWORD` environment variables.