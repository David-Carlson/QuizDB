package com.IO

import com.IO.DB.{createInsert, executeUpdate}

object DataCreator {
  def main(args: Array[String]): Unit = {
    createAdmin()
  }

  def createAdmin(): Unit = {
    val table = "CREATE TABLE admin " +
      "(username varchar(255) UNIQUE NOT NULL," +
      "(first_name varchar(100) NOT NULL," +
      "(last_name varchar(100) NOT NULL," +
      "(password varchar(100) NOT NULL));" 
    val header = "admin(username, first_name, last_name, password)";
    val values = List(
      "'CSGUY', 'Joe', 'Pesci', '4321'",
      "'Shades', 'Mr', 'Cool', '4321'")
    println("drop: " + executeUpdate("DROP TABLE IF EXISTS admin;"))
    println("create: " + executeUpdate(table))
    println("insert: " + executeUpdate(createInsert(header, values)))
  }

}
