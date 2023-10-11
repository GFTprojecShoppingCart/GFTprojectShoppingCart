INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (1, 'DRAFT', 0, 0);
INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (2, 'DRAFT', 0, 0);
INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (1, 'SUBMITTED', 4.5, 0);

INSERT INTO product (price,name, weight) VALUES (20,'balon', 2);
INSERT INTO product (price,name, weight) VALUES (23,'zapato', 4);
INSERT INTO product (price,name, weight) VALUES (5,'tv', 6);

INSERT INTO cart_products (products, cart_id, products_key) VALUES (3, 3, 2);
