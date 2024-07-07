-- Создание таблицы продуктов
CREATE TABLE IF NOT EXISTS product
(
    id                BIGSERIAL PRIMARY KEY,
    description       VARCHAR(255),
    price             DECIMAL,
    quantity_in_stock INTEGER,
    wholesale_product BOOLEAN
);

-- Вставка данных в таблицу продуктов
INSERT INTO product (id, description, price, quantity_in_stock, wholesale_product)
VALUES (1, 'Milk', 1.07, 10, TRUE),
       (2, 'Cream 400g', 2.71, 20, TRUE),
       (3, 'Yogurt 400g', 2.10, 7, TRUE),
       (4, 'Packed potatoes 1kg', 1.47, 30, FALSE),
       (5, 'Packed cabbage 1kg', 1.19, 15, FALSE),
       (6, 'Packed tomatoes 350g', 1.60, 50, FALSE),
       (7, 'Packed apples 1kg', 2.78, 18, FALSE),
       (8, 'Packed oranges 1kg', 3.20, 12, FALSE),
       (9, 'Packed bananas 1kg', 1.10, 25, TRUE),
       (10, 'Packed beef fillet 1kg', 12.8, 7, FALSE),
       (11, 'Packed pork fillet 1kg', 8.52, 14, FALSE),
       (12, 'Packed chicken breasts1kgSour', 10.75, 18, FALSE),
       (13, 'Baguette 360g', 1.30, 10, TRUE),
       (14, 'Drinking water 1.5l', 0.80, 100, FALSE),
       (15, 'Olive oil 500ml', 5.30, 16, FALSE),
       (16, 'Sunflower oil 1l', 1.20, 12, FALSE),
       (17, 'Chocolate Ritter sport 100g', 1.10, 50, TRUE),
       (18, 'Paulaner 0.5l', 1.10, 100, FALSE),
       (19, 'Whiskey Jim Beam 1l', 13.99, 30, FALSE),
       (20, 'Whiskey Jack Daniels 1l', 17.19, 20, FALSE);

-- Создание таблицы дисконтных карт
CREATE TABLE IF NOT EXISTS discount_card
(
    id              BIGSERIAL PRIMARY KEY,
    number_discount INTEGER,
    amount          SMALLINT
);

-- Вставка данных в таблицу дисконтных карт
INSERT INTO discount_card (id, number_discount, amount)
VALUES (1, 1111, 3),
       (2, 2222, 3),
       (3, 3333, 4),
       (4, 4444, 5);