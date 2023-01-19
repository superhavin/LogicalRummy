/*
 * Author:
 * Kevin Kamau
 *
 * CS 142 Team Project: Logical Rummy
 *
 * Making a working game of rummy-
 * thesprucecraft.com/rummy-card-game-rules-and-strategies-411141
 */
import java.util.ArrayList;
import java.util.Scanner;

public class LogicalRummy{
    //Variables that augment the main game,
    private static PlayersHand[] players; //number of players
    private static ArrayList<Cards> deck = new ArrayList<Cards>(); //neutral pile of cards
    private static ArrayList<Cards> discard = new ArrayList<Cards>(); //discard pile of cards
    private static int rounds; //number of rounds
    private static int numStartingHand = 0;
    private static int playerTurnOrder = 1; //number to represent who's turn it is (1-max)
    private static Rounds gameRoundOrder = Rounds.SETS2; //represents which round it is
    private static int roundCounter = 1;
    private static Cards topCard = null;
    //Constant Variables
    private static final int SUITES = 4;
    private static final int RANKS = 13;
    private static final double DECK_SIZE = 52.0;
    private static final int MIN_PLAYER = 2;
    private static final int MIN_ROUNDS = 1;

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
    public LogicalRummy(int numberOfPlayers, int numberOfCardsInDeck, int numberOfRounds, int numberOfCardsInStartingHand){
        //Catching initialization problems
        if(numberOfPlayers < MIN_PLAYER || numberOfCardsInDeck < DECK_SIZE || numberOfRounds < MIN_ROUNDS){
            throw new ArithmeticException("Initialization- Below minimum values");
        }
        if(numberOfCardsInDeck <= numberOfPlayers*numberOfCardsInStartingHand){
            throw new ArithmeticException("Initialization- Not enough cards in the deck");
        }
        //number of rounds
        rounds = numberOfRounds;
        //Creates empty player hands
        players = new PlayersHand[numberOfPlayers];
        for(int i = 0; i < players.length; i++){
            players[i] = new PlayersHand(); //setting up all the players hand doesnt take much
        }
        //Creates a non-shuffled deck full of cards
        addingStartingCardToDeck(numberOfCardsInDeck);
        //Adding cards to player hand
        numStartingHand = numberOfCardsInStartingHand;
        startingHand(numberOfCardsInStartingHand);
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
        total += "Current Hand of Player " + (player+1) + ":\n";
        for(int i = 0; i < players[player].cardsInHand.size(); i++){
            total += /*(i+1) +*/ "|" + players[player].getCard(i).readCard() + "|\n";
        }
        return total;
    }

    /**
     * removes all cards in hand while adding them to discard
     */
    public void discardEntireHand(int player){
        for(int i = 0; i < players[player].cardsInHand.size(); i++){
            discard.add(players[player].getCard(i));
        }
        players[player].cardsInHand.clear();
    }

    /**
     * removes all laid down cards while adding them to discard
     */
    public void discardLaidCards(int player){
        for(int i = 0; i < players[player].laidStacks.size(); i++){
            for(int o = 0; o < players[player].laidStacks.get(i).size(); o++){
                discard.add(players[player].laidStacks.get(i).get(o));
            }
        }
        for(int i = 0; i < players[player].laidStacks.size(); i++){
            players[player].laidStacks.get(i).clear();
        }
    }

    public void returnLaidToHand(int player){
        for(int i = 0; i < players[player].laidStacks.size(); i++){
            for(int o = 0; o < players[player].laidStacks.get(i).size(); o++){
                players[player].addCard(players[player].laidStacks.get(i).get(o));
            }
        }
        for(int i = 0; i < players[player].laidStacks.size(); i++){
            players[player].laidStacks.get(i).clear();
        }
    }

    /**
     * method to read all the cards the player has laid down
     */
    public String readLaidCards(int player){
        String total = "";
        total += "Current Laid Cards of Player " + (player+1) + ":\n";
        for(int i = 0; i < players[player].laidStacks.size(); i++){
            total += " Stack #" + (i+1) + ": \n";
            for(int o = 0; o < players[player].laidStacks.get(i).size(); o++){
                total += "  |" + players[player].laidStacks.get(i).get(o).readCard() + "|\n";
            }
        }
        return total;
    }

