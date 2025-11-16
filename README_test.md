# Expense Tracker SaaS  
A multi-tenant expense tracking backend built with Spring Boot, MySQL, and a clean layered architecture. The goal of this project is to demonstrate a simple yet well-structured SaaS platform where multiple tenants can register, add users, and manage expenses independently.

This repository is created and maintained by **@sujaybn**.

---

## Overview  
Each tenant represents a company or group. A tenant can have multiple users with roles like Admin, Manager, or User. Users authenticate using Basic Auth (email + password) and can track expenses, view profiles, and manage members depending on their role.

The system includes:

- Tenant registration  
- User registration and login  
- Role-based access control  
- Expense CRUD  
- Tenant-scoped user management  
- Public Swagger documentation

This project deliberately avoids JWT for simplicity and uses Spring Security Basic Auth.

---

## Architecture  
The system follows a modular, service-oriented design with clear boundaries between controller, service, repository, and entity layers.

```mermaid
flowchart TD

A[Client / Browser / Postman] --> B[Spring Boot API]
B --> C[Controller Layer]
C --> D[Service Layer]
D --> E[Repository Layer]
E --> F[(MySQL Database)]

subgraph "Spring Boot Application"
C
D
E
end
```

## Authentication Model

The application uses Basic Auth. Every request to a protected endpoint must include the standard header:

```
Authorization: Basic base64(email:password)
```
### Example

```
Authorization: Basic c3VzZXJAbWFpbC5jb206cGFzc3dvcmQxMjM=
```

## Sequence Diagram

Here is the typical flow when a user logs in and retrieves their profile.

```mermaid
sequenceDiagram
    Client->>API: GET /api/users/me (Basic Auth)
    API->>Security: Validate credentials
    Security->>UserRepository: findByEmail()
    UserRepository-->>Security: UserEntity
    Security-->>API: Authentication success
    API->>UserService: getMyProfile()
    UserService->>UserRepository: fetch user
    UserRepository-->>UserService: UserEntity
    UserService-->>API: UserResponse
    API-->>Client: 200 OK (profile JSON)

```

## Database Schema (Simplified)
```
Tenant
- id
- name

User
- id
- name
- email
- password_hash
- role
- tenant_id (FK)

Expense
- id
- description
- amount
- date
- category
- user_id (FK)
- tenant_id (FK)
```



---

## Running the Application

### Prerequisites

- Java 17  
- Maven  
- MySQL 8

### Setup

Clone the repository:



Clone the repo:
```
git clone https://github.com/sujaybn/expense-tracker-saas
cd expense-tracker-saas
```
Update application.properties with your MySQL credentials.

### Run the server:
```
mvn spring-boot:run
```



---

## Swagger Documentation (Public)


Visit: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

You will find the complete list of available APIs.

---

## API Endpoints Summary

### Authentication

| Method | Endpoint              | Description                   |
|--------|-----------------------|-------------------------------|
| POST   | /api/auth/register    | Register a tenant + admin user |
| POST   | /api/auth/login       | Basic Auth login (dummy endpoint for clients) |

### User APIs

| Method | Endpoint             | Description                   |
|--------|----------------------|-------------------------------|
| GET    | /api/users/me        | Get logged-in user profile     |
| GET    | /api/users           | List users of the tenant       |
| PUT    | /api/users/{id}      | Update user name, role, tenant |
| DELETE | /api/users/{id}      | Delete user                   |



### Tenant APIs

| Method | Endpoint            | Description           |
|--------|---------------------|-----------------------|
| POST   | /api/tenants        | Create a new tenant    |
| GET    | /api/tenants/{id}   | Get tenant details     |

### Expense APIs

| Method | Endpoint              | Description                |
|--------|-----------------------|----------------------------|
| POST   | /api/expenses         | Create new expense          |
| GET    | /api/expenses         | List all expenses for tenant/user |
| PUT    | /api/expenses/{id}    | Update an expense           |
| DELETE | /api/expenses/{id}    | Delete an expense           |

---

## Technologies Used

- Spring Boot 3  
- Spring Security Basic Auth  
- MySQL + Spring Data JPA  
- Lombok  
- Swagger / Springdoc OpenAPI  
- Maven

---

## Project Folder Structure


```
src/main/java/com/exptracker/
  ├── controller
  ├── dto
  ├── entity
  ├── enums
  ├── repository
  ├── security
  └── service
```
