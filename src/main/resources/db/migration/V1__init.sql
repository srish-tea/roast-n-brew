
CREATE TABLE `users`
(
    `id`         int          NOT NULL AUTO_INCREMENT,
    `email_id`   varchar(255) NOT NULL,
    `first_name` varchar(255)  DEFAULT NULL,
    `last_name`  varchar(255)  DEFAULT NULL,
    `image_url`  varchar(2148) DEFAULT NULL,
    `designation` varchar(255)  DEFAULT NULL,
    `location` varchar(255)  DEFAULT NULL,
    `bio` text DEFAULT NULL,
    `can_talk_about` text NOT NULL,
    `cannot_talk_about` text NOT NULL,
    `coins_balance` int NOT NULL,
    `is_group` boolean,
    `created_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` boolean DEFAULT false,
    PRIMARY KEY (`id`),
    KEY `idx_email` (`email_id`),
    KEY `idx_first_name` (`first_name`),
    KEY `idx_last_name` (`last_name`)
);


