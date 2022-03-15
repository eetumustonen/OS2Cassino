import Cassino._
import Cassino.Deck._
import scala.collection.mutable.Map

object Test extends App {
  //Adding players............................................
  val game = new Game("12345")
  Console.print("Game ID: " + game.getID() + "\n")
  val plr1 = game.addPlayer("Eetu")
  game.addPlayer("Eetu")
  val plr2 = game.addPlayer("Aleksi")
  val plr3 = game.addPlayer("Mimmi")
  Console.print(game.getPlayerNames().length + " players registered." + "\n")
  Console.print(game)


  //Testing deck modification.................................
  val deck = new Deck
  deck.fullDeck()
  Console.print(deck)
  val testCard = deck.returnCard('♠', 'A')
  deck.removeCard(testCard)
  Console.print(deck)
  deck.addCard(testCard)
  Console.print(deck)
  deck.addCard(testCard)


  //Updating points...........................................
  var newPoints = Map((plr1 -> 1), (plr2 -> 2), (plr3 -> 3))

  //Testing class Round.......................................
  val round = new Round(newPoints)
  Console.print(round)
  round.deal()
  round.nextTurn()
  Console.print(round)

}
