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
public class LauncherTest extends junit.framework.TestCase
{
    /**
     * Tests the regulator and makes sure it initlizes correctly. 
     */
    @Test
    public void testLauncher()
    {
        RoundupLauncher launch = new RoundupLauncher();
        String[] args = new String[]{"-g"};
        launch.main(args);
        
        /*args = new String[] {"-cg"};
        launch.main(args);  
        
        args = new String[] {"-c"};
        launch.main(args);*/
    }

}
