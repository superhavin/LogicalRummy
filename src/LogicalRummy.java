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
import java.util.Scanner;

public class LogicalRummy{
    //Variables that augment the main game, shouldnt need multiple
    public static PlayersHand[] players; //number of players
    public static ArrayList<Cards> deck = new ArrayList<Cards>(); //neutral pile of cards
    public static ArrayList<Cards> discard = new ArrayList<Cards>(); //discard pile of cards
    public static int rounds; //number of rounds
    public static ArrayList<PlayersHand> winners = new ArrayList<PlayersHand>();
    /**
     * uses methods to increment/read
     */
    private static int playerTurnOrder = 0; //number to represent who's turn it is
    private static Rounds gameRoundOrder = Rounds.SETS2;
    public static int roundCounter = 1;
    //Constant Variables
    public static final int SUITES = 4;
    public static final double DECK_SIZE = 52.0;
    public static final int MIN_PLAYER = 2;
    public static final int MIN_ROUNDS = 1;

    /**
     * the different 'rules' for each round of Rummy
     * Sets = 3 or more consecutive cards of the same suit
     * Runs = 4 or more cards in the same suit a sequence of 1, 2, 3, 4; or 12, 11, 10, 9;
     */
    public enum Rounds {SETS2, SET1RUN1, RUNS2, SETS3, SET2RUN1, SET1RUN2, RUN3};

    //Constructor for main game to allow each player to have their own individual hand
    /**
     * @param numberOfPlayers number of total players (min. 2)
     * @param numberOfCardsInDeck number of cards in deck (min. 52)
     */
    public LogicalRummy(int numberOfPlayers, int numberOfCardsInDeck, int numOfRounds){
        //Catching initialization problems
        if(numberOfPlayers < MIN_PLAYER || numberOfCardsInDeck < DECK_SIZE || numOfRounds < MIN_ROUNDS){
            throw new ArithmeticException("Initialization- Below minimum values");
        }
        //number of rounds
        rounds = numOfRounds;
        //Creates empty player hands
        players = new PlayersHand[numberOfPlayers];
        for(int i = 0; i < players.length; i++){
            players[i] = new PlayersHand(); //setting up all the players hand doesnt take much
        }
        //Creates a non-shuffled deck full of cards
        for(int j = 0; j < (int)Math.ceil(numberOfCardsInDeck/DECK_SIZE); j++) { //if cards are over 52, loops over
            for (int i = 0; i < SUITES; i++) { //divides value of cards between its suites
                for (int o = 0; o < numberOfCardsInDeck / SUITES; o++) { //number of suites
                    if(deck.size() >= numberOfCardsInDeck){ //breaks out once the deck is FULL
                        break;
                    }
                    deck.add(new Cards(o, i));
                }
            }
        }
    }
    //Hand Methods
    /**
     * method reads player hand and returns a card in that position
     */
    public Cards getCardFromHand(int player, int position){
        return players[player].getCard(position);
    }

    /**
     * method to add card to players hand
     */
    //return (new Cards(0,0)); //might want to return the card you added
    public void addCardToHand(int player, Cards card){
        players[player].addCard(card);
    }

    /**
     * method discard card from players hand, and returns the card which was removed
     */
    public Cards removeCardFromHand(int player, int position){
        Cards temp = players[player].removeCard(position);
        discard.add(temp);
        return temp;
    }

    /**
     * method to read all the cards in a players hand
     */
    public String readEntireHand(int player){
        String total = "";
        total += "Player " + (player+1) + ":\n";
        for(int i = 0; i < players[player].cardsInHand.size(); i++){
            total += players[player].getCard(i).readCard() + "\n";
        }
        return total;
    }

    /**
     *  NEED TO: Figure out how to remove all cards in hand while adding them to discard
     */
    public void discardEntireHand(int player){
        int max = players[player].cardsInHand.size();
        for(int i = 0; i < max; i++){
            removeCardFromHand(player,0);
        }
    }
    //Pile Methods
    /**
     * shuffling the drawPile
     * basically restarting the drawPile, by adding the discardPile to it
     *
     */
    public void shuffleDrawPile(){
        for(int i = 0; i < discard.size(); i++){
            deck.add(discard.remove(i));
        }
    }

    /**
     * method for displaying the card on the pile
     * for the active cards in play
     */
    public String readPile(){
        String total = "";
        total += "Draw Pile:\n";
        for(int i = 0; i < deck.size(); i++){
            total += deck.get(i) + " ";
        }
        return total;
    }
    public String readPile(boolean isDiscard){
        String total = "";
        total += "Discard Pile:\n";
        for(int i = 0; i < discard.size(); i++){
            total += discard.get(i) + " ";
        }
        return total;
    }

