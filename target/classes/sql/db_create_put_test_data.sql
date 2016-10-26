DROP database IF EXISTS comparisson_service;
create database comparisson_service;
use comparisson_service;


CREATE TABLE  `configuration_schema` (
  `id` int NOT NULL AUTO_INCREMENT,  
  `config_schema` BLOB NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE  `configuration` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL, 
  `id_schema` int NOT NULL,
  PRIMARY KEY (id), 
  unique(`name`), 
  foreign key(id_schema) references configuration_schema(id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO configuration_schema VALUES (1, 123456789);
INSERT INTO configuration_schema VALUES (2, 23456789);
INSERT INTO configuration_schema VALUES (3, 3456789);
INSERT INTO configuration_schema VALUES (4, 456789);
INSERT INTO configuration_schema VALUES (5, 56789);

INSERT INTO configuration(name, id_schema) VALUES('for monitors by diagonal, price, color', 1);
INSERT INTO configuration(name, id_schema) VALUES('phones by price, oc, ram memory', 2);
INSERT INTO configuration(name, id_schema) VALUES('laptops by screen size, cru power, screen resolution', 3);
INSERT INTO configuration(name, id_schema) VALUES('notepads by page amount, color, price', 4);
INSERT INTO configuration(name, id_schema) VALUES('football balls by life time, price', 5);