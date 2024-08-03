# SpringBootLibraryManagement

## Overview

`SpringBootLibraryManagement` is a Spring Boot application designed to manage library operations. It provides functionalities for managing books, users, and library transactions in a user-friendly interface.

## Features

- **Book Management**: Add, update, delete, and view books in the library.
- **User Management**: Register and manage users, including borrowing and returning books.
- **Transaction Management**: Track book borrowing and returning transactions.
- **REST API**: Expose endpoints for integrating with other systems or for building a frontend application.
- **Spring Security**: Secure endpoints with user authentication and authorization.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven (for dependency management)
- Spring Boot

## Getting Started

### Installation

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/lkumarra/SpringBootLibraryManagement.git
    cd SpringBootLibraryManagement
    ```

2. **Install Dependencies:**

    ```bash
    mvn install
    ```

3. **Run the Application:**

    ```bash
    mvn spring-boot:run
    ```

   The application will start on `http://localhost:8080`.

### Configuration

1. **Database Configuration:**

   Configure your database settings in `src/main/resources/application.properties`. Update the following properties as needed:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/library
    spring.datasource.username=root
    spring.datasource.password=password
    ```

2. **Application Properties:**

   Customize other application properties in `src/main/resources/application.properties` to suit your needs.

### Usage

1. **Access the Application:**

   Open your browser and navigate to `http://localhost:8080` to access the library management application.
