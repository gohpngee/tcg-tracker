# CLAUDE.md

Guidance for Claude Code and other coding agents working in this repository.

## Working agreement

- Work from the repository root unless a command explicitly requires `backend/`.
- Inspect `git status` before editing. The working tree may contain user changes; preserve anything unrelated to the requested task.
- Use the Maven Wrapper instead of a globally installed Maven.
- Keep local credentials in ignored files. Never print, commit, or replace values from `backend/.env`.

## Commands

All Maven commands run from `backend/`.

```bash
# Run the implemented catalogue module's full test suite
./mvnw test -pl card

# Run one focused persistence test
./mvnw test -pl card -Dtest=CardRepositoryPersistenceTest

# Run the full Maven test reactor
./mvnw test

# Build all modules
./mvnw clean install

# Intended application command once runtime configuration is complete
./mvnw spring-boot:run -pl application
```

## Repository shape

This is a Spring Boot 4.1.0 / Java 21 Maven reactor rooted at `backend/pom.xml`.

- `card` contains the implemented catalogue domain: `CardGame`, `CardSet`, and `Card` JPA entities; custom `EntityManager` repositories; `CatalogueQueryService`; Spring MVC controllers; and H2-backed tests.
- `price-tracking` is a planned module. Its Java classes are empty placeholders; do not describe price fetching or marketplace integration as implemented.
- `notification` is a planned module. Its POM declares mail support, but it has no application source or tests.
- `application` supplies `TCGTrackerApplication` and assembles the modules. Its committed YAML contains only the application name.

## Current project state

Keep documentation and implementation claims aligned with these facts:

- The `card` module is the only feature-complete area and its 15-test suite passes with H2. The full Maven test reactor currently also passes, but the other modules have little or no test coverage.
- MySQL and Flyway dependencies are declared, but there is no committed datasource configuration, migration directory, or schema.
- `backend/docker-compose.yaml` is empty.
- `backend/.env` defines local `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` values, but the committed application configuration does not load them.
- The directory is `price-tracking`, while its POM artifact ID is `price_tracking`; `application` and `notification` refer to `price-tracking`. The current test reactor passes, but align this name mismatch before relying on clean-clone dependency resolution.
- `CardController` currently has overlapping one-segment mappings for a card ID and a card-set code. Do not add to or document these as stable until the collision is resolved.

## Implementation conventions

- Keep catalogue reads in `CatalogueQueryService`, with persistence logic in the existing custom repositories rather than introducing Spring Data repository interfaces without a clear reason.
- The entities protect construction invariants with constructors and have no general setters. Prefer intention-revealing domain methods such as `CardSet.updateMetadata(...)` for state changes.
- Preserve the established package split: `com.tcgtracker.card` for catalogue code and `com.tcgtracker.price_tracking` for the future price module.
- When changing entity mapping or query behaviour, add focused tests in `backend/card/src/test` and run `./mvnw test -pl card`.
- Add a Flyway migration with every future persistent schema change; do not rely on the H2 test schema as a production migration strategy.
