package Cassino.Deck
import Cassino.{CardDublicateException, EmptyDeckException, NoSuchCardException}

import scala.collection.mutable.Buffer

/*
This class models a collection of cards. The collection can be a full 52 card deck or a subset of cards used in the game.
 */

class Deck {
  private var cards: Buffer[Card] = Buffer()
  private val values: Buffer[Char] = Buffer('A', '2', '3', '4', '5', '6', '7', '8' , '9', 'T', 'J', 'Q', 'K' )
  private val suits: Buffer[Char] = Buffer('♠', '♣', '♥', '♦')

  def pointsAndSpades(): (Int, Int) = {
    var p = 0
    var s = 0
    for(i <- cards){
      if(i.getValue() == 'A') p += 1
      if(i.toString == "♦T") p += 2
      if(i.toString == "♠2") p += 1
      if(i.getSuit == '♠') s += 1
    }
    (p, s)
  }

  def deckSize(): Int = {
    cards.length
  }

  def fullDeck(): Unit = {
    cards.clear()
    for(i <- suits){
      for(j <- values){
        cards += new Card(i, j)
      }
    }
  }

  def shuffle(): Unit = {
    cards = scala.util.Random.shuffle(cards)
  }

  def getCardbyIndex(index: Int): Card = cards(index)

  def returnCard(suit: Char, value: Char): Option[Card] = {
    var ret: Option[Card] = None
    var i = 0
    while(ret == None && i != cards.length){
      ret = cards(i).findThis(suit, value)
      i += 1
    }
    try {
      if(ret == None) throw new NoSuchCardException("In returnCard(): This deck doesn't contain card: " + suit + value + "\n")}
    catch {
      case NoSuchCardException(text) => Console.print(text)
    }
    ret
  }

  def returnFirst(): Option[Card] = {
    var ret: Option[Card] = None
    try {
      if(!cards.isEmpty) {
        ret = Some(cards.head)
      }
      else throw new EmptyDeckException("The deck is empty\n")
    } catch {
      case EmptyDeckException(text) => Console.print(text + "\n")
    }
    ret
  }

  def pickFirst(): Option[Card] = {
    var ret: Option[Card] = None
    try {
      if(!cards.isEmpty) {
        ret = Some(cards.head)
        this.removeCard(ret)
      }
      else throw new EmptyDeckException("The deck is empty\n")
    } catch {
      case EmptyDeckException(text) => Console.print(text + "\n")
    }
    ret
  }

  def addCard(card: Option[Card]): Option[Card] = {
    var ret: Option[Card] = None
    try {
      if(card == None) throw new NoSuchCardException("Parameter card is None" + "\n")
      try {
      if(cards.contains(card.get)) throw new CardDublicateException("Trying to add a card dublicate: " + card.get + "\n")
      else {
        cards += card.get
        ret = card
      }
    } catch {
      case CardDublicateException(text) => Console.print(text + "\n")
    }
    } catch {
      case NoSuchCardException(text) => Console.print(text)
    }
    ret
  }

  def removeCard(card: Option[Card]): Option[Card] = {
    var ret: Option[Card] = None
    try {
      if(card == None) throw new NoSuchCardException("Parameter card is None" + "\n")
      try {
      if(cards.isEmpty) throw new NoSuchCardException("In removeCard(): This deck doesn't contain card: " + card.get + "\n")
      else if(!cards.contains(card.get)) throw new NoSuchCardException("In removeCard(): This deck doesn't contain card: " + card.get + "\n")
      else {
        val index = cards.indexOf(card.get)
        cards.remove(index)
        ret = card
      }
    } catch {
      case NoSuchCardException(text) => Console.print(text)
    }
    } catch {
      case NoSuchCardException(text) => Console.print(text)
    }
    ret
  }

  override def toString(): String = {
    var ret = ""
    for(i <- cards){
      ret = ret + i + "  "
    }
    ret = ret + "\n"
    ret
  }
}
