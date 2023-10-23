INSERT INTO cart (id, user_id, status, final_price, final_weight) VALUES (1, 1, 'DRAFT', 0, 0);
INSERT INTO cart (id, user_id, status, final_price, final_weight) VALUES (2, 1, 'DRAFT', 0, 0);
INSERT INTO cart (id, user_id, status, final_price, final_weight) VALUES (3, 2, 'SUBMITTED', 30, 40);

--INSERT INTO product (id, storage_quantity) VALUES (1, 30);
--INSERT INTO product (id, storage_quantity) VALUES (2, 60);

INSERT INTO cart_product(cart_id, product, quantity, valid) VALUES (1, 1, 20, 0);
INSERT INTO cart_product(cart_id, product, quantity, valid) VALUES (2, 2, 10, 0);

INSERT INTO country (name, tax_percentage) VALUES ('Spain'      , 21);
INSERT INTO country (name, tax_percentage) VALUES ('Stony'      , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Finland'    , 24);
INSERT INTO country (name, tax_percentage) VALUES ('France'     , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Italy'      , 22);
INSERT INTO country (name, tax_percentage) VALUES ('Portugal'   , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Greece'     , 23);

INSERT INTO payment (payment_method, charge_percentage) VALUES ('Bank transfer'    , 2);
INSERT INTO payment (payment_method, charge_percentage) VALUES ('VISA'             , 2);
INSERT INTO payment (payment_method, charge_percentage) VALUES ('Paypal'           , 2);
