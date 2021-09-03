
CREATE TABLE `feedback`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `sender_id`   bigint(20)  NOT NULL,
    `receiver_id` bigint(20)   DEFAULT NULL,
    `is_anonymous` boolean,
    `is_public` boolean,
    `content` json DEFAULT NULL,
    `cards` text DEFAULT NULL,
    `coins` int,
    `is_visible` boolean,
    `receiver_reply` text,
    `created_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` boolean DEFAULT false,
    PRIMARY KEY (`id`),
    KEY `idx_sender` (`sender_id`),
    KEY `idx_receiver` (`receiver_id`),
    KEY `idx_public` (`is_public`),
    KEY `idx_anonymous` (`is_anonymous`)
);


CREATE TABLE `product`
(
    `id`         bigint(20)   NOT NULL AUTO_INCREMENT,
    `name`       varchar(255)  DEFAULT NULL,
    `price`      int,
    `image_url`  varchar(2148) DEFAULT NULL,
    `count`      int DEFAULT 0,
    `created_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` boolean DEFAULT false,
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_price` (`price`)
);

CREATE TABLE `orders`
(
    `id`         bigint(20)   NOT NULL AUTO_INCREMENT,
    `user_id`    bigint(20) NOT NULL,
    `product_id` bigint(20) NOT NULL,
    `Status`     text,
    `created_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` boolean DEFAULT false,
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_product` (`product_id`)
);



