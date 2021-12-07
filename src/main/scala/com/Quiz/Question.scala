package com.Quiz

import scala.util.Random

class Question(question: String, choices: Seq[String], answer: Int) {
  override def toString: String = {
    question + "\n" + (for ((ans, idx) <- choices.zipWithIndex) yield s"\t${idx + 1}) $ans\n").mkString + "\n"
  }
}

object Question {
  def shuffle(question: String, choices: Seq[String], answer: Int): Question = {
    val correct: Seq[String]   = for((ans, idx) <- choices.zipWithIndex if idx+1 == answer) yield ans
    val incorrect: Seq[String] = for((ans, idx) <- choices.zipWithIndex if idx+1 != answer) yield ans
    val newChoices = Random.shuffle(correct ++ incorrect)
    val newAnswer = newChoices.indexOf(correct(0))
//    val newAnswer = for((ans, idx) <- choices.zipWithIndex if ans == answer) yield idx + 1
    new Question(question, newChoices, newAnswer)
    /*
    	correct = [c for (i,c) in enumerate(self.choices) if (i+1) in self.answers]
		incorrect = [c for (i,c) in enumerate(self.choices) if (i+1) not in self.answers]
		self.choices = correct + incorrect
		random.shuffle(self.choices)
		self.answers = [i + 1 for (i, c) in enumerate(self.choices) if c in correct]
		self.wrong_answers = [i + 1 for (i, c) in enumerate(self.choices) if c in incorrect]
     */
  }
}