-- CREATE DATABASE IF NOT EXISTS luxiergerie;

INSERT INTO employee (id, serial_number, first_name, last_name, password) VALUES (UUID_TO_BIN('b947b56e-e411-4212-9165-e4ec544260c7'), '12345678', 'John', 'Doe', '{bcrypt}$2y$10$Bu60rq.7TQqRputduY7ji./v1sQzD1X4mRsG/LlC2wdZ81xeDku1i');
INSERT INTO employee (id, serial_number, first_name, last_name, password) VALUES (UUID_TO_BIN('5d13beb5-e46b-42d7-a0e8-85cfd8ba42bd'), '12345679', 'Dédé', 'Le Dé', '{bcrypt}$2y$10$Bu60rq.7TQqRputduY7ji./v1sQzD1X4mRsG/LlC2wdZ81xeDku1i');
INSERT INTO employee (id, serial_number, first_name, last_name, password) VALUES (UUID_TO_BIN('a3855957-c6d3-40ac-acf7-d44cbd3108c0'), '12345670', 'Max', 'Ime', '{bcrypt}$2y$10$Bu60rq.7TQqRputduY7ji./v1sQzD1X4mRsG/LlC2wdZ81xeDku1i');
INSERT INTO employee (id, serial_number, first_name, last_name, password) VALUES (UUID_TO_BIN('0a3414cf-88d0-4db7-afbf-44af0cf19fc5'), '12345671', 'Emma', 'Ammo', '{bcrypt}$2y$10$Bu60rq.7TQqRputduY7ji./v1sQzD1X4mRsG/LlC2wdZ81xeDku1i');

INSERT INTO role (id, name) VALUES (UUID_TO_BIN('103111f6-d6f3-4587-98f2-fac7b9c55443'), 'ROLE_ADMIN');
INSERT INTO role (id, name) VALUES (UUID_TO_BIN('103111f6-d6f3-4587-98f2-fac7b9c55444'), 'ROLE_EMPLOYEE');
INSERT INTO role (id, name) VALUES (UUID_TO_BIN('103111f6-d6f3-4587-98f2-fac7b9c55445'), 'ROLE_GOLD');
INSERT INTO role (id, name) VALUES (UUID_TO_BIN('103111f6-d6f3-4587-98f2-fac7b9c55446'), 'ROLE_DIAMOND');

INSERT INTO employee_role (role_id, employee_id) VALUES (UUID_TO_BIN('103111f6-d6f3-4587-98f2-fac7b9c55443'), UUID_TO_BIN('b947b56e-e411-4212-9165-e4ec544260c7'));
INSERT INTO employee_role (role_id, employee_id) VALUES (UUID_TO_BIN('103111f6-d6f3-4587-98f2-fac7b9c55444'), UUID_TO_BIN('5d13beb5-e46b-42d7-a0e8-85cfd8ba42bd'));
INSERT INTO employee_role (role_id, employee_id) VALUES (UUID_TO_BIN('103111f6-d6f3-4587-98f2-fac7b9c55444'), UUID_TO_BIN('a3855957-c6d3-40ac-acf7-d44cbd3108c0'));
INSERT INTO employee_role (role_id, employee_id) VALUES (UUID_TO_BIN('103111f6-d6f3-4587-98f2-fac7b9c55444'), UUID_TO_BIN('0a3414cf-88d0-4db7-afbf-44af0cf19fc5'));

