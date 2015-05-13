import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;

/**
 * GameBoard class represents the grid on 
 * which our roundup game will be played on. 
 * 
 * @author Eric Tran 
 * @version 1
 */
public class GameBoard extends Observable
{
    /**
     * Represents red piece on the board.
     */
    public static final Object kRed = -1;
    
    /**
     * Represents green piece on the board.
     */
    public static final Object kGreen = 1;
    
    /**
     * Represents blank space on the board.
     */
    public static final Object kBlank = 0;
    
    /**
     * Represents trail dot on the board.
     */
    public static final Object kTrail = 2;
    
    /** 
     * Max row and col of the grid
     */
    public static final int kMaxRowCol = 7;

    private BoardRegulator regulator;
    private MyTable grid ;
    private int moveCount;
    private boolean loseFlag;
    private boolean winFlag;
    private boolean aboutFlag;
    private boolean hallFlag;
    private boolean alreadySaved;
    private boolean bothViewUp;
    private boolean consoleQuit;
    private int movedRow;
    private int movedCol; 
    private int lastDir;
    private ArrayList<String> moveSeqs;
    private HallOfFame hof;

    
    /**
     * Default constructor for the GameBoard class.
     * @param bothView whether or not two views
     */
    public GameBoard(boolean bothView)
    {
        regulator = new BoardRegulator();
        grid = new MyTable(kMaxRowCol, kMaxRowCol);
        moveSeqs = new ArrayList<String>();

        moveCount = 0;
        movedRow = -1;
        movedCol = -1;
        lastDir = -1;
        loseFlag = false; 
        winFlag = false;
        aboutFlag = false;
        hallFlag = false;
        alreadySaved = false;
        consoleQuit = false; 
        bothViewUp = bothView;
        hof = new HallOfFame();
        newGame(regulator.getBoard());
    }

    /**
     * Gets the grid.
     * @return the abstract table model 
     */
    public MyTable getGrid()
    {
        return grid; 
    }

    /**
     * Gets the board regulator.
     * @return the board regulator
     */
    public BoardRegulator getRegulator()
    {
        return regulator;
    }

    /**
     * Gets the numbers of moves currently at. 
     * @return number of moves currently at
     */
    public int getMoveCount()
    {
        return moveCount;
    }

    /**
     * Gets the lose flag.
     * @return whether or not game is lost
     */
    public boolean getLoseFlag()
    {
        return loseFlag; 
    }

    /**
     * Gets the win flag.
     * @return whether or not game is won
     */
    public boolean getWinFlag()
    {
        return winFlag;
    }

    /**
     * Resets the initial game variables to 0, or false. 
     */
    private void resetWinLose()
    {
        moveCount = 0;
        winFlag = false;
        loseFlag = false;
        alreadySaved = false;
        moveSeqs = new ArrayList<String>();
    }

    /**
     * Gets the last moved row.
     * @return last moved row 
     */
    public int getLastMovedRow()
    {
        return movedRow;
    }

    /**
     * Gets the last moved col.
     * @return last move col
     */
    public int getLastMovedCol()
    {
        return movedCol;
    }

    /**
     * Resets the last moved row and col to -1. 
     */
    public void resetMovedRowCol()
    {
        movedRow = -1;
        movedCol = -1;
    }

    /**
     * Stores the last moved direction.
     * @param code the direction to be saved
     */
    public void setLastDir(int code)
    {
        lastDir = code; 
    }

    /**
     * Gets the last moved direction.
     * @return last move direction
     */
    public int getLastDir()
    {
        return lastDir;
    }

    /**
     * Gets the move sequence used currently.
     * @return list of moves
     */
    public ArrayList<String> getMoves()
    {
        return moveSeqs;
    }

    /**
     * Gets the stored Hall Of Fame of this game. 
     * @return the board's hall of fame
     */
    public HallOfFame getHallOfFame()
    {
        return hof;
    }

    /**
     * Gets the flag for whether or not the 
     * last game was saved. 
     * @return whether or not already saved to hall of fame
     */
    public boolean getAlreadySaved()
    {
        return alreadySaved;
    }
    
    /**
     * Gets the flag for whether or not both views are up.
     * @return whether 2 views
     */
    public boolean getBothViewUp()
    {
        return bothViewUp;
    }
    
    
    /**
     * Gets the aboutFlag.
     * @return about flag
     */
    public boolean getAboutFlag()
    {
        return aboutFlag;
    }
    
    /**
     * Gets the Hall Of Fame flag.
     * @return hall flag
     */
    
    public boolean getHallFlag()
    {
        return hallFlag;
    }
    
    /**
     * Sets the aboutFlag.
     * @param flag new flag to set to 
     */
    public void setAboutFlag(boolean flag)
    {
        aboutFlag = flag;
        setChanged();
        notifyObservers();
    }
    
