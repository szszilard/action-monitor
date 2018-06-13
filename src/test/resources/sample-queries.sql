INSERT INTO client VALUES(10001,'John Free', 'New York', 43);
INSERT INTO client VALUES(10002,'Jacob Smith', 'London', 82);
INSERT INTO client VALUES(10003,'Wendy Johnson', 'Los Angeles', 32);
INSERT INTO client VALUES(10004,'Ethan Martinez', 'Dallas', 65);

UPDATE client SET city = 'San Francisco' WHERE id = 10003;

DELETE FROM client WHERE ID = 10004;