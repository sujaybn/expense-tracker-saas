SELECT * from USERS;


DROP TABLE expenses;
DROP TABLE users;
DROP TABLE categories;
DROP TABLE tenants;
DROP TABLE departments;


SELECT * FROM users; WHERE email = 'john@example.com';

SELECT id, name, tenant_id FROM categories;

INSERT INTO tenants (name) VALUES ('Default Tenant');
INSERT INTO categories (name, tenant_id) VALUES ('General', 1);



ALTER TABLE users ADD COLUMN name VARCHAR(255);

SELECT * from tenants;

UPDATE users
SET role = 'MANAGER'
WHERE id =10;

INSERT INTO users (
    email,
    password_hash,
    role,
    tenant_id,
    name
) VALUES (
    'super_admin@example.com',
    'password_admin',
    'SUPER_ADMIN',
    1,
    'Albus');



