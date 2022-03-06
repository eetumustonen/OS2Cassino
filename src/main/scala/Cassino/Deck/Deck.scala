package Cassino.Deck
import scala.collection.mutable.Buffer

class Deck {
  private val cards: Buffer[Card] = Buffer()
  private val values: Buffer[Char] = Buffer('A', '2', '3', '4', '5', '6', '7', '8' , '9', 'T', 'J', 'Q', 'K' )
  private val suits: Buffer[Char] = Buffer('♠', '♣', '♥', '♦')

  def fullDeck(): Unit = {
    cards.clear()
    for(i <- suits){
      for(j <- values){
        cards += new Card(i, j, false)
      }
    }
  }

  override def toString(): String = {
    var ret = ""
    for(i <- cards){
      ret = ret + i + ", "
    }
    ret
  }

  def shuffle(): Unit = ??? //randomize the deck
  def addCard(): Boolean = ??? //add a card to this deck
  def removeCard(): Boolean = ??? //remove a card from this deck
}
