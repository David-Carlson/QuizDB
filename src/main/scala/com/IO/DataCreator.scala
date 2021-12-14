package com.IO
import com.IO.DB.{executePreparedUpdate, executeUpdate}
import com.IO.UploadCSV.{parseQuestions, parseQuestionsAsStrings}
import com.IO.DBHelper.{getAllPlaceholders, getInsertString, getPreppedInsert, getQuestionPlaceholderValues}
import com.Quiz.Question

object DataCreator {
  def main(args: Array[String]): Unit = {
    createAdmin()
    createUser()
    createQuestions()
    createScoreboard()
  }

  def createAdmin(): Unit = {
    val table = "CREATE TABLE admin (" +
      "ID SERIAL PRIMARY KEY," +
      "username varchar(255) UNIQUE NOT NULL," +
      "first_name varchar(100) NOT NULL," +
      "last_name varchar(100) NOT NULL," +
      "password varchar(100) NOT NULL);"
    val header = "admin(username, first_name, last_name, password)"
    val values = List(
      List("root", "David", "Carlson", "1234"),
      List("CSGUY", "Joe", "Pesci", "2345"),
      List("Shades", "Mr", "Cool", "2345"))
    val prepStr = getAllPlaceholders(values.length, values.head.length)


    println("Handling Admin table...")
    println("Drop admin?: " + executeUpdate("DROP TABLE IF EXISTS admin;"))
    println("Create admin?: " + executeUpdate(table))
    println("Insert admins?: " + executePreparedUpdate(getPreppedInsert(header, prepStr), values.flatten))
    println()
  }

  def createUser(): Unit = {
    val table = "CREATE TABLE user (" +
      "ID SERIAL PRIMARY KEY," +
      "username varchar(255) UNIQUE NOT NULL," +
      "first_name varchar(100) NOT NULL," +
      "last_name varchar(100) NOT NULL," +
      "password varchar(100) NOT NULL);"
    val header = "user(username, first_name, last_name, password)"
    val values = List(
      List("QGuy", "Stephen", "Fry", "1234"),
      List("Elf1", "James", "Harkin", "2345"),
      List("Elf2", "Anna", "Ptaszynski", "2345"),
      List("Elf3", "James", "Murray", "2345"))
    val prepStr = getAllPlaceholders(values.length, values.head.length)

    
    println("Handling User table...")
    println("Drop user?: " + executeUpdate("DROP TABLE IF EXISTS user;"))
    println("Create user?: " + executeUpdate(table))
    println("Insert users?: " + executePreparedUpdate(getPreppedInsert(header, prepStr), values.flatten))
    println()
  }

  def createQuestions(): Unit = {
    val table = "CREATE TABLE question (" +
      "ID SERIAL PRIMARY KEY," +
      "uploader bigint unsigned references admin(ID)," +
      "question varchar(200) UNIQUE NOT NULL," +
      "choice1 varchar(100) NOT NULL," +
      "choice2 varchar(100) NOT NULL," +
      "choice3 varchar(100) NOT NULL," +
      "choice4 varchar(100) NOT NULL," +
      "answer int CHECK(answer BETWEEN 1 AND 4) NOT NULL);"
    val questions = parseQuestions().iterator.to(Iterable).toSeq
    val header = "question(uploader, question, choice1, choice2, choice3, choice4, answer)"
    val prepStr = getAllPlaceholders(questions.length, Question.length)
    val values = getQuestionPlaceholderValues(questions, 1)
    println("Handling Question table...")
    println("Drop question?: " + executeUpdate("DROP TABLE IF EXISTS question;"))
    println("Create question?: " + executeUpdate(table))
    println("Insert questions?: " + executePreparedUpdate(getPreppedInsert(header, prepStr), values))
    println()
  }

  def createScoreboard(): Unit = {
    val table = "CREATE TABLE score (" +
      "user_id bigint unsigned references user(ID)," +
      "bestscoreof5 int NOT NULL DEFAULT 0 CHECK(bestscoreof5 BETWEEN 0 AND 5)," +
      "bestscoreof10 int NOT NULL DEFAULT 0 CHECK(bestscoreof10 BETWEEN 0 AND 10)," +
      "bestscoreof20 int NOT NULL DEFAULT 0 CHECK(bestscoreof20 BETWEEN 0 AND 20)," +
      "totalcorrect int NOT NULL DEFAULT 0 CHECK(totalcorrect >= 0)," +
      "totalincorrect int NOT NULL DEFAULT 0 CHECK(totalincorrect >= 0));"
    val header = "score(user_id, bestscoreof5, bestscoreof10, bestscoreof20, totalcorrect, totalincorrect)"
    val values = List(
      List(1, 3, 6, 13, 33, 25),
      List(2, 5, 8, 17, 89, 70),
      List(3, 2, 5, 11, 27, 30),
      List(4, 0, 0,  0,  0,  0))
    val prepStr = getAllPlaceholders(values.length, values.head.length)

    println(getPreppedInsert(header, prepStr))

    println("Handling score table...")
    println("Drop score?: " + executeUpdate("DROP TABLE IF EXISTS score;"))
    println("Create score?: " + executeUpdate(table))
    println("Insert score?: " + executePreparedUpdate(getPreppedInsert(header, prepStr), values.flatten))
    println()
  }

}
