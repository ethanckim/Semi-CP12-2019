package ca.kim.parroter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parroter {

	/**
	 * Main is the method that runs when the program starts.
	 * 
	 */
	public static void main(String[] args) {
		
		String input;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boolean exit = false;

		System.out.println("Welcome to the Parroter program!");
		System.out.println("To close the program, please type \"close\"");
		while (true) {
			try {
			
				do {
					System.out.println("Input some text and push [ENTER]");
					input = reader.readLine();
					System.out.println("You said: " + input);
					
					if (input.length() <= 0) {
						exit = true;
					}
					
					if (input == "CLOSE") {
						exit = true;
					}
					
				} while(!exit);
			
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error: Please restart the program");
			
			}
		
			System.out.println("No input detected. would you like to close the program?");
			System.out.println("[Y] Yes      [N] No");
		
			try {
				input = reader.readLine();
				switch(input.toUpperCase()) {
				case "Y" :
					System.out.println("Have a nice day!");
					System.exit(0);
					break;
				case "N" :
					exit = false;
					break;	
				}
			
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error: Please restart the program");
			}
			
		}
	}
}
