import java.util.ArrayList;

public class PlayersHand {

    public static ArrayList<Cards> cardsInHand = new ArrayList<Cards>();

    public PlayersHand(){}

    public void addCards(Cards card){
        cardsInHand.add(card);
    }

    public Cards getCards(int position){
        return cardsInHand.get(position);
    }

    public String readAllValues(){
        String total = "";
        for(int i = 0; i < cardsInHand.size(); i++){
            total += "Suit: " + cardsInHand.get(i).getSuit() + " Rank: "  + cardsInHand.get(i).getRanks() + "; \n";
        }
        return total;
    }

    //reads all cards in the player's hand
    public String toString(){
        String total = "";
        for(int i = 0; i < cardsInHand.size(); i++){
            total += cardsInHand.get(i) + " ";
        }
        return total;
    }
}
