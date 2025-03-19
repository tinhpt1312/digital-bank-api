# Digital Bank API

A comprehensive banking API system built with Spring Boot that provides secure digital banking services.

## Overview

Digital Bank API is a robust backend system designed to support modern banking operations including account management, transactions, card services, and user authentication. The system implements role-based access control with fine-grained permissions to ensure secure access to banking features.

## Features

- **User Management**
  - Registration and authentication
  - Role-based access control
  - Profile management
  - Email verification

- **Account Services**
  - Account creation and management
  - Multiple account types support
  - Balance tracking
  - Account status management

- **Transaction Processing**
  - Deposits and withdrawals
  - Fund transfers between accounts
  - Payment processing
  - Transaction history and statements

- **Card Management**
  - Card issuance and activation
  - Different card types support
  - Card status management

- **Security**
  - JWT-based authentication
  - Permission-based authorization
  - Secure password handling

- **Additional Features**
  - AWS S3 integration for file storage
  - Email notifications
  - API documentation with Swagger

## Technical Stack

- **Framework**: Spring Boot
- **Database**: JPA/Hibernate, MySQL
- **Security**: Spring Security, JWT
- **Build Tool**: Maven
- **Documentation**: Swagger
- **Cloud Services**: AWS S3
- **Migration**: Flyway

## Project Structure

The project follows a standard Spring Boot application structure:

- `config`: Configuration classes for security, AWS, email, etc.
- `controller`: REST API endpoints
- `dto`: Data Transfer Objects for request/response handling
- `entity`: JPA entities representing database tables
- `repository`: Data access layer
- `service`: Business logic implementation
- `type`: Enumerations for various types used in the system
- `utils`: Utility classes

## Installation and Setup Guide

### Prerequisites

- Java 11 or higher
- Maven
- MySQL
- Git

### Installation Steps

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-username/digital-bank-api.git
   cd digital-bank-api
   ```
2. Configure database connection in `application.properties`
3. Run the application:
