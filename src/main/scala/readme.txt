
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

DROP TABLE IF EXISTS users;
CREATE TABLE users
(
ID SERIAL PRIMARY KEY,
username varchar(255) unique NOT NULL,
first_name varchar(100) NOT NULL,
last_name varchar(100) NOT NULL,
password varchar(255) NOT NULL
);



join
