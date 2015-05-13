import java.io.*;
import java.util.*;
import java.awt.event.*;

/**
 * ConsoleUI represents one of the different views of the GameBoard.
 * It is the console view of the game and is another way of interacting
 * with the game. 
 * 
 * @author Eric Tran
 * @version 1
 */
public class ConsoleUI implements Observer 
{
    private Scanner console;
    private PrintWriter display;
    private GameBoard gameBoard;
    private BoardRegulator regulator; 
    private StringBuilder lastSeqMove;
    private boolean aboutHallPrint;

    /**
     * Prompt message.
     */
    private static final String kPrompt = "1)Restart 2)New Game 3)Select Game " +
        "4)Set Game 5)Hall of Fame 6)About 7)Quit 8)Prefs";
    
    /**
     * About Game message.
     */
    private static final String kAboutGame = 
        "-- About --\n" +
        "Roundup Version 1.0\n" + 
        "Type in the coordinates of the robot " +
        "then press the U/D/R/L to move the robot.\n" +
        "The robot will move in a straight line in the direction specified.\n" +
        "The robot can only stop when it runs into another robot, or\n" +
        "walks off the board. Your goal is to guide the red robot to\n" +
        "end up positioned on the center square.\n" +
        "There are 18 puzzles that can be played in any order.\n" +
        "Android Icon by http://madeliniz.deviantart.com\n";

    /**
     * Constructs the console view of the application. 
     * @param rdr a Reader from which to read the user's input,
     * usually <code>System.in</code>
     * @param wtr a Writer to which to display the output, 
     * usually <code>System.out</code>
     * @param board the observable gameboard
     */
    public ConsoleUI(Reader rdr, Writer wtr, GameBoard board) 
    {
        console = new Scanner(rdr);
        display = new PrintWriter(wtr, true);   
        gameBoard = board;
        regulator = board.getRegulator();
        aboutHallPrint = false; 
    }

    /**
     * Prints the hall of fame to the display. 
     */
    private void printHallOFame()
    {
        display.println("-- Hall of Fame --");
        ArrayList<Object[]> records = 
            gameBoard.getHallOfFame().getHallOfFamers();
        int recIn;
        // Loops through and prints all entries of the hall of fame. 
        for (int in = 0; in < records.size(); in++)
        {
            recIn = 0;
            display.println(String.format("%3d", (records.get(in)[recIn++])) + " " 
                + records.get(in)[recIn++] + " "  
                + records.get(in)[recIn++] + " "
                + String.format("%2s", (records.get(in)[recIn++])) + "  " 
                + (records.get(in))[recIn++]);
        }
        display.println();
    }

    /**
     * Helper to quit game.
     */
    private String quitGame()
    {
        String quit = "quit";
        // If both views are up, act accordingly. 
        if (gameBoard.getBothViewUp())
        {
            System.exit(0);
        }
        return quit;
    }

    /**
     * Trys to select a new game.
     */
    private void trySelectGame()
    {
        display.println("Select Game: " +
            "Enter desired game number (1 - 18):");
        try
        {
            int board = console.nextInt();
            regulator.setBoard(board);
            gameBoard.newGame(regulator.getBoard());
        }
        catch (InputMismatchException e)
        {
            int board = 0;
        }
    }

    /**
     * Checks the Hall Flags
     */
    private void checkHall()
    {
        // Check if both gui and console are up
        if (!gameBoard.getBothViewUp()) 
        {
            printHallOFame();
        }
        // If it is then do this. 
        else 
        {
            aboutHallPrint = true;
            gameBoard.setHallFlag(true);
        }
    }

    /**
     * Checks the About flag
     */
    private void checkAbout()
    {
        // Checks if both gui and console are up.
        if (!gameBoard.getBothViewUp()) 
        {
            display.println(kAboutGame);
        }
        // If it is true then do this. 
        else 
        {
            aboutHallPrint = true;
            gameBoard.setAboutFlag(true);
        }
    }

    /**
     * Checks set game requirements
     */
    private void setGame()
    {
        display.println("Set Game: Enter board configuration:");
        String coord = console.nextLine();
        getCustomBoard(coord);
    }

    /**
     * Does a specific action based on what the user inputted (for the menu). 
     * @param input the string to be crossreferenced 
     */
    private String menuChecks(String input)
    {
        String quit = "";
        // Menu options
        switch (input)
        {
            case "0":
                gameBoard.getHallOfFame().deleteFile();
                display.println("roundup/halloffame.ser deleted.");
                break;
            case "1": 
                gameBoard.newGame(regulator.getBoard());
                break;
            case "2":
                regulator.nextBoard();
                gameBoard.newGame(regulator.getBoard());  
                break;
            case "3":
                trySelectGame();
                break;
            case "4":
                setGame();
                break;
            case "5":
                checkHall();
                break;
            case "6":
                checkAbout();
                break;
            case "7":
                quit = quitGame();
                break; 
            case "8":
                printPref();
                break;
            default: 
                break;
        }
        return quit; 
    }

