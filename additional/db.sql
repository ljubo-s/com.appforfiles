/*
ALTER SESSION SET "_ORACLE_SCRIPT"=true;
CREATE USER APPSCH IDENTIFIED BY AppSchemaPassword123;
ALTER USER APPSCH IDENTIFIED BY "AppSchemaPassword123#!";
ALTER SESSION SET CURRENT_SCHEMA = APPSCH;
ALTER USER APPSCH QUOTA UNLIMITED ON USERS;  

GRANT CONNECT, RESOURCE, DBA TO APPSCH;
GRANT UNLIMITED TABLESPACE TO APPSCH;
*/
-------------------------------------------------------------------------------------------------------------------- SEQUENCES
--------------------------------------------------------
--  DDL for Sequence APP_USER_SEQUENCE
--------------------------------------------------------
CREATE SEQUENCE  APP_USER_SEQUENCE  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;
--------------------------------------------------------
--  DDL for Sequence APP_ROLE_SEQUENCE
--------------------------------------------------------
CREATE SEQUENCE  APP_ROLE_SEQUENCE  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE; 
--------------------------------------------------------
--  DDL for Sequence APP_USER_ROLE_SEQUENCE
--------------------------------------------------------
CREATE SEQUENCE  APP_USER_ROLE_SEQUENCE  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE; 
--------------------------------------------------------
--  DDL for Sequence APP_LOG_SEQUENCE
--------------------------------------------------------
CREATE SEQUENCE  APP_LOG_SEQUENCE  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;
--------------------------------------------------------
--  DDL for Sequence APP_FILE_SEQUENCE 
--------------------------------------------------------
CREATE SEQUENCE  APP_FILE_SEQUENCE  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;

-------------------------------------------------------------------------------------------------------------------- TABLES
--------------------------------------------------------
--  DDL for Table APP_USER
--------------------------------------------------------
CREATE TABLE APP_USER
(
  ID           		 NUMBER(19) NOT NULL,
  USER_NAME          VARCHAR2(36) NOT NULL,
  ENCRYPTED_PASSWORD VARCHAR2(128) NOT NULL,
  ENABLED            NUMBER(1) NOT NULL
);
--------------------------------------------------------
--  DDL for Table APP_ROLE
--------------------------------------------------------
CREATE TABLE APP_ROLE
(
  ID   		NUMBER(19) NOT NULL,
  ROLE_NAME VARCHAR2(30) NOT NULL
);
--------------------------------------------------------
--  DDL for Table APP_USER_ROLE
--------------------------------------------------------
CREATE TABLE APP_USER_ROLE
(
  ID      NUMBER(19) NOT NULL,
  USER_ID NUMBER(19) NOT NULL,
  ROLE_ID NUMBER(19) NOT NULL
);
--------------------------------------------------------
--  DDL for Table APP_LOG
--------------------------------------------------------
CREATE TABLE APP_LOG
(
  ID				NUMBER(19) NOT NULL,
  USER_ID           NUMBER(19) NOT NULL,
  LOGIN_TIME        TIMESTAMP (6), 
  LOGOUT_TIME		TIMESTAMP (6),
  SID				VARCHAR2(200)
);
--------------------------------------------------------
--  DDL for Table APP_FILE
--------------------------------------------------------
CREATE TABLE APP_FILE
(
  ID					NUMBER(19) NOT NULL,
  LOG_ID           		NUMBER(19) NOT NULL,
  START_DOWNLOAD        TIMESTAMP (6), 
  END_DOWNLOAD			TIMESTAMP (6),
  START_UPLOAD        	TIMESTAMP (6), 
  END_UPLOAD			TIMESTAMP (6),
  START_CHECK        	TIMESTAMP (6), 
  END_CHECK				TIMESTAMP (6),
  START_CREATING_CSV    TIMESTAMP (6), 
  END_CREATING_CSV		TIMESTAMP (6),
  BLOB_FILE_XLSX		BLOB,
  BLOB_FILE_CSV			BLOB,
  CSV_NAME				VARCHAR2(50),
  CSV_PATH				VARCHAR(150),
  XLSX_CLIENTS_NAME		VARCHAR2(50),
  XLSX_ORIGINAL_NAME	VARCHAR2(50),
  XLSX_ORIGINAL_PATH	VARCHAR(150),
  XLSX_STORAGE_NAME		VARCHAR2(50),
  XLSX_STORAGE_PATH		VARCHAR(150),
  FILE_TYPE				VARCHAR2(15) -- invoice, roming...
);

