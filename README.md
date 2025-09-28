# Online Banking Management System


## Table of Contents
- [Introduction](#introduction)
- [Aim](#aim)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Contributing](#contributing)

## Introduction
This project is a comprehensive Online Banking Management System built with Spring Boot. It provides a secure, user-friendly web interface for managing banking operations, including account management, fund transfers, loan processing, and transaction history. The system supports role-based access with separate functionalities for administrators and regular users.

## Aim
To design and implement a robust online banking system that offers secure account management, fund transfers, loan services, and comprehensive transaction tracking with role-based access control.

## Features
- **User Authentication**: Secure login and registration system with role-based access (Admin and User roles)
- **Admin Dashboard**: Overview of system statistics including account count, loan count, and total balance
- **Account Management**:
  - Create, view, and delete bank accounts (Admin)
  - View personal accounts (User)
- **Fund Transfers**: Transfer money between accounts with validation
- **Transaction History**: View and manage transaction records (Admin can delete transactions)
- **Loan Management**:
  - Submit loan requests (User)
  - Approve/reject loan applications (Admin)
  - Make loan payments (User)
- **Security**: Spring Security integration with password encryption
- **Responsive UI**: Bootstrap-based frontend with Thymeleaf templates
- **Error Handling**: Comprehensive error pages and user feedback

## Technologies Used
- **Java 21**: Programming language
- **Spring Boot 3.3.5**: Framework for building the application
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data persistence
- **Hibernate**: ORM for database operations
- **MySQL**: Relational database
- **Thymeleaf**: Server-side Java template engine
- **Bootstrap**: CSS framework for responsive design
- **JavaScript**: Client-side scripting
- **Maven**: Build and dependency management

## Prerequisites
- Java 21 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/groverPranjal/Online-Bank-Managment

2. Navigate to the project directory:
   ```bash
   cd Online-Banking
   ```

3. Set up the MySQL database (see [Database Setup](#database-setup) section).

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Database Setup
1. Install and start MySQL server.

2. Create a database named `demobank`:
   ```sql
   CREATE DATABASE demobank;
   ```

3. Create a user with the following credentials (or update `application.properties` accordingly):
   - Username: `root`
   - Password: `9878`

4. Grant necessary privileges:
   ```sql
   GRANT ALL PRIVILEGES ON demobank.* TO 'root'@'localhost' IDENTIFIED BY '9878';
   FLUSH PRIVILEGES;
   ```

The application will automatically create the required tables using Hibernate's DDL auto-update feature.

## Usage
1. Open your browser and navigate to `http://localhost:8080`.

2. **Admin Access**: Login with default admin credentials:
   - Username: `admin`
   - Password: `admin123`

3. **User Access**: Register a new user account or login with existing credentials.

4. **Navigation**:
   - Admin users can access account management, loan approvals, and system overview.
   - Regular users can manage their accounts, transfer funds, view transactions, and handle loans.

## API Endpoints

| Method | Endpoint                      | Description                             | Role Required |
|--------|-------------------------------|-----------------------------------------|---------------|
| GET    | /                             | Display home page                       | ADMIN         |
| GET    | /accounts                     | View all accounts                       | ADMIN         |
| GET    | /my-accounts                  | View user's accounts                    | USER          |
| GET    | /delete-account               | Delete an account by ID                 | ADMIN         |
| GET    | /add-account                  | Show form to add a new account          | ADMIN         |
| POST   | /add-account                  | Create a new bank account               | ADMIN         |
| GET    | /fund-transfer                | Display fund transfer page              | ADMIN/USER    |
| POST   | /transfer-funds               | Transfer funds between accounts         | ADMIN/USER    |
| GET    | /transaction-history          | View transaction history                | ADMIN/USER    |
| GET    | /delete-transaction/{id}      | Delete a transaction by ID              | ADMIN         |
| GET    | /admin/dashboard              | Admin dashboard with statistics         | ADMIN         |
| GET    | /loan-approval                | View pending loan approvals             | ADMIN         |
| POST   | /approve-loan                 | Approve a loan application              | ADMIN         |
| POST   | /reject-loan                  | Reject a loan application               | ADMIN         |
| GET    | /loan-request                 | Show loan request form                  | USER          |
| POST   | /loan-request                 | Submit a loan request                   | USER          |
| GET    | /loan-payment                 | Show loan payment form                  | USER          |
| POST   | /loan-payment                 | Make a loan payment                     | USER          |

## Testing
Run the test suite using Maven:
```bash
./mvnw test
```

The project includes unit tests for controllers, services, and repositories using Spring Boot Test framework.

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue.
