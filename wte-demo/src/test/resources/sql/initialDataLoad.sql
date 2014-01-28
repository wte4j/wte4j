/*
   This script executes a initial data setup for test / showcase purpose
   Execute this with the swing hsql database manager, for example
*/

\p Inserting  records

DROP TABLE TEMPLATE CASCADE;
DROP TABLE PERSON CASCADE;
DROP TABLE PARTY CASCADE;
DROP TABLE ORGANIZATION CASCADE;
DROP TABLE ADDRESS CASCADE;
DROP TABLE TEMPLATE_TAG CASCADE;
DROP TABLE TEMPLATE_TAG_MAPPING CASCADE;
DROP TABLE MAPPING_CONFIGURATION CASCADE;

INSERT INTO PARTY (PARTY_ID) VALUES(
'1'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'2'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'3'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'4'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'5'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'6'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'7'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'10'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'11'
);
INSERT INTO PARTY (PARTY_ID) VALUES(
'12'
);

INSERT INTO ORGANIZATION (PARTY_ID,NAME) VALUES(
'10','ESL Sprachaufenthalte'
);
INSERT INTO ORGANIZATION (PARTY_ID,NAME) VALUES(
'11','Computer & Ferien Camps AG'
);
INSERT INTO ORGANIZATION (PARTY_ID,NAME) VALUES(
'12','Pfadibewegung Schweiz '
);


INSERT INTO PERSON (PARTY_ID,FIRST_NAME, LAST_NAME) VALUES(
'1','Hans','Berg'
);
INSERT INTO PERSON (PARTY_ID,FIRST_NAME, LAST_NAME) VALUES(
'2','Norbert', 'Meier'
);
INSERT INTO PERSON (PARTY_ID,FIRST_NAME, LAST_NAME) VALUES(
'3','Elke', 'Büchi'
);
INSERT INTO PERSON (PARTY_ID,FIRST_NAME, LAST_NAME) VALUES(
'4','Bernhard', 'Hauser'
);
INSERT INTO PERSON (PARTY_ID,FIRST_NAME, LAST_NAME) VALUES(
'5','Frank','Ackermann'
);
INSERT INTO PERSON (PARTY_ID,FIRST_NAME, LAST_NAME) VALUES(
'6','Benoit', 'Spiegel'
);
INSERT INTO PERSON (PARTY_ID,FIRST_NAME, LAST_NAME) VALUES(
'7','Martin','Knecht'
);


INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('1','Switzerland', 'Könizer Strasse', 'Bern', '3007', '123', '1');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('2','Switzerland', 'Züricher Strasse', 'Bern', '3008', '1', '2');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('3','Switzerland', 'Bümplizer Strasse', 'Bern', '3005', '22', '3');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('4','Switzerland', 'Genfer Strasse', 'Bern', '3006', '33', '4');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('5','Switzerland', 'Freiburger Strasse', 'Bern', '3007', '45', '5');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('6','Switzerland', 'Berner Strasse', 'Bern', '3008', '66', '6');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('7','Switzerland', 'Luzerner Strasse', 'Bern', '3009', '77', '7');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('10','Switzerland', 'Kleine Strasse', 'Bern', '3007', '45', '10');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('11','Switzerland', 'Grosse Strasse', 'Bern', '3008', '66', '11');

INSERT INTO Address (ADDRESS_ID,Country, Street, Town, zipcode, house_number , party_fk)
VALUES ('12','Switzerland', 'Genfer Strasse', 'Bern', '3009', '77', '12');


commit;