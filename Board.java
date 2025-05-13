// Lawrence, Tyson, Chase
// Group 4

import java.util.Scanner;

public class Board extends Game
{
    // the board and an int array to keep track of sunk ships
    private char[][] board = new char[10][10];
    private int[] shipSunk = {0, 0, 0, 0 ,0};

    // populates the board with '~'s
    public Board()
    {
        for (int i = 0; i < board.length; ++i)
        {
            for (int j = 0; j < board[0].length; ++j)
            {
                board[i][j] = '~';
            }
        }
    }

    // Automatically fills in the board with ships
    public void populateAIBoard()
    {
        char[][] board = new char[10][10];
        int shipSize = 5;

        // for each ship type, call aiPlaceShip, give the size and type
        for (int shipType = 4; shipType >= 0; --shipType)
        {
            if (shipSize == 5)
            {
                aiPlaceShip(shipSize, shipType);
                shipSize--;
            }
            else if (shipSize == 4)
            {
                aiPlaceShip(shipSize, shipType);
                shipSize--;
            }
            else if (shipSize == 3 && shipType == 2)
            {
                aiPlaceShip(shipSize, shipType);
            }
            else if (shipSize == 3 && shipType == 1)
            {
                aiPlaceShip(shipSize, shipType);
                shipSize--;
            }
            else if (shipSize == 2)
            {
                aiPlaceShip(shipSize, shipType);
            }
        }
    }

    // randomly places a ship on the board of the class
    public void aiPlaceShip(int size, int type)
    {
        int row = (int)(Math.random() * 10);
        int col = (int)(Math.random() * 10);
        int direction = (int)(Math.random() * 2);
        char[] ships = {'D','S','C','B','A'};
        boolean invalidSpot = true;
        // while loop to ensure that it will not place any overlapping ships
        if(direction == 1)
        {
            while (invalidSpot)
            {
                row = (int)(Math.random() * (10 - size));
                for (int i = 0; i < size; i++)
                {
                    if (board[i + row][col] != '~')
                    {
                        invalidSpot = true;
                        break;
                    }
                    invalidSpot = false;
                }
            }
            for(int i = 0; i < size; i++)
            {
                board[i + row][col] = ships[type];
            }
        }
        // while loop to ensure that it will not place any overlapping ships
        else
        {
            while (invalidSpot)
            {
                col = (int)(Math.random() * (10 - size));
                for (int i = 0; i < size; i++)
                {
                    if (board[row][i + col] != '~')
                    {
                        invalidSpot = true;
                        break;
                    }
                    invalidSpot = false;
                }
            }
            for (int i = 0; i < size; i++)
            {
                board[row][i + col] = ships[type];
            }
        }
    }

    // Returns a board with no ships
    public char[][] getAIBoard()
    {
        // a nested for loop to replace any "ships" from the board with '~'
        char[][] boardOut = new char[10][10];
        for (int i = 0; i < board.length; ++i)
        {
            for (int j = 0; j < board[0].length; ++j)
            {
                if (board[i][j] == 'A' || board[i][j] == 'B' || board[i][j]
                        == 'C' || board[i][j] == 'S' || board[i][j] == 'D')
                {
                    boardOut[i][j] = '~';
                }
                else
                {
                    boardOut[i][j] = board[i][j];
                }
            }
        }
        return boardOut;
    }

    // gets the user to populate their board with ships
    public void populatePlayerBoard()
    {
        // a for loop similar to populateAIBoard but with text prompts
        int shipSize = 5;
        for (int shipType = 4; shipType >= 0; --shipType)
        {
            if (shipType == 4)
            {
                System.out.println("You are placing the Aircraft carrier, " +
                        "please input a valid board location" +
                        " (ex. A1, B4, J10):");
                placeShip(shipType, shipSize);
                --shipSize;
            }
            else if (shipType == 3)
            {
                System.out.println("You are placing the Battleship, " +
                        "please input a valid board location" +
                        " (ex. A1, B4, J10):");
                placeShip(shipType, shipSize);
                --shipSize;
            }
            else if (shipType == 2)
            {
                System.out.println("You are placing the Cruiser, " +
                        "please input a valid board location" +
                        " (ex. A1, B4, J10):");
                placeShip(shipType, shipSize);
            }
            else if (shipType == 1)
            {
                System.out.println("You are placing the Submarine, " +
                        "please input a valid board location" +
                        " (ex. A1, B4, J10):");
                placeShip(shipType, shipSize);
                --shipSize;
            }
            else if (shipType == 0)
            {
                System.out.println("You are placing the Destroyer, " +
                        "please input a valid board location" +
                        " (ex. A1, B4, J10):");
                placeShip(shipType, shipSize);
            }
        }
    }

