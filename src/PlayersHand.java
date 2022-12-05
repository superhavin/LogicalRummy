import java.util.ArrayList;

public class PlayersHand {
    //statics
    int c = 1; //to count which player it is
    //variables
    public ArrayList<Cards> cardsInHand = new ArrayList<Cards>();
    //cards player laid down
    public ArrayList<Cards> laidCards = new ArrayList<Cards>();
    public ArrayList<Cards> laidWinning = new ArrayList<Cards>();
    public int roundPoints = 0;

    public PlayersHand(){
        c++;
    }
    //
    public void addCard(Cards card){
        cardsInHand.add(card);
    }
    public Cards removeCard(int position){
        return cardsInHand.remove(position);
    }
    public Cards getCard(int position){
        return cardsInHand.get(position);
    }
    //cards on the board that are laid down
    public void layDownCard(int position){
        laidCards.add(cardsInHand.remove(position));
    }
    public void displayWin(Cards card){
        laidWinning.add(card);
    }

    public int getRoundPoints(){
        return roundPoints;
    }

    public void returnLaidToHand(){
        for(int i = 0; i < laidCards.size(); i++){
            cardsInHand.add(laidCards.remove(i));
        }
    }
    //read all lists
    /**
     * reads all the values from the players hand
     */
    public String readAllValues(){
        String total = "";
        total += "Player Hand:\n";
        for(int i = 0; i < cardsInHand.size(); i++){
            total += "Suit: " + cardsInHand.get(i).getSuit() + " Rank: "  + cardsInHand.get(i).getRanks() + "; \n";
        }
        return total;
    }
    /**
     * reads all the values that have been laid
     */
    public String readAllLaid(){
        String total = "";
        total += "Laid Cards:\n";
        for(int i = 0; i < laidCards.size(); i++){
            total += "Suit: " + laidCards.get(i).getSuit() + " Rank: "  + laidCards.get(i).getRanks() + "; \n";
        }
        return total;
    }
    /**
     *
     */
    public String readWinnings(){
        String total = "";
        total += "Winning Cards:\n";
        for(int i = 0; i < laidWinning.size(); i++){
            total += " " + laidWinning.get(i).readCard() + "; \n";
        }
        return total;
    }
    /**
     * outputs all cards in the player's hand
     */
    public String toString(){
        String total = "";
        total += "Cards in hand:\n";
        for(int i = 0; i < cardsInHand.size(); i++){
            total += cardsInHand.get(i) + " ";
        }
        total += "Cards that have been laid:\n";
        for(int i = 0; i < laidCards.size(); i++){
            total += laidCards.get(i) + " ";
        }
        return total;
    }
}