    /**
     * method to add a card to the players hand, and remove the card from the draw pile
     */
    public Cards addCardFromDrawPile(int player){
        //checks if draw pile has one card or less, and shuffles it
        if(deck.size() <= 1){
            shuffleDrawPile();
        }
        Cards temp = deck.remove((int)(Math.random()*deck.size()));
        addCardToHand(player,temp);
        return temp;
    }

    public int whoseTurnIsIt(){
        return playerTurnOrder;
    }

    //Game Methods

    /**
     * standard input method for users, to optimize retries
     * @param prompt String which will lead the message
     * @param max Standard form, not indexed
     */
    public int readPromptUserInput(Scanner scan, String prompt, int max){
        while(true) {
            System.out.print(
                    prompt + ", input number from 1-" + max + ":"
            );
            int input = scan.nextInt(); //turning standard into index form
            if (input >= 1 && input <= max) {
                return (input-1);
            }else{
                System.out.println("Try again! Wrong input");
            }
        }
    }

    /**
     *  method to increment the playerTurnOrder
     */
    public void turnCounter(boolean validTurn){
        if(validTurn){
            if(playerTurnOrder >= players.length){
                playerTurnOrder = 0;
                return;
            }
            playerTurnOrder++;
        }else{
            System.out.println("Try again! Invalid Move");
        }
    }

    /**
     * playing out a normal turn with options
     */
    public boolean playerTurn(Scanner scan, int player){
        //Player draws a card and gets to see hand
        System.out.println("Player " + player + " drew this card: " + addCardFromDrawPile(player).readCard());
        System.out.println(" Current Hand:\n " + readEntireHand(player));
        //If the player can let the player options lay down set/run, or,

        switch (gameRoundOrder){
            case SETS2:
                if(isSet(player,true) && isSet(player,true)){
                    ; //if u can lay down cards
                }else{
                    players[player].returnLaidToHand(); //if cards have been laid, conditions are not correct, return to main hand
                }
                break;
            case SET1RUN1:
                if(isSet(player,true) && isRun(player,true)){
                    ;
                }else{
                    players[player].returnLaidToHand();
                }
                break;
            case RUNS2:
                if(isRun(player,true) && isRun(player,true)){
                    ;
                }else{
                    players[player].returnLaidToHand();
                }
                break;
            case SETS3:
                if(isSet(player,true) && isSet(player,true) && isSet(player,true)){
                    ;
                }else{
                    players[player].returnLaidToHand();
                }
                break;
            case SET2RUN1:
                if(isSet(player,true) && isSet(player,true) && isRun(player,true)){
                    ;
                }else{
                    players[player].returnLaidToHand();
                }
                break;
            case SET1RUN2:
                if(isSet(player,true) && isRun(player,true) && isRun(player, true)){
                    ;
                }else{
                    players[player].returnLaidToHand();
                }
                break;
            case RUN3:
                if(isRun(player,true) && isRun(player,true) && isRun(player,true)){
                    ;
                }else{
                    players[player].returnLaidToHand();
                }
                break;
            default:
                throw new ArithmeticException("Player Turn- Order Out of Bound");
        }

        //Player discards card and ends turn
        String prompt = "Player " + player + "Turn: input which card to discard";
        removeCardFromHand(player,
                readPromptUserInput(scan, prompt,
                        players[player].cardsInHand.size())
            );
        return true;
    }

    /**
     *  counter for changing what round it is
     */
    public void roundCounter(){
        roundCounter++;
        switch (gameRoundOrder){ //SETS2, SET1RUN1, RUNS2, SETS3, SET2RUN1, SET1RUN2, RUN3
            case SETS2:
                gameRoundOrder = Rounds.SET1RUN1;
                break;
            case SET1RUN1:
                gameRoundOrder = Rounds.RUNS2;
                break;
            case RUNS2:
                gameRoundOrder = Rounds.SETS3;
                break;
            case SETS3:
                gameRoundOrder = Rounds.SET2RUN1;
                break;
            case SET2RUN1:
                gameRoundOrder = Rounds.SET1RUN2;
                break;
            case SET1RUN2:
                gameRoundOrder = Rounds.RUN3;
                break;
            case RUN3:
                gameRoundOrder = Rounds.SETS2;
                break;
            default:
                throw new ArithmeticException("Game Round- Order Out of Bound");
        }
    }

