INSERT INTO wr_role (id, name) VALUES
(1, 'SYSTEM_ADMIN'), (2, 'WAREHOUSE_MANAGER'), (3, 'CLIENT');

INSERT INTO wr_user (id, username, email, password, active) VALUES
(1, 'test', 'test@gmail.com', 'test', '1'),
(2, 'test', 'test@gmail.com', 'test', '1'),
(3, 'test', 'test@gmail.com', 'test', '1'),
(4, 'test', 'test@gmail.com', 'test', '1'),
(5, 'test', 'test@gmail.com', 'test', '1');

INSERT INTO wr_user_role (fk_user, fk_role) VALUES
(1,1), (2,2), (3,3), (4,1), (5,2);

INSERT INTO wr_customer (id, first_name, last_name, address, postal_code, city, state, phone, fk_user) VALUES
(1, 'Test', 'Test', 'Test', 0000, 'Test', 'Test', 'Test', 1),
(2, 'Test', 'Test', 'Test', 0000, 'Test', 'Test', 'Test', 2),
(3, 'Test', 'Test', 'Test', 0000, 'Test', 'Test', 'Test', 3),
(4, 'Test', 'Test', 'Test', 0000, 'Test', 'Test', 'Test', 4),
(5, 'Test', 'Test', 'Test', 0000, 'Test', 'Test', 'Test', 5);

INSERT INTO wr_order (id, order_number, submitted_date, deadline_date, status, order_price, fk_customer, order_quantity) VALUES
(1, 'Test', null, null, null, 9.0, 1, 34),
(2, 'Test', null, null, null, 9.0, 1, 34),
(3, 'Test', null, null, null, 9.0, 1, 34);

INSERT INTO wr_inventory (id, name, quantity, price, active) VALUES
(1, 'Test', 100, 100.0, '1'),
(2, 'Test', 100, 100.0, '1'),
(3, 'Test', 100, 100.0, '1'),
(4, 'Test', 100, 100.0, '1');