    /**
     * Sets the Hall Of Fame flag.
     * @param flag new flag to set to 
     */
    
    public void setHallFlag(boolean flag)
    {
        hallFlag = flag;
        setChanged();
        notifyObservers();
    }
        
    /**
     * Sets the aboutFlag without notifying the observers.
     * @param flag new flag to set to 
     */
    public void setAboutFlagWithoutNotifying(boolean flag)
    {
        aboutFlag = flag;
    }
    
    /**
     * Sets the hallFlag without notifying observers.
     * @param flag new flag to set to 
     * 
     */
    public void setHallFlagWithoutNotifying(boolean flag)
    {
        hallFlag = flag;
    }
    
    /**
     * Adds a new entry into the HallOfFame.
     * @param moves the winning seqence to add to hall 
     */
    public void addEntryToHallOfFame(StringBuilder moves)
    {
        alreadySaved = true;
        Object[] entry = hof.buildEntry(regulator.getBoardNumber(), 
                regulator.getBoardDifficulty(), "0:00:00", 
                getMoveCount(), moves.toString());
        hof.addEntry(entry);
    }
    
    /**
     * Checks for repeats and correct number of robots.
     * @param atLeast2 whether or not at least 2 chars
     * @param withinBounds at least all are within bounds
     * @param splited array with all the coordinates 
     * @return flags some valid flags (redCount and dupes)
     */
    private boolean[] checkRepeatsNumkReds(
        boolean atLeast2, boolean withinBounds, String[] splited)
    {
        boolean[] flags = new boolean[]{true, true};
        // At least 2 char and within bounds are true
        if (atLeast2 && withinBounds)
        {
            // Check for repeats coordinates through rows
            for (int row = 0; row < splited.length - 1 ; row++)
            {
                // Checks for repeats through cols 
                for (int col = row + 1; col < splited.length; col++)
                {
                    // Found a duplicate
                    if (splited[row].substring(0, 2).equals(
                            splited[col].substring(0, 2)))
                    {
                        flags[0] = false; 
                    }
                }
            }

            int redCount = 0;
            // Checks that there is only one red robot
            for (String string : splited)
            {
                // If theres a R, increment count
                if (string.contains("R") || string.contains("r"))
                {
                    redCount++;
                }
            }
            
            // If there is not only one red
            if (redCount != 1)
            {
                flags[1] = false;    
            }
        }
        return flags;
    }
    
    /**
     * Helper method to help parse and check if coordiante is within
     * the bounds.
     * @param input the coordinate to be checked
     * @return whether or not it is within boun 
     */
    private boolean checkBound(String input)
    {
        int coordX = Integer.parseInt(
                        input.substring(0, 1));
        int coordY = Integer.parseInt(
                        input.substring(1, 2));
        int max = grid.getRowCount() - 2;
        // Checks if the number is within the bounds 
        if (coordX < 1 || coordX > max || 
            coordY < 1 || coordY > max)
        {
            // Out of bounds
            return false; 
        }
        return true;
    }
    
    /**
     * Helper method for checking if a board is valid.
     * @param splited the board to be checked splited into strings
     * @param maxChar the maximum number of chars 
     * @return the results of the check
     */
    private boolean[] checkFlags(String[] splited, int maxChar)
    {
        boolean[] flags = new boolean[] {true, true, true, true};
        // Goes through string of splited coordinates
        for (String input : splited)
        {
            // Each coord must be at most 3 chars 
            if (input.length() < 2 || input.length() > maxChar )
            {
                flags[0] = false;
            }
            // Coord has exactly  chars (RED)
            else if (input.length() == maxChar )
            {
                // Checks if input contains an R
                if (!input.contains("R") && 
                    !input.contains("r"))
                {
                    flags[1] = false; 
                }
                // Checks if input has a R at the end
                if (input.charAt(2) != 'r' && 
                    input.charAt(2) != 'R')
                {
                    flags[2] = false;
                }
            }
             // Coord is normal piece
            else 
            {
                // Checks if first character is a number
                if (input.substring(0, 1).matches("[0-9]"))
                {
                    flags[flags.length - 1] = checkBound(input);
                    // If it ever false, just break immediately.
                    if (!flags[flags.length - 1])
                    {
                        break;
                    }
                }
            }
        }
        return flags;
    }
    
