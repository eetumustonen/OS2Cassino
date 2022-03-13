package Cassino

case class SameNameException(text: String) extends Exception(text)

case class CardDublicateException(text: String) extends Exception(text)

case class NoSuchCardException(text: String) extends Exception(text)

case class EmptyDeckException(text: String) extends Exception(text)