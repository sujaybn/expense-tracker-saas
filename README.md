# Expense Tracker SaaS  
A multi-tenant expense tracking backend built with Spring Boot, MySQL, and a clean layered architecture. The goal of this project is to demonstrate a simple yet well-structured SaaS platform where multiple tenants can register, add users, and manage expenses independently.

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

---

---

## DB Schema

```mermaid
erDiagram
    DEPARTMENTS ||--o{ USERS : has
    USERS ||--o{ EXPENSES : creates
    CATEGORIES ||--o{ EXPENSES : tagged_as
    DEPARTMENTS ||--o{ EXPENSES : owns

    DEPARTMENTS {
        int dept_id PK
        string name
        int manager_id FK
        decimal budget
    }

    USERS {
        int user_id PK
        string name
        string email
        string password
        string role
        int dept_id FK
        boolean is_active
    }

    CATEGORIES {
        int category_id PK
        string name
    }

    EXPENSES {
        int expense_id PK
        int user_id FK
        int dept_id FK
        int category_id FK
        date date
        decimal amount
        string description
        string status
    }
```

# Multi-tenant DB design (draft)
```mermaid
erDiagram
    TENANTS ||--o{ DEPARTMENTS : owns
    TENANTS ||--o{ USERS : owns
    TENANTS ||--o{ CATEGORIES : owns
    TENANTS ||--o{ EXPENSES : owns

    DEPARTMENTS ||--o{ USERS : has
    USERS ||--o{ EXPENSES : creates
    CATEGORIES ||--o{ EXPENSES : tagged_as
    DEPARTMENTS ||--o{ EXPENSES : owns

    TENANTS {
        int tenant_id PK
        string tenant_name
        string domain
        datetime created_at
        boolean is_active
    }

    DEPARTMENTS {
        int dept_id PK
        int tenant_id FK
        string name
        int manager_id FK
        decimal budget
    }

    USERS {
        int user_id PK
        int tenant_id FK
        string name
        string email
        string password
        string role
        int dept_id FK
        boolean is_active
    }

    CATEGORIES {
        int category_id PK
        int tenant_id FK
        string name
    }

    EXPENSES {
        int expense_id PK
        int tenant_id FK
        int user_id FK
        int dept_id FK
        int category_id FK
        date date
        decimal amount
        string description
        string status
    }
```

## Running the Application

### Prerequisites

- Java 17  
- Maven  
- MySQL 8
- npm

### Setup

Clone the repository:

Clone the repo:
```
git clone https://github.com/sujaybn/expense-tracker-saas
cd expense-tracker-saas
```
Update application.properties with your MySQL credentials.

### Run the backend server:
```
run ExpenseTrackerApiApplication.java
```

### Run the frontend server:
In a separate terminal window, run below commands
```
cd expense-tracker-frontend
npm install
npm run dev
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

- Java 17
- Spring Boot 3  
- Spring Security Basic Auth  
- MySQL + Spring Data JPA  
- Lombok  
- Swagger / Springdoc OpenAPI  
- Maven
- REACT

---



## Tables and Relationships

### **Tenants**
| Column | Type | Description |
|---------|------|-------------|
| `tenant_id` | INT (PK) | Unique ID for each department |
| `name` | VARCHAR | Department name |


Each tenant can have one or more users.

---

### **Users**
| Column | Type | Description |
|---------|------|-------------|
| `user_id` | INT (PK) | Unique ID for the user |
| `name` | VARCHAR | Full name of the user |
| `email` | VARCHAR | Login email |
| `password_hash` | VARCHAR | Encrypted password |
| `role` | ENUM | Role type: EMPLOYEE / APPROVER / ADMIN |
| `tenant_id` | INT (FK to departments.dept_id) | Department the user belongs to |
| `is_active` | BOOLEAN | Whether the user account is active |

Each user belongs to a department and can create multiple expenses.

---

### **Categories**
| Column | Type | Description |
|---------|------|-------------|
| `category_id` | INT (PK) | Unique ID for each category |
| `name` | VARCHAR | Expense category name (e.g., Travel, Food, Office Supplies) |

---

### **Expenses**
| Column | Type | Description |
|---------|------|-------------|
| `expense_id` | INT (PK) | Unique ID for each expense |
| `user_id` | INT (FK to users.user_id) | Who created the expense |
| `dept_id` | INT (FK to departments.dept_id) | Which department this expense belongs to |
| `category_id` | INT (FK to categories.category_id) | Expense category |
| `date` | DATE | Expense date |
| `amount` | DECIMAL | Amount spent |
| `description` | VARCHAR | Short description of the expense |
| `status` | ENUM | Status: SUBMITTED / APPROVED / REJECTED / PAID |

