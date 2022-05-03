package CassinoClasses
import CassinoClasses.Deck._
import scala.collection.mutable.Buffer
import scala.collection.mutable.LinkedHashMap

class Round(playerData: LinkedHashMap[Player, Int]) {
  private var points = playerData

  private var stacks: LinkedHashMap[Player, Deck] = LinkedHashMap()
  var playerCards: LinkedHashMap[Player, Deck] = LinkedHashMap()

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
  deck.shuffle()

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
      this.addNewPoints(countPoints(stacks))
    }
    this.nextTurn()
    }


  /*
  This is the algorithm to check whether the user's attempt to capture certain cards is valid.
   */
  def checkValidity(card: Card, cards: Buffer[Card]): Boolean = {
    var ret = false
    //numeric value of the card to capture with, some of them have special values
    var c = 0
    card.getValue() match {
      case 'A' => c = 14
      case 'T' => if(card.getSuit() == '♦') c = 16 else c = 10
      case '2' => if(card.getSuit() == '♠') c = 15 else c = 2
      case _   => c = card.getNumericValue()
    }
    //numeric values of the cards to capture in a buffer
    var cc: Buffer[Int] = Buffer()
    for(i <- cards){
      cc += i.getNumericValue()
    }
    cc = cc.filter(_ != c)
    cc = cc.sorted.reverse

    if(!cc.forall(_ < c)) {}
    else if(cc.sum%c != 0) {}
    else if(cc.isEmpty) ret = true
    else {
      def recursiveFunction(target: Int, values: Buffer[Int]): Unit = {
        val d = target - values(0)
        if(values.contains(d)) {
          cc -= (values(0), d)
          cc = cc.sorted.reverse
          if(!cc.isEmpty) recursiveFunction(target, cc)
          else ret = true
        }
        else {
          if(d == 1 || values.length == 1) ret = false
          else {
            val f = values.filter(_ < d).sorted.reverse
            if(f.isEmpty) ret = false
            else {
              val dropThis = values(0)
              cc -= dropThis
              f -= dropThis
              recursiveFunction(d, f)
            }
          }
        }
      }
      recursiveFunction(c, cc)
    }
    ret
  }

  def capture(s: Char, v: Char, cards: Buffer[Card]) = {
    val card = playerCards(turn).returnCard(s, v)
    try{
      if(checkValidity(card.get, cards)) {
        print("Capture was succesful!\n")
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
          this.addNewPoints(countPoints(stacks))
        }
        this.nextTurn()
      }
      else throw new InvalidCapture("\nTHIS CAPTURE ATTEMP IS ILLEGAL!\n\n")
    } catch {
      case InvalidCapture(text) => Console.print(text)
    }
  }

  def sweep() = {
    points(turn) += 1
  }

  def countPoints(piles: LinkedHashMap[Player, Deck]): LinkedHashMap[Player, Int] = {
    val ret: LinkedHashMap[Player, Int] = LinkedHashMap()
    var maxCards = piles.head._1
    var maxSpades = piles.head._1
    for(i <- piles.keySet){
      ret(i) = piles(i).pointsAndSpades._1
      if(piles(i).deckSize() > piles(maxCards).deckSize()) maxCards = i
      if(piles(i).pointsAndSpades._2 > piles(maxSpades).pointsAndSpades._2) maxSpades = i
    }
    ret(maxCards) += 1
    ret(maxSpades) += 2
    ret
  }

  def addNewPoints(newPoints: LinkedHashMap[Player, Int]): Unit = {
    points = points ++ newPoints.map{ case (plr, pnt) => plr -> (pnt + points.getOrElse(plr, 0))}
  }

  def getPoints(): LinkedHashMap[Player, Int] = points

  override def toString(): String = {
    var ret = "\n"
    for(i <- playerCards){
      var space = ":"
      for(j <- 0 until 8-i._1.getName().length) space += " "
      if(turn.equals(i._1)) ret = ret + "● "+ i._1.getName() + space + i._2
      else {
        ret = ret + "  " + i._1.getName() + space
        for(i <- 0 until i._2.deckSize()){
          ret = ret + " \uD83C\uDCA0  "
        }
        ret = ret + "\n"
      }
    }
    ret = ret + "\n" + "Table: "+ table + "\n"
    ret
  }
}
