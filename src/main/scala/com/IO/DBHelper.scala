package com.IO
import com.Quiz.Question

import java.sql.PreparedStatement
import scala.collection.mutable.ListBuffer

object DBHelper {
  def main(args: Array[String]): Unit = {
    val header = "question(uploader, question, choice1, choice2, choice3, choice4, answer)"
    var q = new Question("What is wrong?", Seq("1", "2", "3", "4"), 1)
    var q2 = new Question("What is two?", Seq("1", "2", "3", "4"), 2)
    val place = getQuestionPlaceholders(Seq(q, q2))
//    println(getPreppedInsert(header, place))
    getQuestionPlaceholderValues(Seq(q, q2), 1).map(println)
  }
  def getInsertString(tableHeader: String, values: Iterable[String]): String = {
    "INSERT INTO " + tableHeader + "\nVALUES\n" + values.map(v => s"($v)").mkString(",\n") + ";"
  }
  def getPreppedInsert(tableHeader: String, prepStr: String): String = {
    "INSERT INTO " + tableHeader + " VALUES " + prepStr + ";"
  }
  def getPlaceholder(n: Int): String = {
    s"(${List.fill(n)("?").mkString(", ")})"
  }
  def getQuestionPlaceholders(questions: Seq[Question]): String = {
    List.fill(questions.length)(getPlaceholder(7)).mkString(", ")
  }
  def getQuestionPlaceholderValues(questions: Seq[Question], uploaderID: Int): ListBuffer[Any] = {
    val buffer = new ListBuffer[Any]()
    questions.foreach(q => {buffer += uploaderID; buffer ++= q.getValues()})
    buffer
  }
}
