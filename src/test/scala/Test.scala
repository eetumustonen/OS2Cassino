import Cassino._
import Cassino.Deck._

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map

object Test extends App {
  //Create a new instance of Game
  val game = new Game("TEST")
  //First we have to add some players
  game.addPlayer("Eetu")
  game.addPlayer("Konsta")
  game.addPlayer("Tuomo")
  game.addPlayer("Sepi")
  game.addPlayer("Jaromir")
  game.addPlayer("Bou")
  //Let's confirm this worked
  Console.print(game)
  //It works
  //Let's start the first round
  game.startRound()
  //Let's confirm this worked
  val round1 = game.currentRound.get
  Console.print(round1)
  //It worked. the hashtags separate each turn and the circle marks whose in turn.
  //Let's confirm we have a full deck. For this test an ordered deck is used.
  Console.print(round1.deck)
  //Yes we have. Let's deal the cards
  round1.deal()
  Console.print(round1)
  //Let's test trailing
  round1.trail('♠', 'A')
  round1.trail('♠', '5')
  Console.print(round1)
  //It worked and the turn changed.
  //Let's capture
  val cardsToCapture = Buffer[Card](round1.table.returnCard('♣', 'Q').get)
  round1.capture('♠', 'Q', cardsToCapture)
  Console.print(round1)
  Console.print(round1.stacksToString())
  //Capturing works in this case and moves the cards to player's stack

  //LETS CREATE ANOTHER GAME TO TEST END GAME
  val game2 = new Game("TEST2")
  game2.addPlayer("Player1")
  game2.addPlayer("Player2")
  game2.addPlayer("Player3")
  game2.addPlayer("Player4")
  game2.startRound()
  val round2 = game2.currentRound.get
  //Round class is modified for this test to have less cards in the starting deck
  //Due to lack of cards some Exceptions are thrown
  for(i <- 0 until 51) round2.deck.pickFirst()
  Console.print(round2.deck)
  round2.deal()
  Console.print(round2)
  round2.trail('♦', 'K')
  Console.print(round2.stacksToString())
  Console.print(game2)
  game2.updatePoints(round2.getPoints())
  Console.print(game2)
  //Player3 gets 3 points, 1 for most cards and 2 for most spades.
}
