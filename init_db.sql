-- База данных online_store
CREATE DATABASE online_store;

-- Создание таблицы продуктов
CREATE TABLE IF NOT EXISTS products
(
    id    UUID PRIMARY KEY,
    name  VARCHAR(255)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- Создание таблицы заказов
CREATE TABLE IF NOT EXISTS orders
(
    id            UUID PRIMARY KEY,
    customer_name VARCHAR(255)   NOT NULL,
    total_price   DECIMAL(10, 2) NOT NULL
);

-- Создание таблицы связи многие-ко-многим
CREATE TABLE IF NOT EXISTS order_products
(
    order_id   UUID NOT NULL,
    product_id UUID NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

-- Добавление данных

INSERT INTO products (id, name, price)
VALUES ('4caffda7-490e-44be-aebf-39adedee5e61', 'Ноутбук', 999.99),
       ('e37787a1-f44a-49b3-9e66-ad67066172b1', 'Смартфон', 499.99);

INSERT INTO orders (id, customer_name, total_price)
VALUES ('00781535-6ac7-4375-a68f-394b3456c126', 'Никита', 1499.98);

INSERT INTO order_products
VALUES ('00781535-6ac7-4375-a68f-394b3456c126', '4caffda7-490e-44be-aebf-39adedee5e61'),
       ('00781535-6ac7-4375-a68f-394b3456c126', 'e37787a1-f44a-49b3-9e66-ad67066172b1');