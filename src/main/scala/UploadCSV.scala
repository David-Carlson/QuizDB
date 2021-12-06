import scala.io.Source

object UploadCSV extends App {
  readQuestions()
  def readQuestions(): Unit = {
//    val filename = Source.fromResource("movies.txt")
    for (line <- Source.fromResource("questions.csv").getLines) {
      val qStr = line.split("\\|").map(_.trim)
      println(qStr(2))
    }
  }

}
