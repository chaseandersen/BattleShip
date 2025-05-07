// Lawrence, Tyson, Chase
// Group 4
/*
Test Data:
Tested all the methods as we made them and fixed any flaws found
Ran through the current full game multiple times with only 1 issue found:
There seems to be a rare chance the code does not get to the game class
but that is fixed by restarting the program
 */
import java.util.Scanner;

public class Main
{
    // the main program
    public static void main(String[] args)
    {

        Scanner scnr = new Scanner(System.in);

        System.out.println("Welcome to a Battleship game\n");
        boolean playAgain = true;

        // a while loop to play again after a game when the user prompts to
        while (playAgain)
        {
            System.out.println("Choose your difficulty");
            System.out.print("1: Easy\n2: Insane\nYour input: ");
            boolean incorrectInput = true;
            // a while loop for selecting the difficulty loops if invalid
            while (incorrectInput)
            {
                String input = scnr.nextLine();
                if (input.equals("1"))
                {
                    System.out.println("\nStarting Easy difficulty");
                    Game game = new Game(1);
                    game.startBattleship();
                    incorrectInput = false;
                }
                else if(input.equals("2"))
                {
                    System.out.println("\nYou already lost");
                    Game game = new Game(2);
                    game.startBattleship();
                    incorrectInput = false;
                }
                else
                {
                    System.out.println("\n" +
                            "Incorrect input\nPlease enter 1 for" +
                            " Easy Difficulty" +
                            "\nOr 2 for Insane Difficulty");
                }
            }

            // a prompt to play again, does not loop through if invalid
            System.out.println("\n\nWould you like to play again?");
            System.out.println("Y/N");
            String input = scnr.nextLine();
            if (input.equals("N") || input.equals("n"))
            {
                System.out.println("Thanks for playing");
                playAgain = false;
            }
            else if (input.equals("Y") || input.equals("y"))
            {
                System.out.println("Welcome back to a Battleship game");
            }
            else
            {
                System.out.println("invalid input, exiting");
                playAgain = false;
            }
        }
    }
}
