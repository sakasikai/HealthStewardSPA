CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_userName_uindex` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `client` (
  `user_id` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `id_number` char(18) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `file_number` int(11) DEFAULT NULL,
  `phone` char(11) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `height` float DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `blood_type` char(2) DEFAULT NULL,
  `remark` text,
  `image` mediumblob,
  PRIMARY KEY (`username`),
  UNIQUE KEY `client_user_id_uindex` (`user_id`),
  CONSTRAINT `client_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `staff` (
  `user_id` int(11) NOT NULL,
  `username` char(20) NOT NULL,
  `nickname` char(20) NOT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `type` char(20) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `job` char(20) DEFAULT NULL,
  `post` char(20) DEFAULT NULL,
  `phone` char(11) DEFAULT NULL,
  `employee_id` char(11) DEFAULT NULL,
  `resume` text,
  `remark` text,
  `image` mediumblob,
  PRIMARY KEY (`username`),
  KEY `staff_user_username_fk` (`username`),
  KEY `staff_user_userId_fk` (`user_id`),
  CONSTRAINT `staff_user_userId_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `staff_user_username_fk` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `staff_image` (
  `user_id` int(11) NOT NULL,
  `image` mediumblob,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `staff_image_staff_staffId_fk` FOREIGN KEY (`user_id`) REFERENCES `staff` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `authentication` (
  `username` varchar(20) NOT NULL,
  `password_in_db` varchar(100) DEFAULT '',
  `salt` varchar(50) DEFAULT '',
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `authentication_userName_uindex` (`username`),
  UNIQUE KEY `authentication_user_id_uindex` (`user_id`),
  CONSTRAINT `authentication_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `health_steward_contract` (
  `contract_id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `staff_id` int(11) NOT NULL,
  `client_confirm` tinyint(4) NOT NULL DEFAULT '-1',
  `staff_confirm` tinyint(4) NOT NULL DEFAULT '-1',
  `creation_time_stamp` datetime DEFAULT NULL,
  `result_time_stamp` datetime DEFAULT NULL,
  `result_comment` varchar(100) DEFAULT NULL,
  `contract_state` tinyint(4) NOT NULL DEFAULT '-1',
  `managed_state` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`contract_id`),
  KEY `health_steward_contract_user_user_id_fk` (`client_id`),
  KEY `health_steward_contract_user_user_id_fk_2` (`staff_id`),
  CONSTRAINT `health_steward_contract_user_user_id_fk` FOREIGN KEY (`client_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `health_steward_contract_user_user_id_fk_2` FOREIGN KEY (`staff_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `apply_item` (
  `apply_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `time_stamp` datetime DEFAULT NULL,
  `apply_note` text,
  `flow_level` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`apply_item_id`),
  KEY `apply_item_user_user_id_fk` (`client_id`),
  CONSTRAINT `apply_item_user_user_id_fk` FOREIGN KEY (`client_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `signed_note` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `staff_id` int(11) NOT NULL,
  `time_stamp` datetime NOT NULL,
  `staff_note` text,
  `contract_id` int(11) NOT NULL,
  PRIMARY KEY (`note_id`),
  KEY `signed_note_user_user_id_fk` (`client_id`),
  KEY `signed_note_user_user_id_fk_2` (`staff_id`),
  KEY `signed_note_health_steward_contract_contract_id_fk_3` (`contract_id`),
  CONSTRAINT `signed_note_health_steward_contract_contract_id_fk_3` FOREIGN KEY (`contract_id`) REFERENCES `health_steward_contract` (`contract_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `signed_note_user_user_id_fk` FOREIGN KEY (`client_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `signed_note_user_user_id_fk_2` FOREIGN KEY (`staff_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `talking_note` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `staff_id` int(11) NOT NULL,
  `time_stamp` datetime NOT NULL,
  `staff_note` text,
  `contract_id` int(11) NOT NULL,
  PRIMARY KEY (`note_id`),
  KEY `talking_note_user_user_id_fk` (`client_id`),
  KEY `talking_note_user_user_id_fk_2` (`staff_id`),
  KEY `talking_note_health_steward_contract_contract_id_fk_3` (`contract_id`),
  CONSTRAINT `talking_note_health_steward_contract_contract_id_fk_3` FOREIGN KEY (`contract_id`) REFERENCES `health_steward_contract` (`contract_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `talking_note_user_user_id_fk` FOREIGN KEY (`client_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `talking_note_user_user_id_fk_2` FOREIGN KEY (`staff_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