INSERT INTO client (id, first_name, last_name, pin, email, phone_number) VALUES (UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910755'), 'Jocelyn', 'De ChNord', '1234', 'jocelyn@jocelyn.fr', '06 01 02 03 04');
INSERT INTO client (id, first_name, last_name, pin, email, phone_number) VALUES (UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910756'), 'Mohammed', 'Ali', '1235', 'ali@ali.fr' , '06 01 02 03 05');
INSERT INTO client (id, first_name, last_name, pin, email, phone_number) VALUES (UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910757'), 'Jean', 'Bon', '1236', 'jambon@yummi.gloups', '06 01 02 03 06');

INSERT INTO room (id, number, client_id, floor) VALUES (UUID_TO_BIN('e4f522cc-d0bd-419f-9034-faf1f19bbd72'), '237', UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910755'), '2');
INSERT INTO room (id, number, client_id, floor) VALUES (UUID_TO_BIN('e4f522cc-d0bd-419f-9034-faf1f19bbd73'), '238', UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910756'), '2');
INSERT INTO room (id, number, client_id, floor) VALUES (UUID_TO_BIN('e4f522cc-d0bd-419f-9034-faf1f19bbd74'), '239', UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910757'), '2');
INSERT INTO room (id, number, client_id, floor) VALUES (UUID_TO_BIN('e4f522cc-d0bd-419f-9034-faf1f19bbd74'), '40', UUID_TO_BIN(''), '2');


INSERT INTO section (id, name, image, title, description) VALUES (UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2d'), 'Room service', 'hostesse.jpg', 'Le service de chambre', 'Bénéficiez de notre service de chambre 24h/24 et 7j/7 pour toutes vos envies');
INSERT INTO section (id, name, image, title, description) VALUES (UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2e'), 'Concergierie', 'concergiere_a_moustache.jpg', 'Le service de concergierie', 'Notre concergierie est à votre service pour toutes vos demandes : pressing, nettoyage, garage, ...');

INSERT INTO category (id, name, image, description, section_id) VALUES (UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca49'), 'Boissons', 'boissons.jpg', 'La carte des boissons froides et chaudes', UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2d'));
INSERT INTO category (id, name, image, description, section_id) VALUES (UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca50'), 'Entrées', 'entrees.jpg', 'La carte des entrées', UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2d'));
INSERT INTO category (id, name, image, description, section_id) VALUES (UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca51'),'Plats', 'plats.jpg', 'La carte des plats', UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2d'));
INSERT INTO category (id, name, image, description, section_id) VALUES (UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca52'), 'Desserts', 'desserts.jpg', 'La carte des desserts', UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2d'));
INSERT INTO category (id, name, image, description, section_id) VALUES (UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca53'), 'Pressing', 'pressing.jpg', 'Le pressing de l"hotel', UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2e'));
INSERT INTO category (id, name, image, description, section_id) VALUES (UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca54'), 'Nettoyage', 'nettoyage.jpg', 'Le service de nettoyage', UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2e'));
INSERT INTO category (id, name, image, description, section_id) VALUES (UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca55'), 'Garage', 'garage.jpg', 'Le garagiste à votre service', UUID_TO_BIN('99f2a753-be71-49a8-b73a-9a10b330af2e'));

INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a1'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca49'), 'Coca', 'coca.jpg', 'Coca cola', '2.5', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a2'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca49'), 'Fanta', 'fanta.jpg', 'Fanta orange', '2.5',  0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a3'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca49'), 'Sprite', 'sprite.jpg', 'Sprite', '2.5', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a4'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca50'), 'Salade verte', 'salade.jpg', 'La salade verte et sa sauce au basilique sauvage (capturé par un dresseur renommé).', '7.90', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a5'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca50'), 'Salade César', 'salade.jpg', 'Salade préparée par Jules César lui-même!! (voyage dans le temps inclus)', '150350', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a6'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca51'), 'Steak frites', 'steak.jpg', 'Steak de boeuf accompagné de frites maison', '15.90', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a7'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca51'), 'Poulet rôti', 'poulet.jpg', 'Poulet rôti accompagné de pommes de terre', '12.90', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a8'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca52'), 'Tarte aux pommes', 'tarte.jpg', 'Tarte aux pommes et sa boule de glace vanille', '7.90', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a9'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca52'), 'Mousse au chocolat', 'mousse.jpg', 'Mousse au chocolat et sa crème chantilly', '7.90', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca110'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca53'), 'Chemise', 'chemise.jpg', 'Nettoyage de chemise', '5.4', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca111'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca53'), 'Pantalon', 'pantalon.jpg', 'Nettoyage de pantalon', '7.2', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca112'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca54'), 'Chambre simple', 'chambre.jpg', 'Nettoyage de chambre simple (si vous avez fait la fête un peu mais pas trop)', '20', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca113'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca54'), 'Chambre double', 'chambre.jpg', 'Nettoyage de chambre double (si vous avez fait la "fête" un peu mais pas trop)', '30', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca114'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca55'), 'Vidange', 'reparation.jpg', 'Vidange de votre véhicule, huile premium de qualité, pressée depuis l"olivier du jardin', '150', 0);
INSERT INTO accommodation (id, category_id, name, image, description, price, is_reservable) VALUES (UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca115'), UUID_TO_BIN('897e8a87-dd69-4019-a275-ac0bdbafca55'), 'Pneus', 'reparation.jpg', 'Une sélection de pneus de qualité, à partir de 200€ le pneu', '200', 0);

INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('ed0c5d4b-171e-442f-9531-ae7893d07ecf'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910755'), 'En cours', '2021-06-01 12:00:00');
INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('730eff62-3b62-4153-aba2-d1e62cb10ed5'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910756'), 'Validée', '2021-06-01 12:00:00');
INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('a495ce02-ad51-4382-908b-180905ea344c'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910757'), 'Terminée', '2021-06-01 12:00:00');
INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('2e14b496-2e14-411a-80e5-3adcb3268527'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910755'), 'En cours', '2021-06-01 12:00:00');
INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('b73c0879-fe7f-4f30-9666-6b8f305cd93b'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910756'), 'En cours', '2021-06-01 12:00:00');
INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('92caa110-e261-491f-b4d1-c4c53d7befd2'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910757'), 'Terminée', '2021-06-01 12:00:00');
INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('8166c677-a237-4936-b639-9ffec6c2862c'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910755'), 'Terminée', '2021-06-01 12:00:00');
INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('349f12f9-fafd-48ef-a975-ea722b2635b6'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910756'), 'Terminée', '2021-06-01 12:00:00');
INSERT INTO purchase (id, client_id, status, date) VALUES (UUID_TO_BIN('3c9896fd-7751-4bcf-90fb-457a4ff7ffd0'), UUID_TO_BIN('a0618bf9-75b5-49d2-9e93-fca420910757'), 'Validée', '2021-06-01 12:00:00');

INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('ed0c5d4b-171e-442f-9531-ae7893d07ecf'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a2'));
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('ed0c5d4b-171e-442f-9531-ae7893d07ecf'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a4'));
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('a495ce02-ad51-4382-908b-180905ea344c'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a6'));
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('a495ce02-ad51-4382-908b-180905ea344c'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a8'));
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('a495ce02-ad51-4382-908b-180905ea344c'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca110'));
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('a495ce02-ad51-4382-908b-180905ea344c'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca112'));
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('8166c677-a237-4936-b639-9ffec6c2862c'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca114'));
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('8166c677-a237-4936-b639-9ffec6c2862c'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a1'));
INSERT INTO purchase_accommodation (purchase_id, accommodation_id) VALUES (UUID_TO_BIN('8166c677-a237-4936-b639-9ffec6c2862c'), UUID_TO_BIN('da304725-ae61-4a2b-ae04-73ba596ca1a3'));
