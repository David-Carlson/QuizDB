package com.IO
import com.IO.DB.{executePreparedUpdate, executeUpdate}
import com.IO.UploadCSV.{parseQuestions, parseQuestionsAsStrings}
import com.IO.DBHelper.{getInsertString, getPreppedInsert, getQuestionPlaceholderValues, getQuestionPlaceholders}

object DataCreator {
  def main(args: Array[String]): Unit = {
//    createAdmin()
//    createUser()
    createQuestions()
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
      "'root', 'David', 'Carlson', '1234'",
      "'CSGUY', 'Joe', 'Pesci', '2345'",
      "'Shades', 'Mr', 'Cool', '2345'")
    println("drop: " + executeUpdate("DROP TABLE IF EXISTS admin;"))
    println("create: " + executeUpdate(table))
    println("insert: " + executeUpdate(getInsertString(header, values)))
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
      "'QGuy', 'Stephen', 'Fry', '1234'",
      "'Elf1', 'James', 'Harkin', '2345'",
      "'Elf2', 'Anna', 'Ptaszynski', '2345'")
    println("drop: " + executeUpdate("DROP TABLE IF EXISTS user;"))
    println("create: " + executeUpdate(table))
    println("insert: " + executeUpdate(getInsertString(header, values)))
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
    val questions = parseQuestions().iterator.to(Iterable).take(2).toSeq
    val header = "question(uploader, question, choice1, choice2, choice3, choice4, answer)"
    val prepStr = getQuestionPlaceholders(questions)
    val values = getQuestionPlaceholderValues(questions, 1)


    println(getPreppedInsert(header, prepStr))
    println("drop: " + executeUpdate("DROP TABLE IF EXISTS question;"))
    println("create: " + executeUpdate(table))
    println("insert: " + executePreparedUpdate(getPreppedInsert(header, prepStr), values))
//    println("insert: " + executeUpdate(createInsertString(header, values)))

  }

}
