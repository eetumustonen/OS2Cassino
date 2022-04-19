package Cassino
import Cassino.Deck.Card

import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
import scala.io.StdIn.readInt

object Main extends App {
  //Console.println("Welcome to Cassino, insert an id for this game instance: "+ "\n")

  //PRINT A GUIDE WITH A COMMANDS AND RULES AND SUCH

  //val id = readLine()
  val id = "123"
  val game = new Game(id)

  //add players

  //REMOVE THE COMMENT INDICATORS FROM NEXT BLOCK

  /*
  var nextPlayer = true
  var plrCount = 1
  while(nextPlayer){
    Console.println("Please enter name for player " + plrCount + " or write \"START\" to start the game")
    val name = readLine()
    if(name.toUpperCase == "START") nextPlayer = false
    else {
      game.addPlayer(name)
      plrCount += 1
    }
    if(plrCount == 13) {
      nextPlayer = false
      Console.println("Maximum number of players added.")
    }
  }
  */

  //DELETE THESE AFTER TESTING
  game.addPlayer("Eetu")
  game.addPlayer("Aleksi")
  game.addPlayer("Mimmi")
  Console.println("Players added, lets start the game\n" + game) // to check it works
  var gameOver = false
  while(!gameOver){
    game.startRound()
    val round = game.currentRound.get
    round.deal()
    var roundOver = false
    while(!roundOver){
      if(round.roundIsOver()) roundOver = true
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
                  print("Now give the indexes of cards to capture from the table, separated with commas. For example 0,2,3\n")
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
              } else print("INVALID INDEX, THE INDEX SHOULD BE WITHIN " + 0 + " to " + (round.playerCards(round.inTurn()).deckSize() - 1) + ". Your turn has started over." + "\n")
            }
            case _ => print("INVALID MOVE, options are C or T. Your turn has started over." + "\n")
          }
        }
      }
    }
    print("First round has ended\n")
    game.updatePoints()
    //check if someone has 16 points
    if(game.getPoints().values.toBuffer.exists(_ >= 16)) gameOver  = true
  }
  print(game)
  val winner = game.getPoints().maxBy(_._2)
  print("Winner is " + winner._1.getName() + "\nGame ended")
}
