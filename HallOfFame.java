import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;

/**
 * HallOfFame class represents the storage of all players
 * who saved their runs in the game.
 * Stores the board number, difficult, number of moves, 
 * and win sequence in a file called "halloffame.ser"
 * located in a subdirectory called "roundup".
 * 
 * @author Eric Tran
 * @version 1
 */
public class HallOfFame
{
    private ArrayList<Object[]> data;
    private String[] columnNames; 
    private File destination;
    private boolean fileExist;
    private PrintWriter out;
    private BufferedWriter bw; 
    
    private static final int kDialogWidth = 600;
    private static final int kDialogHeight = 200;

    /**
     * Default constructor for a Hall Of Fame.
     * Creates a new "halloffame.ser" file if
     * it does not exist. 
     */
    public HallOfFame()
    {
        data = new ArrayList<Object[]>();
        columnNames = new String[] {
            "Board",
            "Difficulty",
            "Solve Time",
            "# Moves",
            "Win Sequence"};

        try
        {
            destination = new File("roundup/halloffame.ser");
            fileExist = destination.exists();
            // If the file does not exist
            if (!fileExist)
            {
                destination.createNewFile(); 
            }
            // File does exist, so just l
            else 
            {
                loadFile();
            }
        }
        catch (IOException e)
        {
            System.out.println("File " + destination.getName() 
                + " could not be made or found.");
        }
    }

    /**
     * Builds an entry of the HallOfFame given information.
     * @param boardNum the board number won
     * @param difficulty the difficulty of won board
     * @param timer the time it took to win 
     * @param numMoves the number of moves it took to win
     * @param winSeq the winning set of moves
     * @return the entry for the hall of fame
     */
    public Object[] buildEntry(
        int boardNum, String difficulty, 
        String timer, int numMoves, String winSeq)
    {
        return new Object[] {boardNum, difficulty, timer, numMoves, winSeq};
    }    

    /**
     * Adds a built entry to the hall of fame. 
     * @param entry the entry to be added to the hall of fame 
     */
    public void addEntry(Object[] entry)
    {
        data.add(entry);
        saveToFile(entry);
    }

    /**
     * Gets the list of HallOfFames.
     * @return the list of hall of famers
     */
    public ArrayList<Object[]> getHallOfFamers()
    {
        return data;
    }

    /**
     * Writes an entry to the hallOfFame. 
     * @param entry the entry to be written to a file
     */
    private void saveToFile(Object[] entry)
    {
        try
        {
            bw = new BufferedWriter(new FileWriter(destination, true));
            out = new PrintWriter(bw);
            int index = 0; 
            out.println(entry[index++] + " " 
                + entry[index++] + " " 
                + entry[index++] + " " 
                + entry[index++] + " " 
                + entry[index++]);
            out.close();
        }
        catch (IOException c)
        {
            System.out.println("Can't save entry to file " 
                + destination.getName());
        }
    }

    /**
     * Deletes the file "halloffame.ser" and
     * reinitialize the hall of fame again to 0. 
     */
    public void deleteFile()
    {
        destination.delete();
        data = new ArrayList<Object[]>();
    }

    /**
     * Loads the "halloffame.ser" file to this hallOfFame. 
     * Used when game initlialzes with an exisiting "halloffame.ser" already.
     */
    private void loadFile()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(destination));
            String line;
            StringTokenizer tokens;
            Object[] entry = null;

            // While file is not empty
            while ((line = br.readLine()) != null) 
            {
                // Process the line.
                tokens = new StringTokenizer(line, " ");
                // Has boardNum, difficulty, time, moveCount, and winSeq
                if (tokens.countTokens() == columnNames.length)
                {
                    entry = buildEntry(
                        Integer.parseInt(tokens.nextToken()), 
                        tokens.nextToken().charAt(0) + "",
                        tokens.nextToken(),
                        Integer.parseInt(tokens.nextToken()),
                        tokens.nextToken());
                }
                // Its a custom board, so difficulty is blank 
                else 
                {
                    entry = buildEntry(
                        Integer.parseInt(tokens.nextToken()), 
                        " ",
                        tokens.nextToken(),
                        Integer.parseInt(tokens.nextToken()),
                        tokens.nextToken());
                }
                data.add(entry);
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println("Can't find file " 
                + destination.getName() + " to read from");
        }
    }

    /**
     * Creates the GUI Hall Of Fame. 
     */
    public void makeVisibleDialog()
    {
        JTable hallOfFameTable 
            = new JTable(
                data.toArray(
                    new Object[data.size()][data.size()]), columnNames);
                    
        hallOfFameTable.setAutoCreateRowSorter(true);
        hallOfFameTable.setPreferredScrollableViewportSize(
            hallOfFameTable.getPreferredSize());
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        hallOfFameTable.getColumn("Board").setCellRenderer( rightRenderer );  
        
        DefaultTableCellRenderer centerRenderer 
            = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(
            DefaultTableCellRenderer.CENTER);
        hallOfFameTable
            .getColumn("Difficulty").setCellRenderer( centerRenderer );  
        hallOfFameTable
            .getColumn("Solve Time").setCellRenderer( centerRenderer );
        hallOfFameTable
            .getColumn("# Moves").setCellRenderer( centerRenderer );
            
        JScrollPane scrollpane = new JScrollPane(hallOfFameTable);
        JPanel panel = new JPanel();
        panel.add(scrollpane);

        JDialog dialog = new JDialog();
        dialog.setTitle("Hall Of Fame");
        dialog.add(scrollpane);
        dialog.setResizable(true);
        dialog.setSize(kDialogWidth, kDialogHeight);
        dialog.setVisible(true);
    }
}