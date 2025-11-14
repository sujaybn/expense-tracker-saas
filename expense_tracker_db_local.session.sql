
CREATE TABLE tenants (
    tenant_id INT AUTO_INCREMENT PRIMARY KEY,
    tenant_name VARCHAR(100) NOT NULL,
    admin_email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    tenant_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('EMPLOYEE','APPROVER','FINANCE','TENANT_ADMIN') NOT NULL,
    dept_id INT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id),
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);


CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    tenant_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id)
);


CREATE TABLE expenses (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    tenant_id INT NOT NULL,
    user_id INT NOT NULL,
    dept_id INT NULL,
    category_id INT NOT NULL,
    date DATE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    description TEXT,
    status ENUM('DRAFT','SUBMITTED','APPROVED','REJECTED','PAID') DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);


drop table users;

DELETE FROM table_name
WHERE condition;
table_name: Replace this with the actual name of the table from which you want to delete records.
condition: This is a search condition that identifies the specific row(s) to be deleted. It can involve one or more columns and use various operators (e.g., =, >, <, LIKE, IN, AND, OR). 
Important Note: If you omit the WHERE clause, all records in the table will be deleted. Exercise extreme caution when using DELETE without a WHERE clause. 
Example:
To delete a customer named "Alfreds Futterkiste" from a table named Customers:
Code

DELETE FROM Customers
WHERE CustomerName = 'Alfreds Futterkiste';
To delete all customers from the Customers table where the City is 'Berlin':
Code

DELETE FROM Customers
WHERE City = 'Berlin';
To delete a record based on its primary key, which is usually the most reliable method:
Code

DELETE FROM users
WHERE id = 1;


SELECT password_hash from users;
