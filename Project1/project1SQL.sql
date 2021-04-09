
--creating look up tables
CREATE TABLE reimbursement_status(
	reim_status_id integer PRIMARY KEY,
	reim_status varchar(20) NOT null
);
INSERT INTO reimbursement_status VALUES (1,'PENDING');
INSERT INTO reimbursement_status VALUES (2,'APPROVED');
INSERT INTO reimbursement_status VALUES (3,'DENIED');

CREATE TABLE reimbursement_type(
	reim_type_id integer PRIMARY KEY,
	reim_type varchar(20) NOT null
);
INSERT INTO reimbursement_type VALUES (1,'LODGING');
INSERT INTO reimbursement_type VALUES (2,'TRAVEL');
INSERT INTO reimbursement_type VALUES (3,'FOOD');
INSERT INTO reimbursement_type VALUES (4,'OTHER');

CREATE TABLE user_roles(
	user_role_id integer PRIMARY KEY,
	user_role varchar(20) NOT null
);
INSERT INTO user_roles VALUES (1,'EMPLOYEE');
INSERT INTO user_roles VALUES (2,'FINANCE_MANAGER');
--making the other tables
CREATE TABLE ERS_USERS(
	ERS_USERS_ID SERIAL PRIMARY KEY,
	ERS_USERNAME VARCHAR(50) UNIQUE NOT NULL,
	ERS_PASSWORD VARCHAR(50) NOT NULL,
	USER_FIRST_NAME VARCHAR(100) NOT NULL,
	USER_LAST_NAME VARCHAR(100) NOT NULL,
	USER_EMAIL VARCHAR(150) UNIQUE NOT NULL,
	USER_ROLE_ID INTEGER NOT NULL,
	FOREIGN KEY (USER_ROLE_ID) REFERENCES USER_ROLES(USER_ROLE_ID)
);

--inserting a user
INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id)
VALUES ('rafael123','rafael123','Rafael','Malespin','rafael@yahoo.com',1);
INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id)
VALUES ('dani321','dani321','Dani','Malespin','dani@yahoo.com',1);
INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id)
VALUES ('gwen11','gwen11','Gwen','Levin','gwen@yahoo.com',2);

CREATE TABLE ERS_REIMBURSEMENT(
	REIMB_ID SERIAL PRIMARY KEY,
	REIMB_AMOUNT NUMERIC NOT NULL,
	REIMB_SUBMITTED TIMESTAMP NOT NULL,
	REIMB_RESOLVED TIMESTAMP,
	REIMB_DESCRIPTION VARCHAR(250),
	REIMB_AUTHOR INTEGER NOT NULL,
	REIMB_RESOLVER INTEGER,
	REIMB_STATUS_ID INTEGER NOT NULL,
	REIMB_TYPE_ID INTEGER NOT NULL,
	FOREIGN KEY (REIMB_AUTHOR) REFERENCES ERS_USERS(ERS_USERS_ID),
	FOREIGN KEY (REIMB_RESOLVER) REFERENCES ERS_USERS(ERS_USERS_ID),
	FOREIGN KEY (REIMB_STATUS_ID) REFERENCES REIMBURSEMENT_STATUS(reim_status_id),
	FOREIGN KEY (REIMB_TYPE_ID) REFERENCES REIMBURSEMENT_TYPE(REIM_TYPE_ID)
);
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (300,CURRENT_TIMESTAMP,'Company dinner',1,1,3);
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (5000,CURRENT_TIMESTAMP,'Medical Care',1,1,4);
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (500,CURRENT_TIMESTAMP,'Holiday Inn - one night',1,1,1);
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (250000,CURRENT_TIMESTAMP,'Collector Edition Cat Burgler Nami Figurine',1,1,4);
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (1000,CURRENT_TIMESTAMP,'Emergency',2,1,2);
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (300,CURRENT_TIMESTAMP,'Airplane Ticket',2,1,2);

--Viewing the different lookup tables
SELECT * FROM reimbursement_status;
SELECT * FROM reimbursement_type;
SELECT * FROM user_roles;

