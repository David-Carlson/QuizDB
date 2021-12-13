package com.IO
import scala.io.Source
import com.Quiz._

object UploadCSV extends {
  def main(args: Array[String]): Unit = {
    parseQuestions().foreach(println)
  }

  def parseQuestions(): Iterator[Question] = {
    val name = "resources/export_questions.csv"
    try {
      Source.fromResource(name)
        .getLines()
        .filter(line => line.nonEmpty)
        .map(_.split("\\|").map(_.trim))
        .map(l => new Question(l(0), l(1).split("%"), l(2).toInt))
    } catch {
      case e: Exception => {
        println(s"Exception: $e")
        Iterator[Question]()
      }
    }
  }

  def parseQuestionsAsStrings(): Iterator[String] = {
    val name = "resources/export_questions.csv"
    for (line: String <- Source.fromResource(name).getLines if line.length > 2) yield{
      try {
        val qStr = line.split("\\|").map(_.trim)
        val question: Question = Question.shuffle(qStr(0), qStr(1).split("%"), qStr(2).toInt, 1, 1)
        question.toQueryString(1)

      } catch {
        case e: Exception => {
          println(s"Exception: $e, $line")
          ("")
        }
      }
    }
  }
}
