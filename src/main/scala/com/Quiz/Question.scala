package com.Quiz

import scala.util.Random

class Question(question: String, choices: Seq[String], answer: Int) {
  val getAnswer = answer
  override def toString: String = {
    question + "\n" + (for ((ans, idx) <- choices.zipWithIndex) yield s"\t${idx + 1}) $ans\n").mkString + "\n"
  }
  def toQueryString(uploader: Int): String = {
    val fchoices = this.choices.map(c => s"'${c}'").mkString(", ")
    s"$uploader, '${this.question}', ${fchoices}, ${answer}"
  }
  def getValues(): List[Any] = {
    List(this.question, this.choices(0), this.choices(1), this.choices(2), this.choices(3), this.answer)
  }
}

object Question {
  def shuffle(question: String, choices: Seq[String], answer: Int): Question = {
    val correct: Seq[String]   = Seq(choices(answer - 1))
    val incorrect: Seq[String] = for((ans, idx) <- choices.zipWithIndex if idx+1 != answer) yield ans
    val newChoices = Random.shuffle(correct ++ incorrect)
    val newAnswer = newChoices.indexOf(correct(0)) + 1
    new Question(question, newChoices, newAnswer)
  }
}