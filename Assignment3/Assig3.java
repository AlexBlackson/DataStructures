//Written By: Alex Blackson
//Last Modified: Wednesday March 15, 2017
//NOTES: Backtracking not actually backtracking AND sample3 not working
//Program designed to search through square character file and find phrases using backtracking

import java.io.*;
import java.util.*;

public class Assig3 {
	
	//xLoc will store the row location of found words
	//yLoc will store the column location of found words
	public int [] xLoc;
	public int [] yLoc;
	
	public static void main(String[] args) {
		//runs program by calling constructor
		new Assig3();
	}
	
	public Assig3(){
		
		//allows user to inputs and file reading capacity
		Scanner inScan = new Scanner(System.in);
		Scanner fReader;
		File fName;
		//target will be the phrase that the user searches for
        String fString = "", target = "";
        //wordCount stores number of words in phrase and w is temp variable to add words into phrase array
        int wordCount, w;
        //wordToAdd will be string be StringBuilder version
        StringBuilder wordToAdd = new StringBuilder("");
        //once converted, each word in wordToAdd will be added to phrase
        StringBuilder[] phrase;
        //found will see if the phrase can be found the file
        boolean found;
       
       	// Make sure the file name is valid
        while (true)
        {
        	//try-catch method to input and find file to search for phrases within
           try
           {
               System.out.println("Please enter grid filename:");
               fString = inScan.nextLine();
               fName = new File(fString);
               fReader = new Scanner(fName);
              
               break;
           }
           catch (IOException e)
           {
               System.out.println("Problem " + e);
           }
        }
        
        //content stores number of rows and columns in the file
        String [] content  = (fReader.nextLine()).split(" ");
        //rows will be number of rows in file and cols will be number of columns
        int rows = Integer.parseInt(content[0]);
        int cols = Integer.parseInt(content[1]);
        
        //board will be the character array version of letter in the file
        char[][] board = new char[rows][cols];
        
        //loop to add letter into the board character array
        for (int r = 0; r < rows; r++){
        	String nextRow = fReader.nextLine();
        	for (int c = 0; c < cols; c++){
        		board[r][c] = Character.toLowerCase(nextRow.charAt(c));
        	}
        }
        
        //displays board for user to see and search
        for (int r = 0; r < rows; r++){
        	for (int c = 0; c < cols; c++){
        		System.out.print(board[r][c] + " ");
        	}
        	System.out.println();
        }
        
        //allows user to input string and stores it in target
        System.out.println("\nPlease enter phrase to search for (separated by a single space, press ENTER to quit):");
        target = (inScan.nextLine()).toLowerCase();
        
        //loop will be launched as long the user does not press enter
        while(!target.equals("")){
        	
        	//loop to count words in the inputted string target
        	wordCount = 1;
        	for (int i = 0; i < target.length(); i++){
        		if (target.charAt(i) == ' ' && i != (target.length()-1)){
        			wordCount++;
        		}
        	}
        	
        	//initializes xLoc and yLoc as linear integer array of row & column locations
        	xLoc = new int[wordCount*2];
        	yLoc = new int[wordCount*2];
        	
        	//loop to add each individual word into phrase array of individual StringBuilder objects
        	phrase = new StringBuilder[wordCount];
        	w = 0;
        	for (int i = 0; i < target.length(); i++){
        		if (target.charAt(i) != ' ' && i != target.length()-1){
        			wordToAdd.append(target.charAt(i));
        		}
        		else{
        			if (target.charAt(i) != ' '){
        				wordToAdd.append(target.charAt(i));
        			}
        			phrase[w] = new StringBuilder(wordToAdd);
        			wordToAdd.setLength(0);;
        			w++;
        		}
        	}
        	
        	//Displays phrase to search for and number of words to find
        	System.out.print("\nLooking for: ");
        	for (int i = 0; i < wordCount; i++){
        		System.out.print(phrase[i].toString() + " ");
        	}
        	System.out.println("\ncontaining " + wordCount + " word(s)");
        	
        	found = false;
        	//Loop to search for start of word in each direction at each location until the phrase is found
        	for (int r = 0; (r < rows && !found); r++){       			
        		for (int c = 0; (c < cols && !found); c++){	
        			for (int d = 0; (d < 4 && !found); d++){
        				// d = 0 then LOOK RIGHT; d = 1 then LOOK DOWN; d = 2 then LOOK LEFT; d = 3 then LOOK UP
        				found = findWords(r, c, wordCount, 0, d, phrase, board);
        			}
        		}
        	}
        	
        	//if the phrase in found, this will be display the locations of each word and updated version of array
        	if (found){
        		System.out.print("\nThe phrase: ");
        		for (int i = 0; i < wordCount; i++){
            		System.out.print(phrase[i].toString() + " ");
            	}
        		System.out.println("\nwas found:");
        		for (int j = 0; j < wordCount; j++){
        			System.out.println(phrase[j] + ": (" + xLoc[j*2] + "," + yLoc[j*2] + ") to (" + xLoc[j*2+1] + "," +yLoc[j*2+1] + ")");
        		}
        		System.out.println("");
        		for (int r = 0; r < rows; r++){
                	for (int c = 0; c < cols; c++){
                		System.out.print(board[r][c] + " ");
                	}
                	System.out.println();
                }
        	}
        	
        	//displays that the phrase was NOT found in the board
        	else{
        		System.out.print("\nThe phrase: ");
        		for (int i = 0; i < wordCount; i++){
            		System.out.print(phrase[i].toString() + " ");
            	}
        		System.out.println("\nwas NOT found:");
        	}
        	
        	//Resets board to all lowercase letters 
        	for (int r = 0; r < rows; r++){
            	for (int c = 0; c < cols; c++){
            		board[r][c] = Character.toLowerCase(board[r][c]);
            	}
        	} 	
        	
        	//resets the word to find, where pressing enter 
        	System.out.println("\nPlease enter phrase to search for (separated by a single space, press ENTER to quit):");
            target = (inScan.nextLine()).toLowerCase();
        }
	}
	
