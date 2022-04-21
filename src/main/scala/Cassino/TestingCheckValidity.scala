package Cassino
import Cassino.Deck._
import scala.collection.mutable.Buffer

object TestingCheckValidity extends App {
  val deck = new Deck
  deck.fullDeck()
  deck.shuffle()
  val card = deck.returnCard('♣', 'J').get
  //val card = deck.returnCard('♦', 'T').get
  //val card = deck.returnCard('♠', '2').get
  val cards: Buffer[Card] = Buffer()
  cards += deck.returnCard('♣', '3').get
  cards += deck.returnCard('♣', '4').get
  cards += deck.returnCard('♣', '9').get
  cards += deck.returnCard('♣', '2').get
  cards += deck.returnCard('♥', '4').get
  cards += deck.returnCard('♥', 'T').get
  cards += deck.returnCard('♥', 'A').get
  cards += deck.returnCard('♥', '8').get
  cards += deck.returnCard('♥', '3').get
  cards += deck.returnCard('♥', 'J').get
  //cards += deck.returnCard('♥', 'Q').get
  val p = scala.collection.mutable.Map((new Player("Test"))->123)
  val round = new Round(p)
  print(card + "\n")
  cards.foreach(print)
  print("\n")
  round.checkValidity(card, cards)

}
