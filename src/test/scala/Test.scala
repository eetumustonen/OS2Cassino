import Cassino._
import Cassino.Deck._

object Test extends App {
  val game = new Game("12345")
  Console.print("Game ID: " + game.getID() + "\n")
  game.addPlayer("Eetu")
  game.addPlayer("Eetu")
  game.addPlayer("Aleksi")
  game.addPlayer("Mimmi")
  Console.print(game.getPlayerNames().length + " players registered." + "\n")
  Console.print(game)

  val deck = new Deck
  deck.fullDeck()
  Console.print(deck)

}
