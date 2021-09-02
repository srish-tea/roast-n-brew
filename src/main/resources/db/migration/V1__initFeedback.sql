
CREATE TABLE `feedback`
(
    `id`          long  NOT NULL AUTO_INCREMENT,
    `sender_id`   long NOT NULL,
    `receiver_id` long  DEFAULT NULL,
    `is_anonymous` boolean,
    `is_public` boolean,
    `content` text NOT NULL,
    `cards` text NOT NULL,
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


