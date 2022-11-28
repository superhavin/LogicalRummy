public class Cards{
    final static String[] RANKS = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    final static String[] SUITS = {"Spades", "Hearts", "Diamonds", "Clubs"};
    public int value = 0;
    public String suit = "";
    public String rank = "";

    /**
     * @param value ranges from 0 to 12, represents the actual value of the card
     * @param suit ranges from 0 to 3, represents what suit its gonna be
     */
    public Cards(int value, int suit){
        try{
            this.value = value;
            this.rank = RANKS[value];
            this.suit = SUITS[suit];
        }catch(Exception e){
            throw new ArithmeticException("input index for values or suit is incorrect");
        }
    }
    public String getSuit(){
        return suit;
    }
    private void setSuit(int suit){
        this.suit = SUITS[suit];
        this.rank = RANKS[suit];
    }
    public int getValue(){
        return value+1;
    }
    private void setValue(int value){
        this.value = value;
    }
    public String getRanks() {
        return rank;
    }
    public String toString(){
        return "\nCard: " + getValue() + "\n Suit: " + getSuit() + "\n Rank: " + getRanks();
    }

}
