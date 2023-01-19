import java.util.Scanner;

public class MainGame {
    /**
     * main game which everything will come together
     * REMINDER: all method inputs are in index form; tho when displaying use normal form
     */
    public static void main(String[] args) {
        //variables which will be used for instantiating a game
        Scanner userInput = new Scanner(System.in);
        final int MAX_PLAYER = 4; //total number of players
        final int NUMBER_SETS = 2; //number of sets of deck
        final int GAME_ROUNDS = 3; //number of rounds in a game (wraps around at 7)
        final int HAND_SIZE = 6; //size of hand, make sure it is not to large
        //Setting Up the Game
        LogicalRummy mainGame = new LogicalRummy(MAX_PLAYER, NUMBER_SETS*52, GAME_ROUNDS, HAND_SIZE);
        //mainGame.addJokersToDrawPile(2); YET TO BE IMPLEMENTED DO NOT USE
        //Creating Turn Header, prints at the start of each turn
        int turnCounter = 0;
        turnCounter++;
        String turnMsg = "\n\n\nTurn #: " + turnCounter + "\nRound: " + mainGame.whatRoundIsIt().name();
        System.out.println(turnMsg);
        while(true){
            System.out.println("Player " + mainGame.whoseTurnIsIt() + " Turn: \n");
            boolean hasMoved = mainGame.playerTurn(userInput, mainGame.whoseTurnIsIt()-1); //will always be true,
            mainGame.turnCounter(hasMoved);
            //check if the game is over, if so displays who won
            if(mainGame.isRoundOverIsGameOver()){
                System.out.println(mainGame.displayWinner());
                break;
            }else{
                turnCounter++; //game not over, display what turn it is
                turnMsg = "\n\n\nTurn #: " + turnCounter + "\nRound: " + mainGame.whatRoundIsIt().name();
                System.out.println(turnMsg);
            }
        }

    }

    //Tested: readPromptUserInput, displayWinner, pointTracker, playerTurn|buyCard, turnCounter|roundCounter,
    // isSet|isRun, checkIsSetIsRun, addToLaidCard, isRoundOverIsGameOver
    //reads all players hand: //for (int i = 0; i < MAX_PLAYER; i++) {System.out.println(mainGame.readEntireHand(i));} System.out.println(mainGame.readPile());
    //reads all of the main deck: //System.out.println(deck.size()); mainGame.removeCardFromDrawPile(); for(int i = 0; i < deck.size(); i++){System.out.println(deck.get(i).readCard());}
    //gives player 1 a winning hand //mainGame.discardEntireHand(0); mainGame.players[0].cardsInHand.add(new Cards(0,1)); mainGame.players[0].cardsInHand.add(new Cards(0,1)); mainGame.players[0].cardsInHand.add(new Cards(0,1)); mainGame.players[0].cardsInHand.add(new Cards(2,1)); mainGame.players[0].cardsInHand.add(new Cards(2,1)); mainGame.players[0].cardsInHand.add(new Cards(2,1));
    //method to read all properties of the class for testing, public void allProperties(){System.out.println("x" + x);}
}