    /**
     * Check to see if input is a valid board.
     * @param board string input that needs to be checked
     * @return whether or not input is valid 
     */
    public boolean checkValid(String board)
    {
        StringTokenizer tokens = new StringTokenizer(board, " ");
        String[] splited;
        boolean sixCoords = false;
        boolean withinBounds = true;
        boolean noDupes = true;
        boolean correctkRedCount = true;
        boolean atLeast2 = true;
        boolean coordHasR = true;
        boolean inRightPlaceR = true;

        // Makes sure 6 coordinates are entered. 
        if (tokens.countTokens() == grid.getRowCount() - 1) 
        {
            splited = new String[tokens.countTokens()];
            int coordX = -1;
            int coordY = -1;
            int max = grid.getRowCount()-2;
            int maxChar = "abc".length();
            int index = 0;
            sixCoords = true;
            
            // While there are still coordinates to check 
            while (tokens.hasMoreTokens())
            {
                splited[index] = tokens.nextToken();
                index++;
            }

            boolean[] flags = checkFlags(splited, maxChar);
            atLeast2 = flags[0];
            coordHasR = flags[1];
            inRightPlaceR = flags[2];
            withinBounds = flags[flags.length - 1];
            flags = checkRepeatsNumkReds(atLeast2, withinBounds, splited);
            noDupes = flags[0];
            correctkRedCount = flags[1];
        }

        return sixCoords && withinBounds && 
               noDupes && correctkRedCount && 
               atLeast2 && coordHasR && inRightPlaceR;
    }

    /**
     * Clears the board of all robots. 
     */
    private void clearBoard() 
    {
        // Goes through rows
        for (int row = 0; row < grid.getRowCount(); row++)
        {
            //Goes through cols
            for (int col = 0; col < grid.getColumnCount(); col++)
            {
                grid.setSpace(row, col, kBlank);
            }
        }
    }

