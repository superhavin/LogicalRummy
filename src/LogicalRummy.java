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
import java.util.ArrayList;

public class LogicalRummy{
    //variables that augment the main game, shouldnt need multiple
    public static PlayersHand[] players; //number of players
    public static ArrayList<Cards> deck = new ArrayList<Cards>(); //neutral pile of cards
    public static ArrayList<Cards> discard = new ArrayList<Cards>(); //discard pile of cards
    public static int whoTurnIsIt = 1; //number to represent who's turn it is
    //Constant Variables
    public static final int SUITES = 4;

    //Constructor for main game to allow each player to have their own individual hand
    /**
     * @param numberOfPlayers number of total players
     * @param numberOfCardsInDeck number of cards in deck
     */
    public LogicalRummy(int numberOfPlayers, int numberOfCardsInDeck){
        players = new PlayersHand[numberOfPlayers];

        for(int i = 0; i < SUITES; i++){ //divides value of cards between its suites
            //numberOfCardsInDeck
            for(int o = 0; o < numberOfCardsInDeck/SUITES; o++){ //number of suites
                deck.add(new Cards(o,i));
            }
        }
    }

    /**
     * method reads player hand and returns a card in that position
     */
    public Cards getCardFromHand(int player, int position){
        return (new Cards(0,0));
    }

    /**
     * method to add card to players hand
     */
    public void addCardToHand(int player, int position){

        //return (new Cards(0,0));
    }

    /**
     * method discard card from players hand, and returns the card which was removed
     */
    public Cards removeCardFromHand(int player, int position){
        return (new Cards(0,0));
    }

    /**
     * shuffling the drawPile
     * basically restarting the drawPile, by adding the discardPile to it
     */
    public void shuffleDrawPile(){

    }

    /**
     * method for displaying the card on the pile
     * For the active cards in play
     */
    public String getDrawPile(){
        String total = "";
        for(int i = 0; i < deck.size(); i++){
            total += deck.get(i) + " ";
        }
        return total;
    }

    public static void main(String[] args){
        LogicalRummy mainGame = new LogicalRummy(4,52);
        System.out.println(mainGame.getDrawPile());
        //mainGame.playingTurn();
    }

    //Static methods used multiple times by the game
    /**
     * method for playing out who ever turn it is
     */
    //it returns boolean, so we can determine whether the turn was valid or not
    static boolean playingTurn(){
        return false;
    }

}