    // places a ship on the board of the type and size sent
    public void placeShip(int type, int size)
    {
        Scanner scnr = new Scanner(System.in);
        displayBoard(board);
        int row = -1;
        int col = -1;
        String orientation = "";
        boolean shipNotPlaced = true;
        char[] ships = {'D','S','C','B','A'};

        // until the user gives both a valid position and orientation
        // this will loop
        while (shipNotPlaced)
        {
            int[] rowCol = checkValidSpot();
            row = rowCol[0];
            col = rowCol[1];
            System.out.println("Please input the orientation:\n" +
                    "1 for vertical, 2 for horizontal.");
            orientation = scnr.nextLine();
            // for vertical checking ships and borders
            if (orientation.equals("1"))
            {
                if (row + size > 10)
                {
                    shipNotPlaced = true;
                    System.out.println("Invalid location for orientation," +
                            " please try again.\nplease input a" +
                            " valid board location (ex. A1, B4, J10)");
                    displayBoard(board);
                }
                else
                {
                    for (int i = 0; i < size; i++)
                    {
                        if (board[i + row][col] != '~')
                        {
                            shipNotPlaced = true;
                            System.out.println("Placement overlaps with" +
                                    " another ship, please try again.\n" +
                                    "please input a valid board location" +
                                    " (ex. A1, B4, J10)");
                            displayBoard(board);
                            break;
                        }
                        shipNotPlaced = false;
                    }
                }
            }
            // for horizontal checking ships and borders
            else if (orientation.equals("2"))
            {
                if (col + size > 10)
                {
                    shipNotPlaced = true;
                    System.out.println("Invalid location for orientation," +
                            " please try again.\nplease input a valid" +
                            " board location (ex. A1, B4, J10)");
                    displayBoard(board);
                }
                else
                {
                    for (int i = 0; i < size; i++)
                    {
                        if (board[row][i + col] != '~')
                        {
                            shipNotPlaced = true;
                            System.out.println("Placement overlaps with" +
                                    " another ship, please try again.\n" +
                                    "please input a valid board location" +
                                    " (ex. A1, B4, J10)");
                            displayBoard(board);
                            break;
                        }
                        shipNotPlaced = false;
                    }
                }
            }
            // effectively a mistake catching message
            else
            {
                System.out.println("Invalid orientation, please try again.\n" +
                        "please input a valid board" +
                        " location (ex. A1, B4, J10)");
                displayBoard(board);
            }
        }
        // for final placement of the ships
        // vertically
        if (orientation.equals("1"))
        {
            for(int i = 0; i < size; i++)
            {
                board[i + row][col] = ships[type];
            }
            logger.info("Placed ship of type " + ships[type] + " vertically at starting position:" +
                    " (" + row + ", " + col + ")");
        }
        // horizontally
        else if (orientation.equals("2"))
        {
            for (int i = 0; i < size; i++)
            {
                board[row][i + col] = ships[type];
            }
            logger.info("Placed ship of type " + ships[type] + " horizontally at starting position:" +
                    " (" + row + ", " + col + ")");
        }
    }

    // checks if there is no ships of a type on board
    // ++s their respective int in the shipSunk array
    public void checkForSunk()
    {
        char[] ships = {'A','B','C','D','S'};
        int[] shipCount = {0, 0, 0, 0, 0};

        // checks for each type of ship
        // if they exist on the board
        for (int i = 0; i < ships.length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                for (int k = 0; k < board[0].length; k++)
                {
                    if (board[j][k] == ships[i])
                    {
                        shipCount[i]++;
                    }
                }
            }
        }
        // if any of the ships did not exist
        // ++ their respective shipSunk value
        for (int i = 0; i < shipCount.length; i++)
        {
            if (shipCount[i] == 0)
            {
                shipSunk[i]++;
            }
        }
    }

    // calls checkForSunk then if everything has been sunk returns true
    public boolean checkWin()
    {
        int numShipsSunk = 0;
        boolean noShips = false;
        checkForSunk();

        for (int i = 0; i < shipSunk.length; i++)
        {
            if (shipSunk[i] > 0)
            {
                 numShipsSunk++;
            }
        }
        if (numShipsSunk == 5)
        {
            noShips = true;
        }
        return noShips;
    }

    // returns the shipSunk array
    public int[] getShipSunk()
    {
        return shipSunk;
    }

    // Returns an unmodified board
    public char[][] getBoard()
    {
        return board;
    }

    // sets the board to whatever board is input
    public void setBoard(char[][] board)
    {
        this.board = board;
    }
}