SELECT * from USERS;


DROP TABLE expenses;
DROP TABLE users;
DROP TABLE categories;
DROP TABLE tenants;
DROP TABLE departments;


SELECT * FROM users WHERE email = 'john@example.com';

SELECT id, name, tenant_id FROM categories;

INSERT INTO tenants (name) VALUES ('Default Tenant');
INSERT INTO categories (name, tenant_id) VALUES ('General', 1);