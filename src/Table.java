import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
public class Table {
    /**
     * Total number of player. Use this variable whenever possible
     */
    private static final int NUM_OF_PLAYERS = 4;
    /**
     * Total number of cards used in this game. Use this variable whenever possible
     */
    private static final int TOTAL_NUMBER_OF_CARD = 104;
    /**
     * The four stacks of cards on the table.
     */
    private Card[][] stacks = new Card[4][6];
    /**
     * This number of cards of each stack on the table. For example, if the variable
     * stacks stores
     * -------------------------
     * | 0 | 10 13 14 -- -- -- |
     * | 1 | 12 45 -- -- -- -- |
     * | 2 | 51 55 67 77 88 90 |
     * | 3 | 42 -- -- -- -- -- |
     * -------------------------
     *
     * stacksCount should be {3, 2, 6, 1}.
     *
     * You are responsible to maintain the data consistency.
     */
    private int[] stacksCount = new int[4];
    /**
     * The array of players
     */
    private Player[] players = new Player[NUM_OF_PLAYERS];

    /**
     * Default constructor
     *
     * In the constructor, you should perform the following tasks:
     *
     * 1. Initialize cards for play. You should construct enough number of cards
     * to play. These cards should be unique (i.e., no two cards share the same
     * number). The value of card must be between 1 to 104. The number of bullHead
     * printed on each card can be referred to the rule.
     *
     * 2. Initialize four player. The first player should be a human player, call
     * "Kevin". The other player should be a computer player. These computer player
     * should have the name "Computer #1", "Computer #2", "Computer #3".
     *
     * 3. Deal randomly 10 cards to each player. A card can only be dealt to one
     * player. That is, no two players can have the same card.
     *
     * 4. Deal a card on each stack. The card dealt on the stack should not be dealt
     * to any player. Card dealt on each stack should also be unique (no two stack
     * have the same card).
     *
     */
    public Table() {
        Card[] allCards = new Card[TOTAL_NUMBER_OF_CARD]; // create an array to store the cards
        for (int i = 0; i < TOTAL_NUMBER_OF_CARD; i++)
            allCards[i] = new Card(i+1);

        //Initialize the four players
        players[0] = new Player("Kevin");
        players[1] = new Player();
        players[2] = new Player();
        players[3] = new Player();


        for (int i = 0; i < 10; i++)
            for (int j = 0; j < NUM_OF_PLAYERS; j++){
                // Deal a random card to each player
                int index = ThreadLocalRandom.current().nextInt(0, allCards.length);
                players[j].dealCard(allCards[index]);

                //Remove that card from the allCards array
                Card[] cardsLeft = new Card[allCards.length - 1];
                for (int x = 0; x < index; x++)
                    cardsLeft[x] = allCards[x];
                for (int x = index; x < cardsLeft.length; x++)
                    cardsLeft[x] = allCards[x+1];

                allCards = cardsLeft;
            }

        for (int i = 0; i < stacksCount.length; i++) {
            // Deal a random card to each stack
            int index = ThreadLocalRandom.current().nextInt(0, allCards.length);
            stacks[i][0] = allCards[index];
            stacksCount[i]++;

            // Remove the card from the stack
            Card[] cardsLeft = new Card[allCards.length - 1];
            for (int x = 0; x < index; x++)
                cardsLeft[x] = allCards[x];
            for (int x = index; x < cardsLeft.length; x++)
                cardsLeft[x] = allCards[x+1];

            allCards = cardsLeft;
        }




    }

    /**
     * This method is to find the correct stack that a card should be added to
     * according to the rule. It should return the stack among which top-card of
     * that stack is the largest of those smaller than the card to be placed. (If
     * the rule sounds complicate to you, please refer to the game video.)
     *
     * In case the card to be place is smaller than the top cards of all stacks,
     * return -1.
     *
     * @param card - the card to be placed
     * @return the index of stack (0,1,2,3) that the card should be place or -1 if
     *         the card is smaller than all top cards
     */
    public int findStackToAdd(Card card) {
        int largerCards = 0; // No. of topmost cards in each stack larger than the card played
        int minDifference = 104; // Set a very high difference which cannot be achieved
        int stackToAdd = -1; // Initialize the variable which will give us the stack to add the card to
        for (int i = 0; i < stacksCount.length; i++)
            if (card.getNumber() < stacks[i][stacksCount[i]- 1].getNumber()) {
                largerCards++;
            }
            else {
                int difference = card.getNumber() - stacks[i][stacksCount[i]- 1].getNumber();
                if (difference < minDifference) {
                    minDifference = difference;
                    stackToAdd = i; // We choose the stack whose topmost card is closest to the card played
                }
            }

        if (largerCards == stacksCount.length) // If all the top cards are larger than the card played
            return -1;
        else return stackToAdd;
    }

