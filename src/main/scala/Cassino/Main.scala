package Cassino
import Cassino.Deck.Card

import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine

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
      print(round)
      print(round.inTurn().getName() + ", it's your turn what would you like to do?\n")
      if(round.roundIsOver()) roundOver = true
      else
      round.trail(round.playerCards(round.inTurn()).returnFirst().get.getSuit(), round.playerCards(round.inTurn()).returnFirst().get.getValue())
    }
    print("First round has ended\n")
    game.updatePoints()
    gameOver = true
  }
  print(game)
  print("Game ended")
}
