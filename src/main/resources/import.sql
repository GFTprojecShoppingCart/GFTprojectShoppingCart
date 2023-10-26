INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (1, 'DRAFT', 0, 0);
INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (1, 'SUBMITTED', 4.5, 0);
INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (2, 'SUBMITTED', 30, 40);

INSERT INTO cart_product(cart_id, product, quantity, valid) VALUES (1, 1, 20, 0);
INSERT INTO cart_product(cart_id, product, quantity, valid) VALUES (2, 2, 10, 0);

INSERT INTO country (name, tax_percentage) VALUES ('Spain'      , 21);
INSERT INTO country (name, tax_percentage) VALUES ('Estonia'    , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Finland'    , 24);
INSERT INTO country (name, tax_percentage) VALUES ('France'     , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Italy'      , 22);
INSERT INTO country (name, tax_percentage) VALUES ('Portugal'   , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Greece'     , 23);

INSERT INTO payment (payment_method, charge_percentage) VALUES ('Credit Card'    , 0);
INSERT INTO payment (payment_method, charge_percentage) VALUES ('Stripe'         , 2);
INSERT INTO payment (payment_method, charge_percentage) VALUES ('PayPal'         , 1);
