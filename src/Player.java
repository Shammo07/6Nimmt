import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    /**
     * The cards held on a player hand
     */
    private Card[] hand;
    /**
     * The number of card held by the player. This variable should be maintained
     * to match array hand.
     */
    private int handCount;
    /**
     * A dynamic array that holds the score pile.
     */
    private Card[] pile;
    /**
     * The name of the player
     */
    private String name;
    /**
     * A static variable that tells how many player has been initialized
     */
    private static int count = 1;

    /**
     * Other constructor that specify the name of the player.
     * 
     * You need to initialize your data member properly.
     */
    public Player(String name) {
        this.name = name;
        hand = new Card[10];
        pile = new Card[0];
        handCount = 0;
    }

    /**
     * Default constructor that set the name of the player as "Computer #1",
     * "Computer #2", "Computer #3"...
     * The number grows when there are more computer players being created.
     * 
     * You need to initialize your data member properly.
     */
    public Player() {
        this("Computer #" + count);
        count++;
    }

    /**
     * Getter of name
     * 
     * @return - the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * This method is called when a player is required to take the card from a stack
     * to his score pile. The variable pile should be treated as a dynamic array so
     * that the array will auto-resize to hold enough number of cards. The length of
     * pile should properly record the total number of cards taken by a player.
     * 
     * Important: at the end of this method, you should also help removing all cards
     * from the parameter array "cards".
     * 
     * 
     * 
     * @param cards - an array of cards taken from a stack
     * @param count - number of cards taken from the stack
     */
    public void moveToPile(Card[] cards, int count) {
        Card[] newPile = new Card[pile.length + count];
        for (int i = 0; i < pile.length; i++)  // copying all cards from old pile to new pile
            newPile[i] = pile[i];
        for (int i = 0; i < count; i++) // adding new cards to the pile
            newPile[pile.length + i] =  cards[i];

        pile = newPile;

        for (int i = 0; i < cards.length; i++) // removing all cards from the parameter array
            cards[i] = null;
    }

    /**
     * This method prompts a human player to play a card. It first print
     * all cards on his hand. Then the player will need to select a card
     * to play by entering the INDEX of the card.
     * 
     * @return - the card to play
     */
    public Card playCard() {
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < hand.length; i++)
            System.out.println(i + ": " + hand[i]); // Print the hand
        int index = -1; // Initialize the variable
        do {
            System.out.println(getName() + " please, select a card to play");
            index = in.nextInt();
        } while (index < 0 || index >= hand.length); // Re-do the loop if index is invalid
        System.out.println(name + ":" + hand[index]);
        Card cardToReturn = hand[index];

        //Reset the size of the array hand
        Card[] newHand = new Card[hand.length - 1];
        for (int x = 0; x < index; x++)
            newHand[x] = hand[x];
        for (int x = index; x < newHand.length; x++)
            newHand[x] = hand[x+1];
        hand = newHand;

        return cardToReturn;
    }

    /**
     * This method let a computer player to play a card randomly. The computer
     * player will pick any available card to play in a random fashion.
     * 
     * @return - card to play
     */
    public Card playCardRandomly() {
        int index = ThreadLocalRandom.current().nextInt(0, hand.length);
        System.out.println(name + ":" + hand[index]);
        Card cardToReturn = hand[index];
        //Reset the size of the array hand.
        Card[] newHand = new Card[hand.length - 1];
        for (int x = 0; x < index; x++)
            newHand[x] = hand[x];
        for (int x = index; x < newHand.length; x++)
            newHand[x] = hand[x+1];
        hand = newHand;
        return cardToReturn;
    }

    /**
     * Deal a card to a player. This should add a card to the variable hand and
     * update the variable handCount. During this method, you do not need to resize
     * the array. You can assume that a player will be dealt with at most 10 cards.
     * That is, the method will only be called 10 times on a player.
     * 
     * After each call of this method, the hand should be sorted properly according
     * to the number of the card.
     * 
     * @param card - a card to be dealt
     */
    public void dealCard(Card card) {
        hand[handCount] = card;
        handCount++;

        for (int i = 0; i < handCount; i++)
            for (int j = i + 1; j < handCount; j++) //Arrange the cards in ascending order
                if (hand[i].getNumber() > hand[j].getNumber()) {
                    Card newCard = hand[i];
                    hand[i] = hand[j];
                    hand[j] = newCard;
                }
    }

    /**
     * Get the score of the player by counting the total number of Bull Head in the
     * score pile.
     * 
     * @return - score, 0 or a positive integer
     */
    public int getScore() {
        int score = 0;
        for (int i = 0; i < pile.length; i++)
            score += pile[i].getBullHead();

        return score;
    }

    /**
     * To print the score pile. This method has completed for you.
     * 
     * You don't need to modify it and you should not modify it.
     */
    public void printPile() {
        for (Card c : pile) {
            c.print();
        }
        System.out.println();
    }

    /**
     * This is a getter of hand's card. This method has been completed for you
     *
     * You don't need to modify it and you should not modify it.
     * 
     * @param index - the index of card to take
     * @return - the card from the hand or null
     */
    public Card getHandCard(int index) {
        if (index < 0 || index >= handCount)
            return null;
        return hand[index];
    }
}
