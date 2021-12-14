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
        case e:NumberFormatException => println("That wasn't a number...")
        case _ => println("An error occurred...")
      }
    } while (num > max || num < min)
    num
  }
}
