import scala.io.Source

object UploadCSV extends App {
  readQuestions()
  def readQuestions(): Unit = {
    for (line <- Source.fromResource("./data/export_questions.csv").getLines if line.length > 2) {
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
