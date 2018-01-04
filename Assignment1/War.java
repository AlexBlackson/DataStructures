// Created by Alex Blackson
// arb171@pitt.edu
// Peoplesoft ID: 3920894
// COE 0445: Data Structures with John Ramirez
// Last Edited: January 22, 2017

// Used to read in user input
import java.util.Scanner;

public class War {

	public static void main(String[] args) {
		
		// Deck will contains all Cards
		MultiDS<Card> deck = new MultiDS<Card>(52);
		
		// playerOne and playerTwo will contain the cards each player currently has
		MultiDS<Card> playerOne = new MultiDS<Card>(52);
		MultiDS<Card> playerTwo = new MultiDS<Card>(52);
		
		// p1Discard and p2Discard will contain each player owns but aren't in there deck in play
		MultiDS<Card> p1Discard = new MultiDS<Card>(52);
		MultiDS<Card> p2Discard = new MultiDS<Card>(52);
		
		// win will contain the cards a player will win from each round
		MultiDS<Card> win = new MultiDS<Card>(52);
		
		// over - determines whether or not game will proceed
		// war - determines whether or not round resulted in a war
		// roundGame - determines whether or not user wants game to stop after certain amount of rounds
		boolean over = false, war = false, roundGame = false;
		
		// round - current round of game
		// count - number of cards to be won in each round
		// last - contains how many rounds user would like to play if they want round-based game
		int round = 1, count = 0, last = 0;
		
		// yn determines whether or not user want round-based game
		String yn;
		
		// draw1 and draw2 will contain the top card of each player's deck to be played
		Card draw1;
		Card draw2;
		

		System.out.println("Welcome to the Game of War!\n\n");
		
		// Makes deck containing all 52 possible cards
		for (Card.Suits s: Card.Suits.values()){
			for (Card.Ranks r: Card.Ranks.values()){
				deck.addItem(new Card(s, r));
			}
		}
		
		// reader instance of Scanner allows for user input
		Scanner reader = new Scanner(System.in);
		
		// Prompts user to see if they would like a round-based game
		System.out.print("Would you like to stop game after certain number of rounds (y/n)? ");
		yn = reader.next();
		yn.toLowerCase();
		// Checks for invalid input
		while (!yn.equals("y") && !yn.equals("n")){
			System.out.println("Please enter y or n");
			System.out.print("Would you like to stop game after certain number of rounds (y/n)? ");
			yn = reader.next();
			yn.toLowerCase();
		}
		
		// Sets roundGame to true for later use
		if (yn.equals("y")){
			roundGame = true;
		}
		
		// If it is a round-based game, user will enter how many rounds they would like to play before stopping
		if (roundGame){
			System.out.print("How many rounds would you like to play before ending game? ");
			last = reader.nextInt();
		}
		deck.shuffle();
		
		System.out.println("Now dealing cards to the player...\n");
		
		// Deals deck to each player, alternating each turn
		for (int i = 0; i < 52; i++){
			if(i % 2 == 0){
				playerOne.addItem(deck.removeItem());
			}
			else{
				playerTwo.addItem(deck.removeItem());
			}
		}

		// Displays contents of each user's hand
		System.out.println("Here is Player 1's Hand:\n" + playerOne.toString() + "\n");
		System.out.println("Here is Player 2's Hand:\n" + playerTwo.toString() + "\n");
		
		System.out.println("Starting the WAR!");
		
		// Game will continue until over is set to true
		while (!over){
			
			//Initializes variables to be used for each round
			count = 0;
			war = true;
			
			while (war){
				// draw1 and draw2 are the top cards of each deck that will be compared to see which player wins round
				draw1 = playerOne.removeItem();
				draw2 = playerTwo.removeItem();
				count += 2;
				
				// Adds draw1 and draw2 to pile to be won
				win.addItem(draw1);
				win.addItem(draw2);
				
				// Checks to see if player's deck needs reset
				if (!p1Discard.empty() && playerOne.empty()){
					System.out.print("\t\tGetting and shuffling the pile for player 1\n");
					// If player hand is empty, it will add the discard cards to hand and shuffle hand
					for (int i = (p1Discard.size() - 1); i >= 0; i--){
						playerOne.addItem(p1Discard.removeItem());
					}
					playerOne.shuffle();
				}
				
				// Repeats above process for player 2
				if (!p2Discard.empty() && playerTwo.empty()){
					System.out.print("\t\tGetting and shuffling the pile for player 2\n");
					for (int i = (p2Discard.size() - 1); i >= 0; i--){
						playerTwo.addItem(p2Discard.removeItem());
					}
					playerTwo.shuffle();
				}
				
				// Prevent nullPointer exception by exiting loop before comparisons if a player has lost
				if ((p1Discard.empty() && playerOne.empty()) || (p2Discard.empty() && playerTwo.empty())){
					break;
				}
				
				// Player 1 plays higher value card
				if(draw1.compareTo(draw2) > 0){
					System.out.println("Player 1 Wins Round " + String.valueOf(round) + ": " + draw1.toString() + " beats " + draw2.toString() + ": " + String.valueOf(count) + " cards");
					// Loop will add each of the prize cards to player's discard pile
					for (int i = 0; i < count; i++){
						p1Discard.addItem(win.removeItem());
					}
					// Sets war to false to proceed to next round
					war = false;
				}
				
				// Player 2 plays higher value card and repeats above process for player 2
				else if (draw1.compareTo(draw2) < 0){
					System.out.println("Player 2 Wins Round " + String.valueOf(round) + ": " + draw1.toString() + " loses to " + draw2.toString() + ": " + String.valueOf(count)+ " cards" );
					for (int i = 0; i < count; i++){
						p2Discard.addItem(win.removeItem());
					}
					war = false;
				}
				
				// If the values are equal, a war will occur
				else{
					
					System.out.println("\t\tWAR: " + draw1.toString() + " ties " + draw2.toString());
					
					// Removes next card from each pile, these will not be compared per the rules of War
					draw1 = playerOne.removeItem();
					draw2 = playerTwo.removeItem();
					System.out.println("\t\t"+ draw1.toString() + " and " + draw2.toString() + " are at risk!");
					
					// Accounts for both face-down cards in case of a War, adds to winning pile
					win.addItem(draw1);
					win.addItem(draw2);
					
					// Checks to see if either player needs their deck reset, as done above
					if (!p1Discard.empty() && playerOne.empty()){
						System.out.print("\t\tGetting and shuffling the pile for player 1\n");
						for (int i = (p1Discard.size() - 1); i >= 0; i--){
							playerOne.addItem(p1Discard.removeItem());
						}
						playerOne.shuffle();
					}
					
					if (!p2Discard.empty() && playerTwo.empty()){
						System.out.print("\t\tGetting and shuffling the pile for player 2\n");
						for (int i = (p2Discard.size() - 1); i >= 0; i--){
							playerTwo.addItem(p2Discard.removeItem());
						}
						playerTwo.shuffle();
					}
					
					// Increments count by 2 to account for two face-down cards
					count += 2;
				}
				
			}
			
			// Checks to see either player has no cards remaining and has hence lost
			if (playerOne.empty() && p1Discard.empty()){
				System.out.println("\nPlayer 1 is out of cards!");
				System.out.println("Player 2 is the winner!");
				// Sets over true to exit loop and program altogether 
				over = true;
			}
			if (playerTwo.empty() && p2Discard.empty()){
				System.out.println("\nPlayer 2 is out of cards!");
				System.out.println("Player 1 is the winner!");
				// Sets over true to exit loop and program altogether 
				over = true;
			}
			
			// If roundGame it is a round game, will check to see if program has hit the final round
			if (roundGame){
				if(round >= last){
					
					/// p1Total and p2Total will store the total number of cards in each has in both piles
					int p1Total = playerOne.size() + p1Discard.size();
					int p2Total = playerTwo.size() + p2Discard.size();
					System.out.println("\nAfter " + String.valueOf(last) + " rounds here is the status:");
					System.out.println("\t\tPlayer 1 has "+ String.valueOf(p1Total) + " cards");
					System.out.println("\t\tPlayer 2 has "+ String.valueOf(p2Total) + " cards");
					
					// Determines the winner based on who has more cards and displays as such
					if (p1Total > p2Total){
						System.out.println("Player 1 is the WINNNER!");
					}
					else if (p1Total < p2Total){
						System.out.println("Player 2 is the WINNNER!");
					}
					else{
						System.out.println("It is a STALEMATE!");
					}
					
					// Sets over to true to exit loop and program altogether
					over = true;
				}
			}
			
			// Increments round by one for display purposes
			round++;
		}
	}
}
