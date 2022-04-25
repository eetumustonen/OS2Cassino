import Cassino._
import Cassino.Deck._
import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
import scala.io.StdIn.readInt
object Test extends App {

}

object TestRoundEnd extends App {
  val game = new Game("12345")
  game.addPlayer("A", 0)
  game.addPlayer("B", 0)
  game.addPlayer("C", 0)
  var gameOver = false
  while(!gameOver){
    game.startRound()
    val round = game.currentRound.get
    round.deal()
    var roundOver = false
    while(!roundOver){
      roundOver = true

      if(roundOver) {
        roundOver = true
        var cmdOk = false
        while(!cmdOk) {
          print("The round is over. If you wish to save the current results write 'save', else write 'play'.\n")
          val a = readLine()
          a.toUpperCase match {
            case "SAVE" => {
              try{
                game.save(game.getID() + ".Cassino")
                print("Game saved, you can load it later with this game's id: " +  game.getID() + "\n")
                cmdOk = true
                var continue = false
                while(!continue) {
                  print("If you wish to start the next round write 'play', else write 'quit'.\n")
                  val play = readLine()
                  play.toUpperCase match {
                    case "PLAY" => continue = true
                    case "QUIT" => {
                      continue = true
                      gameOver = true
                    }
                  }
                }
              } catch {
                case e:FileNotFoundException => print("File not found.\n")
                case e:IOException => print("Error writing to file.\n")
                case _ =>
              }
            }
            case "PLAY" => {
              cmdOk = true
            }
            case _ => print("Invalid input.\n")
          }
        }
      }
    }
    game.updatePoints()
    if(game.getPoints().values.toBuffer.exists(_ >= 16)) gameOver  = true
    else {
      game.changeDealer()
    }
  }
}

object changeDealer extends App {
  val game = new Game("123")
  game.addPlayer("A", 2)
  print(game)
  game.addPlayer("B", 3)
  print(game)
  game.addPlayer("C", 5)
  print(game)
  game.addPlayer("D", 5)
  print(game)
  game.addPlayer("E", 5)
  print(game)
  game.addPlayer("F", 5)
  print(game)

  game.changeDealer()
  game.changeDealer()
  game.changeDealer()
  game.changeDealer()
  game.changeDealer()
  game.changeDealer()
}
