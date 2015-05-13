
/**
 * BoardRegulator class monitors all aspects of the game board.
 * It keeps track of all the possible boards,
 * and which one is currently being used.
 * @author Eric Tran
 * @version 1
 */
public class BoardRegulator
{
    private String[] boards;
    private String currentBoard;
    private String difficulty;
    private int currentBoardNumber;

    /**
     * Default constructor for the BoardRegualtor class.
     */
    public BoardRegulator()
    {
        boards = new String[] {
            "",
            "11 15 32R 34 51 55",
            "22R 14 31 42 44 55",
            "11 21 31R 51 15 45",
            "11 22R 31 35 51 54",
            "11 23 25 31 41R 44",
            "21 22 13R 33 42 43",
            "11 14R 31 33 34 44",
            "11 13R 15 21 45 51",
            "11 15 21 33 41R 44",
            "11 13 21 25R 31 54",
            "13 15 25R 31 44 52",
            "11 15 21 23R 45 51",
            "11 13 15 31 51 55R",
            "11R 15 41 44 53 54",
            "13 15 21R 25 52 55",
            "11R 25 41 51 54 55",
            "14 21R 34 41 45 52",
            "11 15 21 43R 45 51"
        };
        currentBoard = boards[1];   
        currentBoardNumber = 1;            
        difficulty = "EEMMEEMMMDHMDHHHEM"; 
    }

    /**
     * Changes the currentboard to a specific board number.
     * @param boardNumber Board number to be set to
     */
    public void setBoard(int boardNumber) 
    {
        this.currentBoard = boards[boardNumber];
        this.currentBoardNumber = boardNumber;
    }

    /**
     * Gets the currentBoard configuration that is currently used. 
     * @return the string representing this board
     */
    public String getBoard() 
    {
        return this.currentBoard;
    }

    /**
     * Gets the currentBoard configuration's number that is currently used. 
     * @return the current board number
     */
    public int getBoardNumber()
    {
        return this.currentBoardNumber;
    }

    /**
     * Increments the currentBoard by 1.
     * If board 18, loops back to loop 1.
     */
    public void nextBoard()
    {
        // If the board is not currently at the last board
        if (this.currentBoardNumber != boards.length - 1) 
        {
            this.currentBoardNumber++;
        }
        else 
        {
            this.currentBoardNumber = 1;
        }
        this.currentBoard = boards[this.currentBoardNumber];
    }

    /**
     * Sets the board to the custom board 0. 
     * @param board the custom board being set
     */
    public void setCustom(String board)
    {
        this.currentBoard = board;
        this.currentBoardNumber = 0;
    }

    /**
     * Gets the currentBoard's difficulty level. 
     * @return string that represents the difficulty of current board
     */
    public String getBoardDifficulty()
    {
        // If board is not custom
        if (currentBoardNumber != 0)
        {
            return difficulty.charAt(currentBoardNumber - 1) + "";    
        }
        // Board is custom
        else
        {
            return " ";
        }
    }
}