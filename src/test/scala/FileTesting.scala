
import Cassino._

object FileTesting extends App {
  val game = new Game("Game for file testing")
  game.addPlayer("Eetu", 0)
  game.addPlayer("Aleksi", 0)
  game.save("SaveLoadTestFile")
}
