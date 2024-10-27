# Online Banking Management System

## Video Demonstration
You can watch the video demonstration of the project [here](./working/Project2-working.mp4).
https://github.com/user-attachments/assets/58dfb5aa-435c-4db5-ab00-f0e1192d921a


## Table of Contents
- [Introduction](#introduction)
- [Aim](#aim)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)

## Introduction
This project is an Online Banking Management System that provides a user-friendly interface for managing banking operations. Users can create accounts, transfer funds, and view transaction history.

## Aim
Design a comprehensive online banking system with features like account management, fund transfers, and transaction history.


## Features
- User registration and login
- Create and manage bank accounts
- Fund transfer between accounts
- View transaction history
- Error handling and user feedback

## Technologies Used
- Java
- Spring Boot
- Thymeleaf
- MySQL
- Bootstrap
- JavaScript

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Atv23/Project2-CodeClauseJavaInternship.git
    ```

2. Navigate to the project directory:
    ```bash
    cd YourProjectName
    ```

3. Set up the MySQL database and configure application properties.

4. Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```

## Usage
1. Open your browser and navigate to `http://localhost:8080`.
2. Follow the on-screen instructions to create an account, transfer funds, and view transactions.

## API Endpoints

| Method | Endpoint                      | Description                             |
|--------|-------------------------------|-----------------------------------------|
| GET    | /                             | Display home page                       |
| GET    | /accounts                     | View all accounts                       |
| GET    | /delete-account               | Delete an account by ID                 |
| GET    | /add-account                  | Show form to add a new account          |
| POST   | /add-account                  | Create a new bank account               |
| GET    | /fund-transfer                | Display fund transfer page              |
| POST   | /transfer-funds               | Transfer funds between accounts         |
| GET    | /transaction-history          | View transaction history                |
| GET    | /delete-transaction/{id}      | Delete a transaction by ID              |

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue.

