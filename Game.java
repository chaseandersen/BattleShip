// Lawrence, Tyson, Chase
// Group 4

import java.util.Scanner;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

public class Game
{
    // difficulty, 1 = random targeting, 2 = always hits your ships
    private int difficulty;
    static final Logger logger = Logger.getLogger(Game.class.getName());

    static {
        try {
            // Create a FileHandler that writes log messages to "battleship.log"
            FileHandler fileHandler = new FileHandler("battleship.log", true); // 'true' enables append mode

            // Set a simple text formatter for the log messages
            fileHandler.setFormatter(new SimpleFormatter());

            // Add the FileHandler to the logger
            logger.addHandler(fileHandler);

            // Optional: Set the logging level
            logger.setLevel(Level.ALL);

            logger.info("Logger initialized successfully.");
        } catch (IOException e) {
            logger.severe("Failed to initialize logger FileHandler: " + e.getMessage());
        }
    }


    // default constructor, only used by extended board class
    public Game()
    {
        difficulty = 1;
    }

    // constructor, sets difficulty
    public Game(int diffIn)
    {
        this.difficulty = diffIn;
    }

    // Starts and plays the game
    public void startBattleship()
    {
        logger.info("Game initialized with difficulty level: " + this.difficulty);
        int roundNum = 1;
        Board playerB = new Board();    // player board
        Board aiB = new Board();        // ai board
        playerB.populatePlayerBoard();  // gets the ship location from player
        aiB.populateAIBoard();          // autopopulates AI board randomly

        // boolean used to exit game loop when false
        boolean gameRunning = true;

        // main game loop
        while (gameRunning)
        {
            logger.info("waiting...");
            wait3sec();

            System.out.println("----------Round " + roundNum + "----------");
            System.out.println("Your turn\n");

            displayGame(aiB.getBoard(), playerB.getBoard());


            // Player attacks
            // checks the AI board for sunk ships
            // then displays if any ships were sunk
            aiB.setBoard(attack(aiB.getBoard()));
            aiB.checkWin();
            shipSunkAI(aiB.getShipSunk());

            displayGame(aiB.getAIBoard(), playerB.getBoard());

            // checks win condition then displays player win message
            // breaks so the AI doesn't play its turn
            gameRunning = !(aiB.checkWin() || playerB.checkWin());
            if (!gameRunning && difficulty == 1)
            {
                System.out.println("\n\nCongratulations, you won!");
                logger.info("Game over. you won");
                break;

            }
            if (!gameRunning && difficulty == 2)
            {
                System.out.println("\n\nYou have done the impossible, " +
                        "Congratulations!");
                logger.info("Game over. you won");
                break;
            }
            logger.info("waiting...");
            wait3sec();



            System.out.println("AI's turn\n");

            // AI attacks
            // checks the player board for sunk ships
            // then displays if any ships were sunk
            playerB.setBoard(aiAttack(playerB.getBoard()));
            playerB.checkWin();
            shipSunkPlayer(playerB.getShipSunk());

            displayGame(aiB.getAIBoard(), playerB.getBoard());

            // checks win condition then displays AI win message
            gameRunning = !(aiB.checkWin() || playerB.checkWin());
            if (!gameRunning && difficulty == 1)
            {
                System.out.println("\n\nHa, I won, thanks for playing!");
                logger.info("Game over. ai won");
            }
            if (!gameRunning && difficulty == 2)
            {
                System.out.println("\n\nAs expected, I won");
                logger.info("Game over. ai won");
            }

            roundNum++;
        }
    }

    // prints out the board used as input
    public void displayBoard(char[][] board)
    {
        System.out.println("   1  2  3  4  5  6  7  8  9  10");
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for(int i = 0; i < board.length; ++i)
        {
            System.out.print(chars[i] + "  ");
            for (int j = 0; j < board[0].length; ++j)
            {
                // sets each type of character used to a specific color on the board
                if (board[i][j] == '~')
                {
                    System.out.print("\u001B[34m" + board[i][j] + "\u001B[0m" + "  ");
                }
                else if (board[i][j] == 'X')
                {
                    System.out.print("\u001B[31m" + board[i][j] + "\u001B[0m" + "  ");
                }
                else if (board[i][j] == 'o')
                {
                    System.out.print("\u001B[37m" + board[i][j] + "\u001B[0m" + "  ");
                }
                else
                {
                    System.out.print("\u001B[32m" + board[i][j] + "\u001B[0m" + "  ");
                }
            }
            System.out.println();
        }
    }

    // prints out both input boards and titles for them
    public void displayGame(char[][] aiBoard, char[][] playerBoard)
    {
        System.out.println("-----------AI's Board-----------");
        displayBoard(aiBoard);
        System.out.println("-----------Your Board-----------");
        displayBoard(playerBoard);
    }

