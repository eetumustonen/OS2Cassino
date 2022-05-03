package CassinoClasses
import CassinoClasses.Deck.Card



import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
import scala.io.StdIn.readInt

object Cassino extends App {
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

  var help: String =
    "Rules:\n One must collect points which are calculated after every round.\n The game continues until someone reaches 16 points.\n" +
    " A player can play out one of his/her cards: it can be used either for \"Capturing\" cards from the table or to just \"Trail\" it on the table.\n" +
    " If the player cannot take anything from the table, he/she must put one of his/her cards on the table.\n" +
    " The next two tasks are done automatically:\n   If the player takes cards from the table, he/she puts them in a separate pile of his/her own. The pile is used to count the points after the round has ended.\n" +
    "   Player must draw a new card from the deck after using a card so that he/she has always 4 cards in his/her hand.\n" +
    " The number of cards on the table can vary. For example, if someone takes all the cards from the table, the next player must put a card on the empty table.\n" +
    " (When the deck runs out, everyone plays until there are no cards left in any player’s hand).\n\n" +
    "Capturing:\n" +
    " Player can use a card to capture one or more cards of the same value and cards such that their summed value is equal to the used card.\n" +
    " For example, a player can capture with a J (value 11):\n {J}, {4,7}, {4,7, J}\n" +
    " If some player gets all the cards from the table at the same time, he/she gets a so called sweep which adds 1 point to the player's points.\n\n" +
    "Special Cards:\n There are a couple of cards that are more valuable in the hand than in the table,\n Aces: 14 in hand, 1 on table\n Diamonds-10: 16 in hand, 10 on table\n Spades-2: 15 in hand, 2 on table\n" +
    " For example: you can take two eights from the table with Diamonds-10\n For the other cards the values are same in the hand and on the table.\n\n" +
    "Scoring:\n When every player runs out of cards, the last player to take cards from the table gets the rest of the cards from the table. After this the points are calculated and added to the existing scores.\n\n" +
    " The following things grant points:\n\n Every sweep grants 1 point.\n Every Ace grants 1 point.\n The player with most cards gets 1 point.\n The player with most spades gets 2 points.\n The player with Diamonds-10 gets 2 points.\n The player with Spades-2 gets 1 point.\n\n" +
    "The textual user interface will give you instructions as you play. Game can be saved in a file but only results between rounds can be saved.\n\n"

  //THE GAME STARTS HERE
  print("\nWelcome to Cassino!\n\n" + help)
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
        print("\n" + round.inTurn().getName() + ", it's your turn." + "\n")
        var moveSuccesful = false
        while(!moveSuccesful){
          print(round)
          var move = ""
          if(round.table.deckSize() == 0) {
            print("You can only trail because there is no cards to capture.\n")
            move = "T"
          } else {
            print("Write\n'c' to capture\n't' to trail.\n\n'help' for rules.\n'quit' to save and exit the game.\n")
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
            case "QUIT" => {
              var cmdOk = false
              while(!cmdOk) {
                print("If you wish to save the results from before this round write 'save', else press Enter.\n")
                val a = readLine()
                a.toUpperCase match {
                  case "SAVE" => {
                    try{
                      game.save(game.getID() + ".Cassino")
                      print("Game saved, you can load it later with this game's id: " +  game.getID() + "\n")
                      cmdOk = true
                    } catch {
                      case e:FileNotFoundException => throw new FileNotFoundException("File not found.\n")
                      case e:IOException => throw new IOException("Error writing to file.\n")
                      case _ =>
                    }
                  }
                  case _ => {
                    cmdOk = true
                    moveSuccesful = true
                    roundOver = true
                    gameOver = true
                  }
                }
              }
            }
            case "HELP" => print(help)
            case _ => print("INVALID MOVE, options are 'C','T','quit' or 'help'. Your turn has started over." + "\n")
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
