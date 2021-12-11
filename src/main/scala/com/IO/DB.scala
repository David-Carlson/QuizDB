package com.IO
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException, SQLIntegrityConstraintViolationException}
import com.IO.DBHelper._

import scala.collection.mutable.ListBuffer

object DB {
  val driver = sys.env("driver")

  def main(args: Array[String]): Unit = {
//    test()
    val header = "admin(username, first_name, last_name, password)";
    val values: Seq[String] = List("'CSGUY', 'Joe', 'Pesci', '4321'", "'Shades', 'Mr', 'Cool', '4321'")
    println(executeUpdate(getInsertString(header, values)))
  }

  def getConnection(): Connection = {
    val url = sys.env("url")
    val username = sys.env("username")
    val password = sys.env("password")
    DriverManager.getConnection(url, username, password)
  }

  def prepareValues(statement: PreparedStatement, values: ListBuffer[Any]): Unit = {
    println("Starting preparation")
    for((v, idx) <- values.zipWithIndex) {
      v match {
        case x: Int => statement.setInt(idx + 1, x)
        case x: String => statement.setString(idx + 1, x)
        case _: Any => println("Unhandled prepared type, ")
      }
    }
    println("Ending preparation")

  }

  def executeUpdate(query: String): Boolean = {
    var succeeded = true;
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = getConnection()
      val statement = connection.createStatement()
      statement.executeUpdate(query)
    } catch {
      case e: SQLException => e.printStackTrace
        succeeded = false
    }
    connection.close()
    succeeded
  }

  def executePreparedUpdate(preparedQuery: String, values: ListBuffer[Any]): Boolean = {
    var succeeded = true;
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = getConnection()
      val statement: PreparedStatement = connection.prepareStatement(preparedQuery)
      prepareValues(statement, values)
      statement.executeUpdate(preparedQuery)
    } catch {
      case e: SQLException => e.printStackTrace
        succeeded = false
    }
    connection.close()
    succeeded
  }




  def test(): Unit = {
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = getConnection()

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM users;")
      while (resultSet.next()) {
        println(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3))
      }
    } catch {
      case e: SQLException => e.printStackTrace
    }
    connection.close()
  }
}
