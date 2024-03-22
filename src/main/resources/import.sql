-- CREATE DATABASE IF NOT EXISTS luxiergerie;

INSERT INTO employee (id, serial_number, first_name, last_name, password) VALUES ('1', '12345678', 'John', 'Doe', 'password');
INSERT INTO employee (id, serial_number, first_name, last_name, password) VALUES ('2', '12345679', 'Dédé', 'Le Dé', 'password1');
INSERT INTO employee (id, serial_number, first_name, last_name, password) VALUES ('3', '12345670', 'Max', 'Ime', 'password2');
INSERT INTO employee (id, serial_number, first_name, last_name, password) VALUES ('4', '12345671', 'Emma', 'Amme', 'password3');

INSERT INTO role (id, name) VALUES ('1', 'ROLE_ADMIN');
INSERT INTO role (id, name) VALUES ('2', 'ROLE_EMPLOYEE');
INSERT INTO role (id, name) VALUES ('3', 'ROLE_GOLD');
INSERT INTO role (id, name) VALUES ('4', 'ROLE_DIAMOND');

INSERT INTO employee_role (role_id, employee_id) VALUES ('1', '1');
INSERT INTO employee_role (role_id, employee_id) VALUES ('2', '2');
INSERT INTO employee_role (role_id, employee_id) VALUES ('2', '3');
INSERT INTO employee_role (role_id, employee_id) VALUES ('2', '4');

INSERT INTO client (id, first_name, last_name, pin, email, phone_number) VALUES ('1', 'Jocelyn', 'De ChNord', '1234', 'jocelyn@jocelyn.fr', '06 01 02 03 04');
INSERT INTO client (id, first_name, last_name, pin, email, phone_number) VALUES ('2', 'Mohammed', 'Ali', '1235', 'ali@ali.fr' , '06 01 02 03 05');
INSERT INTO client (id, first_name, last_name, pin, email, phone_number) VALUES ('3', 'Jean', 'Bon', '1236', 'jambon@yummi.gloups', '06 01 02 03 06');

INSERT INTO room (id, number, client_id, floor) VALUES ('1', '237', '1', '2');
INSERT INTO room (id, number, client_id, floor) VALUES ('2', '238', '2', '2');
INSERT INTO room (id, number, client_id, floor) VALUES ('3', '239', '3', '2');

INSERT INTO section (id, name, image) VALUES ('1', 'Room service', 'hostesse.jpg');
INSERT INTO section (id, name, image) VALUES ('2', 'Concergierie', 'concergiere_a_moustache.jpg');

INSERT INTO category (id, name, image, description, section_id) VALUES ('1', 'Boissons', 'boissons.jpg', 'La carte des boissons froides et chaudes', '1');
INSERT INTO category (id, name, image, description, section_id) VALUES ('2', 'Entrées', 'entrees.jpg', 'La carte des entrées', '1');
INSERT INTO category (id, name, image, description, section_id) VALUES ('3', 'Plats', 'plats.jpg', 'La carte des plats', '1');
INSERT INTO category (id, name, image, description, section_id) VALUES ('4', 'Desserts', 'desserts.jpg', 'La carte des desserts', '1');
INSERT INTO category (id, name, image, description, section_id) VALUES ('5', 'Pressing', 'pressing.jpg', 'Le pressing de l"hotel', '2');
INSERT INTO category (id, name, image, description, section_id) VALUES ('6', 'Nettoyage', 'nettoyage.jpg', 'Le service de nettoyage', '2');
INSERT INTO category (id, name, image, description, section_id) VALUES ('7', 'Garage', 'garage.jpg', 'Le garagiste à votre service', '2');

INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('1', '1', 'Coca', 'coca.jpg', 'Coca cola', '2.5');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('2', '1', 'Fanta', 'fanta.jpg', 'Fanta orange', '2.5');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('3', '1', 'Sprite', 'sprite.jpg', 'Sprite', '2.5');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('4', '2', 'Salade verte', 'salade.jpg', 'La salade verte et sa sauce au basilique sauvage (capturé par un dresseur renommé).', '7.90');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('5', '2', 'Salade César', 'salade.jpg', 'Salade préparée par Jules César lui-même!! (voyage dans le temps inclus)', '150350');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('6', '3', 'Steak frites', 'steak.jpg', 'Steak de boeuf accompagné de frites maison', '15.90');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('7', '3', 'Poulet rôti', 'poulet.jpg', 'Poulet rôti accompagné de pommes de terre', '12.90');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('8', '4', 'Tarte aux pommes', 'tarte.jpg', 'Tarte aux pommes et sa boule de glace vanille', '7.90');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('9', '4', 'Mousse au chocolat', 'mousse.jpg', 'Mousse au chocolat et sa crème chantilly', '7.90');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('10', '5', 'Chemise', 'chemise.jpg', 'Nettoyage de chemise', '5.4');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('11', '5', 'Pantalon', 'pantalon.jpg', 'Nettoyage de pantalon', '7.2');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('12', '6', 'Chambre simple', 'chambre.jpg', 'Nettoyage de chambre simple (si vous avez fait la fête un peu mais pas trop)', '20');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('13', '6', 'Chambre double', 'chambre.jpg', 'Nettoyage de chambre double (si vous avez fait la "fête" un peu mais pas trop)', '30');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('14', '7', 'Vidange', 'reparation.jpg', 'Vidange de votre véhicule, huile premium de qualité, pressée depuis l"olivier du jardin', '150');
INSERT INTO accommodation (id, category_id, name, image, description, price) VALUES ('15', '7', 'Pneus', 'reparation.jpg', 'Une sélection de pneus de qualité, à partir de 200€ le pneu', '200');

INSERT INTO purchase (id, room_id, status) VALUES ('1', '1', 'En cours');
INSERT INTO purchase (id, room_id, status) VALUES ('2', '2', 'Validée');
INSERT INTO purchase (id, room_id, status) VALUES ('3', '3', 'Terminée');
INSERT INTO purchase (id, room_id, status) VALUES ('4', '1', 'En cours');
INSERT INTO purchase (id, room_id, status) VALUES ('5', '2', 'En cours');
INSERT INTO purchase (id, room_id, status) VALUES ('6', '3', 'Terminée');
INSERT INTO purchase (id, room_id, status) VALUES ('7', '1', 'Terminée');
INSERT INTO purchase (id, room_id, status) VALUES ('8', '2', 'Terminée');
INSERT INTO purchase (id, room_id, status) VALUES ('9', '3', 'Validée');

INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('1', '2');
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('1', '4');
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('3', '6');
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('3', '8');
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('3', '10');
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('3', '12');
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('7', '14');
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('7', '1');
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES ('7', '3');
