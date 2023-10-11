INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (1, 'DRAFT'    , 0, 0);
INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (2, 'DRAFT'    , 0, 0);
INSERT INTO cart (user_id, status, final_price, final_weight) VALUES (1, 'SUBMITTED', 4.5, 0);

INSERT INTO product (price,name, weight) VALUES (20 ,'balon'    , 2);
INSERT INTO product (price,name, weight) VALUES (23 ,'zapato'   , 4);
INSERT INTO product (price,name, weight) VALUES (5  ,'tv'       , 6);

INSERT INTO cart_products (products, cart_id, products_key) VALUES (3, 3, 2);

INSERT INTO country (name, tax_percentage) VALUES ('Spain'      , 21);
INSERT INTO country (name, tax_percentage) VALUES ('Stony'      , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Finland'    , 24);
INSERT INTO country (name, tax_percentage) VALUES ('France'     , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Italy'      , 22);
INSERT INTO country (name, tax_percentage) VALUES ('Portugal'   , 20);
INSERT INTO country (name, tax_percentage) VALUES ('Greece'     , 23);

INSERT INTO payments (payment_method, charge_percentage) VALUES ('Bank transfer'    , 2);
INSERT INTO payments (payment_method, charge_percentage) VALUES ('VISA'             , 2);
INSERT INTO payments (payment_method, charge_percentage) VALUES ('Paypal'           , 2);
