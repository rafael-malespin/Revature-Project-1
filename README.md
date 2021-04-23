# Employee Reimbursement System (ERS)
## Project Description
A reimbursement system for a company's employees. Employees can request reimbursements for expenses used for either LODGING, TRAVEL, FOOD, or OTHER, the employee may also view the status of previously submitted requests and filter by status.  Finance Managers can view sent in reimbursement requests and either approve or deny any PENDING request.
## Technologies Used
- Java
- JDBC
- JUnit
- Log4J
- Gradle
- PostgreSQL
- HTML/CSS
- JavaScript
## Features
Current Features
- Users can view the status of previously submitted requests
- Users can filter previously submitted requests by status
- Users can submit new reimbursement requests
- Managers can view all past requests from all users and filter them by status
- Managers can approve or deny any pending requests
<br/>To-Do Features
- Users can upload an image of their receipt(s)
- Password Hashing
- Allow managers to submit reimbursement requests that only other managers can approve/deny
- Adjust H2 initialization and ReimbursementUpdate tests so that expected values consistently appear

## Getting Started
- First, you need to clone the project
  - git clone https://github.com/rafael-malespin/Revature_Project_1.git
- Second, you will need to setup the database
  1. Open DBeaver (install if not installed) and connect to a RDS database
  2. In the Database Manager, right click the connection and select "Create" then "Database"
  3. Set "Tablespace" to Default and give the database a name
  4. Enter the following in a sql script file and then execute it. This is to create the initial users and reimbursements
```sql 

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
VALUES ('fake_employee','fake_employee','Fake','Employee','fake_employee@fakemail.com',1);
INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id)
VALUES ('fake_manager','fake_manager','Fake','Manager','fake_manager@fakemail.com',2);

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
VALUES (250000,CURRENT_TIMESTAMP,'Collector Edition Figurine',1,1,4);
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (1000,CURRENT_TIMESTAMP,'Emergency',1,1,2);
INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)
VALUES (300,CURRENT_TIMESTAMP,'Airplane Ticket',1,1,2);

```
- Third, set up the environment variables
  1. In environment variables, create a variable "TRAINING_DB_NAME" and set its value to be the name of the database that was created in the previous point
  2. create a variable "TRAINING_DB_ENDPOINT" and set its value to be the endpoint of the RDS database that was created in AWS
  3. create a variable "TRAINING_DB_USERNAME" and set its value to be the username of the RDS database that was created in AWS
  4. create a variable "TRAINING_DB_PASSWORD" and set its value to be the password of the RDS database that was created in AWS
- Fourth, you will need to open the project in an IDE (preferably IntelliJ 2020.3 or above)
  - Open IntelliJ
  - Click File -> Open
  - Go to where the repository was cloned
  - Select the Project 1 file 
- Fifth, you will need to be navigate to the MainDriver class
  - Open the files(in IntelliJ) in this order, Project 1 -> src -> main -> java -> com -> driver -> MainDriver
- Sixth, right click while your mouse's cursor is above the "main" method and select the "Run..." option to start the server

## Usage
- Once the server is started Navigate to the "http://localhost:9003/" url, which will take you into the login page
- Enter the username and password into the respective input fields and click the "Submit" button
  - If the correct combination is entered, the user will be taken to a dashboard that is slightly different if they entered as an EMPLOYEE or as a FINANCE_MANAGER
  - If the incorrect combination is entered, the user will recieve an error message and will be asked to try again 
- If the user logs in as an EMPLOYEE, they should see a box containing a welcome message and a table displaying all the reimbursements(and their information) they have in the database or a message stating no reimbursements present if the EMPLOYEE has no reimbursements for the currently selected status (ALL, APPROVED, DENIED, or PENDING)
  - There is a dropdown menu labeled "Filter By Status" that will give the user the option to filter the user's reimbursements based on if they were APPROVED, DENIED, or if they are still PENDING.
  - There is also a navbar at the top of the page that gives the user the option to either logout out of the logged in account or go to a form to make a new reimbursement request
  - If the EMPLOYEE goes to the new reimbursement page, they will find a form requesting that they input an AMOUNT for the reimbursement, a DESCRIPTION for what the money was spent on, and what the reimbursement is for (LODGING, TRAVEL, FOOD, or OTHER (if nothing is selected, will default to OTHER)). Once the Submit button is clicked, the user will be redirected to EMPLOYEE dashboard, where they should see that the reimbursement was added to their list of reimbursements.
- If the user logs in as a FINANCE_MANAGER, they should see a box containing a welcome message and a table displaying all the reimbursements (and their information) created by every user in the database.  If there are no reimbursements in the database, a messaging saying no reimbursements for the currently selected status (ALL, APPROVED, DENIED, or PENDING) should appear. 
  - There is a dropdown menu labeled "Filter By Status" that will give the user the option to filter the reimbursements based on if they were APPROVED, DENIED, or if they are still PENDING.
  - Any reimbursement entry that is still pending should have (in the Column "Approve/Deny) two buttons that say "Approve" and "Deny" respectively.  Once the FINANCE_MANAGER, clicks on one of these buttons, the page should reload and the reimbursement will now appear as either APPROVED or DENIED with information on when they were updated and who approved/denied the reimbursement appearing in the table.
  - Like the EMPLOYEE dashboard, the FINANCE_MANAGER dashboard has a navbar that contains a logout button that once clicked, will log the user out of the account.