-------------------------------------------------------------------------------------------------------------------- INDEXES
--------------------------------------------------------
--  DDL for Index APP_USER_I
--------------------------------------------------------
CREATE UNIQUE INDEX APP_USER_I ON APP_USER (ID) 
PCTFREE 10 INITRANS 2 MAXTRANS 255;
--------------------------------------------------------
--  DDL for Index APP_ROLE_I
--------------------------------------------------------
CREATE UNIQUE INDEX APP_ROLE_I ON APP_ROLE (ID) 
PCTFREE 10 INITRANS 2 MAXTRANS 255;
--------------------------------------------------------
--  DDL for Index APP_USER_ROLE_I
--------------------------------------------------------
CREATE UNIQUE INDEX APP_USER_ROLE_I ON APP_USER_ROLE (ID) 
PCTFREE 10 INITRANS 2 MAXTRANS 255;
--------------------------------------------------------
--  DDL for Index APP_LOG_I
--------------------------------------------------------
CREATE UNIQUE INDEX APP_LOG_I ON APP_LOG (ID) 
PCTFREE 10 INITRANS 2 MAXTRANS 255;
--------------------------------------------------------
--  DDL for Index APP_FILE_I
--------------------------------------------------------
CREATE UNIQUE INDEX APP_FILE_I ON APP_FILE (ID) 
PCTFREE 10 INITRANS 2 MAXTRANS 255;

-------------------------------------------------------------------------------------------------------------------- CONSTRAINTS - PR.KEYS
--------------------------------------------------------
--  Constraints for Table APP_USER
--------------------------------------------------------
ALTER TABLE APP_USER ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table APP_ROLE
--------------------------------------------------------
ALTER TABLE APP_ROLE ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table APP_USER_ROLE
--------------------------------------------------------
ALTER TABLE APP_USER_ROLE ADD PRIMARY KEY (ID);  
--------------------------------------------------------
--  Constraints for Table APP_LOG
--------------------------------------------------------
ALTER TABLE APP_LOG ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table APP_FILE
--------------------------------------------------------
ALTER TABLE APP_FILE ADD PRIMARY KEY (ID);

-------------------------------------------------------------------------------------------------------------------- CONSTRAINTS-FR.KEYS
--------------------------------------------------------
--  Ref Constraints for Table APP_USER_ROLE
--------------------------------------------------------  
ALTER TABLE APP_USER_ROLE
  ADD CONSTRAINT APP_USER_ROLE_FK1 FOREIGN KEY (USER_ID)
  REFERENCES APP_USER (ID);
--------------------------------------------------------
--  Ref Constraints for Table APP_USER_ROLE
--------------------------------------------------------  
ALTER TABLE APP_USER_ROLE
  ADD CONSTRAINT APP_USER_ROLE_FK2 FOREIGN KEY (ROLE_ID)
  REFERENCES APP_ROLE (ID);
--------------------------------------------------------
--  Ref Constraints for Table APP_LOG
--------------------------------------------------------  
ALTER TABLE APP_LOG
  ADD CONSTRAINT APP_LOG_FK1 FOREIGN KEY (USER_ID)
  REFERENCES APP_USER (ID);
--------------------------------------------------------
--  Ref Constraints for Table APP_FILE
--------------------------------------------------------  
ALTER TABLE APP_FILE
  ADD CONSTRAINT APP_FILE_FK1 FOREIGN KEY (LOG_ID)
  REFERENCES APP_LOG (ID);
  
-------------------------------------------------------------------------------------------------------------------- CONSTRAINTS UNIQUES
ALTER TABLE APP_LOG ADD CONSTRAINT APP_LOG_UK UNIQUE(ID, USER_ID);

ALTER TABLE APP_FILE ADD CONSTRAINT APP_FILE_UK UNIQUE(ID, LOG_ID);

ALTER TABLE APP_USER ADD CONSTRAINT APP_USER_UK UNIQUE (USER_NAME);

ALTER TABLE APP_ROLE ADD CONSTRAINT APP_ROLE_UK UNIQUE (ROLE_NAME);

ALTER TABLE APP_USER_ROLE ADD CONSTRAINT APP_USER_ROLE_UK UNIQUE(USER_ID, ROLE_ID);

