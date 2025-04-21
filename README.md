# Finance Tracker App

A simple monolithic web application built with **Spring Boot** to track expenses, budgets, and manage payments. The app
allows users to log their spending, create budgets, and monitor recurring payments. This application follows a *
*Hexagonal Architecture** (also known as Ports and Adapters) for a clean and maintainable codebase.

---

## 🏗️ Architecture

The app is structured using **Hexagonal Architecture**, which separates concerns into the following layers:

- **Domain Layer**: Contains the core business logic and domain models (e.g., `Expense`, `Budget`, `PaymentMethod`).
- **Application Layer**: Contains the use cases and application services that coordinate the interactions between the
  domain layer and the outside world.
- **Adapters Layer**: Manages the communication with external systems (e.g., databases, web services).
- **Configuration Layer**: Handles Spring Boot configuration and dependency injection.

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

### Running the Project Locally

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/finance-tracker.git
   cd finance-tracker
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
- `GET /api/expenses`: Retrieve a list of all expenses
- `GET /api/expenses/{id}`: Retrieve a specific expense by ID
- `PUT /api/expenses/{id}`: Update an expense by ID
- `DELETE /api/expenses/{id}`: Delete an expense by ID

### Budgets

- `POST /api/budgets`: Add a new budget
- `GET /api/budgets`: Retrieve a list of all budgets
- `PUT /api/budgets/{id}`: Update a budget by ID
- `DELETE /api/budgets/{id}`: Delete a budget by ID

---

## 🧪 Running Tests

You can run unit and integration tests using Maven:

```bash
mvn test
```

---

## 🛠️ Development

### Add a New Feature

1. Create a new domain model (e.g., `Income`, `Transaction`).
2. Define use cases for the new feature in the `application/port/in` folder.
3. Implement the use cases in the `application/service` folder.
4. Create necessary repositories in the `adapters/out` folder (e.g., JPA repository).
5. Create an API controller in the `adapters/in` folder for the feature.
6. Write tests for the new feature.
7. Run and test locally.

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

