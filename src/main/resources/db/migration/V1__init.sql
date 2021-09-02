
CREATE TABLE `users`
(
    `id`         bigint(20)          NOT NULL AUTO_INCREMENT,
    `email_id`   varchar(255) NOT NULL,
    `first_name` varchar(255)  DEFAULT NULL,
    `last_name`  varchar(255)  DEFAULT NULL,
    `image_url`  varchar(2148) DEFAULT NULL,
    `designation` varchar(255)  DEFAULT NULL,
    `location` varchar(255)  DEFAULT NULL,
    `bio` text DEFAULT NULL,
    `can_talk_about` text DEFAULT NULL,
    `cannot_talk_about` text DEFAULT NULL,
    `coins_balance` int NOT NULL DEFAULT 0,
    `is_group` boolean,
    `created_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` boolean DEFAULT false,
    PRIMARY KEY (`id`),
    KEY `idx_email` (`email_id`),
    KEY `idx_first_name` (`first_name`),
    KEY `idx_last_name` (`last_name`)
);

CREATE TABLE `notifications`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`    bigint(20),
    `type`       varchar(512)        DEFAULT NULL,
    `message`    text                DEFAULT NULL,
    `is_read`    boolean             DEFAULT FALSE,
    `entity_id`  bigint(20)          DEFAULT NULL,
    `from_id`    bigint(20)          DEFAULT NULL,
    `created_ts` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_ts` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` boolean             DEFAULT false,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`, `is_read`)
);
