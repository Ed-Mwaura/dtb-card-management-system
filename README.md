# Card Management Backend

## Overview

This project implements a microservices architecture backend for a card management system. The services use PostgreSQL for data storage and are buildable with Maven.

## Project Structure

```
card-management-backend/
├── customer-service/
├── account-service/
├── card-service/
├── api-aggregator/
├── db/
│   ├── schema.sql
│   └── data.sql
├── pom.xml
└── README.md
```

## Prerequisites

-   Java 17+
-   Maven 3.6+
-   PostgreSQL 12+
-   (Optional) Postman or similar API client

## Database Setup

1. Install PostgreSQL if not already installed.

2. Create the database and user.

3. Apply the schema and optional test data found in the db folder.


## Configuration

Each microservice contains an `application-template.properties` file in `src/main/resources/`. Copy and rename it to `application.properties` and update values accordingly.

Update database credentials and any service URLs as needed.

## Build and Run

Build all microservices with Maven:

```bash
mvn clean install
```

Run each microservice individually:

```bash
cd customer-service
mvn spring-boot:run
```

Replace `customer-service` with `account-service` or `card-service` or `api-aggregator` as needed.

## API Documentation
The api-aggregator module allows for all microservices documentation to be available in one page.
Access Swagger UI for all the services at:

```
http://localhost:8080/swagger-ui.html
```

This documentation contains all endpoints for all the microservices

## Notes

-   Make sure all services are running for inter-service communication.
-   For any issues, refer to the logs for debugging.

---

Thank you!
