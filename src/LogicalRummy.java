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
            for(int o = 0; o < numberOfCardsInDeck/SUITES; o++){ //number of suites
                deck.add(new Cards(o,i));
            }
        }
    }

    //method reads player hand and returns a card in that position
    public Cards getCardFromHand(int player, int position){

        return (new Cards(0,0));
    }
    //method to add card to players hand
    public Cards addCardToHand(int player, int position){

        return (new Cards(0,0));
    }

    //method discard card from players hand
    public Cards removeCardFromHand(int player, int position){

        return (new Cards(0,0));
    }

    //method for displaying the card on the pile
    public String getCardPile(){
        String total = "";
        for(int i = 0; i < deck.size(); i++){
            total += deck.get(i) + " ";
        }
        return total;
    }

    public static void main(String[] args){
        LogicalRummy mainGame = new LogicalRummy(4,52);
        System.out.println(mainGame.getCardPile());
    }

    //Static methods used multiple times by the main method
    //method for playing out who ever turn it is
    //its a boolean so you can verify if the turn was successful
    static boolean playingTurn(){
        return false;
    }

}