    /**
     * check if the round has ended, then if the game is over
     */
    public boolean isGameOver(){
        boolean isValid = false;
        int winningPlayer = 0;
        //has anyone won
        try {
            //check each player's laid cards to see if they have laid down all the cards they need to for the round
            for (int i = 0; i < players.length; i++) {
                switch (gameRoundOrder){
                    case SETS2:
                        if(isSet(i) && isSet(i)){
                            winningPlayer = i;
                            isValid = true;
                        }
                        break;
                    case SET1RUN1:
                        if(isSet(i) && isRun(i)){
                            winningPlayer = i;
                            isValid = true;
                        }
                        break;
                    case RUNS2:
                        if(isRun(i) && isRun(i)){
                            winningPlayer = i;
                            isValid = true;
                        }
                        break;
                    case SETS3:
                        if(isSet(i) && isSet(i) && isSet(i)){
                            winningPlayer = i;
                            isValid = true;
                        }
                        break;
                    case SET2RUN1:
                        if(isSet(i) && isSet(i) && isRun(i)){
                            winningPlayer = i;
                            isValid = true;
                        }
                        break;
                    case SET1RUN2:
                        if(isSet(i) && isRun(i) && isRun(i)){
                            winningPlayer = i;
                            isValid = true;
                        }
                        break;
                    case RUN3:
                        if(isRun(i) && isRun(i) && isRun(i)){
                            winningPlayer = i;;
                            isValid = true;
                        }
                        break;
                    default:
                        throw new ArithmeticException("Game Over- Order Out of Bound");
                }
            }

        }
        catch(Exception e){
            throw new ArithmeticException("Game Over- Win State Check Failure");
        }
        //will do once round is finished, then checks for if the game is over
        if(isValid){
            discardEntireHand(winningPlayer); //discard winning players hand first, so they get 0 points
            pointTracker(); //gets the total points of all the remaining players cards
            for(int i = 0; i < players.length; i++){
                discardEntireHand(i); //discards all cards from ALL players
            }
            roundCounter(); //moves onto next round
            isValid = false;
            if(roundCounter >=  rounds){
                isValid = true; //if the entire game is over
            }
        }
        return isValid;
    }

    /**
     * Display who won
     */
    public String displayWinner(){
        String total = "";
        //S
        PlayersHand[] tempPlayer = players.clone();
        int lowest = tempPlayer[0].getRoundPoints();
        while(winners.size() <= players.length) {
            for (int i = 0; i < tempPlayer.length; i++) {
                if (tempPlayer[i].getRoundPoints() < lowest) {
                    lowest = tempPlayer[i].getRoundPoints();
                }

            }
        }
        total += "The Winners Are...\n";
        for(int i = 0; i < winners.size(); i++){
            total += "Rank #" + i + " Player " + winners.get(i).c +
                    " got this many points: " + winners.get(i).getRoundPoints() + ".\n";
        }
        return total;
    }

    /**
     * method to add a certain number of cards to a player's hand
     */
    public void startingHand(int numOfCards){
        for(int i = 0; i < players.length; i++){
            for(int o = 0; o < numOfCards; o++){
                addCardFromDrawPile(i);
            }
        }
    }

    /**
     * check whether a set has been made,
     */
    //COPY-PASTE-WARNING: isSet-2/isRun-2 are both WIP
    private boolean isSet(int player){
        for(int i = 0; i < players[player].laidCards.size()-2; i++){
            if (players[player].laidCards.get(i).getValue() == players[player].laidCards.get(i+1).getValue() &&
                    players[player].laidCards.get(i+1).getValue() == players[player].laidCards.get(i+2).getValue() &&
                    players[player].laidCards.get(i+2).getValue() == players[player].laidCards.get(i).getValue()) {
                players[player].displayWin(players[player].laidCards.remove(i));
                players[player].displayWin(players[player].laidCards.remove(i+1));
                players[player].displayWin(players[player].laidCards.remove(i+2));
                return true;
            }
        }
        return false;
    }

    /**
     * @param isPlayerTurn for a standard turn
     */
    private boolean isSet(int player, boolean isPlayerTurn){
        for(int i = 0; i < players[player].cardsInHand.size()-2; i++){
            if (players[player].cardsInHand.get(i).getValue() == players[player].cardsInHand.get(i+1).getValue() &&
                    players[player].cardsInHand.get(i+1).getValue() == players[player].cardsInHand.get(i+2).getValue() &&
                    players[player].cardsInHand.get(i+2).getValue() == players[player].cardsInHand.get(i).getValue()) {
                players[player].layDownCard(i);
                players[player].layDownCard(i+1);
                players[player].layDownCard(i+2);
                return true;
            }
        }
        return false;
    }

