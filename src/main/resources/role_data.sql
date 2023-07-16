INSERT INTO `massms`.`roles` (`name`) VALUES ('ADMIN');
INSERT INTO `massms`.`roles` (`name`) VALUES ('CREATOR');
INSERT INTO `massms`.`roles` (`name`) VALUES ('EDITOR');
INSERT INTO `massms`.`roles` (`name`) VALUES ('MANAGER');
INSERT INTO `massms`.`roles` (`name`) VALUES ('MEMBER');
INSERT INTO `massms`.`roles` (`name`) VALUES ('USER');
INSERT INTO `massms`.`user` (`first_name`, `enabled`, `last_name`, `username`, `password`) VALUES ('SUPER', true, 'ADMIN', 'admin@admin.com', '$2a$10$qEUb3RImXK99yw3/BAjlrOrXN9enZGquT2ooBl9PJSv5jbVhhEUJy');
INSERT INTO `massms`.`user_roles` (`user_id`, `role_id`) VALUES (1, 1);