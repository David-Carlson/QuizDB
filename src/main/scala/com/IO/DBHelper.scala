package com.IO
import com.Quiz.Question

import scala.collection.mutable.ListBuffer

object DBHelper {
  def createInsertString(tableHeader: String, values: Iterable[String]): String = {
    "INSERT INTO " + tableHeader + "\nVALUES\n" + values.map(v => s"($v)").mkString(",\n") + ";"
  }
  def prepPlaceholder(n: Int): String = {
    s"(${List.fill(n)("?").mkString(", ")})"
  }
  def prepInsertQuestionPlaceholders(questions: Seq[Question]): String = {
    prepPlaceholder(7).mkString(", ")
  }

}
