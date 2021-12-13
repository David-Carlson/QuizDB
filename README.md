# QuizDB
QuizDB is a scala based quiz game! It interacts with 
a MySQL database to read in questions, as well as write
new questions, authenticate user high scores, etc.

## Technologies
- Scala
- MySQL
- SBT

## Setup
### Environment Variables
These must be set up to find the database:
- url: jdbc:mysql://localhost:3306/quizdb
- driver: com.mysql.cj.jdbc.Driver
- username: root
- password: yourPassword