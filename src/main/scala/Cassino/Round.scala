package Cassino
import Cassino.Deck._
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class Round(playerData: Map[Player, Int]) {
  private var points = playerData

  private var stacks: Map[Player, Deck] = Map()
  var playerCards: Map[Player, Deck] = Map()

  val players = points.keySet.toBuffer
  private var turn = players.head
  private var turnIndex = 0
  private var lastCapturer: Player = players.head

  for(i <- players){
    stacks(i) = new Deck
    playerCards(i) = new Deck
  }

  val table: Deck = new Deck
  val deck: Deck = new Deck
  deck.fullDeck()
  //deck.shuffle()                      REMOVE THE COMMENTS FROM THIS LINE

  def stacksToString(): String = {
    var ret = "Players' stacks: \n"
    for(i <- stacks){
      var space = ":"
      for(j <- 0 until 8-i._1.getName().length) space += " "
      if(turn.equals(i._1)) ret = ret + "● "+ i._1.getName() + space + i._2
      else ret = ret + "  " + i._1.getName() + space + i._2
    }
    ret += "\n"
    ret
  }

  def roundIsOver(): Boolean = {
    var ret = true
    for(i <- players){
      if(playerCards(i).deckSize() != 0) ret = false
    }
    ret
  }
  def inTurn(): Player = turn

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

  def pickNew() = {
    playerCards(turn).addCard(deck.pickFirst())
  }

  def trail(s: Char, v: Char) = {
    val card = playerCards(turn).returnCard(s, v)
    val c = playerCards(turn).removeCard(card)
    table.addCard(c)
    if(deck.deckSize() != 0) this.pickNew()
    if(this.roundIsOver()){
      for(i <- 0 until table.deckSize()){
        stacks(lastCapturer).addCard(table.pickFirst())
      }
      this.addNewPoints(countPoints())
      Console.print("ROUND IS OVER AND THE POINTS ARE READY TO BE UPDATED \n")
    }
    this.nextTurn()
    }


  /*
  This is the algorithm to check whether the user's attempt to capture certain cards is valid.
  First removing all cards that match the player’s card.
  Then starting from combinations of 2 cards which’s sum is the player’s card are removed.
  Then combinations of 3, 4, 5

   */
  def checkValidity(card: Card, cards: Buffer[Card]): Boolean = {
    var ret = false
    //numeric value of the card to capture with
    var c = 0
    card.getValue() match {
      case 'A' => c = 14
      case 'T' => if(card.getSuit() == '♦') c = 16 else c = 10
      case '2' => if(card.getSuit() == '♠') c = 15 else c = 2
      case _   => c = card.getNumericValue()
    }
    print(c + "\n")
    //numeric values of the cards to capture in a buffer
    val cc: Buffer[Int] = Buffer()
    for(i <- cards){
      cc += i.getNumericValue()
      print(i.getNumericValue() + ", ")
    }
  //EVERYTHING WORKS THIS FAR
    if(!cc.forall(_ <= c)) print("\nThe move is invalid.")
    else {
      cc.filterNot(_ == c)
    }



    ret
  }

  def capture(s: Char, v: Char, cards: Buffer[Card]) = {
    val card = playerCards(turn).returnCard(s, v)
    try{
      if(checkValidity(card.get, cards)) {
        for(i <- cards){
          stacks(turn).addCard(table.removeCard(Some(i)))
        }
        if(table.deckSize() == 0) this.sweep()
        lastCapturer = turn
        stacks(turn).addCard(playerCards(turn).removeCard(card))
        if(deck.deckSize() != 0) this.pickNew()
        if(this.roundIsOver()){
          for(i <- 0 until table.deckSize()){
            stacks(lastCapturer).addCard(table.pickFirst())
          }
          this.addNewPoints(countPoints())
          Console.print("ROUND IS OVER AND THE POINTS ARE READY TO BE UPDATED \n")
        }
        this.nextTurn()
      }
      else throw new InvalidCapture("This capture attempt is illegal.")
    } catch {
      case InvalidCapture(text) => Console.print(text)
    }
  }

  def sweep() = {
    points(turn) += 1
  }

  def countPoints(): Map[Player, Int] = {
    val ret: Map[Player, Int] = Map()
    var maxCards = stacks.head._1
    var maxSpades = stacks.head._1
    for(i <- players){
      ret(i) = stacks(i).pointsAndSpades._1
      if(stacks(i).deckSize() > stacks(maxCards).deckSize()) maxCards = i
      if(stacks(i).pointsAndSpades._2 > stacks(maxSpades).pointsAndSpades._2) maxSpades = i
    }
    ret(maxCards) += 1
    ret(maxSpades) += 2
    ret
  }

  def addNewPoints(newPoints: Map[Player, Int]): Unit = {
    points = points ++ newPoints.map{ case (plr, pnt) => plr -> (pnt + points.getOrElse(plr, 0))}
  }

  def getPoints(): Map[Player, Int] = points

  override def toString(): String = {
    var ret = "\n"
    for(i <- playerCards){
      var space = ":"
      for(j <- 0 until 8-i._1.getName().length) space += " "
      if(turn.equals(i._1)) ret = ret + "● "+ i._1.getName() + space + i._2
      else ret = ret + "  " + i._1.getName() + space + i._2
    }
    ret = ret + "\n" + "Table: "+ table + "\n"
    ret
  }
}
