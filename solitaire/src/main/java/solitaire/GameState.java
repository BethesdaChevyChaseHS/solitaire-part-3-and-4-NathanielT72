package solitaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameState {
    private Stack<Card> deck; // Full deck of cards
    private Stack<Card>[] gamePiles; // Seven piles on the tableau
    private Stack<Card> visibleCards; // Stack for visible cards
    private Stack<Card> discardedCards; // Discard pile
    private Stack<Card>[] foundationPiles; // Four foundation piles

    @SuppressWarnings("unchecked")
    public GameState() {
        // Initialize the game state
        deck = new Stack<>();
        gamePiles = new Stack[7]; // Array of 7 stacks
        visibleCards = new Stack<>();
        discardedCards = new Stack<>();

        // Initialize each game pile
        for (int i = 0; i < gamePiles.length; i++) {
            gamePiles[i] = new Stack<>();
        }
        foundationPiles = new Stack[4];
        for (int i = 0; i < foundationPiles.length; i++) {
            foundationPiles[i] = new Stack<>();
        }

        initializeDeck();
        shuffleDeck();
        dealInitialCards();
    }

    //REPLACE THE FOLLOWING 4 functions with your code from part 2

    // Creates a full deck of cards with all combinations of suits and ranks
    private void initializeDeck() {
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                Card card = new Card(s,r);
                    deck.push(card);
            }
        }
    }

    // Shuffles the deck
    private void shuffleDeck() {
        java.util.Collections.shuffle(deck);
    }

    // Deals cards to the 7 game piles
    private void dealInitialCards() {
        for(int j = 6; j > -1; j--) {
            for(int i = j + 1; i > 0; i--) {
                if(i == 1) {
                    (gamePiles[j].push(deck.pop())).flip();
                }
                else {gamePiles[j].push(deck.pop());
                }
            }        
        }
    }

    // Draws up to three cards from the deck into visibleCards
    public void drawFromDeck() {
        for(int i = 0; i < 3; i++) {
            (visibleCards.push(deck.pop())).flip();
        }

    }

    public void discardCards() {
        for(int m = 0; m <= visibleCards.size() + 1; m++) {
            discardedCards.push(visibleCards.pop());
        }
    }

    // new methods from part 3

    
    public boolean canCardMove(Card card, int toPile){
        /*a card can be moved from the visible cards to a pile if 
            A) The card is the opposite color and its rank is ONE smaller than the card it will be placed on
            B) The pile is empty and the card is a King
        */
        //List<Rank> ranks = Arrays.asList(Rank.values());
        //System.out.println(card.getRank());
        if(gamePiles[toPile].size() == 0) {
            if(card.getRank() == Rank.KING) {
                return true;
            }
        }

else if((gamePiles[toPile].peek()).getRank().ordinal() - 1  == card.getRank().ordinal()) {

        if((gamePiles[toPile].peek()).getSuit() == Suit.HEARTS ||  gamePiles[toPile].peek().getSuit() == Suit.DIAMONDS) {
            if(card.getSuit() == Suit.SPADES || card.getSuit() == Suit.CLUBS) {
                return true;
            }
        }

        else if(gamePiles[toPile].peek().getSuit() == Suit.SPADES || gamePiles[toPile].peek().getSuit() == Suit.CLUBS) {
            if(card.getSuit() == Suit.HEARTS || card.getSuit() == Suit.DIAMONDS) {
                return true;
            }
        }
}

        
         return false;
    }
    // attempts to move top card from visible card stack to the toPileIndex
    // returns true if successful and false if unsuccessful
    public boolean moveCardFromVisibleCardsToPile(int toPileIndex) {
        /* 
            If a card can be moved, it should be popped from the visible cards pile and pushed to the pile it is added to
            hints: use peek() and ordinal() to determine whether or not a card can be moved. 
            USE the method you just made, canCardMove
        */
        if(canCardMove(visibleCards.peek(),toPileIndex)) {
            gamePiles[toPileIndex].push(visibleCards.pop());
            return true;
        }
        return false;
    }

    // Move a card from one pile to another
    public boolean moveCards(int fromPileIndex, int cardIndex, int toPileIndex) {
        Stack<Card> fromPile = gamePiles[fromPileIndex];

        // Create a sub-stack of cards to move
        ArrayList<Card> cardsToMove = new ArrayList<>(fromPile.subList(cardIndex, fromPile.size()));

        Card bottomCard = cardsToMove.get(0); // the bottom card to be moved

        // Check if bottomCard can be moved to the toPile
        // if we can move the cards, add cardsToMove to the toPile and remove them from the fromPile
        // Then, flip the next card in the fromPile stack
        
        if(canCardMove(bottomCard, toPileIndex)) {
            for(int i = 0; i < cardsToMove.size(); i++) {
            gamePiles[toPileIndex].push(cardsToMove.get(i));
            gamePiles[fromPileIndex].pop();
            if(!(gamePiles[toPileIndex].peek().isFaceUp())) {
                gamePiles[toPileIndex].peek().flip();
            }
                if(gamePiles[fromPileIndex].size() > 0 && (!(gamePiles[fromPileIndex].peek().isFaceUp()))) {
                    gamePiles[fromPileIndex].peek().flip();
                }
            //}
        }
        return true;
    }

        //return true if successful, false if unsuccessful

        return false;
    }
    private boolean canMoveToFoundation(Card card, int foundationIndex){
        //The foundation piles are the 4 piles that you have to build to win the game. 
        //In order for a card to be added to the pile, it needs to be one larger than the 
        //current top card of the foundation pile. It needs to be the same suit. 
        //If the foundation pile is empty, the new card must be an ace

        if(foundationPiles[foundationIndex].size() == 0) {
            if(card.getRank() == Rank.ACE) {
                return true;
            }
        }

        if(foundationPiles[foundationIndex].peek().getSuit() == card.getSuit()) {
            if(foundationPiles[foundationIndex].peek().getRank().ordinal() + 1 == card.getRank().ordinal()) {
                return true;
            }
        }

        //This method should return true if a card can be moved to the foundation, and false otherwise. 
        
        //hint: another good time to use peek() and ordinal()
        return false;
    }
    public boolean moveToFoundation(int fromPileIndex, int foundationIndex) {
        //check if we can move the top card of the fromPile to the foundation at foundationIndex
        
        //remember to flip the new top card if it is face down
        if(canMoveToFoundation(gamePiles[fromPileIndex].peek(), foundationIndex)) {
            foundationPiles[foundationIndex].push(gamePiles[fromPileIndex].pop());
            if(gamePiles[fromPileIndex].size() != 0) {
            if(!(gamePiles[fromPileIndex].peek().isFaceUp())) {
                gamePiles[fromPileIndex].peek().flip();
            }
        }
            return true;
        }

        //return true if successful, false otherwise
        return false;
    }

    public boolean moveToFoundationFromVisibleCards(int foundationIndex) {
        //similar to the above method, 
        //move the top card from the visible cards to the foundation pile with index foundationIndex if possible
        if(canMoveToFoundation(visibleCards.peek(), foundationIndex)) {
            foundationPiles[foundationIndex].push(visibleCards.pop());
            return true;
        }
        //return true if successful, false otherwise. 
        return false;
    }

    

    // Don't change this, used for testing
    public void printState() {
        System.out.println("Deck size: " + deck.size());

        System.out.print("Visible cards: ");
        if (visibleCards.isEmpty()) {
            System.out.println("None");
        } else {
            for (Card card : visibleCards) {
                System.out.print(card + " ");
            }
            System.out.println();
        }

        System.out.println("Discarded cards: " + discardedCards.size());

        System.out.println("Game piles:");
        for (int i = 0; i < gamePiles.length; i++) {
            System.out.print("Pile " + (i + 1) + ": ");
            if (gamePiles[i].isEmpty()) {
                System.out.println("Empty");
            } else {
                for (Card card : gamePiles[i]) {
                    System.out.print(card + " ");
                }
                System.out.println();
            }
        }
    }

    // getters
    public Stack<Card> getGamePile(int index) {
        return gamePiles[index];
    }

    public Stack<Card> getFoundationPile(int index) {
        return foundationPiles[index];
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    public Stack<Card> getVisibleCards() {
        return visibleCards;
    }
}
