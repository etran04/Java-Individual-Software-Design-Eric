import java.util.Locale;
import java.io.*;
import jargs.gnu.CmdLineParser;

/**
 * The main component of the application. 
 * Creates the interconnection between the model and the two views. 
 * Initalizes console, gui, or both depending on arguments passed through main.
 * 
 * @author Eric Tran
 * @version 1
 * 
 */
public class RoundupLauncher 
{
    /**
     * Helps parse in file.
     * @param defaultRdr original Reader to be used
     * @param inFile the name of the file to be read in 
     * @return the reader if it was succesfully made
     */
    private InputStreamReader parseInFile(
        InputStreamReader defaultRdr, String inFile)
    {
        InputStreamReader rdr = defaultRdr;
        // If option was entered
        if (inFile != null)
        {
            try 
            {
                rdr = new InputStreamReader(
                    new FileInputStream(new File(inFile))); 
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Couldn't find file ");
            }
        }
        return rdr;
    }

    /**
     * Helper method to parse outfiles.
     * @param defaultWtr the default writer to write to
     * @param outfile the name of the file to write to
     * @return the new writer for that file
     */
    private OutputStreamWriter parseOutFile(
        OutputStreamWriter defaultWtr, String outFile)
    {
        OutputStreamWriter wtr = defaultWtr;
        // If outfile flag was put
        if (outFile != null)
        {
            try
            {
                wtr = new OutputStreamWriter(
                    new FileOutputStream(new File(outFile)));
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Couldn't find file");
            }
        }
        return wtr;
    }

    /**
     * Helper method to help run console flag.
     * @param CmdLineParser cmdParser
     * @param fileInOption the file in input
     * @param fileOutOption the file out output
     */
    private void runConsoleFlag(
        CmdLineParser cmdParser,
        CmdLineParser.Option fileInOption, 
        CmdLineParser.Option fileOutOption)
    {
        InputStreamReader rdr = new InputStreamReader(System.in);
        OutputStreamWriter wtr = new OutputStreamWriter(System.out);
        RoundupLauncher launch = new RoundupLauncher();
        String inFile = 
            (String) cmdParser.getOptionValue(fileInOption);
        rdr = launch.parseInFile(rdr, inFile);
        String outFile = 
            (String)cmdParser.getOptionValue(fileOutOption);
        wtr = launch.parseOutFile(wtr, outFile);
                
        GameBoard board = new GameBoard(false);
        ConsoleUI console = new ConsoleUI(rdr, wtr, board); 
        board.addObserver(console);
        console.update(board, null);
        console.run();
    }
    
    /** 
    /**
     * The main function.
     * Parse the arguments and functions accordingly. 
     * @param args The arguments passed in. Console vs gui. 
     */
    public static void main(String[] args)
    {
        GameBoard board;
        RoundupLauncher launch = new RoundupLauncher();
        InputStreamReader rdr = new InputStreamReader(System.in);
        OutputStreamWriter wtr = new OutputStreamWriter(System.out);

        CmdLineParser cmdParser = new CmdLineParser();
        CmdLineParser.Option consoleOpt = 
            cmdParser.addBooleanOption('c', "-c");
        CmdLineParser.Option guiOpt = 
            cmdParser.addBooleanOption('g', "-g");
        CmdLineParser.Option fileInOption = 
            cmdParser.addStringOption('i', "infile");
        CmdLineParser.Option fileOutOption = 
            cmdParser.addStringOption('o', "outfile");
        try 
        {
            cmdParser.parse(args);
            // Console flag showed
            if (cmdParser.getOptionValue(consoleOpt) != null)
            {
                launch.runConsoleFlag(cmdParser, fileInOption, fileOutOption);
            }
            // Gui flag showed
            else if (cmdParser.getOptionValue(guiOpt) != null)
            {
                board = new GameBoard(false);
                RoundUpGUI gui = new RoundUpGUI(board);
                gui.layoutGUI();   // do the layout of widgets of the GUI
                board.addObserver(gui);
                gui.update(board, null);
            }
            // No flag or -cg showed
            else 
            {
                board = new GameBoard(true);
                RoundUpGUI gui = new RoundUpGUI(board);
                gui.layoutGUI(); 
                ConsoleUI console = new ConsoleUI(rdr, wtr, board);
                board.addObserver(gui);
                board.addObserver(console);
                gui.update(board, null);
                console.update(board, null);
                console.run();
            }
        }
        catch (CmdLineParser.IllegalOptionValueException e) 
        {
            System.out.println("ILLEGAL");
        }
        catch (CmdLineParser.UnknownOptionException c)
        {
            System.out.println("UNKNOWN");
        }
    } //end main 

}