package com.Quiz
import Question._
import com.IO.DBHelper.getAllQuestions
import com.IO.IO


object Game {
  def main(args: Array[String]): Unit = {
//   println(s"Got: ${getInt(0, 5)}")
  }


  def main_menu(): Unit = {
    println("Welcome to Quizbowl!")
    println("Enter a number: ")
    println()
    println("1) Play a round of 10 questions")
    println("2) Play a round of 20 questions")
    println("3) Enter admin mode")
    println("4) View scores")
    println()
    var ans = IO.getInt()

  }

  // Ask how many questions
  def play_round(n: Int): Unit = {
    val allQuestions = getAllQuestions().take(n)
    for ((ans, idx) <- allQuestions.zip(LazyList.from(1))) {
      println(s"Question $idx: ")
      println(ans)

    }
    allQuestions.zipWithIndex

  }



}
