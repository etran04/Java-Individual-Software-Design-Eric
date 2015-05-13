import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.awt.event.KeyEvent;

/**
 * The test class GameBoardTest.
 *
 * @author  Eric Tran   
 * @version 1
 */
public class GameBoardTest extends junit.framework.TestCase
{
    /**
     * Tests the initialization of the gameBoard, 
     * along with its flags getters and setters.
     */
    @Test
    public void testInitGetters()
    {
        GameBoard board = new GameBoard(true);
        GameBoard.MyTable grid = board.getGrid();
        BoardRegulator regulator = board.getRegulator();
        HallOfFame hof = board.getHallOfFame();
        int moveCount = board.getMoveCount();
        boolean loseFlag = board.getLoseFlag();
        boolean winFlag = board.getWinFlag();
        int lastRow = board.getLastMovedRow();
        int lastCol = board.getLastMovedCol();
        int lastDir = board.getLastDir();
        ArrayList < String > moves = board.getMoves();

        assertEquals("Move count should be init to 0", 0, moveCount);
        assertFalse(winFlag);
        assertFalse(loseFlag);
        assertFalse(board.getHallFlag());
        assertFalse(board.getAlreadySaved());
        assertTrue(board.getBothViewUp());
        assertFalse(board.getAboutFlag());
        
        assertEquals("MoveRow should be init to -1", -1, lastRow);
        assertEquals("MoveCol should be init to -1", -1, lastCol);
        assertEquals("LastDir should be init to -1", -1, lastDir);
        assertEquals(
            "List of moves should be init to empty, 0", 0, moves.size());
    }

    /**
     * Tests the method used to check whether a string
     * is a valid configuration to be played on. 
     */
    @Test
    public void testCheckValid()
    {
        GameBoard board = new GameBoard(true);

        String multipleR = "11R 22r 33R 44R 55r 12";
        String dupes = "11 11 12 13 14 15 23R";
        String outOfBounds = "71 22 33 44R 55 15";
        String valid = "11 12 13 14 15 25r";
        String invalidChars1 = "1 13 14 12R 25 34";
        String invalidChars2 = "132 12R 14 15 24 22";

        assertEquals(
            "Multiple Reds should return false", 
                false, board.checkValid(multipleR));
        assertEquals(
            "Dupes should return false", 
                false, board.checkValid(dupes));
        assertEquals(
            "OutOfBounds should return false", 
                false, board.checkValid(outOfBounds));
        assertEquals(
            "InvalidChar1 should return false", 
                false, board.checkValid(invalidChars1));
        assertEquals(
            "InvalidChar2 should return false",
                false, board.checkValid(invalidChars2));
        assertEquals(
            "Valid should return true", 
                true, board.checkValid(valid));
    }

    /**
     * Tests the newGame method of the gameboard.
     */
    @Test
    public void testNewGame()
    {
        GameBoard board = new GameBoard(true);
        String valid = "11 12 13 14 15 25R";
        GameBoard.MyTable grid = board.getGrid();
        
        // Original game 1 board
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 1));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 5));
        assertEquals("Red ", GameBoard.kRed, grid.getValueAt(3, 2));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(3, 4));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(5, 1));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(5, 5));
        
        board.newGame(valid);
        // New custom board 0 of string valid
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 1));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 2));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 3));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 4));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 5));
        assertEquals("Red ", GameBoard.kRed, grid.getValueAt(2, 5));
        
        board.resetMovedRowCol();
        board.setAboutFlag(true);
        board.setHallFlag(true);
    }
    
    /**
     * Tests the movePiece method of the gameboard. 
     */
    @Test
    public void testMovePiece()
    {
        GameBoard board = new GameBoard(true);
        String valid = "11 12 13 14 15 25R";
        GameBoard.MyTable grid = board.getGrid();
        
        // test move right
        assertEquals("Red piece here", 
            GameBoard.kRed, grid.getValueAt(3, 2));
        
        board.movePiece(3, 2, KeyEvent.VK_RIGHT);
        board = new GameBoard(true);  
        grid = board.getGrid();
        board.movePiece(3, 2, KeyEvent.VK_F);
        assertEquals(
            "Red piece gone from prev spot, replace by trail", 
                GameBoard.kTrail, grid.getValueAt(3, 2));
        assertEquals(
            "Red piece moved to the right by 1", 
                GameBoard.kRed, grid.getValueAt(3, 3));
        
        board = new GameBoard(true);      
        grid = board.getGrid();
        // test move down        
        assertEquals("Red piece here", 
        GameBoard.kRed, grid.getValueAt(3, 2));
        
        board.movePiece(3, 2, KeyEvent.VK_DOWN);
        board = new GameBoard(true);  
        grid = board.getGrid();
        board.movePiece(3, 2, KeyEvent.VK_D);
        assertEquals(
            "Red piece gone from prev spot, replace by trail", 
                GameBoard.kTrail, grid.getValueAt(3, 2));
        assertEquals(
            "Red piece moved to the down", 
                GameBoard.kRed, grid.getValueAt(6, 2));
                
        board = new GameBoard(true);   
        grid = board.getGrid();
        // test move left        
        assertEquals("Red piece here", 
        GameBoard.kRed, grid.getValueAt(3, 2));
        
        board.movePiece(3, 2, KeyEvent.VK_LEFT);
        board = new GameBoard(true);  
        grid = board.getGrid();
        board.movePiece(3, 2, KeyEvent.VK_S);
        assertEquals(
            "Red piece gone from prev spot, replace by trail", 
                GameBoard.kTrail, grid.getValueAt(3, 2));
        assertEquals(
            "Red piece moved to the left", 
                GameBoard.kRed, grid.getValueAt(3, 0));
                
        board = new GameBoard(true);  
        grid = board.getGrid();
        // test move up     
        assertEquals("Red piece here", 
        GameBoard.kRed, grid.getValueAt(3, 2));
        
        board.movePiece(3, 2, KeyEvent.VK_UP);
        board = new GameBoard(true);  
        grid = board.getGrid();
        board.movePiece(3, 2, KeyEvent.VK_E);
        
        assertEquals(
            "Red piece gone from prev spot, replace by trail", 
                GameBoard.kTrail, grid.getValueAt(3, 2));
        assertEquals(
            "Red piece moved to the up", 
                GameBoard.kRed, grid.getValueAt(0, 2));        
    }
   
    /**
     * Test adding to gameboard's hall of fame.
     */
    public void testAddEntryToHall()
    {
        GameBoard board = new GameBoard(true);
        StringBuilder string = new StringBuilder();
        string.append("32R");
        board.addEntryToHallOfFame(string);
    }
}

