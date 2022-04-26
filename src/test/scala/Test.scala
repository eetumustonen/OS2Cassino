import Cassino._
import Cassino.Deck._

import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException}
import scala.collection.mutable.Buffer
import scala.io.StdIn.readLine
import scala.collection.mutable.LinkedHashMap

/*
  This is test for basic features of the Deck.
 */
object DeckTest extends App {
  val deck = new Deck()
  deck.fullDeck()
  print(deck)
  deck.shuffle()
  print(deck)
  print(deck.deckSize() + "\n")
  val aceofspades: Card = deck.returnCard('♠', 'A').get
  print(aceofspades + "\n")

  val smallDeck = new Deck()
  smallDeck.fullDeck()
  for(i <- 0 until 39){
    smallDeck.pickFirst()
  }
  print(smallDeck)
  val dA = smallDeck.returnFirst()
  smallDeck.removeCard(dA)
  print(smallDeck)
  smallDeck.addCard(dA)
  print(smallDeck)
  //adding a dublicate throws an exception
  smallDeck.addCard(dA)
}

/*
  This is a test to see that the Game and Round work.
  As this is only one case, it isn't nearly enough to call it a complete testing.
 */
object TestGameAndRound extends App {
  val game = new Game("Test")
  game.addPlayer("A", 0)
  game.addPlayer("B", 0)
  game.startRound()
  val round = game.currentRound.get
  round.deal()
  print(round + round.deck.toString() +  round.stacksToString())
  while(!round.roundIsOver()){
    round.trail(round.playerCards(round.inTurn()).returnFirst().get.getSuit(), round.playerCards(round.inTurn()).returnFirst().get.getValue())
  }
  print(round + round.deck.toString() +  round.stacksToString())
  game.updatePoints()
  print(game)
}

/*
  This is a test for the method checkValidity()
  It is the most complex algorithm so proper testing is necessary.
  In the testing there are card dublicates because a Buffer is used instead of a Deck.
  Testing cases:
  1. "cards" contains a card larger in value than "card"
  2. sum of "cards" divided by "card" doesn't leave a remainder zero (0)
  3. sum is correct but it's not possible to form subsums of "card"
  4. sum is correct and can be split into subsums of "card"
  5. "cards" contains only cards of value "card"
  6. 10 of diamonds shoul return true with 16 aces.
  Should print:
    false
    false
    false
    true
    true
    true
 */
object TestingCheckValidity extends App {
  val p = scala.collection.mutable.LinkedHashMap((new Player("Test"))->123)
  val round = new Round(p)
  val deck = new Deck
  deck.fullDeck()
  val card = deck.returnCard('♣', '7').get
  val cards: Buffer[Card] = Buffer()
  //Case1, cards 3,4,9
  cards += deck.returnCard('♣', '3').get
  cards += deck.returnCard('♣', '4').get
  cards += deck.returnCard('♣', '9').get
  val ret1 = round.checkValidity(card, cards)
  print(ret1 + "\n")
  //Case2, cards 3,4,6
  cards -= deck.returnCard('♣', '9').get
  cards += deck.returnCard('♣', '6').get
  val ret2 = round.checkValidity(card, cards)
  print(ret2 + "\n")
  //Case3, cards 3,4,6,6,6,3
  cards += deck.returnCard('♣', '6').get
  cards += deck.returnCard('♣', '6').get
  cards += deck.returnCard('♣', '3').get
  val ret3 = round.checkValidity(card, cards)
  print(ret3 + "\n")
  //case4, cards 3,4,6,6,6,3,1,1,1,2,2
  cards += deck.returnCard('♣', 'A').get
  cards += deck.returnCard('♣', 'A').get
  cards += deck.returnCard('♣', 'A').get
  cards += deck.returnCard('♣', '2').get
  cards += deck.returnCard('♣', '2').get
  val ret4 = round.checkValidity(card, cards)
  print(ret4 + "\n")
  //case5, cards 7,7
  val cards2: Buffer[Card] = Buffer()
  cards2 += deck.returnCard('♣', '7').get
  cards2 += deck.returnCard('♣', '7').get
  val ret5 = round.checkValidity(card, cards2)
  print(ret5 + "\n")
  //case6, cards 16*1
  val card2 = deck.returnCard('♦', 'T').get
  val cards3: Buffer[Card] = Buffer()
  for(i <- 0 until 16){
    cards3 += deck.returnCard('♣', 'A').get
  }
  val ret6 = round.checkValidity(card2, cards3)
  print(ret6 + "\n")
}
/*
 This a test to check that Round's method countPoints()
 and Deck's method pointsndSpades() works correctly.
 Player A should have 2 points for ten of diamonds and 1 point for a sweep.
 Player B should have 1 point for two of spades and 2 points for most spades.
 Player C should have 1 point for most cards and 1 point for an ace.
 A: 3
 B: 3
 C: 2
 */
