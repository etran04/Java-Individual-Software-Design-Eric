import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;
import java.lang.reflect.Method;

/**
 * The test class ConsoleUITest.
 *
 * @author  Eric Tran   
 * @version 1
 */
public class BoardRegulatorTest extends junit.framework.TestCase
{
    /**
     * Tests the regulator and makes sure it initlizes correctly. 
     */
    @Test
    public void testRegulator()
    {
        BoardRegulator regulate = new BoardRegulator();

        // Stats off board 1
        assertEquals("CurrentBoard should be 1", 1, regulate.getBoardNumber());
        assertEquals("Current board format ", "11 15 32R 34 51 55", 
                        regulate.getBoard());
        assertEquals("CurrentBoard diff should be E", "E", 
                        regulate.getBoardDifficulty());
        
        // Board 2
        regulate.nextBoard();
        assertEquals("CurrentBoard should be 2", 2, regulate.getBoardNumber());
        assertEquals("Current board format ", "22R 14 31 42 44 55", 
                        regulate.getBoard());
        assertEquals("CurrentBoard diff should be E", "E", 
                        regulate.getBoardDifficulty());
        
        // Last board
        regulate.setBoard(18);
        assertEquals("CurrentBoard should be 18", 18, 
                        regulate.getBoardNumber());
        assertEquals("Current board format ", "11 15 21 43R 45 51", 
                        regulate.getBoard());
        assertEquals("CurrentBoard diff should be M", "M", 
                        regulate.getBoardDifficulty());
        
        // Test loop back to 1
        regulate.nextBoard();
        assertEquals("CurrentBoard should be 1", 1, regulate.getBoardNumber());
        assertEquals("Current board format ", "11 15 32R 34 51 55", 
                        regulate.getBoard());
        assertEquals("CurrentBoard diff should be E", "E", 
                        regulate.getBoardDifficulty());
        
        // Test custom board 0
        String custom = "11 12 13 14 15 25R";
        regulate.setCustom(custom);
        assertEquals("CurrentBoard should be 0", 0, regulate.getBoardNumber());
        assertEquals("Current board format ", "11 12 13 14 15 25R", 
                        regulate.getBoard());
        assertEquals("CurrentBoard diff should be blank", " ", 
                        regulate.getBoardDifficulty());
        
        // Test loop back to 1
        regulate.nextBoard();
        assertEquals("CurrentBoard should be 1", 1, regulate.getBoardNumber());
        assertEquals("Current board format ", "11 15 32R 34 51 55", 
                        regulate.getBoard());
        assertEquals("CurrentBoard diff should be E", "E", 
                        regulate.getBoardDifficulty());
    }
}