SELECT * FROM ers_reimbursement;

--deleting the tables if we need to change them
DROP TABLE ers_reimbursement;
DROP TABLE ers_users;
DROP TABLE reimbursement_status;
DROP TABLE reimbursement_type;
DROP TABLE user_roles;


--testing a few things
--getting the reimbursement with the status and type
SELECT reimb_id,reimb_amount,reimb_submitted,reimb_resolved,reimb_description,reimb_author,reimb_resolver,rs.reim_status,rt.reim_type,eu.user_first_name,eu.user_last_name,
eu2.user_first_name AS resolver_first_name,
eu2.user_last_name AS resolver_last_name
FROM ers_reimbursement r
INNER JOIN reimbursement_status rs 
ON r.reimb_status_id =rs.reim_status_id
INNER JOIN reimbursement_type rt 
ON r.reimb_type_id =rt.reim_type_id
INNER JOIN ers_users eu
ON r.reimb_author =eu.ers_users_id
LEFT JOIN ers_users eu2
ON r.reimb_resolver =eu2.ers_users_id
ORDER BY r.reimb_submitted DESC,r.reimb_resolved DESC;

--selecting reimbursement based on status
SELECT reimb_id,reimb_amount,reimb_submitted,reimb_resolved,reimb_description,reimb_author,reimb_resolver,rs.reim_status,rt.reim_type,eu.user_first_name,eu.user_last_name,
eu2.user_first_name AS resolver_first_name,
eu2.user_last_name AS resolver_last_name
FROM ers_reimbursement r
INNER JOIN reimbursement_status rs 
ON r.reimb_status_id =rs.reim_status_id
INNER JOIN reimbursement_type rt 
ON r.reimb_type_id =rt.reim_type_id
INNER JOIN ers_users eu
ON r.reimb_author =eu.ers_users_id
LEFT JOIN ers_users eu2
ON r.reimb_resolver =eu2.ers_users_id
WHERE rs.reim_status ='APPROVED'
ORDER BY r.reimb_submitted DESC;

--selecting reimbursement based on status and user_id
--selecting reimbursement based on status
SELECT reimb_id,reimb_amount,reimb_submitted,reimb_resolved,reimb_description,reimb_author,reimb_resolver,rs.reim_status,rt.reim_type,eu.user_first_name,eu.user_last_name,
eu2.user_first_name AS resolver_first_name,
eu2.user_last_name AS resolver_last_name
FROM ers_reimbursement r
INNER JOIN reimbursement_status rs 
ON r.reimb_status_id =rs.reim_status_id
INNER JOIN reimbursement_type rt 
ON r.reimb_type_id =rt.reim_type_id
INNER JOIN ers_users eu
ON r.reimb_author =eu.ers_users_id
LEFT JOIN ers_users eu2
ON r.reimb_resolver =eu2.ers_users_id
WHERE 
r.reimb_author =1
AND rs.reim_status ='PENDING'
ORDER BY r.reimb_submitted DESC;

--select the user with the role
SELECT ers_users_id,ers_username,ers_password,user_first_name,user_last_name,user_email,ur.user_role
FROM ers_users eu
INNER JOIN user_roles ur
ON eu.user_role_id =ur.user_role_id;

--selecting based on username and password
SELECT ers_users_id,ers_username,ers_password,user_first_name,user_last_name,user_email,ur.user_role
FROM ers_users eu
INNER JOIN user_roles ur
ON eu.user_role_id =ur.user_role_id
WHERE ers_username = 'rafael123' 
AND ers_password = 'rafael123';

--
SELECT * FROM ers_reimbursement er;
SELECT * FROM ers_users eu;


--inserting into the reimbursement
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (300,CURRENT_TIMESTAMP,'Company dinner',1,1,3);

--updating the status of a reimbursement
UPDATE ers_reimbursement SET reimb_status_id = 2,reimb_resolver =3 WHERE reimb_author = 1 AND reimb_id =1;

--updating the password of the user
UPDATE ers_users  SET ers_password ='daniFen' WHERE ers_users_id = 2;
