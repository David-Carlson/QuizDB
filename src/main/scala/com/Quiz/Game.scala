package com.Quiz
import com.IO.DBHelper.{getAllQuestions, getBestOfN, getBestOfNByUser, getUserScore, insertNewScore, loginAdmin, loginOrCreateAcnt, updateScore}
import com.IO.{DBHelper, DataCreator, IO}

import scala.io.StdIn
import sys.exit


object Game {
  var logged_in_user: Option[(Int, String)] = None
  def getUserID = (logged_in_user.get)._1
  def getUsername = (logged_in_user.get)._2
  val cheat = false

  def main(args: Array[String]): Unit = {
    mainMenu()
  }


  def mainMenu(): Unit = {
    while(true) {
      printMenu()
      IO.readInt(0, 6) match {
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
        printMiddleMenu()
        println("6) Log out")
      }
      case None => {
        println(s"Welcome to QuizBowl!")
        printMiddleMenu()
        println("6) Log in")
      }
    }
    println("0) Quit")
    println()
  }
  private def printMiddleMenu(): Unit = {
    println()
    println("1) Play a round of 5 questions")
    println("2) Play a round of 10 questions")
    println("3) Play a round of 20 questions")
    println("4) Enter admin mode")
    println("5) View scores")
  }
  def adminMode(): Unit = {
    while(true) {
      println("Admin Portal")
      IO.printShortBreak()
      println("1) Login and reset databases")
      println("2) Go back to main menu")
      println("0) Quit program")
      println()
      val ans = IO.readInt(0, 2)
      ans match {
        case 1 =>
          adminLogin()
          return
        case 2 => return
        case 0 => exit
      }
    }
  }

  def adminLogin(): Unit = {
    val (name, password) = IO.readUsernameAndPassword()
    loginAdmin(name, password) match {
      case Some((id, usrname)) =>
        println(s"Admin $usrname, choose carefully: ")
        IO.printBreak()
        println("1) Drop and restore databases")
        println("2) Return to main method")
        println()
        val ans = IO.readInt(1,2)
        ans match {
          case 1 =>
            DataCreator.resetTables()
        }
      case None => println("Login information not correct, returning...")
    }
    IO.pressEnter()
  }
  def printBestOfN(n: Int): Unit = {
    println(s"Best scores for $n questions: ")
    println("User    Score   Ratio")
    IO.printShortBreak()
    getBestOfN(n).foreach(res => {
      val percent = res._3.toFloat/(res._3 + res._4)
      println(f"${res._1}%8s    ${res._2}%4d    $percent%.2f%%")
    })
    println()
  }

  def printUserBestOfN(n: Int, id:Int): Unit = {
    val (_, score, correct, incorrect) = getBestOfNByUser(id, n).get
    val best = s"Best over $n:"
    val scoreStr: String = f"$score/$n"
    println(f"$best%14s  $scoreStr%6s")
    println()
  }

  def viewScores(): Unit = {
    if (logged_in_user.isDefined) {
      val (id, name) = ((logged_in_user.get)._1, (logged_in_user.get)._2)
      val (_, _, correct, incorrect) = getBestOfNByUser(id, 5).get
      println(f"$name has a win/loss ratio of: ${correct.toFloat/(correct + incorrect)}%.2f%%")
      println(s"Best scores: ")
      IO.printShortBreak()
      printUserBestOfN(20, id)
      printUserBestOfN(10, id)
      printUserBestOfN(5, id)
      IO.pressEnter()
    }
    printBestOfN(20)
    printBestOfN(10)
    printBestOfN(5)
    IO.pressEnter()
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
      val (user, password) = IO.readUsernameAndPassword()
      logged_in_user = DBHelper.loginOrCreateAcnt(user, password)
    } while (logged_in_user.isEmpty)
    println("Logged in")
    IO.pressEnter()
  }

  // Ask how many questions
  def playRound(n: Int): Unit = {
    IO.printBreak()
    val allQuestions = getAllQuestions().take(n)

    val results = for ((q, idx) <- allQuestions.zip(LazyList.from(1))) yield {
      println(s"Question $idx: ")
      println(q)
      if (cheat)
        println(s"Answer: ${q.getAnswer}")
      val ans = IO.readInt()
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
    val prevScore = getUserScore(getUserID)
    prevScore match {
      case Some((prev5, prev10, prev20, prevCorrect, prevIncorrect)) =>
        val newCorrect = prevCorrect + correct
        val newIncorrect = prevIncorrect + incorrect
        n match {
          case 5 => updateScore(getUserID, math.max(prev5, correct), prev10, prev20, newCorrect, newIncorrect)
          case 10 => updateScore(getUserID, prev5, math.max(prev10, correct), prev20, newCorrect, newIncorrect)
          case 20 => updateScore(getUserID, prev5, prev10, math.max(prev20, correct), newCorrect, newIncorrect)
        }
      case None =>
        n match {
          case 5 => insertNewScore(getUserID, correct, 0, 0, correct, incorrect)
          case 10 => insertNewScore(getUserID, 0, correct, 0, correct, incorrect)
          case 20 => insertNewScore(getUserID, 0, 0, correct, correct, incorrect)
        }
    }
  }




}
