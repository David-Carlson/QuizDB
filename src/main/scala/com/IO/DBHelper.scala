package com.IO
import com.IO.DB.{executePreparedQuery, executePreparedUpdate}
import com.Quiz.Question

import java.sql.{PreparedStatement, ResultSet}
import scala.collection.mutable.ListBuffer
import scala.util.Random

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

  def getAllQuestions(): List[Question] = {
    Random.shuffle(executePreparedQuery[Question](parseQuestion, "SELECT * from question LIMIT 5;")
      .map(q => q.shuffle()))
  }
  def parseLogin(rs: ResultSet): Option[(Int, String)] = {
    Some(rs.getInt("id"), rs.getString("username"))
  }
  def loginOrCreateAcnt(table: String, user: String, password: String): Option[(Int, String)] = {
    val res = executePreparedQuery[Option[(Int, String)] ](
      parseLogin,
      s"SELECT id, username from $table WHERE username=? AND password=?;",
      List(user, password))
    if (res.length == 1) {
      res.head
    } else if (executePreparedQuery[Option[(Int, String)] ](
      parseLogin,s"SELECT id, username from $table WHERE username=?;", List(user)).nonEmpty) {
      println("Password incorrect")
      None
    } else {
      println(s"Creating a new $table...")
      val (first, last) = IO.inputFirstLastName()
      createNewUser(table, user, first, last, password)
    }
  }
  def createNewUser(table: String, username: String, first: String, last:String, password: String): Option[(Int, String)] = {
    val header = s"${table}(username, first_name, last_name, password)"
    val prepStr = getAllPlaceholders(1, 4)
    val values = List(username, first, last, password)
    if (executePreparedUpdate(getPreppedInsert(header, prepStr), values)) {
      executePreparedQuery[Option[(Int, String)] ](
        parseLogin,
        s"SELECT id, username from $table WHERE username=? AND password=?;",
        List(username, password)).head
    } else {
      println(s"Error creating $table")
      None
    }
  }
  def parseBestOf(rs: ResultSet): (String, Int, Int, Int) = {
    (rs.getString("username"), rs.getInt("best"), rs.getInt("totalcorrect"), rs.getInt("totalincorrect"))
  }
  def getBestOfN(bestOfN: Int): List[(String, Int, Int, Int)] = {
    val query = s"SELECT u.username, bestscoreof${bestOfN} best, totalcorrect, totalincorrect " +
      s"FROM score JOIN user u on score.user_id = u.ID ORDER BY bestscoreof${bestOfN} DESC, " +
      "totalcorrect / (totalcorrect + totalincorrect) DESC LIMIT 3"
    executePreparedQuery[(String, Int, Int, Int)](parseBestOf, query)
  }
  def getBestOfNByUser(user_id: Int, bestOfN: Int): List[(String, Int, Int, Int)] = {
    val query = s"SELECT u.username, bestscoreof${bestOfN} best, totalcorrect, totalincorrect " +
      s"FROM score JOIN user u on score.user_id = u.ID WHERE u.ID=?"
    executePreparedQuery[(String, Int, Int, Int)](parseBestOf, query, List(user_id))
  }
}