    /**
     * Start a new game by putting new values in the board.
     * @param board new board to be changed to 
     */
    public void newGame(String board)
    {
        clearBoard();
        resetWinLose();
        Scanner scanner = new Scanner(board);
        String location = "";
        int locationX = 0;
        int locationY = 0;
        // Goes through the string 
        while (scanner.hasNext())
        {
            location = scanner.next();
            locationX = Integer.parseInt(location.substring(0, 1));
            locationY = Integer.parseInt(location.substring(1, 2));
            // If there is an R, means red
            if (location.contains("R") || location.contains("r"))
            {
                grid.setSpace(locationX, locationY, kRed);
            }
            // It is green
            else 
            {
                grid.setSpace(locationX, locationY, kGreen);
            }
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Clears the previous trail to blanks.
     */
    private void clearTrails()
    {
        // Goes through rows
        for (int row = 0; row < grid.getRowCount(); row++)
        {
            // Goes through cols 
            for (int col = 0; col < grid.getColumnCount(); col++)
            {
                // If current square is a trail, remove it
                if (grid.getValueAt(row, col).toString().equals("piecedot") ||
                    grid.getValueAt(row, col).equals(GameBoard.kTrail))
                {
                    grid.setSpace(row, col, kBlank);
                }
            }
        }
    }

    /**
     * Checks at the end of each move, if user has won the game.
     * @param movedPiece the gamePiece to be analyzed
     */
    private void checkWinGame(Object movedPiece)
    {
        int winSpot = "abc".length(); 
        // If piece is the red robot
        if (movedPiece.equals(kRed))
        { 
            // Check if the middle red square has the red robot
            if (grid.getValueAt(winSpot, winSpot).equals(kRed)) 
            {
                winFlag = true;
            }
        }
        setChanged();
        notifyObservers(); 
    }

    /**
     * Determines if the piece we're moving is red or green.
     * @param row the x coord
     * @param col the y coord
     * @return the correct color piece at that location 
     */
    private Object selectRightPiece(int row, int col)
    {
        Object moveThisPiece = grid.getValueAt(row, col);
        Object movedPiece;
        // The piece is red 
        if (moveThisPiece.toString().equals("red") || 
            moveThisPiece.toString().equals("reddown") ||
            moveThisPiece.toString().equals("redleft") || 
            moveThisPiece.toString().equals("redright") ||
            moveThisPiece.equals(GameBoard.kRed)) 
        {   
            movedPiece = kRed;
        }
        // The piece is green 
        else 
        {
            movedPiece = kGreen;
        }
        return movedPiece;
    }

    /**
     * Helps move the gamePiece to a location.
     * @param row the x coord
     * @param col the y coord 
     * @param movedPiece the color to be moved
     * @return the direction moved
     */
    private String moveDirectionDown(int row, int col, Object movedPiece)
    {
        int in = row;
        // Go all the way up
        while(in < grid.getRowCount() - 1 && 
            grid.getValueAt(in + 1, col) == kBlank)
        {
            grid.setSpace(in, col, kTrail);
            grid.setSpace(in + 1, col, movedPiece);
            in++;
        }
        // If off board
        if (in == grid.getRowCount() - 1) 
        {
            loseFlag = true;
        }
        movedRow = in;
        movedCol = col;
        return "D";
    }
    
    /**
     * Helps move the gamePiece to a location.
     * @param row the x coord
     * @param col the y coord 
     * @param movedPiece the color to be moved
     * @return the direction moved
     */
    private String moveDirectionUp(int row, int col, Object movedPiece)
    {
        int in = row;
        // Go all the way down
        while (in > 0 && grid.getValueAt(in - 1, col) == kBlank)
        {
            grid.setSpace(in, col, kTrail);
            grid.setSpace(in - 1, col, movedPiece);
            in--;
        }
        // If off board
        if (in == 0) 
        {
            loseFlag = true;
        }
        movedRow = in;
        movedCol = col;
        return "U";
    }
    
     /**
     * Helps move the gamePiece to a location.
     * @param row the x coord
     * @param col the y coord 
     * @param movedPiece the color to be moved
     * @return the direction moved
     */
    private String moveDirectionRight(int row, int col, Object movedPiece)
    {
        int in = col;
        // Go all the way right
        while (in < grid.getColumnCount() - 1 && 
             grid.getValueAt(row, in + 1) == kBlank)
        {
            grid.setSpace(row, in, kTrail);
            grid.setSpace(row, in + 1, movedPiece);
            in++;
        }
        // If off board
        if (in == grid.getColumnCount() - 1) 
        {
            loseFlag = true;
        }
        movedRow = row;
        movedCol = in;
        return "R";
    }

    /**
     * Helps move the gamePiece to a location.
     * @param row the x coord
     * @param col the y coord 
     * @param movedPiece the color to be moved
     * @return the direction moved
     */
    private String moveDirectionLeft(int row, int col, Object movedPiece)
    {
        int in = col;
        // Go all the way left
        while (in > 0 && grid.getValueAt(row, in - 1) == kBlank)
        {
            grid.setSpace(row, in, kTrail);
            grid.setSpace(row, in - 1, movedPiece);
            in--;
        }
        // If off board
        if (in == 0) 
        {
            loseFlag = true;
        }
        movedRow = row;
        movedCol = in;
        return "L";
    }
    
    /**
     * Moves a gamepiece in the direction of keycode.
     * @param row the x coord
     * @param col the y coord
     * @param keyCode the direction key entered
     */  
    public void movePiece(int row, int col, int keyCode)
    {
        // If  game isn't won or lost yet 
        if (!loseFlag && !winFlag)
        {
            clearTrails();
            Object movedPiece = selectRightPiece(row, col);
            String newMove = Integer.toString(row) + Integer.toString(col);
            // If key entered was down or d
            if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_D)
            {
                newMove += moveDirectionDown(row, col, movedPiece);
            }
            // If key entered was up or e
            else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_E)
            {  
                newMove += moveDirectionUp(row, col, movedPiece);
            }
            // If key entered was left or s
            else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_S)
            {
                newMove += moveDirectionLeft(row, col, movedPiece);
            }
            // If key entered was right or f
            else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_F)
            {
                newMove += moveDirectionRight(row, col, movedPiece);
            }    
            moveSeqs.add(newMove);
            grid.setCurRowCol(0, 0);
            moveCount++;
            checkWinGame(movedPiece);
        }
    }

    /**
     * Inner class representing the underlying grid of the board. 
     */
    class MyTable extends AbstractTableModel
    {
        private Object[][] absTable;
        private String[] columns = {"", "", "", "", "", "", "", };
        private int curRow;
        private int curCol; 
       
        /**
         * Default constructor of the grid. 
         */
        public MyTable(int rows, int cols)
        {
            absTable = new Object[rows][cols];
            curRow = 0;
            curCol = 0; 
        }
        
        /**
         * Sets the current row and col. 
         */
        public void setCurRowCol(int row, int col)
        {
            curRow = row;
            curCol = col;
        }

        /**
         * Gets the current row.
         */
        public int getCurRow()
        {
            return curRow;
        }

        /**
         * Gets the current col.
         */
        public int getCurCol()
        {
            return curCol;
        }

        /**
         * Sets the current row and col in the grid to a desired state. 
         */
        public void setSpace(int row, int col, Object state)
        {
            absTable[row][col] = state;
        }

        /**
         * Gets the 2d grid. 
         */
        public Object[][] getTable()
        {
            return absTable;
        }

        /**
         * Gets the columns.
         */
        public String[] getColumns()
        {
            return columns;
        }

        /**
         * Gets the Object at a certain row, col. 
         */
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return absTable[rowIndex][columnIndex];
        }

        /**
         * Gets the column name at an index. 
         */
        public String getColumnName(int columnIndex)
        {
            return columns[columnIndex];
        }

        /**
         * Gets the number of columns. 
         */
        public int getColumnCount()
        {
            return columns.length;
        }

        /**
         * Gets the numner of rows. 
         */
        public int getRowCount()
        {
            return absTable.length;
        }
    }
}