    //Pile Methods
    /**
     * shuffling the drawPile
     * basically restarting the drawPile, by adding the discardPile to it
     */
    public void shuffleDrawPile(){
        for(int i = 0; i < discard.size(); i++){
            deck.add(discard.remove(0));
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
    public String readPile(boolean isDiscard){ //shit code make better in the future
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
        Cards temp = removeCardFromDrawPile();
        addCardToHand(player,temp);
        return temp;
    }
    /**
     * method to remove card from draw pile and return it
     */
    public Cards removeCardFromDrawPile(){
        //checks if draw pile has one card or less, and shuffles it
        if(deck.size() <= 1){
            System.out.println("Deck Empty");
            shuffleDrawPile();
        }
        return deck.remove((int)(Math.random()*deck.size()));
    }

    /**
     * adding special joker card to deck
     */
    public void addJokersToDrawPile(int numOfCards){
        for(int i = 0; i < numOfCards; i++){
            deck.add(new JokerCards());
        }
    }

    /**
     * returns the standard form of whose turn it is
     */
    public int whoseTurnIsIt(){
        return playerTurnOrder;
    }

    /**
     * returns what GameRound it is
     */
    public Rounds whatRoundIsIt(){
        return gameRoundOrder;
    }
    //Game Methods

    /**
     * standard input method for users, to optimize retries
     * @param prompt String which will lead the message
     * @param max Standard form, not indexed
     */
    public int readPromptUserInput(Scanner scan, String prompt, int max){
        int input = 0;
        while(true) {
            System.out.print(
                    prompt + ", input number from 1-" + max + ":"
            );
            if(scan.hasNextInt()) {
                input = scan.nextInt();
            }else{
                input = 0;
                scan.next();
            }
            if (input >= 1 && input <= max) {
                return (input-1); //turning standard into index form
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
                playerTurnOrder = 1;
                return;
            }
            playerTurnOrder++;
        }else{
            System.out.println("Try again! Invalid Turn");
        }
    }

    /**
     * playing out a normal turn with options
     */
    public boolean playerTurn(Scanner scan, int player) {
        //Player draws a card and gets to see hand
        String playerIntro = "Player " + (player + 1) + " ";
        //asks to buy card from plays, and adds a card to deck depending on their choices
        boolean hasBought = buyCard(scan, player);
        if (hasBought) {
            System.out.println("\n" + playerIntro + "drew this card: [" + addCardFromDrawPile(player).readCard() + "]\n");
        } else {
            addCardToHand(player, topCard);
            System.out.println("\n" + playerIntro + "drew this card: " + topCard.readCard() + "\n");
            topCard = null;
        }
        //checking if the players Laid Cards are empty
        Boolean isLaidEmpty = null; //will throw null error if statement below breaks
        if (players[player].laidStacks.get(0).size() <= 0) {
            isLaidEmpty = true;
        } else if (players[player].laidStacks.get(0).size() > 0) {
            isLaidEmpty = false;
        }

        //the player can let the player options lay down set/run,
        if (isLaidEmpty){ //only if player hasn't laid
            try {
                if(checkIsSetIsRun(player)){
                    isLaidEmpty = false; //if the player has laid down a set, then the laid is no longer empty
                }
            } catch (Exception e) {
                throw new ArithmeticException("Check Set/Run Error"); //look out for ERROR while checking for Runs
            }
        }

        //the player can add cards in their hand to other laid piles (including their own),
        if (!(isLaidEmpty)){ //only if player has laid down a card
            try {
                addToLaidCard(player);
                System.out.println("\n" + readLaidCards(player)); //shows all players laid card,
            } catch (Exception e) {
                throw new ArithmeticException("Check Laid Addition Error");
            }
        }

        //Player discards card and ends turn
        String discardPrompt = playerIntro + "End of Turn: input which card to discard";
        if(players[player].cardsInHand.size() > 0) {
            System.out.println(readEntireHand(player)); //readEntireHand has title "Current Hand Player i"
            int playerDiscardChoice = readPromptUserInput(scan, discardPrompt, players[player].cardsInHand.size());
            Cards discardTemp = removeCardFromHand(player, playerDiscardChoice);
            discard.add(discardTemp);
            System.out.println(playerIntro + "Discarded this card: *" + discardTemp.readCard() + "*\n\n");
        }else{
            System.out.println(playerIntro + "Hand is Empty!"); //if player hand is empty
            //return false; //isRoundOverIsGameOver(); //what should I do to end game quicker if hand is empty
        }
        return true;
    }

    public boolean checkIsSetIsRun(int player){
        String laidDownSuccessMsg = "Player " + (player+1) + " laid down,";
        switch (gameRoundOrder){
            case SETS2:
                if(isSet(player,true)){
                    addToLaidCard(player, players[player].laidStacks.get(0).get(0)); //removes the rest of the cards in that set
                    if(isSet(player,true)) {
                        addToLaidCard(player, players[player].laidStacks.get(1).get(0));
                        System.out.println(laidDownSuccessMsg + " 2 Sets!"); //if u can lay down cards, removing from hand, adding to laidcards
                        return true;
                    }else{
                       returnLaidToHand(player);
                    }
                }else{
                    returnLaidToHand(player); //if cards have been laid, conditions are not correct, return to main hand
                }
                break;
            case SET1RUN1:
                if(isSet(player,true)){
                    addToLaidCard(player, players[player].laidStacks.get(0).get(0));
                    if(isRun(player,true)) {
                        addToLaidCard(player, players[player].laidStacks.get(1).get(0));
                        System.out.println(laidDownSuccessMsg + " 1 Set, 1 Run!");
                        return true;
                    }else{
                        returnLaidToHand(player);
                    }
                }else{
                    returnLaidToHand(player);
                }
                break;
            case RUNS2:
                if(isRun(player,true)){
                    addToLaidCard(player, players[player].laidStacks.get(0).get(0));
                    if(isRun(player,true)) {
                        addToLaidCard(player, players[player].laidStacks.get(1).get(0));
                        System.out.println(laidDownSuccessMsg + " 2 Runs!");
                        return true;
                    }else{
                        returnLaidToHand(player);
                    }
                }else{
                    returnLaidToHand(player);
                }
                break;
            case SETS3:
                if(isSet(player,true)){
                    addToLaidCard(player, players[player].laidStacks.get(0).get(0));
                    if(isSet(player,true)){
                        addToLaidCard(player, players[player].laidStacks.get(1).get(0));
                        if(isSet(player,true)){
                            addToLaidCard(player, players[player].laidStacks.get(2).get(0));
                            System.out.println(laidDownSuccessMsg + " 3 Sets!");
                            return true;
                        }else{
                            returnLaidToHand(player);
                        }
                    }else{
                        returnLaidToHand(player);
                    }
                }else{
                    returnLaidToHand(player);
                }
                break;
            case SET2RUN1:
                if(isSet(player,true)){
                    addToLaidCard(player, players[player].laidStacks.get(0).get(0));
                    if(isSet(player,true)) {
                        addToLaidCard(player, players[player].laidStacks.get(1).get(0));
                        if(isRun(player,true)) {
                            addToLaidCard(player, players[player].laidStacks.get(2).get(0));
                            System.out.println(laidDownSuccessMsg + " 2 Sets, 1 Run!");
                            return true;
                        }else{
                            returnLaidToHand(player);
                        }
                    }else{
                        returnLaidToHand(player);
                    }
                }else{
                    returnLaidToHand(player);
                }
                break;
            case SET1RUN2:
                if(isSet(player,true)){
                    addToLaidCard(player, players[player].laidStacks.get(0).get(0));
                    if(isRun(player,true)) {
                        addToLaidCard(player, players[player].laidStacks.get(1).get(0));
                        if (isRun(player, true)) {
                            addToLaidCard(player, players[player].laidStacks.get(2).get(0));
                            System.out.println(laidDownSuccessMsg + " 1 Set, 2 Runs!");
                            return true;
                        }else{
                            returnLaidToHand(player);
                        }
                    }else{
                        returnLaidToHand(player);
                    }
                }else{
                    returnLaidToHand(player);
                }
                break;
            case RUN3:
                if(isRun(player,true)){
                    addToLaidCard(player, players[player].laidStacks.get(0).get(0));
                    if(isRun(player,true)){
                        addToLaidCard(player, players[player].laidStacks.get(1).get(0));
                        if(isRun(player,true)){
                            addToLaidCard(player, players[player].laidStacks.get(2).get(0));
                            System.out.println(laidDownSuccessMsg + " 3 Runs!");
                            return true;
                        }else{
                            returnLaidToHand(player);
                        }
                    }else{
                        returnLaidToHand(player);
                    }
                }else{
                    returnLaidToHand(player);
                }
                break;
            default:
                throw new ArithmeticException("Player Turn- Order Out of Bound");
        }
        return false;
    }

    /**
     * before player turn, ask each player (not including actingPlayer) if they want the top card
     * if no one buys return false, if anyone buys return true
     */
    public boolean buyCard(Scanner scan, int thisPlayer){
        Cards newCard = removeCardFromDrawPile(); //grabs a new card
        System.out.println(readEntireHand(thisPlayer));
        String mcPrompt = "Do you want this card [" + newCard.readCard() + "]?\nYes = 1, No = 2";
        int mcChoice = readPromptUserInput(scan,mcPrompt,2);
        if(mcChoice == 0){
            topCard = newCard;
            return false;
        }else if(mcChoice == 1){
            ; //continues to next round
        }else{
            throw new ArithmeticException("Buy Round- Choice Out of Bound (MC)");
        }

        for(int i = 0; i < players.length; i++){
            //if the buyer is the mc, or if the others have anything in laid (mc = thisplayer)
            if(i == thisPlayer || players[i].laidStacks.get(0).size() > 0){
                continue;
            }
            System.out.println(readEntireHand(i));
            String prompt = "Player " + (i+1) + " Do you want this card [" + newCard.readCard() + "]?\nYes = 1, No = 2";
            int buyChoice = readPromptUserInput(scan,prompt,2); //in index form
            if(buyChoice == 0){
                addCardToHand(i, newCard);
                System.out.print("\nPlayer " + (i+1) + " drew 2 cards: [" + newCard.readCard() + " & " + addCardFromDrawPile(i).readCard() + "]");
                return true;
            }else if(buyChoice == 1){
                ; //continues to next round
            }else{
                throw new ArithmeticException("Buy Round- Choice Out of Bound");
            }
            System.out.print("\n");
        }
        topCard = newCard; //if no one buys card
        return false;
    }

    /**
     * after laying down cards, if players can they will add cards to someone else's laid card pile
     * if no one has laid returns false, if anyone has returns true
     */
    public void addToLaidCard(int thisPlayer){
        String playerIntro = "Player " + (thisPlayer+1) + " ";
        int c = 0;
        ArrayList<Cards> tempDiscard = new ArrayList<>();
        //loops through other all players laid cards, thisplayer = mc, j = others
        for(int i = 0; i < players.length; i++) {
            String msgLaidConfirmation = ""; //confirmation msg
            //allow if the index is the mc, dont allow if the others havent laid cards
            //if the other player has a laid cards, check if mc can add card to other's laid pile
            for(int o = 0; o < players[thisPlayer].cardsInHand.size(); o++){

                Cards playersCurrentCard = players[thisPlayer].cardsInHand.get(o);
                msgLaidConfirmation = playerIntro +
                        " dropped this card: [" + playersCurrentCard.readCard() + "] to Player " +
                        (i+1) + "'s Laid Stack";
                for(int j = 0; j < players[i].laidStacks.size(); j++) {
                    if(players[i].laidStacks.get(j).size() <= 0){
                        continue;
                    }
                    switch (gameRoundOrder) {
                        case SETS2:
                        case SETS3:
                            //if card in players hand is eqaual to the first card in the other players set
                            if (players[i].laidStacks.get(j).get(0).checkSet(playersCurrentCard)){
                                tempDiscard.add(players[thisPlayer].getCard(o));
                                players[i].addCardToLaid(j, players[thisPlayer].getCard(o));
                                System.out.println(msgLaidConfirmation);
                                c++;
                            }
                            break;
                        case SET1RUN1:
                        case SET2RUN1:
                        case SET1RUN2:
                            if (players[i].laidStacks.get(j).get(0).checkSet(playersCurrentCard)
                            || players[i].laidStacks.get(j).get(players[i].laidStacks.get(j).size()-1).checkRun(playersCurrentCard)){
                                tempDiscard.add(players[thisPlayer].getCard(o));
                                players[i].addCardToLaid(j, players[thisPlayer].getCard(o));
                                System.out.println(msgLaidConfirmation);
                                c++;
                            }
                            break;
                        case RUNS2:
                        case RUN3:
                            if (players[i].laidStacks.get(j).get(players[i].laidStacks.get(j).size()-1).checkRun(playersCurrentCard)){
                                tempDiscard.add(players[thisPlayer].getCard(o));
                                players[i].addCardToLaid(j, players[thisPlayer].getCard(o));
                                System.out.println(msgLaidConfirmation);
                                c++;
                            }
                            break;
                        default:
                            throw new ArithmeticException("Adding to Laid Cards- Order Out of Bound");
                    }
                }
            }
        }
            for(int i = 0; i < tempDiscard.size(); i++){
                players[thisPlayer].removeCard(tempDiscard.get(i));
            }
        if(c <= 0){
            System.out.println(playerIntro + "did not lay down any cards into an existing stacks");
        }else{
            ;
        }
    }

    /**
     * to lay down specific cards in the player's own stacks
     * additional laid card method for isSet/isRun,
     */
    public void addToLaidCard(int player, Cards card){   //note
       ArrayList<Cards> tempDiscard = new ArrayList<>();
           for(int o = 0; o < players[player].cardsInHand.size(); o++){
               Cards playersCurrentCard = players[player].cardsInHand.get(o);
               for(int j = 0; j < players[player].laidStacks.size(); j++) {
                   if(players[player].laidStacks.get(j).size() <= 0){
                       continue;
                   }
                   switch (gameRoundOrder) {
                       case SETS2:
                       case SETS3:
                           //if card in players hand is eqaual to the first card in the other players set
                           if (players[player].laidStacks.get(j).get(0).checkSet(playersCurrentCard)){
                               tempDiscard.add(players[player].getCard(o));
                               players[player].addCardToLaid(j, players[player].getCard(o));
                           }
                           break;
                       case SET1RUN1:
                       case SET2RUN1:
                       case SET1RUN2:
                           if (players[player].laidStacks.get(j).get(0).checkSet(playersCurrentCard)
                           || players[player].laidStacks.get(j).get(players[player].laidStacks.get(j).size()-1).checkRun(playersCurrentCard)){
                               tempDiscard.add(players[player].getCard(o));
                               players[player].addCardToLaid(j, players[player].getCard(o));
                           }
                           break;
                       case RUNS2:
                       case RUN3:
                           if (players[player].laidStacks.get(j).get(players[player].laidStacks.get(j).size()-1).checkRun(playersCurrentCard)){
                               tempDiscard.add(players[player].getCard(o));
                               players[player].addCardToLaid(j, players[player].getCard(o));
                           }
                           break;
                       default:
                           throw new ArithmeticException("Adding to Laid Cards- Order Out of Bound");
                   }
               }
           }

       for(int i = 0; i < tempDiscard.size(); i++){
           players[player].removeCard(tempDiscard.get(i));
       }
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
                throw new ArithmeticException("Round Counter- Order Out of Bound");
        }
    }

    /**
     * check if the round has ended, then if the game is over
     */
    public boolean isRoundOverIsGameOver(){
        boolean isValid = false;
        int winningPlayer = 0;
        //check if anyone has won
        try {
            //check each player's laid cards to see if they have laid down all the cards they need to for the round
            for (int i = 0; i < players.length; i++) {
                    String playerIntro = "Player " + (i+1);
                    String laidMsg = playerIntro + " has laid down " + gameRoundOrder;
                    String cardsLeftMsg = playerIntro + " has this many cards: " + players[i].cardsInHand.size();
                    switch (gameRoundOrder) {
                        case SETS2:
                            //MIGHT need to improve default set to make sure it isnt double checking cards in laid
                            if (isSet(i) && isSet(i)) { //sets if 3 cards have the same values, if so add to laidCards[]
                                winningPlayer = i;
                                System.out.println(laidMsg);
                                if(players[i].cardsInHand.size() <= 0) { //lays down cards, then check if no cards in hand
                                    isValid = true;
                                }else{
                                    System.out.println(cardsLeftMsg);
                                }
                            } else {
                                returnLaidToHand(i);
                            }
                            break;
                        case SET1RUN1:
                            if (isSet(i) && isRun(i)) { //runs if 4 cards are in a running value
                                winningPlayer = i;
                                System.out.println(laidMsg);
                                if(players[i].cardsInHand.size() <= 0) {
                                    isValid = true;
                                }else{
                                    System.out.println(cardsLeftMsg);
                                }
                            } else {
                                returnLaidToHand(i);
                            }
                            break;
                        case RUNS2:
                            if (isRun(i) && isRun(i)) {
                                winningPlayer = i;
                                System.out.println(laidMsg);
                                if(players[i].cardsInHand.size() <= 0) {
                                    isValid = true;
                                }else{
                                    System.out.println(cardsLeftMsg);
                                }
                            } else {
                                returnLaidToHand(i);
                            }
                            break;
                        case SETS3:
                            if (isSet(i) && isSet(i) && isSet(i)) {
                                winningPlayer = i;
                                System.out.println(laidMsg);
                                if(players[i].cardsInHand.size() <= 0) {
                                    isValid = true;
                                }else{
                                    System.out.println(cardsLeftMsg);
                                }
                            } else {
                                returnLaidToHand(i);
                            }
                            break;
                        case SET2RUN1:
                            if (isSet(i) && isSet(i) && isRun(i)) {
                                winningPlayer = i;
                                System.out.println(laidMsg);
                                if(players[i].cardsInHand.size() <= 0) {
                                    isValid = true;
                                }else{
                                    System.out.println(cardsLeftMsg);
                                }
                            } else {
                                returnLaidToHand(i);
                            }
                            break;
                        case SET1RUN2:
                            if (isSet(i) && isRun(i) && isRun(i)) {
                                winningPlayer = i;
                                System.out.println(laidMsg);
                                if(players[i].cardsInHand.size() <= 0) {
                                    isValid = true;
                                }else{
                                    System.out.println(cardsLeftMsg);
                                }
                            } else {
                                returnLaidToHand(i);
                            }
                            break;
                        case RUN3:
                            if (isRun(i) && isRun(i) && isRun(i)) {
                                winningPlayer = i;
                                System.out.println(laidMsg);
                                if(players[i].cardsInHand.size() <= 0) {
                                    isValid = true;
                                }else{
                                    System.out.println(cardsLeftMsg);
                                }
                            } else {
                                returnLaidToHand(i);
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
            System.out.println("Player " + (winningPlayer+1) + " won this round: " + gameRoundOrder);
            pointTracker(); //gets the total points of all the remaining players cards
            for(int i = 0; i < players.length; i++){
                discardEntireHand(i); //discards all cards from ALL players
                discardLaidCards(i); //discard all laid cards
            }
            startingHand(numStartingHand); //adds new cards to the player's hand which equal the orginal starting hand size
            roundCounter(); //moves onto next round
            playerTurnOrder = 1; //(winningPlayer+1)
            isValid = false;
            if(roundCounter >=  rounds){
                System.out.println("GAME OVER");
                isValid = true; //if the entire game is over
            }
        }
        return isValid;
    }

    /**
     * Display who won
     */
    public String displayWinner(){
        //List of winners
        ArrayList<PlayersHand> winners = new ArrayList<PlayersHand>();
        //Clone the player array to a temporary list
        ArrayList<PlayersHand> tempPlayer = new ArrayList<PlayersHand>();
        for(int i = 0; i < players.length; i++){
            tempPlayer.add(players[i]);
        }
        //loops through until winners is full,
        for(int i = 0; i < players.length; i++) { //goes for player length since winner.length = player.length
            int lowest = tempPlayer.get(0).getRoundPoints();
            int lowestIndex = 0;
            for (int o = 0; o < tempPlayer.size(); o++) {
                if (tempPlayer.get(o).getRoundPoints() < lowest) {
                    lowest = tempPlayer.get(o).getRoundPoints();
                    lowestIndex = o;
                }
            }
            winners.add(tempPlayer.remove(lowestIndex)); //removes a tempPlayer from the list, adding it to winners
        }
        //final output string builder
        String total = "";
        total += "The Winner Is:";
        total += " Player " + winners.get(0).c + "!\n\n";
        total += "The Rankings Are...\n";
        for(int i = 0; i < winners.size(); i++){
            total += "Rank #" + (i+1) + " Player " + winners.get(i).c +
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
     * adds cards to deck in normal deck order
     * in order of Spades, Hearts, Diamonds, Clubs; then 1, 2, 3, 4, etc.
     */
    public void addingStartingCardToDeck(int numberOfCards){
        for(int j = 0; j < (int)Math.ceil(numberOfCards/DECK_SIZE); j++) { //if cards are over 52, loops over
            for (int i = 0; i < SUITES; i++) { //divides value of cards between its suites
                for (int o = 0; o < RANKS; o++) { //number of suites
                    if(deck.size() >= numberOfCards){ //breaks out once the deck is FULL
                        break;
                    }
                    deck.add(new Cards(o, i));
                }
            }
        }
    }

    /**
     * check whether a set has been made,
     */
    //COPY-PASTE-WARNING: isSet-2/isRun-2 are both WIP
    private boolean isSet(int player){
        //don't need to sort laid stacks
        for(int i = 0; i < players[player].laidStacks.size(); i++) {
            for (int o = 0; o < players[player].laidStacks.get(i).size() - 2; o++) {
                if(players[player].laidStacks.get(i).get(o).checkSet(players[player].laidStacks.get(i).get(o+1)) &&
                players[player].laidStacks.get(i).get(o+1).checkSet(players[player].laidStacks.get(i).get(o+2)) &&
                players[player].laidStacks.get(i).get(o).checkSet(players[player].laidStacks.get(i).get(o+2))){
                    players[player].laidStacks.get(i).get(o);
                    players[player].laidStacks.get(i).get(o + 1);
                    players[player].laidStacks.get(i).get(o + 2);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param isPlayerTurn for a standard turn
     */
    //MIGHT, implement the ability to choose which "type" of stack to drop using
    private boolean isSet(int player, boolean isPlayerTurn){ //shit code will take time to make better
        players[player].sortForSets(); //sorts playerhand to make checking for sets easier
        for(int i = 0; i < players[player].cardsInHand.size()-2; i++){
            if(players[player].cardsInHand.get(i).checkSet(players[player].cardsInHand.get(i+1)) &&
            players[player].cardsInHand.get(i+1).checkSet(players[player].cardsInHand.get(i+2)) &&
            players[player].cardsInHand.get(i+2).checkSet(players[player].cardsInHand.get(i))){
                //pain gate, finds a set then lays them down depending on the gameRoundOrder
                Cards tempCard = players[player].cardsInHand.get(i);
                Cards tempCard2 = players[player].cardsInHand.get(i+1);
                Cards tempCard3 = players[player].cardsInHand.get(i+2);
                switch (gameRoundOrder){
                    case SETS2:
                    case SET1RUN1:
                        //if laidStack is empty, fill it up, if not fill up other
                        //MIGHT, need to change so if all a stacks are full it does nothing
                        if(players[player].laidStacks.get(0).size() <= 0) {
                            players[player].layDownCard(0,tempCard);
                            players[player].layDownCard(0,tempCard2);
                            players[player].layDownCard(0,tempCard3);
                        }else{
                            if(players[player].laidStacks.get(0).get(0).getValue() != tempCard.getValue()) {
                                //if tempCard value is not unique to LaidStack 1's value, then return false
                                players[player].layDownCard(1, tempCard);
                                players[player].layDownCard(1, tempCard2);
                                players[player].layDownCard(1, tempCard3);
                            }else{
                                return false;
                            }
                        }
                        break;
                    case SETS3:
                    case SET2RUN1:
                    case SET1RUN2:
                        if(players[player].laidStacks.get(0).size() <= 0){
                            players[player].layDownCard(0,tempCard);
                            players[player].layDownCard(0,tempCard2);
                            players[player].layDownCard(0,tempCard3);
                        }else if(players[player].laidStacks.get(1).size() <= 0){
                            if(players[player].laidStacks.get(0).get(0).getValue() != tempCard.getValue()) {
                                players[player].layDownCard(1, tempCard);
                                players[player].layDownCard(1, tempCard2);
                                players[player].layDownCard(1, tempCard3);
                            }else{
                                return false;
                            }
                        }else{
                            if(players[player].laidStacks.get(1).get(0).getValue() != tempCard.getValue()) {
                                players[player].layDownCard(2, tempCard);
                                players[player].layDownCard(2, tempCard2);
                                players[player].layDownCard(2, tempCard3);
                            }else{
                                return false;
                            }
                        }
                        break;
                    case RUNS2:
                    case RUN3:
                        //do nothing
                        break;
                    default:
                        throw new ArithmeticException("Is Set- Order Out of Bound");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * check whether a set has been made,
     */
    private boolean isRun(int player){
        //don't need to sort laid stacks
        for(int i = 0; i < players[player].laidStacks.size(); i++){
            for(int o = 0; o < players[player].laidStacks.get(i).size()-3; o++){
                //check if value is incremented value, and, if the suit is equal
                if(players[player].laidStacks.get(i).get(o).checkRun(players[player].laidStacks.get(i).get(o+1)) &&
                        players[player].laidStacks.get(i).get(o+1).checkRun(players[player].laidStacks.get(i).get(o+2)) &&
                        players[player].laidStacks.get(i).get(o+2).checkRun(players[player].laidStacks.get(i).get(o+3))){
                    players[player].laidStacks.get(i).get(o);
                    players[player].laidStacks.get(i).get(o+1);
                    players[player].laidStacks.get(i).get(o+2);
                    players[player].laidStacks.get(i).get(o+3);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @isPlayerTurn for a standard turn
     */
    private boolean isRun(int player, boolean isPlayerTurn){
        players[player].sortForRuns(); //sorts playerhand to make checking for runs easier
        for(int i = 0; i < players[player].cardsInHand.size()-3; i++) {
            if(players[player].cardsInHand.get(i).checkRun(players[player].cardsInHand.get(i+1)) &&
                    players[player].cardsInHand.get(i+1).checkRun(players[player].cardsInHand.get(i+2)) &&
                    players[player].cardsInHand.get(i+2).checkRun(players[player].cardsInHand.get(i+3))){
                //pain gate, finds a run then lays them down depending on the gameRoundOrder
                Cards tempCard = players[player].cardsInHand.get(i);
                Cards tempCard2 = players[player].cardsInHand.get(i+1);
                Cards tempCard3 = players[player].cardsInHand.get(i+2);
                Cards tempCard4 = players[player].cardsInHand.get(i+3);
                switch (gameRoundOrder){
                    case SETS2:
                    case SETS3:
                        //do nothing
                        break;
                    case SET1RUN1:
                    case RUNS2:
                        //if laidStack is empty, fill it up, if not fill up other
                        if(players[player].laidStacks.get(0).size() <= 0) {
                            players[player].layDownCard(0,tempCard);
                            players[player].layDownCard(0,tempCard2);
                            players[player].layDownCard(0,tempCard3);
                            players[player].layDownCard(0,tempCard4);
                        }else{
                            if(players[player].laidStacks.get(0).get(0).getSuit() != tempCard.getSuit()) {
                                players[player].layDownCard(1, tempCard);
                                players[player].layDownCard(1, tempCard2);
                                players[player].layDownCard(1, tempCard3);
                                players[player].layDownCard(1, tempCard4);
                            }else{
                                return false;
                            }
                        }
                        break;
                    case SET2RUN1:
                    case SET1RUN2:
                    case RUN3:
                        if(players[player].laidStacks.get(0).size() <= 0) {
                            players[player].layDownCard(0,tempCard);
                            players[player].layDownCard(0,tempCard2);
                            players[player].layDownCard(0,tempCard3);
                            players[player].layDownCard(0,tempCard4);
                        }else if(players[player].laidStacks.get(1).size() <= 0){
                            if(players[player].laidStacks.get(0).get(0).getSuit() != tempCard.getSuit()) {
                                players[player].layDownCard(1, tempCard);
                                players[player].layDownCard(1, tempCard2);
                                players[player].layDownCard(1, tempCard3);
                                players[player].layDownCard(1, tempCard4);
                            }else{
                                return false;
                            }
                        }else{
                            if(players[player].laidStacks.get(1).get(0).getSuit() != tempCard.getSuit()) {
                                players[player].layDownCard(2, tempCard);
                                players[player].layDownCard(2, tempCard2);
                                players[player].layDownCard(2, tempCard3);
                                players[player].layDownCard(2, tempCard4);
                            }else{
                                return false;
                            }
                        }
                        break;
                    default:
                        throw new ArithmeticException("Is Run- Order Out of Bound");
                }
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
                        players[i].roundPoints += 25; // 1 [Ace]
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        players[i].roundPoints += 5; // 2-10
                        break;
                    case 11:
                    case 12:
                    case 13:
                        players[i].roundPoints += 10; //11-13 [Ten, Jack, Queen, King]
                        break;
                    default:
                        throw new ArithmeticException("Point Mistake- Card Value Out of Bound");
                }
            }
        }
    }

    //TESTING
    //public static void main(String[] args){}

}