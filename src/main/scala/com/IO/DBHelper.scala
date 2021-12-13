package com.IO
import com.Quiz.Question

import java.sql.{PreparedStatement, ResultSet}
import scala.collection.mutable.ListBuffer

object DBHelper {
  def main(args: Array[String]): Unit = {
//    val header = "question(uploader, question, choice1, choice2, choice3, choice4, answer)"
//    var q = new Question("What is wrong?", Seq("1", "2", "3", "4"), 1)
//    var q2 = new Question("What is two?", Seq("1", "2", "3", "4"), 2)
////    val place = getQuestionPlaceholders(Seq(q, q2))
////    println(getPreppedInsert(header, place))
//    getQuestionPlaceholderValues(Seq(q, q2), 1).map(println)
  }
  def getInsertString(tableHeader: String, values: Iterable[String]): String = {
    "INSERT INTO " + tableHeader + "\nVALUES\n" + values.map(v => s"($v)").mkString(",\n") + ";"
  }
  def getPreppedInsert(tableHeader: String, prepStr: String): String = {
    "INSERT INTO " + tableHeader + " VALUES " + prepStr + ";"
  }
  private def getAPlaceholder(n: Int): String = {
    s"(${List.fill(n)("?").mkString(", ")})"
  }
  def getAllPlaceholders(numValues: Int, valueSize: Int): String = {
    List.fill(numValues)(getAPlaceholder(valueSize)).mkString(", ")
  }
  def getQuestionPlaceholderValues(questions: Seq[Question], uploaderID: Int): List[Any] = {
    val buffer = new ListBuffer[Any]()
    questions.foreach(q => {buffer += uploaderID; buffer ++= q.getValues()})
    buffer.toList
  }

  def parseQuestion(rs: ResultSet): Question = {
    val ques = rs.getString("question")
    val c1 = rs.getString("choice1")
    val c2 = rs.getString("choice2")
    val c3 = rs.getString("choice3")
    val c4 = rs.getString("choice4")
    val ans = rs.getInt("answer")
    val id = rs.getInt("ID")
    val uploader = rs.getInt("uploader")
    Question.shuffle(ques, List(c1, c2, c3, c4), ans, id, uploader)
  }
}
