package ca.kim.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Hangman {

	/*
	 * ERROR LOG
	 * 
	 */
	public static void main(String[] args) {

		// Reader
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		// The string that will contain the user input from the console.
		String input = "";
		
		// Words that are preset to be the answer
		String presetWords[] = { "ball", "beach", "tower", "house", "boy", "zoo", "zebra", "kazoo", "flamingo",
				"bumblebee", "computer", "whiteboard", "capital", "printer", "comicsansms", "yoga", "island", "monkey",
				"gazeebo", "assault", "fossil", "confusion", "costume", "frequency", "knock", "faithful", "penetrate",
				"depend", "venus" };

		// If true, play again when a single game ends.
		boolean playagain = true;
		
		//ONE BIG MASSIVE LOOP (Should have used methods, lol. That would have been way less complicated).
		while(playagain) {
			
			// initial hangman 2D Array with no man hanging
			char hangman[][] = {
					{' ', ' ', ' ', ' ', ' ', ',', '%', '%', '%', '%', '%', '%', '%', '%', ',', ' ', ' ', ' ', ' ', ' '},
					{' ', ' ', ' ', ' ', ' ', '%', '%', 'o', '%', '%', '/', '%', '%', '%', '%', '%', '%', ' ', ' ', ' '},
					{' ', ' ', ' ', ' ', '%', '%', '>', '%', '%', '%', '/', '%', '%', '%', '%', 'o', '%', '%', ' ', ' '},
					{' ', ' ', ' ', ' ', '%', '%', '%', '%', '%', 'o', '%', '%', '\\', '%', '%', '/', '/', '%', ' ', ' '},
					{' ', ' ', ' ', ' ', '%', '\\', 'o', '%', '\\', '%', '%', '/', '%', 'o', '/', '%', '%', ' ', ' ', ' '},
					{' ', ' ', ' ', ' ', ' ', '\'', '%', '%', '\\', ' ', '\'', '%', '/', '%', '%', '%', '\'', ' ', ' ', ' '},
					{' ', ' ', ' ', ' ', ' ', ' ', ' ', '\'', '%', '|', ' ', '|', '%', ' ', '%', '\'', ' ', ' ', ' ', ' '},
					{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, 
					{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
					{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
					{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '/', ' ', ' ', ' ', '\\', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
					{'^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^', '^'}
			};
			/*
	      ,%%%%%%%%,
	      %%o%%/%%%%%%
	     %%%%\%%%<%%%%%
	     %%>%%%/%%%%o%%
	     %%%%%o%%\%%//%
	     %\o%\%%/%o/%%'
	      '%%\ `%/%%%'
	        '%| |% %'
	          | |
	          | |
	          | |
	          | |
	         /   \
	  ^^^^^^^^^^^^^^^^^^^^
			 */
			
			// presetWords Index Randomizer
			int answerIndex = (int) (Math.random() * presetWords.length);
			
			// The word from the presetWords that will be used as the final answer for a
			// single game.
			String answer = presetWords[answerIndex];
			
			// The String that will display the progress of the player's guesses.
			String guessDisplay = "";
			
			// The String that contains the progress of the player's guesses. This string is
			// used to determine win condition.
			String guessProgress = "";
			
			// String containing characters already guessed
			String alreadyGuess = "";
			
			// The countdown to death
			int countdown = 7;
			
			//Boolean to determine win by guessing the whole word
			boolean win = false;
			
			//Boolean to determine lose by completing the hangman
			boolean lose = false;
			
			// true when the inputed character does not match anything. Resets to false for
			// every input.
			boolean match = false;
			
			// Skip the check for matching the input and the answer.
			boolean skipCheck = false;
			
			// true if the Input is invalid: input = "";
			boolean invalidInput = false;
			
			// true if you already guessed the character
			boolean guessAlready = false;
			
			//true for hard mode.
			boolean hardmode = false;
			
			
			
			// Beginning Text
			System.out.println("Welcome to Hangman!");
			pause(500);
			
			// Enable hard mode
			System.out.println("Would you like to enable hard mode?");
			System.out.println("Yes: [Y]    No: [any button]    press [ENTER] to continue");
			try {
				input = reader.readLine();
				input = input.toLowerCase();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (input.equals("y")) {
				pause(500);
				System.out.println("Hard mode enabled. You will only have 5 lives. This is impossible.");
				hardmode = true;
			} else System.out.println("You will only have 7 lives.");
			
			//Configure custom word mode
			pause(500);
			System.out.println("Would you like to turn on custom word 2 player mode?");
			System.out.println("Yes: [Y]    No: [any button]    press [ENTER] to continue");
			try {
				input = reader.readLine();
				input = input.toLowerCase();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (input.equals("y")) {
				System.out.println("Make sure the guesser (The other player) is not looking at the screen!");
				pause(1000);
				System.out.println("Please type the word to be used and press [ENTER].");
				try {
					input = reader.readLine();
					input = input.toLowerCase();
				} catch (IOException e) {
					e.printStackTrace();
				}
				answer = input;
				pause(500);
				System.out.println(answer + "... hmm, a good choice.");
				pause(1000);
				bunchOfEmptyLines();
				pause(500);
				System.out.println("Now the guesser can look at the screen." + "\n");
				pause(1000);
				
			} else {
				pause(500);
				System.out.println("A preset word has been chosen to guess." + "\n");
			}
			
			
			
			// print hangman
			for (int i = 0; i < hangman.length; i++) {
				System.out.println(hangman[i]);
			}
			
			
			// print length of word and guess based on the answer chosen.
			for (int i = 0; i < answer.length(); i++) {
				guessDisplay += "_ ";
				guessProgress += " ";
			}
			System.out.print(guessDisplay + "\n");
			
			while (!win && !lose) {
				System.out.println("type one alphabet you want to guess and press [ENTER].");
				
				//print aready guessed characters
				if (alreadyGuess.length() != 0) System.out.print("You have aready guessed: [");
				for (int i = 0; i < alreadyGuess.length(); i++) {
					System.out.print(alreadyGuess.charAt(i));
					if (i < alreadyGuess.length() - 1)  System.out.print(", ");
				}
				if (alreadyGuess.length() != 0) System.out.println("]");
				
				
				// Reads input from user on the console
				try {
					input = reader.readLine();
					input = input.toLowerCase();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// Checks if the inputed string is invalid
				if (input.length() > 1) {
					pause(200);
					System.out.println("please input only one character.");
					skipCheck = true;
					invalidInput = true;
				}
				if (input.length() == 0) {
					pause(200);
					System.out.println("please input a character.");
					skipCheck = true;
					invalidInput = true;
				}
				
				// Checks if you already inputed the string
				// if invalid input = false, don't run this. It throws a string index out of
				// bounds.
				if (invalidInput) {
					invalidInput = false;
				} else {
					for (int i = 0; i < alreadyGuess.length(); i++) {
						if (alreadyGuess.charAt(i) == input.charAt(0)) {
							skipCheck = true;
							guessAlready = true;
						}
					}
				}
				if (guessAlready) {
					System.out.println("You have aready guessed this letter. Please try another one.");
					skipCheck = true;
					guessAlready = false;
				} else if (skipCheck) {
				} else {
					alreadyGuess += input;
				}
				
				// Checks if you need to skip the answer check.
				if (skipCheck)
					skipCheck = false;
				else {
					
					// Checks if the user input's character matches the answer's character, then
					// does necessary changes
					match = false;
					for (int i = 0; i < guessDisplay.length(); i += 2) {
						if (answer.charAt(i / 2) == input.charAt(0)) {
							guessDisplay = guessDisplay.substring(0, i) + input
									+ guessDisplay.substring(i + 1, guessDisplay.length());
							guessProgress = guessProgress.substring(0, i / 2) + input
									+ guessProgress.substring(i / 2 + 1, answer.length());
							match = true;
						}
					}
					
					// Check if you won
					if (guessProgress.equals(answer)) {
						win = true;
					}
					
					// Checks if your guess was incorrect
					if (match) {
						System.out.println("Correct!");
						match = false;
					} else {
						System.out.println("Wrong! The hangman is being drawed!");
						
						// Edit the hangman
						/*
				      ,%%%%%%%%,
				      %%o%%/%%%%%%
				     %%%%\%%%<%%%%%
				     %%>%%%/%%%%o%%
				     %%%%%o%%\%%//%
				     %\o%\%%/%o/%%'
				      '%%\ `%/%%%'
				        '%| |%|%'
				          | | (O
				          | | |\
				          | | >>
				          | |
				         /   \
				  ^^^^^^^^^^^^^^^^^^^^
						 */
						countdown--;
						if (!hardmode) {
							switch (countdown) {
							case 6:
								hangman[6][13] = '|';
								break;
							case 5:
								hangman[7][13] = '(';
								break;
							case 4:
								hangman[7][14] = 'O';
								break;
							case 3:
								hangman[8][13] = '|';
								break;
							case 2:
								hangman[8][14] = '\\';
								break;
							case 1:
								hangman[9][13] = '>';
								break;
							case 0:
								hangman[9][14] = '>';
								lose = true;
								break;
							}
						} else {
							switch (countdown) {
							case 6:
								hangman[6][13] = '|';
								hangman[7][13] = '(';
								break;
							case 5:
								hangman[7][14] = 'O';
								hangman[8][13] = '|';
								break;
							case 4:
								hangman[8][14] = '\\';
								break;
							case 3:
								hangman[9][13] = '>';
								break;
							case 2:
								hangman[9][14] = '>';
								lose = true;
								break;
							}
						}
						
						// Redraw the hangman
						for (int i = 0; i < hangman.length; i++) {
							System.out.println(hangman[i]);
						}
						
					}
					System.out.println(guessDisplay);
				}
			}
			
			//You exited the loop. The game is finished. Win or loss?
			if (win) {
				System.out.println("\n" + "Congratulations! You have won the game.");
			} else if (lose) {
				System.out.println("\n" + "Oh no!" + "\n" + "You coudn't finish the hangman in time... Better luck next time!");
				System.out.println("The word was: " + answer);
			}
			
			
			//Try again?
			System.out.println("\n" + "Would you like to play again?");
			System.out.println("Yes: [Y]    No: [any button]    press [ENTER] to continue");
			try {
				input = reader.readLine();
				input = input.toLowerCase();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (input.equals("y")) {
				bunchOfEmptyLines();
				playagain = true;
			} else {
				playagain = false;
				System.out.println("Hope to see you soon!");
			}
		}
	}
	
	/**
	 * CREATES A BUNCH OF EMPTY LINES FOR RESET PURPOSES
	 * BECAUSE YOU CAN'T DELETE LINES ON THE CONSOLE
	 */
	private static void bunchOfEmptyLines() {
		for (int i = 0; i < 100; i++)
			System.out.println();
	}

	/**
	 * Pauses the program for the desired amount of miliseconds
	 * @param milliseconds
	 */
	public static void pause(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
