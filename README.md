# 🛡️ Spring Boot Security Application

## Overview
This project demonstrates how to secure a web application using **Spring Boot** and **Spring Security**. It provides basic **authentication** and **authorization** features, utilizing **MySQL** for user storage and **Thymeleaf** templates for the user interface.

---

## Key Features
- 🔒 **User authentication and authorization** using Spring Security
- 🧑‍💻 **Custom login page** for user login
- 📁 **MySQL database** for storing user information
- 🎭 **Role-based Access Control (RBAC)** allowing different views for ADMIN and USER
- 🖥️ **Thymeleaf templates** for building the user interface

---

## Installation Guide
1. Create a database name `securitydb`

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Usage Guide
- Access the application at `http://localhost:8080`
- Log in with the following credentials:
  - User:
      - Username: `user1`
      - Password: `user123`
  - Admin:
      - Username: `admin`
      - Password: `admin123`

