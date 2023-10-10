INSERT INTO cart (user_id, status) VALUES (1, 'DRAFT');
INSERT INTO cart (user_id, status) VALUES (2, 'DRAFT');
INSERT INTO cart (user_id, status) VALUES (1, 'SUBMITTED');

INSERT INTO product (precio,nombre, peso) VALUES (20,'balon', 2);
INSERT INTO product (precio,nombre, peso) VALUES (23,'zapato', 4);
INSERT INTO product (precio,nombre, peso) VALUES (5,'tv', 6);

INSERT INTO cart_products (products, cart_id, products_key) VALUES (3, 3, 2);
