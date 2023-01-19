public class JokerCards extends Cards{

    public static int c = 0;
    String ordinal;

    /**
     * constructs a generic Ace of Spades, then changes the values into a Joker of Wilds
     * jokers will be added to the deck DIFFERENTLY than other cards
     */
    public JokerCards() {
        super(0,0);
        c++;
        ordinal = ordinalNum(c);
        value = -1;
        suit = "Wilds";
        rank = "Joker";
        suitIndex = 4; //out ranges the other indexes
    }


    @Override
    public String readCard() {
        return "The " + ordinal + " Joker"; //"Well, Im the Joker, Baby!"
    }

    /**
     * @param num
     * @return ordinal form of num
     */
    private static String ordinalNum(int num){
        if(num % 10 == 1 && num != 11){
            return(num + "st");
        }else if(num % 10 == 2 && num != 12){
            return(num + "nd");
        }else if(num % 10 == 3 && num != 13){
            return(num + "rd");
        }else{
            return(num + "th");
        }
    }

    /**
     * the joker value needs to variable, it can be ANY card its used in
     * for example, "Joker", Ace of Hearts, Ace of Spades works, cuz the joker's value can be anything
     * ISSUE, how can we make a card whos value can non-binary and work in both set/runs
     */
    @Override
    public int getValue() {
        return super.getValue(); //FIGURE OUT LATER
    }


}
