
import Cassino._

import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable.Buffer

object FileTesting extends App {
  val game = new Game("12345")
  game.addPlayer("Eetu", 0)
  game.addPlayer("Aleksi", 0)
  try{
    game.save("SaveHere")
  } catch {
    case e:FileNotFoundException => print("File not found.\n")
    case e:IOException => print("Error writing to file.\n")
  }

  def load(filename: String): Buffer[String] = {
    var ret: Buffer[String] = Buffer()
    try {
      val fileIn = new FileReader(filename)
      val linesIn = new BufferedReader(fileIn)
      try {
        var oneLine = ""
        while({oneLine = linesIn.readLine();oneLine != "END" && oneLine != null}){
          ret = ret :+ oneLine
        }
      } finally {
        fileIn.close()
        linesIn.close()
      }
    } catch {
      case notFound: FileNotFoundException => Console.println("FileNotFound")
      case e: IOException => Console.println("IOException")
    }
    ret
  }

  val data = load("SaveHere")
  data.foreach(println)

}

