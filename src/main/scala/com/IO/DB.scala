package com.IO

import com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping

import java.sql.{Connection, DriverManager, SQLException, SQLIntegrityConstraintViolationException}


object DB {
  val driver = sys.env("driver")

  def main(args: Array[String]): Unit = {
//    test()

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

  def createInsert(tableHeader: String, values: Seq[String]): String = {
    "INSERT INTO " + tableHeader + "\nVALUES\n" + values.map(v => s"($v)").mkString(",\n") + ";"
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
