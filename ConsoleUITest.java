import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/**
 * The test class ConsoleUITest.
 *
 * @author  Eric Tran   
 * @version 1
 */
public class ConsoleUITest extends junit.framework.TestCase
{
    /**
     * Tests the construction of the console. 
     * And some of its functionality. 
     */
    @Test
    public void testConstructingConsole()
    {
        GameBoard board = new GameBoard(true);
        BoardRegulator regulate = board.getRegulator();
        StringWriter output = new StringWriter();
        ConsoleUI console = new ConsoleUI(
            new StringReader("32R"), new StringWriter(), board);
        GameBoard.MyTable grid = board.getGrid();
        assertEquals("Red ", GameBoard.kRed, grid.getValueAt(3, 2));
        console.run();
        assertEquals(
            "Red piece gone from prev spot, replace by trail", 
                GameBoard.kTrail, grid.getValueAt(3, 2));
        assertEquals(
            "Red piece moved to the right by 1", 
                GameBoard.kRed, grid.getValueAt(3, 3));

        // Tests new game, should be board 2
        console = 
            new ConsoleUI(new StringReader("2"), new StringWriter(), board);
        console.run();
        grid = board.getGrid();

        assertEquals("Red ", GameBoard.kRed, grid.getValueAt(2, 2));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 4));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(3, 1));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(4, 2));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(4, 4));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(5, 5));

        // Tests select game, should be board 1
        console = 
            new ConsoleUI(new StringReader("3\n1"), new StringWriter(), board);
        console.run();
        grid = board.getGrid();

        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 1));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(1, 5));
        assertEquals("Red ", GameBoard.kRed, grid.getValueAt(3, 2));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(3, 4));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(5, 1));
        assertEquals("Green ", GameBoard.kGreen, grid.getValueAt(5, 5));

        // Tests custom game, should be board 0, valid board
        console = 
            new ConsoleUI(
                new StringReader(
                    "4\n11 12 13 14 15 25R"), new StringWriter(), board);
        console.run();
        // Test custom board 0
        regulate = board.getRegulator();
        assertEquals("CurrentBoard should be 0", 0, regulate.getBoardNumber());
        assertEquals(
            "Current board format ", "11 12 13 14 15 25R", regulate.getBoard());
        assertEquals(
            "CurrentBoard diff should be blank", 
                " ", regulate.getBoardDifficulty());

        // Tests custom game, should stay board 0, invalid board
        output = new StringWriter();
        console = 
            new ConsoleUI(
                new StringReader("4\n11 11 12 13 14 15 23R"), output, board);
        console.run();
        
        // Test custom board 0
        String errorMessage = "Set Game: Enter board configuration:\n" +
            "-- Error --\n" +
            "Not a valid board configuration.";
        assertEquals(
            "CurrentBoard should be 0", 0, regulate.getBoardNumber());
        assertEquals(
            "Current board format ", "11 12 13 14 15 25R", regulate.getBoard());
        assertEquals(
            "CurrentBoard diff should be blank", 
                " ", regulate.getBoardDifficulty());
        
        // Test lose
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("32L\n"), output, board);
        console.run();
        
        // Test Win
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("32R\ny"), output, board);
        console.run();
       
        // Test new Game
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("1\n"), output, board);
        console.run();   
        
        // Test check hall console only
        board = new GameBoard(false);
        console = 
            new ConsoleUI(
                new StringReader("5\n"), output, board);
        console.run();
        
        // Test check hall console + gui
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("5\n"), output, board);
        console.run();
        
        // Test about console only
        board = new GameBoard(false);
        console = 
            new ConsoleUI(
                new StringReader("6\n"), output, board);
        console.run();
        
        // Test about console + gui
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("6\n"), output, board);
        console.run();
        
        // Test deleting files
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("0\n"), output, board);
        console.run();
        
        // Test pref
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("8\n"), output, board);
        console.run();
        
        // Test the board prints 
        console.printBoard(); 
        console.update(board, null);
    }

    /**
     * Test the adding into the hall of fame. 
     */
    @Test 
    public void testAddToHallOfFame()
    {
        GameBoard board = new GameBoard(true);
        StringWriter output = new StringWriter();

        ConsoleUI console = 
            new ConsoleUI(new StringReader("32R\ny"), output, board);
        console.run();
        board.getHallOfFame().deleteFile();
        assertEquals(
            "Hall Of Fame should be 0", 
                0, board.getHallOfFame().getHallOfFamers().size());
                
        // Test move up
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("32U\n"), output, board);
        console.run();       
        
        // Test move down
        board = new GameBoard(true);
        console = 
            new ConsoleUI(
                new StringReader("32D\n"), output, board);
        console.run();  
        
        // Test quit console only
        board = new GameBoard(false);
        console = 
            new ConsoleUI(
                new StringReader("7\n"), output, board);
        console.run();  
        
        // Test trying to save
        board = new GameBoard(false);
        console = 
            new ConsoleUI(
                new StringReader("y\n"), output, board);
        console.run();  
        
        console.printWin();
    }
}