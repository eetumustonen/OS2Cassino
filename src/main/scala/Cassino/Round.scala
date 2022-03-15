package Cassino
import Cassino.Deck._
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class Round(playerData: Map[Player, Int]) {
  private var points = playerData

  val players = points.keySet.toBuffer
  private var turn = players.head
  private var turnIndex = 0

  private var stacks: Map[Player, Deck] = Map()
  private var playerCards: Map[Player, Deck] = Map()

  for(i <- players){
    stacks(i) = new Deck
    playerCards(i) = new Deck
  }

  val table: Deck = new Deck

  val deck = new Deck
  deck.fullDeck()
  deck.shuffle()

  def nextTurn(): Unit = {
    if(turnIndex == players.size -1) turnIndex = 0
    else turnIndex += 1
    turn = players(turnIndex)
  }

  def deal(): Unit = {
    for(i <- playerCards){
      for(j <- 0 until 4) {
        i._2.addCard(deck.pickFirst())
      }
    }
    for(j <- 0 until 4) {
        table.addCard(deck.pickFirst())
      }
  }

  def trail() = {???}
  def pickNew() = {???}
  def checkValidity(card: Card, cards: Buffer[Card]): Boolean = {???}
  def capture = {???}
  def sweep = {???}
  def countPoints() = {???}

  def addNewPoints(newPoints: Map[Player, Int]): Unit = {
    points = points ++ newPoints.map{ case (plr, pnt) => plr -> (pnt + points.getOrElse(plr, 0))}
  }

  def getPoints(): Map[Player, Int] = points

  override def toString(): String = {
    var ret = ""
    for(i <- playerCards){
      if(turn.equals(i._1)) ret = ret + "● "+ i._1.getName() + ": " + i._2
      else ret = ret + "  " + i._1.getName() + ": " + i._2
    }
    ret = ret + "\n" + "Table: "+ table + "\n"
    ret
  }
}
