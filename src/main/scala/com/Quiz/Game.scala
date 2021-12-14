package com.Quiz
import Question._
import com.IO.DBHelper.getAllQuestions
import com.IO.IO
import sys.exit


object Game {
  var logged_in_user: Option[(Int, String)] = None
  def main(args: Array[String]): Unit = {
//    play_round(5)
    logged_in_user = Some((10, "David"))
    logged_in_user match {
      case Some((id, name)) => {
        println(s"$id with $name")
      }
      case None => println("No user")
    }
  }


  def main_menu(): Unit = {
    while(true) {
      printMenu()
      var ans = IO.getInt(0, 6)
      ans match {
        case 1 => play_round(5)
        case 2 => play_round(10)
        case 3 => play_round(20)
        case 4 => admin_mode()
        case 5 => view_scores()
        case 0 => sys.exit
      }
    }
  }
  def printMenu(): Unit = {
    logged_in_user match {
      case Some((id, name)) => {
        println(s"Welcome back, $name, to QuizBowl!")
        print_middle_menu()
        println("6) Log out")
      }
      case None => {
        println(s"Welcome to QuizBowl!")
        print_middle_menu()
        println("6) Log in")
      }
    }
    println("0) Quit")
    println()
  }
  private def print_middle_menu(): Unit = {
    println("Enter a number: ")
    println()
    println("1) Play a round of 5 questions")
    println("2) Play a round of 10 questions")
    println("3) Play a round of 20 questions")
    println("4) Enter admin mode")
    println("5) View scores")
  }
  def admin_mode(): Unit = {

  }

  def view_scores(): Unit = {

  }
  def log_in_or_out(): Unit = {
    logged_in_user match {
      case Some((id, name)) =>
        println(s"Bye bye, $name!")
        logged_in_user = None
      case None =>
        println("Enter your username and password in one line: ")

    }
  }


  // Ask how many questions
  def play_round(n: Int): Unit = {
    val allQuestions = getAllQuestions().take(n)

    var results = for ((q, idx) <- allQuestions.zip(LazyList.from(1))) yield {
      println(s"Question $idx: ")
      println(q)
      println(s"Answer: ${q.getAnswer}")
      println(s"Id: ${q.getID}")
      var ans = IO.getInt()
      (q.getID, ans == q.getAnswer)
    }

    var (correct, incorrect) = (0, 0)
    for((id, wasCorrect) <- results) {
      if(wasCorrect)
        correct += 1
      else
        incorrect += 1
    }
    println(s"You got $correct right out of $n!")

  }



}
