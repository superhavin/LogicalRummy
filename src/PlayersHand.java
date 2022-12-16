import java.util.ArrayList;

public class PlayersHand {

    //statics
    /**
     * counter of each playerhand for displaying the winner
     */
    private static int counter = 0;
    public int c; //to count which number player it is
    //variables
    public ArrayList<Cards> cardsInHand = new ArrayList<Cards>();
    /**
     * all the laid cards, sorted into different arrays depending on the round
     */
    public ArrayList<ArrayList<Cards>> laidStacks = new ArrayList<ArrayList<Cards>>(); //cards player laid down
    public int roundPoints = 0;
    private final int MAX_STACKS = 3;

    public PlayersHand(){
        counter++;
        c = counter;
        for(int i = 0; i < MAX_STACKS; i++){
            laidStacks.add(new ArrayList<Cards>()); //3 stacks of laid piles
        }
    }
    //basic mechanics

    public void addCard(Cards card){
        cardsInHand.add(card);
    }
    public Cards removeCard(int position){
        return cardsInHand.remove(position);
    }
    public Cards removeCard(Cards card){
        for(int i = 0; i < cardsInHand.size(); i++){
            if(cardsInHand.get(i).equals(card)){
                return cardsInHand.remove(i);
            }
        }
        return null;
    }
    public Cards getCard(int position){
        return cardsInHand.get(position);
    }

    /**
     * takes card from hand and lays it down on stack
     */
    public void layDownCard(int stack, Cards card){
        int index = 0;
        for(int i = 0; i < cardsInHand.size(); i++){
            if(cardsInHand.get(i).equals(card)){
                index = i;
                break;
            }
        }
        //System.out.println("cards laid down " + card.readCard());
        Cards temp = cardsInHand.remove(index);
        laidStacks.get(stack).add(temp);
    }
    public Cards removeCardFromLaid(int stack, int position){return laidStacks.get(stack).remove(position);}

    public void addCardToLaid(int stack, Cards card){
        laidStacks.get(stack).add(card);
    }

    public int getRoundPoints(){
        return roundPoints;
    }

    public void sortForSets(){
        Cards temp = null;
        for(int i = 0; i < cardsInHand.size(); i++){
            for(int o = i+1; o < cardsInHand.size(); o++){
                if(cardsInHand.get(i).getValue() > cardsInHand.get(o).getValue()){
                    //swaps places, if "i".value is greater, pushing "i" forward to where "o" is
                    temp = cardsInHand.get(i);
                    cardsInHand.set(i,cardsInHand.get(o));
                    cardsInHand.set(o,temp);
                }
            }
        }
    }
    public void sortForRuns(){ //duplicate cards causes ERROR, like 1H, 2H, 3H, 3H, 4H
        //find a way to sort duplicates so there out of the way of a good line up
        Cards temp = null;
        for(int i = 0; i < cardsInHand.size(); i++){
            for(int o = i+1; o < cardsInHand.size(); o++){
                //if index is bigger swap
                if(cardsInHand.get(i).suitIndex > cardsInHand.get(o).suitIndex){
                    temp = cardsInHand.get(i);
                    cardsInHand.set(i,cardsInHand.get(o));
                    cardsInHand.set(o,temp);
                    //continue;
                }
                //if suit are equal check value
                if(cardsInHand.get(i).suitIndex == cardsInHand.get(o).suitIndex){
                    if(cardsInHand.get(i).getValue() > cardsInHand.get(o).getValue()){
                        temp = cardsInHand.get(i);
                        cardsInHand.set(i,cardsInHand.get(o));
                        cardsInHand.set(o,temp);
                    }
                }
            }
        }
    }

    //read all lists, not needed, but there for testing
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
        for(int i = 0; i < laidStacks.size(); i++){
            total += "Stack #" + i + ": ";
            for(int o = 0; o < laidStacks.get(i).size(); o++) {
                total += " Suit: " + laidStacks.get(i).get(o).getSuit() + " Rank: " + laidStacks.get(i).get(o).getRanks() + "; \n";
            }
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
        for(int i = 0; i < laidStacks.size(); i++){
            total += "Stack #" + i + ": ";
            for(int o = 0; o < laidStacks.get(i).size(); o++){
                total += " " + laidStacks.get(i).get(o) + " ";
            }
        }
        return total;
    }
}