-------------------------------------------------------------------------------------------------------------------- STORED PROCEDURES
CREATE OR REPLACE NONEDITIONABLE PROCEDURE DUMMY_IMPORT_INVOICE (filename IN VARCHAR2, 
																 message OUT VARCHAR2,
																 status OUT NUMBER) 
IS
BEGIN
    message := 'Application Schema - message from stored PROCEDURE - DUMMY_IMPORT_INVOICE \n ' || 'file name: ' || filename;
 	status := 1; 
RETURN;
END;
/

-------------------------------------------------------------------------------------------------------------------- INSERTS
INSERT INTO APP_ROLE (ID, ROLE_NAME) VALUES (1,'ADMIN');
INSERT INTO APP_ROLE (ID, ROLE_NAME) VALUES (2,'USER');

--K65HC&x'NBSu5V(m
INSERT INTO APP_USER (ID, USER_NAME, ENCRYPTED_PASSWORD, ENABLED)
VALUES (1, 'admin', '$$2y$12$h3gmgbU5Gnx.PbfI88pCVeIEfuZN0BxqCHUFxysdkjDS7hdwYxhbW', 1);

--c)q_3!dp2av#^{X~
INSERT INTO APP_USER (ID, USER_NAME, ENCRYPTED_PASSWORD, ENABLED)
VALUES (2, 'user', '$2y$12$nvMM0hcMPsd2ynYJoW5bmueZDj6APhMkFn.s2Y97u7.lkVBI1f6HS', 1);

INSERT INTO APP_USER_ROLE (ID, USER_ID, ROLE_ID) VALUES (1, 1, 1);
INSERT INTO APP_USER_ROLE (ID, USER_ID, ROLE_ID) VALUES (2, 2, 2);

-------------------------------------------------------------------------------------------------------------------- SCHEMA 2
ALTER SESSION SET "_ORACLE_SCRIPT"=true;
CREATE USER APPSCH2 IDENTIFIED BY AppSchema2Password123;
ALTER USER APPSCH2 IDENTIFIED BY "AppSchema2Password123!";
ALTER SESSION SET CURRENT_SCHEMA = APPSCH2;
ALTER USER APPSCH2 QUOTA UNLIMITED ON USERS;  
GRANT CONNECT, RESOURCE, DBA TO APPSCH2;
GRANT UNLIMITED TABLESPACE TO APPSCH2;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE APPSCH2.DUMMY_IMPORT_STORNO (filename IN VARCHAR2, 
																		message OUT VARCHAR2,
																		status OUT NUMBER)
IS
BEGIN
    message := 'Application Schema 2 - message from stored PROCEDURE - DUMMY_IMPORT_STORNO \n ' || 'file name: ' || filename;
    status := 1; 
RETURN;
END;

-------------------------------------------------------------------------------------------------------------------- DELETES
/*
DELETE FROM APP_USER_ROLE;
DELETE FROM APP_USER;
DELETE FROM APP_ROLE;
DELETE FROM APP_FILE;
DELETE FROM APP_LOG;

ALTER SEQUENCE APP_ROLE_SEQUENCE RESTART START WITH 1;
ALTER SEQUENCE APP_USER_SEQUENCE RESTART START WITH 1;
ALTER SEQUENCE APP_USER_ROLE_SEQUENCE RESTART START WITH 1;
ALTER SEQUENCE APP_FILE_SEQUENCE RESTART START WITH 1;
ALTER SEQUENCE APP_USER_ROLE_SEQUENCE RESTART START WITH 1;

-------------------------------------------------------------------------------------------------------------------- DROPS
DROP SEQUENCE APP_ROLE_SEQUENCE;
DROP SEQUENCE APP_USER_SEQUENCE;
DROP SEQUENCE APP_USER_ROLE_SEQUENCE;
DROP SEQUENCE APP_LOG_SEQUENCE;
DROP SEQUENCE APP_FILE_SEQUENCE;

DROP TABLE APP_FILE;
DROP TABLE APP_USER_ROLE;
DROP TABLE APP_ROLE;
DROP TABLE APP_LOG;
DROP TABLE APP_USER;

DROP PROCEDURE DUMMY_IMPORT_INVOICE;
DROP PROCEDURE APPSCH2.DUMMY_IMPORT_STORNO;

DROP USER APPSCH CASCADE;
DROP USER APPSCH2 CASCADE;
*/
