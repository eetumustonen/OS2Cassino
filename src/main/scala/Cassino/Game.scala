package Cassino
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class Game(id: String) {
  private val ID = id
  private var players: Map[Player, Int] = Map()

  //MAYBE MAKE PRIVATE
  var currentRound: Option[Round] = None

  def getID(): String = ID

  def getPlayerNames(): Buffer[String] = {
    val ret: Buffer[String] = Buffer()
    for(i <- players){
      ret += i._1.getName()
    }
    ret
  }

  def addPlayer(name: String): Player = {
    val player = new Player(name)
    try{
      if(this.getPlayerNames().contains(name)) throw new SameNameException("Player with the name \"" + name + "\" already exists" )
      else {
        players(player) = 0
      }
    } catch {
        case SameNameException(text) => Console.print(text + "\n")
      }
    player
  }

  def startRound() = {
    try {
      //CHECK THIS AGAIN IN THE USER INTERFACE AND ASK PLAYER NAME
      if(players.size == 0) throw new PlayersMissingException("Can't start a round before at least one player is added.")
      else currentRound = Some(new Round(players))
    } catch {
      case PlayersMissingException(text) => Console.print(text)
    }
  }


  def updatePoints(): Unit = {
    players = currentRound.get.getPoints()
  }

  def save(filename: String): Unit = {}
  def load(filename: String): Unit = {}

  override def toString(): String = {
    var ret: String = ID + "\n"
    for(i <- players){
      ret = ret + i._1.getName() + ": " + i._2 + "\n"
    }
    ret += "\n"
    ret
  }

}
