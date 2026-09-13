# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Commands

All commands run from `backend/` using the Maven wrapper.

```bash
# Build all modules
./mvnw clean install

# Run the application
./mvnw spring-boot:run -pl application

# Run tests for all modules
./mvnw test

# Run tests for a single module
./mvnw test -pl card

# Run a single test class
./mvnw test -pl card -Dtest=CardServiceTest
```

## Architecture

This is a **Spring Boot 4.1.0 / Java 21 multi-module Maven project**. The root `pom.xml` in `backend/` defines four modules with a strict dependency order:

```
card  ←  price-tracking  ←  notification
 ↑                                ↑
 └──────────── application ────────┘
```

- **`card`** — card catalog domain. Entities: `CardGame` (Pokemon, OnePiece), `CardSet` (set name, code, language), `Card` (linked to a set). All JPA entities. Contains the shared domain model consumed by other modules.
- **`price-tracking`** — fetches and averages eBay sold prices for cards. Uses Spring WebFlux (`WebClient`) for reactive HTTP calls to eBay.
- **`notification`** — price-change alerts via email (Spring Mail). Depends on both `card` and `price-tracking`.
- **`application`** — the runnable Spring Boot app (`TCGTrackerApplication`). Assembles all modules, owns `application.yaml`, MySQL datasource config, and Flyway migrations.

## Key Tech Choices

- **Database**: MySQL with Flyway for schema migrations. Migrations live in `application/src/main/resources/db/migration/`.
- **Lombok**: Used across all modules — `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` on entities. Annotation processor is wired in the root `pom.xml`.
- **WebFlux**: Only in `price-tracking` (for eBay API calls). The rest of the app uses Spring MVC.
- **Module artifact IDs**: use underscores (`price-tracking`, `notification`) — match exactly when referencing in `-pl` flags.
