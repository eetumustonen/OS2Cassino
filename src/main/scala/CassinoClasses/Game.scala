package CassinoClasses
import java.io.{BufferedWriter, FileNotFoundException, FileWriter, IOException}
import scala.collection.mutable.LinkedHashMap
import scala.collection.mutable.Buffer

class Game(id: String) {
  private val ID = id
  private var players: LinkedHashMap[Player, Int] = LinkedHashMap()
  var currentRound: Option[Round] = None

  def getID(): String = ID

  def getPoints(): LinkedHashMap[Player, Int] = players

  def getPlayerNames(): Buffer[String] = {
    val ret: Buffer[String] = Buffer()
    for(i <- players){
      ret += i._1.getName()
    }
    ret
  }

  def changeDealer(): Unit = {
    val first = players.head
    players.remove(first._1)
    players(first._1) = first._2
  }

  def addPlayer(name: String, points: Int): Player = {
    val player = new Player(name)
    try{
      if(this.getPlayerNames().contains(name)) throw new SameNameException("Player with the name \"" + name + "\" already exists" )
      else {
        players(new Player(name)) = points
      }
    } catch {
        case SameNameException(text) => {
          Console.print(text + "\n")
          throw new SameNameException("Player with the name \"" + name + "\" already exists" )
        }
      }
    player
  }

  def startRound() = {
    try {
      if(players.size == 0) throw new PlayersMissingException("Can't start a round before at least one player is added.")
      else currentRound = Some(new Round(players))
    } catch {
      case PlayersMissingException(text) => Console.print(text)
    }
  }


  def updatePoints(): Unit = {
    players = currentRound.get.getPoints()
  }

  def save(filename: String) = {
    val data = Buffer("CassinoClasses", ID)
    for(i <- players){
      data += i._1.getName() + ":" + i._2
    }
    data += "END"
    try {
      val file = new FileWriter(filename)
      val linesOut = new BufferedWriter(file)
      try {
        for(i <- 0 until data.length){
          linesOut.write(data(i) + "\n")
        }
      } finally {
        linesOut.close()
        file.close()
      }
    }
    catch {
      case notFound: FileNotFoundException => throw new FileNotFoundException()
      case e: IOException => throw new IOException()
    }
  }

  override def toString(): String = {
    var ret: String = ID + "\n"
    for(i <- players){
      ret = ret + i._1.getName() + ": " + i._2 + "\n"
    }
    ret += "\n"
    ret
  }

}
