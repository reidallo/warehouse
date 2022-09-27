CREATE TABLE `wr_role` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `name` varchar(25) DEFAULT NULL,
                           PRIMARY KEY (`id`)
);
CREATE TABLE `wr_user` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `username` varchar(25) NOT NULL,
                           `email` varchar(50) NOT NULL,
                           `password` char(100) DEFAULT NULL,
                           `active` boolean DEFAULT NULL,
                           PRIMARY KEY (`id`)
);
CREATE TABLE `wr_user_role` (
                                `fk_user` int NOT NULL,
                                `fk_role` int NOT NULL,
                                PRIMARY KEY (`fk_user`,`fk_role`)
);
CREATE TABLE `wr_truck` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `chassis_number` varchar(100) NOT NULL,
                            `license_plate` varchar(50) NOT NULL,
                            `active` boolean DEFAULT NULL,
                            PRIMARY KEY (`id`)
);
CREATE TABLE `wr_inventory` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `name` varchar(50) NOT NULL,
                                `quantity` int DEFAULT NULL,
                                `price` decimal(9,2) DEFAULT NULL,
                                `active` boolean DEFAULT NULL,
                                PRIMARY KEY (`id`)
);
CREATE TABLE `wr_customer` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `first_name` varchar(25) NOT NULL,
                               `last_name` varchar(25) NOT NULL,
                               `address` varchar(100) DEFAULT NULL,
                               `postal_code` int DEFAULT NULL,
                               `city` varchar(50) DEFAULT NULL,
                               `state` varchar(50) DEFAULT NULL,
                               `phone` varchar(25) DEFAULT NULL,
                               `fk_user` int NOT NULL,
                               PRIMARY KEY (`id`),
                               CONSTRAINT `fk_user` FOREIGN KEY (`fk_user`) REFERENCES `wr_user` (`id`)
);
CREATE TABLE `wr_order`
(
    `id`             int NOT NULL AUTO_INCREMENT,
    `order_number`   varchar(24)   DEFAULT NULL,
    `submitted_date` date          DEFAULT NULL,
    `deadline_date`  date          DEFAULT NULL,
    `status`         varchar(25)   DEFAULT NULL,
    `order_price`    decimal(9, 2) DEFAULT NULL,
    `fk_customer`    int NOT NULL,
    `order_quantity` int           DEFAULT NULL,
    `address`        varchar(100) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_customer` FOREIGN KEY (`fk_customer`) REFERENCES `wr_customer` (`id`)
);

CREATE TABLE `wr_item` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `item_quantity` bigint DEFAULT NULL,
                           `fk_inventory_item` int NOT NULL,
                           `fk_order` int NOT NULL,
                           PRIMARY KEY (`id`),
                           CONSTRAINT `fk_inventory_item` FOREIGN KEY (`fk_inventory_item`) REFERENCES `wr_inventory` (`id`),
                           CONSTRAINT `fk_order` FOREIGN KEY (`fk_order`) REFERENCES `wr_order` (`id`)
);
CREATE TABLE `wr_delivery` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `delivery_date` date NOT NULL,
                               `delivery_code` varchar(50) NOT NULL,
                               `fk_order` int NOT NULL,
                               `fk_truck` int NOT NULL,
                               `delivery_status` boolean DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               CONSTRAINT `fk_delivery_order` FOREIGN KEY (`fk_order`) REFERENCES `wr_order` (`id`),
                               CONSTRAINT `fk_delivery_truck` FOREIGN KEY (`fk_truck`) REFERENCES `wr_truck` (`id`)
);
CREATE TABLE `wr_reset_password` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `token` varchar(40) NOT NULL,
                                     `expiration_date` date NOT NULL,
                                     `fk_user` int NOT NULL,
                                     PRIMARY KEY (`id`),
                                     CONSTRAINT `fk_user_token` FOREIGN KEY (`fk_user`) REFERENCES `wr_user` (`id`)
);