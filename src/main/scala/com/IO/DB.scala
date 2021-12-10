package com.IO
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException, SQLIntegrityConstraintViolationException}
import com.IO.DBHelper._

object DB {
  val driver = sys.env("driver")

  def main(args: Array[String]): Unit = {
//    test()
    val header = "admin(username, first_name, last_name, password)";
    val values: Seq[String] = List("'CSGUY', 'Joe', 'Pesci', '4321'", "'Shades', 'Mr', 'Cool', '4321'")
    println(executeUpdate(createInsertString(header, values)))
  }

  def getConnection(): Connection = {
    val url = sys.env("url")
    val username = sys.env("username")
    val password = sys.env("password")
    DriverManager.getConnection(url, username, password)
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

  def executePreparedUpdate(preparedQuery: String, args: List[Any]): Boolean = {
    var succeeded = true;
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = getConnection()
      val statement: PreparedStatement = connection.prepareStatement(preparedQuery)
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
