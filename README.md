# Moni Challenge

This is a Spring Boot project that implements a Moni Challenge. It allows you to manage a fleet of drones and medication deliveries.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 11 or later installed.
- Maven (for building the project).
- Docker (optional, for running a database in a container).

## Getting Started

To get started with this project, follow these steps:

### 1. Clone the Repository

Clone this repository to your local machine:

```shell
git clone https://github.com/joelezeu/moni-challenge.git
```

### 2. Build the Project

```
cd moni-challange
mvn clean package
```

### 3. Run the Application

You can run the application locally with an in-memory H2 database or a PostgreSQL database in a Docker container.

The application will be accessible at http://localhost:8080.

```
docker run --name drone-db -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=dronedb -p 5432:5432 -d postgres
```

### 4. Test the API
You can use tools like Postman or curl to test the API endpoints. Refer to the API documentation below for available endpoints and request/response formats.

### 5. Run Unit Tests
```
mvn test
```

