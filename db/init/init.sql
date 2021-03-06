CREATE DATABASE accounting default character SET UTF8;

CREATE TABLE accounting.users(
	ID VARCHAR(42) PRIMARY KEY,
	NAME VARCHAR(12) NOT NULL,
	PASSWORD VARCHAR(64) NOT NULL,
	REFRESH_TOKEN VARCHAR(254)
);

CREATE TABLE accounting.reports(
	ID INT PRIMARY KEY AUTO_INCREMENT,
	AMOUNT INT NOT NULL,
	MAIN_CATEGORY VARCHAR(6) NOT NULL,
	SUB_CATEGORY  VARCHAR(30) NOT NULL,
	REPORTING_DATE DATE NOT NULL,
	USER_ID VARCHAR(42),
	MEMO VARCHAR(90),
	hidden boolean,
	FOREIGN KEY(USER_ID)
	REFERENCES users(ID)
);