    /**
     * check whether a set has been made,
     */
    private boolean isRun(int player){
        for(int i = 0; i < players[player].laidCards.size()-3; i++) {
            if(players[player].laidCards.get(i).getValue() +1 == players[player].laidCards.get(i+1).getValue() &&
                    players[player].laidCards.get(i+1).getValue() +1 == players[player].laidCards.get(i+2).getValue() &&
                    players[player].laidCards.get(i+2).getValue() +1 == players[player].laidCards.get(i+3).getValue()){
                players[player].displayWin(players[player].laidCards.remove(i));
                players[player].displayWin(players[player].laidCards.remove(i+1));
                players[player].displayWin(players[player].laidCards.remove(i+2));
                players[player].displayWin(players[player].laidCards.remove(i+3));
                return true;
            }
        }
        for(int i = 0; i < players[player].laidCards.size()-3; i++) {
            if(players[player].laidCards.get(i).getValue() -1 == players[player].laidCards.get(i+1).getValue() &&
                    players[player].laidCards.get(i+1).getValue() -1 == players[player].laidCards.get(i+2).getValue() &&
                    players[player].laidCards.get(i+2).getValue() -1 == players[player].laidCards.get(i+3).getValue()){
                players[player].displayWin(players[player].laidCards.remove(i));
                players[player].displayWin(players[player].laidCards.remove(i+1));
                players[player].displayWin(players[player].laidCards.remove(i+2));
                players[player].displayWin(players[player].laidCards.remove(i+3));
                return true;
            }
        }
        return false;
    }

    /**
     * @isPlayerTurn for a standard turn
     */
    private boolean isRun(int player, boolean isPlayerTurn){
        for(int i = 0; i < players[player].cardsInHand.size()-3; i++) {
            if(players[player].cardsInHand.get(i).getValue() +1 == players[player].cardsInHand.get(i+1).getValue() &&
                    players[player].cardsInHand.get(i+1).getValue() +1 == players[player].cardsInHand.get(i+2).getValue() &&
                    players[player].cardsInHand.get(i+2).getValue() +1 == players[player].cardsInHand.get(i+3).getValue()){
                players[player].layDownCard(i);
                players[player].layDownCard(i+1);
                players[player].layDownCard(i+2);
                players[player].layDownCard(i+3);
                return true;
            }
        }
        for(int i = 0; i < players[player].cardsInHand.size()-3; i++) {
            if(players[player].cardsInHand.get(i).getValue() -1 == players[player].cardsInHand.get(i+1).getValue() &&
                    players[player].cardsInHand.get(i+1).getValue() -1 == players[player].cardsInHand.get(i+2).getValue() &&
                    players[player].cardsInHand.get(i+2).getValue() -1 == players[player].cardsInHand.get(i+3).getValue()){
                players[player].layDownCard(i);
                players[player].layDownCard(i+1);
                players[player].layDownCard(i+2);
                players[player].layDownCard(i+3);
                return true;
            }
        }
        return false;
    }
    /**
     * giving each player's points for the remaining cards in hand
     * also removes all cards from player hand (use at end of a round)
     * can be changed, research unconfirmed
     */
    private void pointTracker(){
        for(int i = 0; i < players.length; i++){
            for(int o = 0; o < players[i].cardsInHand.size(); o++){
                switch (getCardFromHand(i,o).getValue()){
                    case 1:
                        players[i].roundPoints += 25;
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        players[i].roundPoints += 5;
                        break;
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        players[i].roundPoints += 15;
                        break;
                    default:
                        throw new ArithmeticException("Point Mistake- Card Value Out of Bound");
                }
            }
        }
    }

    /**
     * main game which everything will come together
     *
     * REMINDER: all method inputs are in index form; tho when displaying use normal form
     */
    public static void main(String[] args) {
        //variables which will be used for instantiating a game
        Scanner userInput = new Scanner(System.in);
        final int MAX_PLAYER = 4; //total number of players
        final int NUMBER_SETS = 1; //number of sets of deck
        final int GAME_ROUNDS = 3; //number of rounds in a game
        final int HAND_SIZE = 6;
        //Setting Up the Game
        LogicalRummy mainGame = new LogicalRummy(MAX_PLAYER, NUMBER_SETS * 52, GAME_ROUNDS);
        mainGame.startingHand(HAND_SIZE);

        //Main Game
        while (mainGame.isGameOver()) {
            boolean validMove = mainGame.playerTurn(userInput, mainGame.whoseTurnIsIt());
            mainGame.turnCounter(validMove);
        }

        //for testing: for (int i = 0; i < MAX_PLAYER; i++) {System.out.println(mainGame.readEntireHand(i));} System.out.println(mainGame.readPile());

    }
    //Static methods which are used multiple times by the main game

}