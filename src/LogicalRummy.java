/*
 * Authors:
 * Kevin Kamau
 * Cory Vo
 * David Mizic
 *
 * CS 142 Team Project: Logical Rummy
 *
 * Making a working game of rummy
 */

public class LogicalRummy{
    //shared variables that needed by multiple methods
    public static int numberOfPlayers = 0; //number of players
    public static int maxHandSize = 0; //max number of cards hands can hold
    final static int numberOfCardsInDeck = 52; //max number of cards in the deck
    public static int whosTurnIsIt = 1; //number to represent who's turn it is (1-4)

    //Constructor for main game to allow each player to have thier own indiviual hand
    public LogicalRummy(int numberOfPlayers, int maxHandSize){
        this.numberOfPlayers = numberOfPlayers;
        this.maxHandSize = maxHandSize;
    }

    //method reads player hand and returns a card in that position
    public int getHand(int position){
        return 0;
    }

    //method for playing out who ever turn it is
    static int playingTurn(){
        return 0;
    }

    //method for displaying the card
    static void getBoard(){
        System.out.println("");
    }
}