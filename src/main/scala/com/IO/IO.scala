package com.IO
import scala.io.StdIn

object IO {
  def getInt(min: Int = 1, max: Int = 4): Int = {
    var num = -1
    do {
      println(s"Enter a number from $min to $max: ")
      try {
        num = StdIn.readInt()
      } catch {
        case _: NumberFormatException => println("That wasn't a number...")
        case _ => println("An error occurred...")
      }
    } while (num > max || num < min)
    num
  }

  def inputUserAndPassword(): (String, String) = {
    var (user, password) = ("", "")
    do {
      println("Enter your username and password in one line: ")
      val line = StdIn.readLine().split(" ")
      if (line.length != 2) {
        println("Format: 'Username password', e.g 'Hunter fDj3ml'")
      } else {
        user = line(0)
        password = line(1)
        if (user.length < 4 || password.length < 4)
          println("Username and password must be at least 4 characters")
      }
    } while(user.length < 4 || password.length < 4)
    (user, password)
  }
  def inputFirstLastName(): (String, String) = {
    var (first, last) = ("", "")
    do {
      println("Enter your first and last name in one line: ")
      val line = StdIn.readLine().split(" ")
      println(line.length, "is my length")
      if (line.length != 2) {
        println("Format: 'First Last', e.g 'Keanu Reeves'")
      } else {
        println()
        first = line(0)
        last = line(1)
        if (first.length < 3 || last.length < 3)
          println("First and Last name must be at least 3 characters")
      }
    } while(first.length < 3 || last.length < 3)
    (first, last)
  }
}
