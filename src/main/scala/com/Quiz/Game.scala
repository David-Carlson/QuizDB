package com.Quiz
import Question._
import com.IO.DBHelper.{getAllQuestions, getBestOfN, getBestOfNByUser, loginOrCreateAcnt}
import com.IO.{DBHelper, IO}

import scala.io.StdIn
import sys.exit


object Game {
  var logged_in_user: Option[(Int, String)] = None
  def getUserID = (logged_in_user.get)._1
  def getUsername = (logged_in_user.get)._2

  def main(args: Array[String]): Unit = {
    logged_in_user = Some((1, "QGuy"))
    viewScores()

//    mainMenu()
//    logged_in_user = Some((10, "David"))
//    logged_in_user = loginOrCreateAcnt("user", "Quizlet", "fdsa")
//    logged_in_user match {
//      case Some((id, name)) => {
//        println(s"$id logged in with $name")
//      }
//      case None => println("No user returned")
//    }
  }


  def mainMenu(): Unit = {
    while(true) {
      printMenu()
        IO.getInt(0, 6) match {
        case 1 => playRound(5)
        case 2 => playRound(10)
        case 3 => playRound(20)
        case 4 => adminMode()
        case 5 => viewScores()
        case 6 => loginOrLogout()
        case 0 => exit
        case _ => println("Didn't understand command")
      }
    }
  }
  def printMenu(): Unit = {
    IO.printBreak()
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
    println()
    println("1) Play a round of 5 questions")
    println("2) Play a round of 10 questions")
    println("3) Play a round of 20 questions")
    println("4) Enter admin mode")
    println("5) View scores")
  }
  def adminMode(): Unit = {

  }
  def printBestOfN(n: Int): Unit = {
    println(s"Best scores for $n questions: ")
    println("User    Score   Ratio")
    IO.printShortBreak()
    getBestOfN(n).foreach(res => {
      val percent = res._3.toFloat/(res._3 + res._4)
      println(f"${res._1}    ${res._2}%2d    $percent%.2f%%")
    })
    println()
  }

  def printUserBestOfN(n: Int, id:Int): Unit = {
    val (_, score, correct, incorrect) = getBestOfNByUser(id, n).get
    val best = s"Best over $n: "
    val scoreStr: String = f"$score/$n"
    println(f"$best%14s  $scoreStr%5s   Ratio: ${correct.toFloat/(correct + incorrect)}%.2f%%")
    println()
  }

  def viewScores(): Unit = {
    if (logged_in_user.isDefined) {
      val (id, name) = ((logged_in_user.get)._1, (logged_in_user.get)._2)
      println(s"$name's best scores: ")
      IO.printShortBreak()
      printUserBestOfN(20, id)
      printUserBestOfN(10, id)
      printUserBestOfN(5, id)
      println("Press Enter to continue: ")
      StdIn.readLine()
    }
    printBestOfN(20)
    printBestOfN(10)
    printBestOfN(5)
    println("Press Enter to continue: ")
    StdIn.readLine()
  }
  def loginOrLogout(): Unit = {
    logged_in_user match {
      case Some((id, name)) =>
        println(s"Bye bye, $name!")
        logged_in_user = None
      case None => logInUser()
    }
  }
  def logInUser(): Unit = {
    do {
      val (user, password) = IO.inputUserAndPassword()
      logged_in_user = DBHelper.loginOrCreateAcnt("user", user, password)
    } while (logged_in_user.isEmpty)
  }


  // Ask how many questions
  def playRound(n: Int): Unit = {
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
    if (logged_in_user.isEmpty) {
      println("Login or create an account to save your score!")
      logInUser()
    }
    val prevScore = getBestOfNByUser(getUserID, n)
    prevScore match {
      case Some((_, prevBest, prevCorrect, prevIncorrect)) =>
//        val newCorrect =
      case None =>
        // create data,
    }

    // get previous score
    // if it exists, max it and update row
    // else insert new row
  }




}