	//findWords will recursively find the location of the words in the phrase and return if found
	//r,c - current row and col, wCount - total number of words in phrase, currWord word index analyzed, dir - direction to search in, b - character 2D array to search within
	public boolean findWords(int r, int c, int wCount, int currWord, int dir, StringBuilder[] words, char[][] b){
		
		//answer will store if the entire phrase is found and foundWord will store if the word for this iteration is found
		boolean answer = false;
		boolean foundWord = false;
		
		//tests that the r and c are valid locations
		if (r >= b.length || r < 0 || c >= b[0].length || c < 0){
			return false;
		}
		
		//Test if the first letter is the same before looping in certain direction
		else if (b[r][c] != words[currWord].charAt(0)){
			return false;
		}
		
		else {
			//Look Right
			//Checks to see that the word can be contained in board, checks the direction, and that the found hasn't been found
			if (((c+words[currWord].length()) <= b[0].length) && !foundWord && dir ==0){
				for (int i = c+1; i < (c+words[currWord].length()); i++){
					//checks to see if current character matches the words, exits if not
					if (words[currWord].charAt(i-c) != b[r][i]){
						return false;
					}
					
					//loop reached the end without returning, word found, locations added, direction of search confirmed, and exits loop
					if (i == (c+words[currWord].length()-1)){
						dir = 0;
						foundWord = true;
						xLoc[currWord*2] = r;
						yLoc[currWord*2] = c;
						for (int j = c; (j < (c+words[currWord].length())); j++){
							b[r][j] = Character.toUpperCase(b[r][j]);
						}
						c = c + words[currWord].length() - 1;
						xLoc[currWord*2+1] = r;
						yLoc[currWord*2+1] = c;
						break;
					}
				}
			}
			
			//Look Down
			//Checks to see that the word can be contained in board, checks the direction, and that the found hasn't been found
			if ((r+words[currWord].length() <= b.length) && !foundWord && dir == 1){
				for (int i = r+1; (i < (r+words[currWord].length())); i++){
					//checks to see if current character matches the words, exits if not
					if (words[currWord].charAt(i-r) != b[i][c]){
						return false;
					}
					
					//loop reached the end without returning, word found, locations added, direction of search confirmed, and exits loop
					if (i == (r+words[currWord].length()-1)){
						foundWord = true;
						dir = 1;
						xLoc[currWord*2] = r;
						yLoc[currWord*2] = c;
						for (int j = r; (j < (r+words[currWord].length())); j++){
							b[j][c] = Character.toUpperCase(b[j][c]);
						}
						r = r + words[currWord].length() - 1;
						xLoc[currWord*2+1] = r;
						yLoc[currWord*2+1] = c;
						break;
					}
				}
			}
			
			//Look Left
			//Checks to see that the word can be contained in board, checks the direction, and that the found hasn't been found
			if ((c-words[currWord].length()) >= -1 && !foundWord && dir == 2){
				for (int i = c-1; (i > (c-words[currWord].length())); i--){
					//checks to see if current character matches the words, exits if not
					if (words[currWord].charAt(c-i) != b[r][i]){
						return false;
					}
					
					//loop reached the end without returning, word found, locations added, direction of search confirmed, and exits loop
					if (i == (c-words[currWord].length()+1)){
						foundWord = true;
						dir = 2;
						xLoc[currWord*2] = r;
						yLoc[currWord*2] = c;
						for (int j = c; (j > (c-words[currWord].length())); j--){
							b[r][j] = Character.toUpperCase(b[r][j]);
						}
						c = c - words[currWord].length() + 1;
						xLoc[currWord*2+1] = r;
						yLoc[currWord*2+1] = c;
						break;
					}
				}
			}
			
			//Look Up
			//Checks to see that the word can be contained in board, checks the direction, and that the found hasn't been found
			if ((r-words[currWord].length() >= -1) && !foundWord && dir == 3){
				for (int i = r-1; (i > (r-words[currWord].length())); i--){
					//checks to see if current character matches the words, exits if not
					if (words[currWord].charAt(r-i) != b[i][c]){
						return false; 
					}
					
					//loop reached the end without returning, word found, locations added, direction of search confirmed, and exits loop
					if (i == (r-words[currWord].length()+1)){
						foundWord = true;
						dir = 3;
						xLoc[currWord*2] = r;
						yLoc[currWord*2] = c;
						for (int j = r; (j > (r-words[currWord].length())); j--){
							b[j][c] = Character.toUpperCase(b[j][c]);
						}
						r = r - words[currWord].length() + 1;
						xLoc[currWord*2+1] = r;
						yLoc[currWord*2+1] = c;
						break;
					}
				}
			}
		}	
		
		if (foundWord){
			//Base Case - All Words in sequence found
			if (currWord + 1 == wCount){
				return true;
			} 
			else{
				//not the end of the phrase, checks every direction recursively
				answer = findWords(r, c+1, wCount, currWord+1, 0, words, b);
				if (!answer)
					answer = findWords(r+1, c, wCount, currWord+1, 1, words, b);
				if(!answer)
					answer = findWords(r, c-1, wCount, currWord+1, 2, words, b);
				if(!answer)
					answer = findWords(r-1, c, wCount, currWord+1, 3, words, b);
			}
		}
		
		//Base Case - Word not found, entire sequence is now false
		if (!answer || !foundWord){
			//BACKTRACKS to reset capitalized letters to lowercase and sets locations back to zero, based upon the direction of search
			if (dir == 0){
				for (int i = yLoc[2*currWord]; i <= yLoc[2*currWord+1]; i++){
					b[r][i] = Character.toLowerCase(b[r][i]);
				}
			}
			else if (dir == 1){
				for (int i = xLoc[2*currWord]; i <= xLoc[2*currWord+1]; i++){
					b[i][c] = Character.toLowerCase(b[i][c]);
				}
			}
			else if (dir == 2){
				for (int i = yLoc[2*currWord+1]; i <= yLoc[2*currWord]; i++){
					b[r][i] = Character.toLowerCase(b[r][i]);
				}
			}
			else{
				for (int i = xLoc[2*currWord+1]; i <= xLoc[2*currWord]; i++){
					b[i][c] = Character.toLowerCase(b[i][c]);
				}
			}
			xLoc[2*currWord+1] = 0;
			yLoc[2*currWord+1] = 0;
			xLoc[2*currWord] = 0;
			yLoc[2*currWord] = 0;
		}
		//returns either backtracked false or carries true when the entire phrase is found
		return answer;
	}
	
}
