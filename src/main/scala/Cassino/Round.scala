package Cassino
import Cassino.Deck._
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class Round(playerData: Map[Player, Int]) {
  private var points = playerData
  var stacks: Map[Player, Buffer[Card]] = Map()
  var playerCards: Map[Player, Buffer[Card]] = Map()
  var table: Buffer[Card] = Buffer()

  val deck = new Deck
  deck.fullDeck()
  deck.shuffle()

  def getPoints(): Map[Player, Int] = points


  def deal(): Unit = {
    for(i <- playerCards){
      for(i <- 0 until 4) {
        //i._2 =+
      }
    }
  }

  /*
  TODO
  getPoints()  DONE
  deal()
  moveCard()
  checkValidity()
  countPoints()
   */

  def addNewPoints(newPoints: Map[Player, Int]): Unit = {
    points = points ++ newPoints.map{ case (plr, pnt) => plr -> (pnt + points.getOrElse(plr, 0))}
  }


  /*
  this method checks the players move's validity
  1. check that cards' sum divided by card's value remainder is 0
  2. remove all cards with value param@card
  3. check the param@cards length
  cards.length match {
    case 1 => not valid
    case 2 => check the sum
    case 3 => check the sum
    case 4 => check the sum OR it's 2 and 2 so make a pair with sum of the card's value if possible and check the other pair's sum
    case 5 => check the sum OR it's 2 and 3 so make a pair with sum of the card's value if possible and check the triple's sum
    case 6 => check the sum OR it's 2,2,2 or 3,3 or 4,2 so check those as before
    case 7 => check the sum OR it's 2,2,3 or 4,3 so check those as before
    case 8 => check the sum OR it's 2,2,2,2 or 3,3,2 or 4,4 or 4,2,2
  }
   */
  def checkValidity(card: Card, cards: Buffer[Card]): Boolean = {
    var ret = false
    ret
  }
}
