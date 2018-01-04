import java.io.*;
import java.util.*;


public class Assignment4 {
	
	public static void main (String[] args){
		
		// Variables needed to read input file from user input 
		Scanner fReader;
		File fName;
		String fString = "";
		
		while (true)
        {
        	//try-catch method to input and find file to search for phrases within from command line
           try
           {
               fString = args[0];
               fName = new File(fString);
               fReader = new Scanner(fName);
              
               break;
           }
           catch (IOException e)
           {
               System.out.println("Problem " + e);
           }
        }
		// letters will store the text file contents in a single line with no spaces
		StringBuilder letters = new StringBuilder("");
		// currLine will represent the current line being evaluated from the text file
		String [] currLine;

		while (fReader.hasNext()){
			// removes spaces
			currLine = (fReader.nextLine()).split(" ");
			for (int i = 0; i < currLine.length; i++){
				letters.append(currLine[i]);
			}
		}
		
		// declares Huffman binary tree from the text file contents in letters
		HuffmanBinaryTree tree = new HuffmanBinaryTree(letters);
		
		// choice will represent user choice of operation
		int choice = 0;
		// reader used to scan user input
		Scanner reader = new Scanner(System.in);
		// pattern and pat will either contained set of binary digits or text to convert to Huffman string
		StringBuilder pattern;
		String pat;
		// valid represents whether user input string is a valid sequence
		boolean valid;
		
		// program will run as long as user the user does not choose option 3
		while (choice != 3){
			// displays menu, reads choice, and checks for error
			System.out.println("\nPlease choose from the following: ");
			System.out.println("1) Encode a text string");
			System.out.println("2) Decode a Huffman string");
			System.out.println("3) Quit");
			choice = reader.nextInt();
			while (choice < 1 || choice > 3){
				System.out.println("Please choose a valid option (1, 2, or 3)");
				choice = reader.nextInt();
			}
			
			// user choose to encode text string and convert to Huffman binary value
			if (choice == 1){
				System.out.println("Enter a String from the following characters: ");
				// displays options and reads in string
				tree.inOrder();
				pat = reader.next();
				// converts to uppser case to prevent error
				pat.toUpperCase();
				// converts to StringBuilder
				pattern = new StringBuilder(pat);
				// checks that user input is valid and stores in boolean valid
				valid = tree.checkLetters(pattern);
				// displays error message if the user input does not contain letters in the tree
				if(!valid){
					System.out.println("There was an error in your inputted string, please try again");
				}
				else{
					// calls encode method which will display the encoded values for each letter based off of the encoding table
					System.out.println("Huffman String: ");
					tree.encode(pattern);
				}
			}
			
			// user chooses to  decode a binary value to sequence of strings
			else if (choice == 2){
				System.out.println("Here is the encoding table:");
				// calls method to display the encoding table for user
				tree.dispTable();
				// user inputs Huffman string and is converted into StringBuilder pattern
				System.out.println("Please enter a Huffman string (one line, no spaces): ");
				pat = reader.next();
				pattern = new StringBuilder(pat);
				// checks for valid pathways based off encoding table
				valid = tree.checkNumbers(pattern);
				// error message if invalid
				if (!valid){
					System.out.println("There was an error in your inputted string, please try again");
				}
				// if valid, calls decode() to traverse through tree and finds/displays nodes in order
				else{
					System.out.println("Text String: ");
					tree.decode(pattern);
				}
			}
			// escape message when choice == 3
			else{
				System.out.println("Goodbye");
			}
		}
	}
}
