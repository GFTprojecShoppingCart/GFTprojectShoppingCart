INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (1, 'DRAFT'    , 0, 0);
INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (2, 'DRAFT'    , 0, 0);
INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (1, 'SUBMITTED', 4.5, 0);

INSERT INTO cart_invalid_products (cart_id, invalid_products ) VALUES (1, 1);
INSERT INTO cart_invalid_products (cart_id, invalid_products ) VALUES (1, 5);
INSERT INTO cart_invalid_products (cart_id, invalid_products ) VALUES (1, 7);
INSERT INTO cart_invalid_products (cart_id, invalid_products ) VALUES (2, 7);

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
