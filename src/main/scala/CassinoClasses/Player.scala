package CassinoClasses



/*
Simple class representing the players.
Used in class Cassino.Game and Round and the user interface.
getName() returns the player's name that also works as an id, which is private so it can't be modified.
* */

class Player(name: String) {
  private val id: String = name
  def getName(): String = id
  override def toString(): String = id
}