object TestPointCount extends App {
  val playerA = new Player("A")
  val playerB = new Player("B")
  val playerC = new Player("C")
  val deckA = new Deck
  val deckB = new Deck
  val deckC = new Deck
  deckA.addCard(Some(new Card('♦', 'T')))
  deckA.addCard(Some(new Card('♦', '9')))
  deckA.addCard(Some(new Card('♠', '7')))
  deckB.addCard(Some(new Card('♠', '6')))
  deckB.addCard(Some(new Card('♠', '5')))
  deckB.addCard(Some(new Card('♠', '2')))
  deckC.addCard(Some(new Card('♣', '7')))
  deckC.addCard(Some(new Card('♣', 'J')))
  deckC.addCard(Some(new Card('♣', 'K')))
  deckC.addCard(Some(new Card('♣', 'A')))
  val points = LinkedHashMap(playerA -> 0, playerB -> 0, playerC -> 0)
  val round = new Round(points)
  val newPoints = round.countPoints(LinkedHashMap(playerA -> deckA, playerB -> deckB, playerC -> deckC))
  round.addNewPoints(newPoints)
  round.sweep()
  print(round.getPoints())
}

/*
  This is a test to see ending a round in User interface works.
  It is copied and modified from class Cassino
  It shows that saving the game is succesfull and gives the option to either quit the game or continue playing.
 */
object TestRoundEnd extends App {
  val game = new Game("12345")
  game.addPlayer("A", 0)
  game.addPlayer("B", 0)
  game.addPlayer("C", 0)
  var gameOver = false
  while(!gameOver){
    game.startRound()
    val round = game.currentRound.get
    round.deal()
    var roundOver = false
    while(!roundOver){
      roundOver = true
      if(roundOver) {
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
                case e:FileNotFoundException => print("File not found.\n")
                case e:IOException => print("Error writing to file.\n")
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
    }
    game.updatePoints()
    if(game.getPoints().values.toBuffer.exists(_ >= 16)) gameOver  = true
    else {
      game.changeDealer()
    }
  }
}


//THIS IS A TEST TO CONFIRM THE DEALER CHANGES CORRECTLY AND THAT PLAYERS ARE IN THE INSERTED ORDER
object changeDealer extends App {
  val game = new Game("123")
  game.addPlayer("A", 2)
  print(game)
  game.addPlayer("B", 3)
  print(game)
  game.addPlayer("C", 5)
  print(game)
  game.addPlayer("D", 5)
  print(game)
  game.addPlayer("E", 5)
  print(game)
  game.addPlayer("F", 5)
  print(game)

  game.changeDealer()
  print(game)
  game.changeDealer()
  print(game)
  game.changeDealer()
  print(game)
  game.changeDealer()
  print(game)
  game.changeDealer()
  print(game)
  game.changeDealer()
  print(game)
}


/*
This is a test that shows that saving and loading a game's state into a file works.
First it creates a game and saves it into a file. Then it loads that file and creates a new game based on the file's contents.
Finally it prints out the game just to see it worked.
 */
object FileTesting extends App {
  val game = new Game("12345")
  game.addPlayer("Eetu", 0)
  game.addPlayer("Aleksi", 0)
  try{
    game.save(game.getID() + ".Cassino")
  } catch {
    case e:FileNotFoundException => print("File not found.\n")
    case e:IOException => print("Error writing to file.\n")
  }

  //THIS IS A METHOD IN CLASS CASSINO
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
      case notFound: FileNotFoundException => Console.println("FileNotFound\n")
      case e: IOException => Console.println("IOException\n")
    }
    ret
  }
  val data = load(game.getID() + ".Cassino")
  val game2 = new Game(data(1))
  for(i <- 2 until data.length){
    val plrData = data(i).split(":")
    game2.addPlayer(plrData(0), plrData(1).toInt)
  }
  print(game)
}
