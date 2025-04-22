# Finance Tracker App

A simple monolithic web application built with **Spring Boot** to track expenses, budgets, and manage payments. The app
allows users to log their spending, create budgets, and monitor recurring payments. This application follows a *
*Hexagonal Architecture** (also known as Ports and Adapters) for a clean and maintainable codebase.

---

## 🏗️ Architecture

The app is structured using **Hexagonal Architecture**, which separates concerns into the following layers:

- **Domain Layer**: Contains the core business logic and domain models (e.g., `Expense`, `Budget`, `PaymentMethod`), as well as domain services and enumerations.
- **Application Layer**: Contains the use cases and application services that coordinate interactions between the domain layer and the outside world, including inbound and outbound ports.
- **Infrastructure Layer**: Manages the communication with external systems, including outbound adapters (e.g., repositories, mappers), inbound adapters (e.g., controllers), and any external services like databases, APIs, or message brokers.
- **Shared Layer**: Includes shared components that can be used across multiple layers, such as utility classes or shared configuration settings.

---

## 📁 Project Structure

```text
ExpenseTrackerApp/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── expensetrackerapp/
│                   ├── application/                         # Application Layer (business logic and use cases)
│                   │   ├── port/                            # Port interfaces for inbound (use cases) and outbound (repositories). Entry and exit points for the application.
│                   │   │   ├── in/                          # Inbound ports (use case interfaces). Input.
│                   │   │   │   └── SaveExpense              # Specific use case related to saving expenses
│                   │   │   └── out/                         # Outbound ports (repository interfaces). Output.
│                   │   └── service/                         # Application Services (use case implementations)
│                   ├── domain/                              # Domain Layer (core business logic and domain models)
│                   │   ├── model/                           # Core entities representing domain objects (POJOs)
│                   │   ├── service/                         # Domain services for complex business logic
│                   │   └── enums/                           # Enums related to domain logic
│                   ├── dto/                                 # Data Transfer Objects (used for communication between layers)
│                   ├── infrastructure/                      # Infrastructure Layer (implementation details such as DB, external services)
│                   │   ├── inbound/                         # Inbound Adapters (controllers that handle incoming requests)
│                   │   │   └── controller                   # Controllers for handling HTTP requests
│                   │   │       └── rest                     # REST API controllers
│                   │   └── outbound/                        # Outbound Adapters (JPA implementations for persistence)
│                   │       ├── adapters/                    # Adapters for mapping models to database entities
│                   │       ├── entities/                    # JPA entity classes for database representation
│                   │       ├── mappers/                     # Mappers for converting between entities, DTOs, requests, and domain models
│                   │       └── repositories/                # JPA repository interfaces for data access
│                   ├── shared/                              # Shared utilities, constants, or helpers
│                   ├── ExpenseTrackerAppApplication.java    # Main Spring Boot application class (entry point)
│                   └── resources/                           # Resources folder (configuration and static resources)
│                       ├── application.yml                  # Configuration for Spring Boot (e.g., DB settings, properties)
│                       └── log4j2.xml                       # Logging configuration (Log4j2 settings)
└── pom.xml                                                  # Maven or Gradle configuration file for dependencies and build settings

```
---
# 🗃️ Example of Architecture Flow

1. **Client → HTTP POST** `/expenses` with JSON payload.
2. **ExpenseController** deserializes into `SaveExpenseRequest` and calls `saveExpenseUseCase.saveExpense(...)`.
3. **SaveExpenseService** (application service) maps the DTO → `Expense` domain object.
4. Service calls the outbound port `SaveExpensePort.saveExpense(...)`.
5. **SaveExpenseRepository** (adapter) maps domain → `ExpenseEntity`.
6. Adapter calls **ExpenseRepository** (Spring Data JPA) to `save(entity)`.
7. JPA persists the row in your **expenses** table.
8. Control returns up the stack, and the HTTP **200** (or **201**) is returned to the client.

This clean layering ensures each component has a single responsibility and depends only on narrow, well‑defined interfaces.

---

## 📝 Features

- **Expense Management**: Track all your expenses, including name, description, amount, date/time, payment method (cash,
  card), and more.
- **Budgeting**: Set and track budgets for categories (e.g., groceries, entertainment).
- **Recurring Payments**: Track recurring payments and mark them as paid or to be paid in installments.
- **Invoice Management**: Optionally flag transactions that require an invoice.
- **Payment Methods**: Track payments with various payment methods, such as cash, credit cards, etc.

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.4
- **Database**: PostgreSQL
- **JPA**: Hibernate for ORM
- **Web Framework**: Spring MVC for controllers
- **Hexagonal Architecture**: Clean and maintainable separation of concerns
- **Lombok**: To reduce boilerplate code (getters, setters, constructors)

---

## 🚀 Getting Started

### Prerequisites

- Java 21
- Maven 3.x
- PostgreSQL
- Optional: IDE (IntelliJ IDEA, Eclipse, etc.)

### Docker-Compose for Database Deployment

The `docker-compose.yml` file contains the necessary configuration to deploy a container for the database. This file sets up a local instance of the database (e.g., PostgreSQL) so that the application can communicate with it. It defines the database container, specifies the image to use, and sets up necessary environment variables like database username, password, and connection details.

The `docker-compose.yml` file typically includes the following:

- **Database Service**: A containerized service for the database, such as PostgreSQL, that is configured with the necessary environment variables (e.g., `DB_USERNAME`, `DB_PASSWORD`).
- **Networking**: Ensures that the application and database containers can communicate with each other over a defined network.
- **Volumes**: Provides persistent storage for the database to retain data across container restarts.

To start the database container, run:

```bash
docker-compose up -d
```

### Running the Project Locally

1. **Clone the repository**:
   ```bash
   git clone https://github.com/PotatoDoge/ExpenseTrackerApp
   cd ExpenseTrackerApp
   ```
2. **Install dependencies:y**:
   ```bash
   mvn clean install
   ```
   or
    ```bash
       ./mvnw clean install
    ```
3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   or
    ```bash
       ./mvnw spring-boot:run
    ```

4. **Access the app: Open a browser and navigate to http://localhost:8080.**

---

## 💻 API Endpoints

### Expenses

- `POST /api/expenses`: Add a new expense

---

## 🧪 Running Tests

You can run unit and integration tests using Maven:

```bash
mvn test
```

or

```bash
./mvnw test
```

---

## 🛠️ Development

### Add a New Feature

1. Create a new domain model (e.g., `Income`, `Transaction`) in the `domain/model` folder.
2. Define use cases for the new feature by creating interfaces in the `application/port/in` folder.
3. Implement the use cases in the `application/service` folder.
4. Create necessary outbound repositories in the `infrastructure/outbound/repositories` folder (e.g., JPA repository).
5. Create mappers in the `infrastructure/outbound/mappers` folder to map domain models to entities.
6. Create an API controller for the feature in the `infrastructure/inbound/controller/rest` folder.
7. Write tests for the new feature in the appropriate test directories.
8. Run and test the application locally.

---

## 🤝 Contributing

1. Fork the repository.
2. Create a new branch:

```bash
git checkout -b feature/my-feature
```

3. Make your changes.
4. Commit your changes:

```bash
git commit -am 'Add new feature'
```

5. Push to your branch:

```bash
git push origin feature/my-feature
```

6. Open a pull request.

---

## 📜 License

This project is licensed under the MIT License - see the `LICENSE` file for details.

---

## 📚 Further Reading

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Hexagonal Architecture Guide](https://alistair.cockburn.us/hexagonal-architecture/)