    // prompts the user to select a spot on the board
    // then sets it to a hit 'X' or miss 'o' if valid
    public char[][] attack(char[][] board)
    {
        System.out.println("You are attacking, choose a" +
                " valid location on the enemy board");
        boolean noValidHit = true;

        // while loop for checking if hit is valid
        // aka, on board, and not a hit or a miss already
        while (noValidHit)
        {
            int[] rowCol = checkValidSpot();
            int row = rowCol[0];
            int col = rowCol[1];
            if (board[row][col] != '~' && board[row][col] != 'X' &&board[row]
                    [col] != 'o')
            {
                board[row][col] = 'X';
                System.out.println("You hit!");
                noValidHit = false;
            }
            else if (board[row][col] == '~')
            {
                board[row][col] = 'o';
                System.out.println("You Missed.");
                noValidHit = false;
            }
            else
            {
                System.out.println("Not a valid location, " +
                        "try hitting a different spot");
            }
        }
        return board;
    }

    // selects a random valid position to attack on the board
    // if higher difficulty, only valid position is ship tiles
    public char[][] aiAttack(char[][] board)
    {
        boolean noValidHit = true;

        // gets random position and rerolls until it's valid
        while (noValidHit)
        {
            int row = (int)(Math.random() * 10);
            int col = (int)(Math.random() * 10);
            // if it's easy difficulty, reroll for any hit or miss
            if (difficulty == 1 && board[row][col] != 'X' && board[row][col] !=
                    'o')
            {
                if (board[row][col] == '~')
                {
                    board[row][col] = 'o';
                    System.out.println("The AI missed.");
                }
                else
                {
                    board[row][col] = 'X';
                    System.out.println("The AI hit!");
                }
                noValidHit = false;
            }
            // if it's insane difficulty, reroll for any tile that's not a ship
            if (difficulty == 2 && board[row][col] != 'X' && board[row][col] != 'o'
                    && board[row][col] != '~')
            {
                board[row][col] = 'X';
                System.out.println("You were hit, I never miss");
                noValidHit = false;
            }
        }
        return board;
    }

    // gets input from user and checks if it's on the board
    // then returns 2 integers in an array
    public int[] checkValidSpot()
    {
        Scanner scnr = new Scanner(System.in);
        String input = scnr.nextLine();
        boolean inputInvalid = true;
        int row = -1;
        int col = -1;
        /*
        first checks if letter matches, and it's greater than 2 in length
        then checks if it's 3 characters long and if characters 2 and 3 == "10"
        otherwise checks if it's 2 characters long and if character 2 == 1-9
        if both of the last two checks are false then it's not a valid location
        */
        while (inputInvalid)
        {
            String[] chars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
            for(int i = 0; i < chars.length; ++i)
            {
                if (input.length() >= 2 &&
                        input.substring(0, 1).equals(chars[i]))
                {
                    if (input.length() == 3 &&
                            input.substring(1, 3).equals("10"))
                    {
                        inputInvalid = false;
                        row = i;
                        col = 9;
                        logger.info("Valid spot at: (" + row + ", " + col + ")");
                    }
                    else
                    {
                        for (int j = 49; j <= 58; ++j)
                        {
                            if (input.length() == 2 &&
                                    input.charAt(1) == j)
                            {
                                inputInvalid = false;
                                row = i;
                                col = j - 49;
                                logger.info("Valid spot at: (" + row + ", " + col + ")");
                            }
                        }
                    }
                }
            }
            if (inputInvalid == true)
            {
                System.out.println("Input " + input +
                        " is not on the board," +
                        " please try again.\n(Try A1, H2, G4)");
                input = scnr.nextLine();
            }
        }
        return new int[]{row, col};
    }

    // a self-contained method with a try-catch for a 3-second sleep
    public void wait3sec()
    {
        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    // print statements for displaying if ships were sunk for the AI
    public void shipSunkAI(int[] aiShipSunk)
    {
        if (aiShipSunk[0] == 1)
        {
            System.out.println("You sank my Aircraft carrier!");
        }
        if (aiShipSunk[1] == 1)
        {
            System.out.println("You sank my Battleship!");
        }
        if (aiShipSunk[2] == 1)
        {
            System.out.println("You sank my Cruiser!");
        }
        if (aiShipSunk[3] == 1)
        {
            System.out.println("You sank my Destroyer!");
        }
        if (aiShipSunk[4] == 1)
        {
            System.out.println("You sank my Submarine!");
        }
    }

    // print statements for displaying if ships were sunk for the player
    public void shipSunkPlayer(int[] playerShipSunk)
    {
        if (playerShipSunk[0] == 1)
        {
            System.out.println("Ha, I sank your Aircraft carrier!");
        }
        if (playerShipSunk[1] == 1)
        {
            System.out.println("Ha, I sank your Battleship!");
        }
        if (playerShipSunk[2] == 1)
        {
            System.out.println("Ha, I sank your Cruiser!");
        }
        if (playerShipSunk[3] == 1)
        {
            System.out.println("Ha, I sank your Destroyer!");
        }
        if (playerShipSunk[4] == 1)
        {
            System.out.println("Ha, I sank your Submarine!");
        }
    }

}