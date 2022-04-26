package Cassino.Deck


/*
A class for creating card objects in class Deck.
 */

class Card(s: Char, v: Char) {
  private val suit = s
  private val value = v

  def getValue(): Char = value
  def getSuit(): Char = suit

  //These are table values, hand values are handled checkValidity().
  def getNumericValue(): Int = {
    var r = 0
    value match {
      case 'T' => r = 10
      case 'J' => r = 11
      case 'Q' => r = 12
      case 'K' => r = 13
      case 'A' => r = 1
      case _   => r = value.asDigit
    }
    r
  }
  //This method is used in Deck.returnCard() to find a card by suit and value.
  def findThis(st: Char, vl: Char): Option[Card] = {
    var ret: Option[Card] = None
    if(st == suit && vl == value) ret = Some(this)
    ret
  }

  override def toString(): String = suit.toString + value.toString
}
