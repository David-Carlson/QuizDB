
# Goals
Read JSON/CSV File (output from python)
Create/destroy tables
Upload initial data to tables
Read quiz data
Upload new ones (with passwords)


# Tables
Quiz questions, answers, right/wrong, uploader
Multi choice questions?
Admin names/passwords
Usernames/passwords/names/questions right, questions wrong

DROP TABLE IF EXISTS user;
CREATE TABLE user
(
ID SERIAL PRIMARY KEY,
username varchar(255) UNIQUE NOT NULL,
first_name varchar(100) NOT NULL,
last_name varchar(100) NOT NULL,
password varchar(255) NOT NULL
);

DROP TABLE IF EXISTS question;
CREATE TABLE question(
ID SERIAL PRIMARY KEY,
question varchar(200) NOT NULL,
choice1 varchar(100) NOT NULL,
choice2 varchar(100) NOT NULL,
choice3 varchar(100) NOT NULL,
choice5 varchar(100) NOT NULL,
answer int CHECK(answer BETWEEN 1 AND 4));



join
