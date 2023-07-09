# Getting started

### Task Requirements
- Java 17
- Maven 3.8+
- PostgreSQL


### How to run:
- you can run it as simple spring boot app if all task requirements are followed.
- you can run application with docker compose:
  1) build application `mvn clean install`
  2) run docker compose `docker compose up -d` (`docker compose down` to stop)
  3) open swagger `http://localhost:8085/swagger-ui/index.html`