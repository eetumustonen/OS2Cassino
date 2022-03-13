package Cassino.Deck


/*
A class for creating card objects in class Deck. This class has a subclass SpecialCard.
 */

class Card(s: Char, v: Char, spec: Boolean) {
  private val suit = s
  private val value = v
  private val special = spec
  def isSpecial(): Boolean =  special
// Getting special card's value is implemented later
  def getValue(): Char = value
  def getSuit(): Char = suit

  def findThis(st: Char, vl: Char): Option[Card] = {
    var ret: Option[Card] = None
    if(st == suit && vl == value) ret = Some(this)
    ret
  }

  override def toString(): String = suit.toString + value.toString
}