    /**
     * To print the stacks on the table. Please refer to the demo program for the
     * format. Within each stack, the card should be printed in ascending order,
     * left to right. However, there is no requirement on the order of stack to
     * print.
     */
    public void print() {
        System.out.println("----------Table----------");
        for (int i = 0; i < stacksCount.length; i++) {
            System.out.print("Stack " + i + ":");
            for (int j = 0; j < stacksCount[i]; j++) {
                System.out.print(stacks[i][j]);
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    /**
     * This method is the main logic of the game. You should create a loop for 10
     * times (running 10 rounds). In each round all players will need to play a
     * card. These cards will be placed to the stacks from small to large according
     * to the rule of the game.
     *
     * In case a player plays a card smaller than all top cards, he will be
     * selecting one of the stack of cards and take them to his/her own score pile.
     * If the player is a human player, he will be promoted for selection. If the
     * player is a computer player, the computer player will select the "cheapest"
     * stack, i.e. the stack that has fewest bull heads. If there are more than
     * one stack having fewest bull heads, selecting any one of them.
     */
    public void runApp() {
        for (int turn = 0; turn < 10; turn++) {
            // print Table
            print();

            Card[] cardsPlayed = new Card[NUM_OF_PLAYERS]; // Array to hold the cards played by the players
            Card playersCard = players[0].playCard();
            cardsPlayed[0] = playersCard; // for the player Kevin
            for (int i = 1; i < NUM_OF_PLAYERS; i++)
                cardsPlayed[i] = players[i].playCardRandomly(); // for the computer players

            Player[] playersInOrder = new Player[players.length]; // To order the players based on the cards they played
            for (int x = 0; x < players.length; x++)
                playersInOrder[x] = players[x];

            // Arrange cards (and their corresponding players) in ascending order
            for (int i = 0; i < cardsPlayed.length; i++)
                for (int j = i + 1; j < cardsPlayed.length; j++)
                    if (cardsPlayed[i].getNumber() > cardsPlayed[j].getNumber()){
                        Card dummyCard = cardsPlayed[i];
                        Player dummyPlayer = playersInOrder[i];
                        cardsPlayed[i] = cardsPlayed[j];
                        playersInOrder[i] = playersInOrder[j];
                        cardsPlayed[j] = dummyCard;
                        playersInOrder[j] = dummyPlayer;
                    }

            for (int i = 0; i < cardsPlayed.length; i++) {
                System.out.println("Place the card " + cardsPlayed[i] + " for " + playersInOrder[i].getName());
                if (cardsPlayed[i].equals(playersCard)) { // If the player is playing;
                    int k = findStackToAdd(cardsPlayed[i]);
                    if (k != -1) { // If the card played is not smaller than all the final cards in the stacks
                        if (stacksCount[k] < stacks[k].length) { // If the stack is not full
                            stacks[k][stacksCount[k]] = cardsPlayed[i];
                            stacksCount[k]++;
                        }
                        if (stacksCount[k] == stacks[k].length){ // If the stack is full
                            playersInOrder[i].moveToPile(stacks[k], stacks[k].length - 1); // Move that stack to pile
                            stacks[k][0] = cardsPlayed[i]; // Put the card played as the first card in the stack
                            stacksCount[k] = 1;
                        }
                    } else {  // If the card played is smaller than all the final cards in the stacks
                        int stack = 0;
                        do {
                            Scanner in = new Scanner(System.in);
                            System.out.println("Pick a stack to collect the cards");
                            stack = in.nextInt();
                        } while (stack < 0 || stack > 3);
                        playersInOrder[i].moveToPile(stacks[stack], stacksCount[stack]);// Move that stack to pile
                        stacks[stack][0] = cardsPlayed[i]; // Put the card played as the first card in the stack
                        stacksCount[stack] = 1;
                    }


                } else { // If the computer is playing
                    int k = findStackToAdd(cardsPlayed[i]);
                    if (k != -1) { // If the card played is not smaller than all the final cards in the stack
                        if (stacksCount[k] < stacks[k].length) { // If the stack is not full
                            stacks[k][stacksCount[k]] = cardsPlayed[i];
                            stacksCount[k]++;
                        }
                        if (stacksCount[k] == stacks[k].length){ // If the stack is full
                            playersInOrder[i].moveToPile(stacks[k], stacks[k].length - 1); //Move that stack to pile
                            stacks[k][0] = cardsPlayed[i]; // Put the card played as the first card in the stack
                            stacksCount[k] = 1;
                        }
                    } else {  // If the card played is smaller than all the final cards in the stacks
                        int[] stackScore = new int[stacksCount.length];
                        for (int x = 0; x < stacksCount.length; x++){
                            for (int y = 0; y < stacksCount[x]; y++){
                                stackScore[x] += stacks[x][y].getBullHead(); // Getting the scores of the stacks
                            }
                        }
                        int minScore = stackScore[0];
                        int stackToChoose = 0;
                        for (int z = 1; z < stackScore.length; z++)
                            if (minScore > stackScore[z]) {
                                minScore = stackScore[z]; //Finding the cheapest stack
                                stackToChoose = z;
                            }
                        playersInOrder[i].moveToPile(stacks[stackToChoose], stacksCount[stackToChoose]); // Move that stack to pile
                        stacks[stackToChoose][0] = cardsPlayed[i];  // Put the card played as the first card in the stack
                        stacksCount[stackToChoose] = 1;
                    }
                }
            }


        }
        for (Player p : players) {
            System.out.println(p.getName() + " has a score of " + p.getScore());
            p.printPile();
        }
    }

    /**
     * Programme main. You should not change this.
     *
     * @param args - no use.
     */
    public static void main(String[] args) {
        new Table().runApp();
    }

}
