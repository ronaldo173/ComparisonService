DROP database IF EXISTS comparisson_service;
create database comparisson_service;
use comparisson_service;


CREATE TABLE  `configuration_schema` (
  `id` int NOT NULL AUTO_INCREMENT,  
  `config_content` TEXT NOT NULL,  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE  `configuration` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL unique, 
  `id_schema` int NOT NULL,
  PRIMARY KEY (id), 
  foreign key(id_schema) references configuration_schema(id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO configuration_schema VALUES (1, '<?xml version="1.0"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
<xs:element name="notepad">
  <xs:complexType>
    <xs:sequence>
      <xs:element name="pages" type="xs:string"/>
      <xs:element name="color" type="xs:string"/>
      <xs:element name="price" type="xs:string"/>
    </xs:sequence>
  </xs:complexType>
</xs:element>
</xs:schema>');
INSERT INTO configuration_schema VALUES (2, 23456789);
INSERT INTO configuration_schema VALUES (3, 3456789);
INSERT INTO configuration_schema VALUES (4, 456789);
INSERT INTO configuration_schema VALUES (5, 56789);

INSERT INTO configuration(name, id_schema) VALUES('for notepads (with page amount, color, price)', 1);
INSERT INTO configuration(name, id_schema) VALUES('phones (with price, oc, ram memory)', 2);
INSERT INTO configuration(name, id_schema) VALUES('laptops (with screen size, cpu power, screen resolution)', 3);
INSERT INTO configuration(name, id_schema) VALUES('for cars (with price, speed, power, color)', 4);
INSERT INTO configuration(name, id_schema) VALUES('for football balls', 5);