    /** 
     * Prints the preferences for the user to pick. 
     */
    private void printPref()
    {
        display.println("[Skin]");
        display.println(
            "(a) default (robots) = robots  (b) sheep = sheep" +
            "  (c) cows = cows  (d) rainbow robots = rainbow  "); 
        display.println("Your choice?");
        display.println("Roundup - board " + regulator.getBoardNumber() 
            + " " + regulator.getBoardDifficulty());
        display.println("Moves: " + gameBoard.getMoveCount());
        printBoard();
    }

    /**
     * Depending on the parameter, 
     * will set the currentBoard to the custom board.
     * @param coord the string to be validated
     */
    private void getCustomBoard(String coord)
    {
        // Provided string is valid. 
        if (gameBoard.checkValid(coord))
        {
            regulator.setCustom(coord);
            gameBoard.newGame(coord);
        }
        // Invalid string. 
        else
        {
            display.println("-- Error --");
            display.println("Not a valid board configuration.");
        }
    }

    /**
     * Check inputs of 1 length.
     */
    private void tokenLengthOne(String firstToken)
    {
        // Checks if the token is a y for saving into hallOfFame
        if (firstToken.equals("y") && 
            !gameBoard.getAlreadySaved() && 
            lastSeqMove != null)
        {
            gameBoard.addEntryToHallOfFame(lastSeqMove);
            lastSeqMove = null;
        }
    }

    /**
     * Moves a piece based on its direction. 
     * @param locationX the x coord to move to
     * @param locationY the y coord to move to
     * @param dir the direction moving towards
     */
    private void moveChecks(int locationX, int locationY, String dir)
    {
        // Different directions means different events. 
        switch (dir)
        {
            case "D":
                gameBoard.setLastDir(KeyEvent.VK_DOWN);
                gameBoard.movePiece(locationX, locationY, KeyEvent.VK_DOWN);
                break;
            case "R":
                gameBoard.setLastDir(KeyEvent.VK_RIGHT);
                gameBoard.movePiece(locationX, locationY, KeyEvent.VK_RIGHT);
                break;
            case "L":
                gameBoard.setLastDir(KeyEvent.VK_LEFT);
                gameBoard.movePiece(locationX, locationY, KeyEvent.VK_LEFT);
                break;
            case "U":
                gameBoard.setLastDir(KeyEvent.VK_UP);
                gameBoard.movePiece(locationX, locationY, KeyEvent.VK_UP);
                break;
            default:
                break;
        }
    }

    /**
     * Helper to check if piece is red or green.
     * @param locationX the x coord
     * @param locationY the y coord
     * @param dir the direction
     * @param gamePiece the piece to be analyzed
     */
    private void checkMovable(int locationX, int locationY, String dir, Object gamePiece)
    {
        // If there is a movable piece at location    
        if (gamePiece.equals(GameBoard.kRed) || 
            gamePiece.equals(GameBoard.kGreen) ||
            gamePiece.toString().equals("red") || 
            gamePiece.toString().equals("green") ||
            gamePiece.toString().equals("reddown") || 
            gamePiece.toString().equals("greendown") ||
            gamePiece.toString().equals("redleft") || 
            gamePiece.toString().equals("greenleft") ||
            gamePiece.toString().equals("redright") || 
            gamePiece.toString().equals("greenright") )
        {
            moveChecks(locationX, locationY, dir);
        }
    }
    
    /**
     * Parses the location from a token of length 3.
     * @firstToken the string of length three
     */
    private void tokenLengthThree(String firstToken)
    {
        int locationX = Integer.parseInt(firstToken.substring(0, 1));
        int locationY = Integer.parseInt(firstToken.substring(1, 2));
        int maxBound = gameBoard.getGrid().getRowCount() - 2;
        // Location is within the bounds. 
        if (locationX >= 1 && locationX <= maxBound && 
            locationY >= 1 && locationY <= maxBound)
        {
            String dir = firstToken.substring(2).toUpperCase();
            GameBoard.MyTable grid = gameBoard.getGrid();
            Object gamePiece = 
                grid.getValueAt(locationX, locationY);

            checkMovable(locationX, locationY, dir, gamePiece);
        }
    }

    /**
     * Prompts the user for some action. 
     */
    public void run()
    {
        String firstToken = "";
        String input = "";
        String quit = "";
        int maxChars = "abc".length();

        StringTokenizer tokens;

        // While console is open and reading lines. 
        while (console.hasNextLine())
        {
            input = console.nextLine();
            tokens = new StringTokenizer(input, " ");
            // If the next line has at least one token 
            if (tokens.hasMoreTokens())
            {
                firstToken = tokens.nextToken();
                // User input was only 1 character, accesses the menu 
                if (firstToken.length() == 1)
                {
                    tokenLengthOne(firstToken);
                    quit = menuChecks(input);
                    // Menu option returns quit. 
                    if (quit.equals("quit"))
                    {
                        break;
                    }

                }
                // User input is 3 chars, 2 to represent coord, 1 for dir
                else if (firstToken.substring(0, 2).matches("[0-9]+") 
                    && firstToken.length() == maxChars) 
                {
                    tokenLengthThree(firstToken);
                }

            }
        }
    }

