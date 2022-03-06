package Cassino
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class Game(id: String) {
  private val ID = id
  private val players: Map[Player, Int] = Map()

  def getID(): String = ID

  def getPlayerNames(): Buffer[String] = {
    val ret: Buffer[String] = Buffer()
    for(i <- players){
      ret += i._1.getName()
    }
    ret
  }

  override def toString(): String = {
    var ret: String = ""
    for(i <- players){
      ret = ret + i._1.getName() + ": " + i._2 + "\n"
    }
    ret
  }

  def addPlayer(name: String): Unit = {
    try{
      if(this.getPlayerNames().contains(name)) throw new SameNameException("Player with the name \"" + name + "\" already exists" )
      else {
        val player = new Player(name)
        players(player) = 0
      }
    } catch {
        case SameNameException(text) => Console.print(text + "\n")
      }
  }
}
