package Cassino
import Cassino.Deck.Card


import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
import scala.io.StdIn.readInt

object Main extends App {
  //ONLY SAVED FILES SHOULD BE ABLE TO BE LOADED
  // require that its "#####.Cassino" # is a digit
  def load(filename: String): Buffer[String] = {
    var ret: Buffer[String] = Buffer()
    try {
      val fileIn = new FileReader(filename)
      val linesIn = new BufferedReader(fileIn)
      try {
        var oneLine = ""
        while({oneLine = linesIn.readLine();oneLine != "END" && oneLine != null}){
          ret = ret :+ oneLine
        }
      } finally {
        fileIn.close()
        linesIn.close()
      }
    } catch {
      case notFound: FileNotFoundException => Console.println("FileNotFound")
      case e: IOException => Console.println("IOException")
    }
    ret
  }

  def newGame(): Game = {
    print("Give the new game an ID that is 5 digits without whitespace:\n")
    var idOk = false
    var game = new Game("")
    while(!idOk){
      val id = readLine()
      if(id.length == 5 && id.forall(_.isDigit)){
        idOk = true
        game = new Game(id)
        var nextPlayer = true
        var plrCount = 0
        while(nextPlayer){
          print("Please enter name for player " + (plrCount + 1) + " or write 'start' to start the game.\n")
          val name = readLine()
          if(name.toUpperCase == "START") {
            if(plrCount < 2) {
              print("At least two players must be added before starting the game.\n")
            } else nextPlayer = false
          }
          else {
            try {
              if(name.isEmpty) print("The name must be atleast one character.\n")
              else {
                game.addPlayer(name, 0)
                plrCount += 1
              }
            } catch {
              case e:SameNameException =>
            }
          }
          if(plrCount == 13) {
            nextPlayer = false
            print("Maximum number of players added.\n")
          }
        }
        print("Players added, lets start the game\n" + game) // to check it works
      } else {
        print("ID is invalid. It has to be 5 digits without whitespace. Try again.\n")
      }
    }
    game
  }

  //THE GAME STARTS HERE
  print("Welcome to Cassino!\n")
  //INSTRUCTIONS HERE
  var game = new Game("")
  var idOk = false
  while(!idOk){
  print("If you want to start a new game write 'new'\n" + "If you want to load an excisting game write the saved game's id.\n")
  val line = readLine()
  if(line.toUpperCase == "NEW") {
    game = newGame()
    idOk = true
  } else {
      if(line.length != 5 && !line.forall(_.isDigit)) print("ID is invalid. It has to be 5 digits without whitespace.\n")
      else {
        try {
          val data = load(line + ".Cassino")
          game = new Game(data(1))
          for(i <- 2 until data.length){
            val plrData = data(i).split(":")
            game.addPlayer(plrData(0), plrData(1).toInt)
          }
          idOk = true
        } catch {
          case e: Exception => {
            print("Loading game failed.\n")
          }
        }
      }
    }

  }

  //IF THE PLAYER WANTS TO SAVE CALL GAME.SAVE(ID.CASSINO)

  var gameOver = false
  while(!gameOver){
    game.startRound()
    val round = game.currentRound.get
    round.deal()
    var roundOver = false
    while(!roundOver){
      if(round.roundIsOver()) {
        roundOver = true
        var cmdOk = false
        while(!cmdOk) {
          print("The round is over. If you wish to save the current results write 'save', else write 'play'.\n")
          val a = readLine()
          a.toUpperCase match {
            case "SAVE" => {
              try{
                game.save(game.getID() + ".Cassino")
                print("Game saved, you can load it later with this game's id: " +  game.getID() + "\n")
                cmdOk = true
                var continue = false
                while(!continue) {
                  print("If you wish to start the next round write 'play', else write 'quit'.\n")
                  val play = readLine()
                  play.toUpperCase match {
                    case "PLAY" => continue = true
                    case "QUIT" => {
                      continue = true
                      gameOver = true
                    }
                  }
                }
              } catch {
                case e:FileNotFoundException => throw new FileNotFoundException("File not found.\n")
                case e:IOException => throw new IOException("Error writing to file.\n")
                case _ =>
              }
            }
            case "PLAY" => {
              cmdOk = true
            }
            case _ => print("Invalid input.\n")
          }
        }
      }
      else {
        print(round.inTurn().getName() + ", it's your turn." + "\n")
        var moveSuccesful = false
        while(!moveSuccesful){
          print(round)
          var move = ""
          if(round.table.deckSize() == 0) {
            print("You can only trail because there is no cards to capture.\n")
            move = "T"
          } else {
            print("Write\n'C' to capture\n'T' to trail.\n")
            move = readLine().toUpperCase
          }
          move match {
            case "C" => {
              print("Give the index of your card you would like to capture with.\nIndexing starts from 0\n")
              try{
                val cardIndex = readInt()
                if(0 until round.playerCards(round.inTurn()).deckSize() contains cardIndex) {
                  val captureWith = round.playerCards(round.inTurn()).getCardbyIndex(cardIndex)
                  print("Now give the indexes of cards to capture from the table, separated with commas. For example 0,1\n")
                  val cardsStr = readLine()
                  val cardsArray = cardsStr.split(",").distinct
                  cardsArray.foreach(_.trim())
                  if(cardsArray.forall(_.forall(_.isDigit))) {
                    var captureOk = true
                    val actualCards: Buffer[Card] = Buffer()
                    for(i <- cardsArray){
                      val j = i.toInt
                      if(0 until round.table.deckSize() contains j) {
                        actualCards += round.table.getCardbyIndex(j)
                      } else {
                        print("TRYING A CAPTURE WITH INDEX OUT OF BOUNDS. Your turn has started over.\n")
                        captureOk = false
                      }
                    }
                    if(captureOk) {
                      round.capture(captureWith.getSuit(), captureWith.getValue(), actualCards)
                      moveSuccesful = true
                    }
                  } else print("INPUT IS INVALID. Your turn has started over.\n")
                } else print("INVALID INDEX, THE INDEX SHOULD BE WITHIN " + 0 + " to " + (round.playerCards(round.inTurn()).deckSize() - 1) + ". Your turn has started over." + "\n")
              } catch {
                case e:NumberFormatException => print("INPUT IS NOT A NUMBER. Your turn has started over.\n")
              }
            }
            case "T" => {
              print("Give the index of your card you would like to trail.\nIndexing starts from 0\n")
              val cardIndex = readInt()
              if(0 until round.playerCards(round.inTurn()).deckSize() contains cardIndex) {
                val trailCard = round.playerCards(round.inTurn()).getCardbyIndex(cardIndex)
                round.trail(trailCard.getSuit(), trailCard.getValue())
                moveSuccesful = true
                print("Trail succesfull.\n")
              } else print("INVALID INDEX, THE INDEX SHOULD BE WITHIN " + 0 + " to " + (round.playerCards(round.inTurn()).deckSize() - 1) + ". Your turn has started over." + "\n")
            }
            case _ => print("INVALID MOVE, options are C or T. Your turn has started over." + "\n")
          }
        }
      }
    }
    game.updatePoints()
    if(game.getPoints().values.toBuffer.exists(_ >= 16)) gameOver  = true
    else {
      game.changeDealer()
    }
  }
  if(game.getPoints().values.toBuffer.exists(_ >= 16)) {
    print(game)
    val winner = game.getPoints().maxBy(_._2)
    print("Winner is " + winner._1.getName() + "\nGame ended")
  }
}
