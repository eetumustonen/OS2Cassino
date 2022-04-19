package Cassino
import Cassino.Deck._
import scala.collection.mutable.Buffer

object TestingCheckValidity extends App {
  val deck = new Deck
  deck.fullDeck()
  deck.shuffle()
  val card = deck.pickFirst().get
  //val card = deck.returnCard('♦', 'T').get
  //val card = deck.returnCard('♠', '2').get
  val cards: Buffer[Card] = Buffer()
  for(i <- 0 to 5) {
    cards += deck.pickFirst().get
  }
  val p = scala.collection.mutable.Map((new Player("Test"))->123)
  val round = new Round(p)
  print(card + "\n")
  cards.foreach(print)
  print("\n")
  round.checkValidity(card, cards)

}
