CREATE DATABASE IF NOT EXISTS dhanushmart;
USE dhanushmart;

DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS admins;

CREATE TABLE admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(30),
    role VARCHAR(30) DEFAULT 'USER',
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(30),
    address VARCHAR(255)
);

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    size VARCHAR(30),
    color VARCHAR(50),
    stock INT NOT NULL DEFAULT 0,
    image VARCHAR(255),
    category_id BIGINT,
    seller_id BIGINT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PLACED',
    shipping_address VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT INTO admins(username,password) VALUES ('admin','admin123');

INSERT INTO users(name,username,password,email,phone,role,status) VALUES ('Store Staff','staff','staff123','staff@dhanushmart.com','9999999999','USER','APPROVED');

INSERT INTO categories(name) VALUES
('T-Shirts'),('Shirts'),('Jeans'),('Trousers'),('Formal Wear'),
('Casual Wear'),('Jackets'),('Hoodies'),('Sweaters'),('Shorts'),
('Innerwear'),('Ethnic Wear'),('Accessories');

INSERT INTO products(name,description,price,size,color,stock,image,category_id) VALUES
('Classic Cotton T-Shirt','Comfortable round-neck cotton T-shirt',599,'M','Black',50,'tshirt-black.jpg',1),
('Premium Casual Shirt','Full sleeve casual shirt for men',999,'L','Blue',30,'shirt-blue.jpg',2),
('Slim Fit Jeans','Comfortable slim fit denim jeans',1499,'32','Dark Blue',25,'jeans-blue.jpg',3),
('Formal Trousers','Classic formal trousers for men',1299,'32','Black',20,'trousers-black.jpg',4),
('Winter Hoodie','Warm hooded sweatshirt',1199,'L','Grey',35,'hoodie-grey.jpg',8),
('Denim Jacket','Classic denim jacket',1899,'L','Blue',15,'jacket-blue.jpg',7),
('Men Sweater','Soft knitted sweater',1399,'M','Navy',18,'sweater-navy.jpg',9),
('Casual Shorts','Comfortable casual shorts',699,'M','Khaki',25,'shorts-khaki.jpg',10),
('Oversized Graphic T-Shirt','Trendy oversized graphic print T-shirt',749,'L','White',40,'tshirt-white.jpg',1),
('Polo T-Shirt','Smart cotton polo T-shirt',899,'M','Maroon',35,'polo-maroon.jpg',1),
('V-Neck T-Shirt','Soft V-neck everyday T-shirt',649,'XL','Navy',28,'tshirt-navy.jpg',1),
('Striped Casual Shirt','Modern striped casual shirt',1099,'M','White Blue',22,'shirt-striped.jpg',2),
('Linen Summer Shirt','Lightweight linen shirt for summer',1299,'L','Beige',18,'shirt-linen.jpg',2),
('Checked Shirt','Classic checked full-sleeve shirt',1049,'XL','Green',24,'shirt-checked.jpg',2),
('Oxford Formal Shirt','Premium Oxford formal shirt',1399,'M','White',20,'shirt-oxford.jpg',5),
('Slim Fit Black Jeans','Stretch slim fit black denim',1599,'34','Black',30,'jeans-black.jpg',3),
('Regular Fit Blue Jeans','Classic regular fit denim',1499,'34','Blue',32,'jeans-regular.jpg',3),
('Tapered Fit Jeans','Modern tapered fit jeans',1699,'32','Indigo',20,'jeans-tapered.jpg',3),
('Chino Trousers','Cotton stretch chino trousers',1199,'32','Beige',27,'chino-beige.jpg',4),
('Slim Formal Trousers','Slim fit office trousers',1399,'34','Navy',18,'trousers-navy.jpg',5),
('Pleated Formal Trousers','Classic pleated formal trousers',1499,'32','Grey',15,'trousers-grey.jpg',5),
('Casual Joggers','Comfortable everyday joggers',899,'L','Charcoal',35,'joggers-charcoal.jpg',6),
('Cargo Pants','Utility style men''s cargo pants',1299,'32','Olive',24,'cargo-olive.jpg',6),
('Track Pants','Lightweight sports track pants',799,'M','Black',40,'trackpants-black.jpg',6),
('Bomber Jacket','Casual lightweight bomber jacket',1999,'L','Black',16,'bomber-black.jpg',7),
('Puffer Jacket','Warm winter puffer jacket',2499,'XL','Navy',12,'puffer-navy.jpg',7),
('Fleece Jacket','Soft fleece casual jacket',1799,'M','Grey',18,'fleece-grey.jpg',7),
('Zip Hoodie','Full-zip cotton hoodie',1299,'L','Black',30,'ziphoodie-black.jpg',8),
('Pullover Hoodie','Classic pullover hoodie',1199,'M','Maroon',25,'pullover-maroon.jpg',8),
('Cable Knit Sweater','Warm cable knit sweater',1599,'L','Cream',14,'sweater-cream.jpg',9),
('V-Neck Sweater','Classic V-neck knitted sweater',1499,'M','Grey',17,'sweater-grey.jpg',9),
('Crew Neck Sweater','Everyday crew neck sweater',1399,'XL','Black',20,'sweater-black.jpg',9),
('Denim Shorts','Casual denim shorts',799,'32','Blue',26,'shorts-denim.jpg',10),
('Cotton Shorts','Lightweight cotton shorts',699,'M','Navy',30,'shorts-navy.jpg',10),
('Cargo Shorts','Utility cargo shorts',899,'L','Olive',22,'shorts-olive.jpg',10),
('Cotton Briefs Pack','Comfortable cotton briefs',599,'M','Black',45,'briefs-black.jpg',11),
('Cotton Boxer Pack','Soft cotton boxer set',699,'L','Grey',40,'boxers-grey.jpg',11),
('Vest Innerwear Pack','Cotton sleeveless vests',549,'L','White',50,'vest-white.jpg',11),
('Kurta Classic','Traditional men''s cotton kurta',999,'L','White',20,'kurta-white.jpg',12),
('Printed Kurta','Festive printed men''s kurta',1299,'M','Blue',15,'kurta-blue.jpg',12),
('Nehru Jacket','Traditional modern Nehru jacket',1899,'L','Black',10,'nehru-black.jpg',12),
('Leather Belt','Classic men''s leather belt',699,'Free','Brown',30,'belt-brown.jpg',13),
('Formal Wallet','Compact men''s wallet',799,'Free','Black',25,'wallet-black.jpg',13),
('Baseball Cap','Casual men''s baseball cap',399,'Free','Black',40,'cap-black.jpg',13),
('Cotton Socks Pack','Comfortable cotton ankle socks',299,'Free','Mixed',60,'socks-mixed.jpg',13),
('Classic Tie','Formal necktie',499,'Free','Navy',20,'tie-navy.jpg',13),
('Suspenders','Adjustable formal suspenders',599,'Free','Black',15,'suspenders-black.jpg',13),
('Men''s Casual Blazer','Smart casual single-breasted blazer',2499,'L','Charcoal',12,'blazer-charcoal.jpg',5),
('Business Suit Jacket','Premium formal suit jacket',3499,'L','Black',8,'suit-black.jpg',5),
('Flannel Overshirt','Warm flannel overshirt',1399,'XL','Red Black',18,'flannel-red.jpg',6),
('Henley T-Shirt','Casual three-button henley',799,'M','Olive',24,'henley-olive.jpg',1),
('Rugby Polo','Sporty striped polo shirt',999,'L','Navy White',20,'rugby-polo.jpg',1),
('Mandarin Collar Shirt','Modern collarless casual shirt',1099,'M','Black',17,'mandarin-shirt.jpg',2),
('Corduroy Trousers','Textured corduroy trousers',1399,'32','Brown',16,'corduroy-brown.jpg',4),
('Water Resistant Jacket','Lightweight rain-ready jacket',2199,'XL','Olive',14,'rain-jacket.jpg',7),
('Thermal Hoodie','Warm thermal-lined hoodie',1499,'L','Charcoal',19,'thermal-hoodie.jpg',8),
('Wool Blend Sweater','Premium winter wool blend sweater',1899,'L','Burgundy',12,'wool-sweater.jpg',9),
('Performance Shorts','Quick-dry athletic shorts',899,'M','Black',28,'performance-shorts.jpg',10),
('Cotton Lounge Pants','Soft relaxed lounge pants',999,'L','Grey',26,'lounge-pants.jpg',6),
('Traditional Dhoti','Men''s traditional cotton dhoti',899,'Free','Cream',18,'dhoti-cream.jpg',12),
('Embroidered Kurta','Festive embroidered kurta',1599,'L','Maroon',11,'embroidered-kurta.jpg',12);
