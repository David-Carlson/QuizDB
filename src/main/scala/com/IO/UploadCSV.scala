package com.IO
import scala.io.Source
import com.Quiz._

object UploadCSV extends {
  def main(args: Array[String]): Unit = {
    readQuestions()
  }

  def readQuestions(): Unit = {
    val name = "resources/export_questions.csv"
    for (line: String <- Source.fromResource(name).getLines if line.length > 2) {
      try {
        val qStr = line.split("\\|").map(_.trim)
        val question: Question = Question.shuffle(qStr(0), qStr(1).split("%"), qStr(2).toInt)
        print(question)
      } catch {
        case e: Exception => println(s"Exception: $e, $line")
      }
    }
  }
}
