package com.IO

import java.sql.{Connection, DriverManager}

object DB {
  val driver = sys.env("driver")

  def getConnection(): Connection = {
    val url = sys.env("url")
    val username = sys.env("username")
    val password = sys.env("password")
    DriverManager.getConnection(url, username, password)
  }

  def main(args: Array[String]): Unit = {
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
      case e: Throwable => e.printStackTrace
    }
    connection.close()
  }
}