    /**
     * Helps print the initial layout of the board. 
     * @param in the row num
     */
    private void printSideGrid(int in)
    {
        // Only print the colons for 1 to 5
        if (in != 0 && in != gameBoard.getGrid().getRowCount() - 1)
        {
            display.printf("%2s:", in);  
        }
        // Otherwise print nothing
        else 
        {
            display.printf("%2s", "   ");
        }
    }

    /**
     * Checks the bounds for printing board.
     */
    private void printBound(int row, int col)
    {
        // Check if it falls off the board. 
        if (row == 0 || row == gameBoard.getGrid().getRowCount() - 1 || 
            col == 0 || col == gameBoard.getGrid().getColumnCount() - 1)
        {
            display.printf("%3s", "  X");
        }
        // Didn't fall off. 
        else
        {
            display.printf("%3s", "  o");
        }
    }

    /**
     * Helper method to help print easier.
     * @param gamePiece the piece to analyze
     */
    private void printLeftOver(Object gamePiece)
    {
        // Game piece is red
        if (gamePiece.toString().equals("red") ||
            gamePiece.equals(GameBoard.kRed) ||
            gamePiece.toString().equals("redleft") || 
            gamePiece.toString().equals("reddown") ||
            gamePiece.toString().equals("redright") 
        )
        {
            display.printf("%3s", "  *");
        }
        // If the piece is a trailing dot.
        else if (gamePiece.equals(GameBoard.kTrail))
        {
            display.printf("%3s", "  .");
        }
        // It has to be a blank space. 
        else 
        {
            display.printf("%3s", "   ");
        }
    }
    
    /**
     * Checks the game piece and determines how to print it.
     * @param gamePiece the piece being manipulated
     * @param row the x coord
     * @param col the y coord
     */
    private void checkPieceToPrint(Object gamePiece, int row, int col)
    {
        // If the piece is a green piece
        if (gamePiece.toString().equals("greenright") || 
            gamePiece.equals(GameBoard.kGreen) ||
            gamePiece.toString().equals("greenleft") || 
            gamePiece.toString().equals("greendown") ||
            gamePiece.toString().equals("green") )
        {
            printBound(row, col);
        }
        // If the piece is a red piece
        else 
        {
            printLeftOver(gamePiece);
        }
    }

    /**
     * Prints the board to the display. 
     */
    public void printBoard()
    {
        Object gamePiece; 
        display.println("        1  2  3  4  5   ");
        // Goes through the entire row
        for (int row = 0; row < gameBoard.getGrid().getRowCount(); row++)
        {
            printSideGrid(row);
            // Goes through all columns
            for (int col = 0; col < gameBoard.getGrid().getColumnCount(); col++)
            {   
                gamePiece =  gameBoard.getGrid().getValueAt(row, col);
                checkPieceToPrint(gamePiece, row, col);
            }
            display.println();
        }
        display.println(" -----------------------");
        display.println(kPrompt);
    }

    /**
     * Prints out the congratulatory message with the win sequence, 
     * and asks if you would like to save. 
     */
    public void printWin()
    {
        String toAddHall = "";
        display.println("Moves: " + gameBoard.getMoveCount());
        printBoard();
        ArrayList<String> allMoves = gameBoard.getMoves();
        StringBuilder moves = new StringBuilder();
        // Builds the move sequence.
        for (String move : allMoves)
        {
            moves.append(move);
        }
        display.println("Game Won Notification: You won game " 
            + regulator.getBoardNumber() + "!");
        display.println(moves.toString());
        display.println("Save your time of 0:00? (y/n)");
        lastSeqMove = moves;
    }

    /**
     * Called whenever the model is changed. 
     * Updates the console view. 
     * @param obs the obserable object
     * @param obj the object that called the update method 
     */
    public void update(Observable obs, Object obj)
    {
        // Check to avoid double printing. 
        if (!aboutHallPrint)
        {
            display.println("Roundup - board " + regulator.getBoardNumber() 
                + " " + regulator.getBoardDifficulty());
            // Game was lost.     
            if (gameBoard.getLoseFlag())
            {
                display.println("LOSE");
                printBoard();
            }
            // Game was won. 
            else if (gameBoard.getWinFlag())
            {
                printWin();
            }
            // Game is going. 
            else
            {
                display.println("Moves: " + gameBoard.getMoveCount());
                printBoard();
            }
        }
        aboutHallPrint = false;
    }
}