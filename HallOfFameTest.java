import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * The test class GameBoardTest.
 *
 * @author  Eric Tran   
 * @version 1
 */
public class HallOfFameTest extends junit.framework.TestCase
{
    /**
     * Tests the construction and adding to a hall of fame. 
     */
    @Test
    public void testEmptyBuildAddEntry()
    {
        HallOfFame hof = new HallOfFame();
        hof.deleteFile();
        assertEquals(
            "Hall of famers should be 0", 
                0, hof.getHallOfFamers().size());
        Object[] entry = hof.buildEntry(1, "H", "0:00:00", 2, "32R");

        assertNotNull(hof.getHallOfFamers());
        assertEquals(
            "Hall of famers should be 0",
                0, hof.getHallOfFamers().size());
        assertEquals("Board number is 1", entry[0], 1);
        assertEquals("Board difficulty is 'H'", entry[1], "H");
        assertEquals("Board time is 0:00:00", entry[2], "0:00:00");
        assertEquals("Board movecount is 2", entry[3], 2);
        assertEquals("Board winseq is '32R'", entry[4], "32R");

        hof.addEntry(entry);
        assertEquals(
            "Hall of famers should be 1", 
                1, hof.getHallOfFamers().size());
                
        // Test dialog works
        hof.makeVisibleDialog();
    }
}
