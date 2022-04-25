package Cassino
import Cassino.Deck._
import scala.collection.mutable.Buffer
import scala.collection.mutable.LinkedHashMap
/*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ValidityTest extends AnyFlatSpec with Matchers {

  "checkValidity()" should "return correct boolean value" in {

  }
}
*/


object TestingCheckValidity extends App {
  val p = scala.collection.mutable.LinkedHashMap((new Player("Test"))->123)
  val round = new Round(p)
  val deck = new Deck
  deck.fullDeck()
  val card = deck.returnCard('♣', '7').get
  val cards: Buffer[Card] = Buffer()
  cards += deck.returnCard('♣', '3').get
  cards += deck.returnCard('♣', '4').get
  cards += deck.returnCard('♣', '5').get
  val ret = round.checkValidity(card, cards)
  print(ret)

}
