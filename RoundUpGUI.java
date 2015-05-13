import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.util.*;

/**
 * RoundUpGUI represents one of the different views of the GameBoard.
 * It is the GUI view of the game and is another way of interacting
 * with the game. 
 * 
 * @author Eric Tran
 * @version 1
 */
public class RoundUpGUI extends JFrame implements Observer
{
    /* Main components of the GUI */
    // DO NOT CHANGE THE FOLLOWING THREE LINES
    private JTable table;
    private JLabel myStatus = null;
    private JMenuBar menuBar;
    // The underlying data model
    private GameBoard gameBoard; 
    private BoardRegulator regulator;
    // The images to be displayed
    private ImageIcon redUp;
    private ImageIcon greenUp;
    private ImageIcon background;
    private ImageIcon redDown; 
    private ImageIcon redLeft;
    private ImageIcon redRight;
    private ImageIcon trailDot;
    private ImageIcon greenDown; 
    private ImageIcon greenLeft; 
    private ImageIcon greenRight; 
    private ImageIcon deadDown; 
    private ImageIcon deadUp; 
    private ImageIcon deadRight; 
    private ImageIcon deadLeft;
    /* Image dimensions, in pixels */
    private int numCols;
    private int numRows;
    /* Keeps track of the hall of fame */
    private HallOfFame hof;

    /**
     * Represents the About message,
     * used to print when the user needs information. 
     */
    private static final String kAboutGame = 
        "Roundup Version 1.0 by Eric Tran\n" + 
        "Click on a robot then press a cursor key to move the robot.\n " + 
        "The robot will move in a straight line in the direction specified."
        + "\nThe robot can only stop when it runs into another robot," + 
        " or walks off the board.\n" + 
        "Your goal is to guide the red robot to end " +
        "positioned on the center sqaure.\n" + 
        "There are 18 puzzles that can be played in any order.\n" +
        "Android images courtesy of http://madelinix.deviantart.com";

    /**
     * Represents the message to be send,
     * when an invalid board is entered. 
     */
    private static final String kInvalid = 
        "Not a valid board configuration.\n";  

    /** Create a GUI. 
     *  Will use the System Look and Feel when possible.
     */
    public RoundUpGUI(GameBoard passIn)
    {
        super();

        this.gameBoard = passIn;
        this.regulator = gameBoard.getRegulator();  
        hof = gameBoard.getHallOfFame();

        numCols = gameBoard.getGrid().getColumnCount();
        numRows = gameBoard.getGrid().getRowCount();  

        try
        {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
    }

    /** Place all the Swing widgets in the frame of this GUI.
     */
    public void layoutGUI()
    {
        setTitle("Roundup - board " + regulator.getBoardNumber() + " " 
            + regulator.getBoardDifficulty());
        loadImages();
        gameBoard.newGame(regulator.getBoard()); 
        
        table = new ImageJTable(
            gameBoard.getGrid().getTable(), gameBoard.getGrid().getColumns());

        // Define the layout manager that will control order of components
        getContentPane().setLayout(
            new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        layoutMenus();

        // Create a panel for the status information
        JPanel statusPane = new JPanel();
        myStatus = new JLabel("Moves: " + gameBoard.getMoveCount());
        statusPane.add(myStatus);
        statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(statusPane);

        for (int col = 0; col < numCols; col++)
        {
            TableColumn column = table.getColumnModel().getColumn(col);
            column.setMaxWidth(background.getIconWidth() / numCols + 1);
            column.setMinWidth(background.getIconWidth() / numCols + 1);
        }
        // remove editor makes table not editable
        table.setDefaultEditor(Object.class, null);  
        // define how cell selection works
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        // other miscellaneous settings
        table.setRowHeight(background.getIconHeight() / numRows);
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Add a custom mouse and key listener
        table.addMouseListener(myMouseListener);
        table.addKeyListener(myKeyListener);
        // finally, add the table to the content pane
        getContentPane().add(table);

        // Make the GUI visible and available for user interaction
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    } // end layout GUI

    /**
     * Setup the menubar and submenus. 
     */
    private void layoutMenus()
    {
        menuBar = new javax.swing.JMenuBar();
        JMenu mnuGame = new JMenu("Game");
        menuBar.add(mnuGame);

        //sets up 'Restart' in the menu
        JMenuItem mnuRestart = new JMenuItem("Restart Game");
        mnuRestart.setMnemonic('R');
        mnuRestart.setAccelerator(
            KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
        mnuRestart.setActionCommand("Restart Game");        
        mnuRestart.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {             
                    background = new ImageIcon(
                        Toolkit.getDefaultToolkit()
                            .getImage(this.getClass()
                                .getResource("images/backgroundbkgd.png")));
                    gameBoard.newGame(regulator.getBoard());
                }
            });
        mnuGame.add(mnuRestart);

        //sets up 'New Game' in the menu
        JMenuItem mnuNew = new JMenuItem("New Game");
        mnuNew.setMnemonic('N');
        mnuNew.setAccelerator(
            KeyStroke.getKeyStroke('N', ActionEvent.ALT_MASK));
        mnuNew.setActionCommand("New Game");    
        mnuNew.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {        
                    background = new ImageIcon(
                        Toolkit.getDefaultToolkit()
                            .getImage(this.getClass()
                                .getResource("images/backgroundbkgd.png")));
                    regulator.nextBoard();
                    gameBoard.newGame(regulator.getBoard());  
                    setTitle("Roundup - Board " 
                        + regulator.getBoardNumber() + " " 
                        + regulator.getBoardDifficulty()); 
                }
            });
        mnuGame.add(mnuNew);

        //sets up 'select game' in the menu
        JMenuItem mnuSelect = new JMenuItem("Select Game");
        mnuSelect.setMnemonic('S');
        mnuSelect.setAccelerator(
            KeyStroke.getKeyStroke('S', ActionEvent.ALT_MASK));
        mnuSelect.setActionCommand("Select Game");    
        mnuSelect.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {           
                    String input = (String)JOptionPane.showInputDialog(
                            null,
                            "Input a game board number (1-18)",
                            "Select game number",
                            JOptionPane.PLAIN_MESSAGE);
                    if (input != null) 
                    {
                        background = new ImageIcon(
                            Toolkit.getDefaultToolkit()
                                .getImage(this.getClass()
                                    .getResource("images/backgroundbkgd.png")));
                        regulator.setBoard(Integer.parseInt(input));
                        gameBoard.newGame(regulator.getBoard());
                        setTitle("Roundup - board " + 
                            regulator.getBoardNumber() + 
                            " " + regulator.getBoardDifficulty());

                    }
                }
            });
        mnuGame.add(mnuSelect);

        //sets up 'set game' in the menu
        JMenuItem mnuSet = new JMenuItem("Set Game");
        mnuSet.setMnemonic('T');
        mnuSet.setAccelerator(
            KeyStroke.getKeyStroke('T', ActionEvent.ALT_MASK));
        mnuSet.setActionCommand("Set Game");    
        mnuSet.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {        
                    String input = (String)JOptionPane.showInputDialog(
                            null,
                            "Enter board configuration:",
                            "Set game",
                            JOptionPane.PLAIN_MESSAGE);
                    if (input != null) 
                    {
                        if (gameBoard.checkValid(input)) 
                        {
                            background = new ImageIcon(
                                Toolkit.getDefaultToolkit()
                                    .getImage(this.getClass()
                                        .getResource(
                                            "images/backgroundbkgd.png")));

                            regulator.setCustom(input);
                            gameBoard.newGame(input);
                            setTitle("Roundup - Board 0");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null,
                                kInvalid,
                                "Error",
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                }
            });
        mnuGame.add(mnuSet);

        //sets up hall of fame in the menu
        JMenuItem mnuHall = new JMenuItem("Hall of Fame");
        mnuHall.setMnemonic('H');
        mnuHall.setAccelerator(
            KeyStroke.getKeyStroke('H', ActionEvent.ALT_MASK));
        mnuHall.setActionCommand("Hall of Fame");    
        mnuHall.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {           
                    hof.makeVisibleDialog();
                }
            });
        mnuGame.add(mnuHall);

        //sets up about in the menu
        JMenuItem mnuAbout = new JMenuItem("About");
        mnuAbout.setMnemonic('A');
        mnuAbout.setAccelerator(
            KeyStroke.getKeyStroke('A', ActionEvent.ALT_MASK));
        mnuAbout.setActionCommand("About");        
        mnuAbout.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {           
                    JOptionPane.showMessageDialog(null,
                        kAboutGame,
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
        mnuGame.add(mnuAbout);

        //sets up quit in the menu
        JMenuItem mnuQuit = new JMenuItem("Quit");
        mnuQuit.setMnemonic('Q');
        mnuQuit.setAccelerator(
            KeyStroke.getKeyStroke('Q', ActionEvent.ALT_MASK));
        mnuQuit.setActionCommand("Quit");
        mnuQuit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    dispose();
                }
            });
        mnuGame.add(mnuQuit);

        JMenu mnuEdit = new JMenu("Edit");
        menuBar.add(mnuEdit);

        //sets up preferences in the menu
        JMenuItem mnuPref = new JMenuItem("Preferences");
        mnuPref.setMnemonic('F');
        mnuPref.setAccelerator(
            KeyStroke.getKeyStroke('F', ActionEvent.ALT_MASK));
        mnuPref.setActionCommand("Preferences");
        mnuPref.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {      
                    PreferencesDialog pDialog = new PreferencesDialog();
                    String response = pDialog.getResponse();       
                }
            });
        mnuEdit.add(mnuPref);

        setJMenuBar(menuBar);        
    } // end layout Menus

    /* Listener to respond to mouse clicks on the table */
    private MouseAdapter myMouseListener = new MouseAdapter()
        {
            public void mouseReleased(MouseEvent ev)
            {
                int row = (int) (ev.getPoint().getY() /
                        (background.getIconHeight() / numRows));
                int col = (int) (ev.getPoint().getX() /
                        (background.getIconWidth() / numCols));
                Object boardPiece = gameBoard.getGrid().getValueAt(row, col);

                if (boardPiece.toString().equals("red") || 
                    boardPiece.toString().equals("green") ||
                    boardPiece.toString().equals("reddown") || 
                    boardPiece.toString().equals("greendown") ||
                    boardPiece.toString().equals("redleft") || 
                    boardPiece.toString().equals("greenleft") ||
                    boardPiece.toString().equals("redright") || 
                    boardPiece.toString().equals("greenright")) 
                {
                    gameBoard.getGrid().setCurRowCol(row, col);
                }
            }
        };  // end mouse listener

    /** 
     * Listener to respond to keyboard presses on the table. 
     */
    private KeyAdapter myKeyListener = new KeyAdapter()
        {
            public void keyPressed (KeyEvent e) {
                int c = e.getKeyCode ();
                int row = gameBoard.getGrid().getCurRow();
                int col = gameBoard.getGrid().getCurCol();

                if (row != 0 && col != 0)
                {
                    gameBoard.setLastDir(c);
                    gameBoard.movePiece(row, col, c);
                }
            }
        }; // end key listener 

    /**
     * Load the images to be displayed. 
     */
    private void loadImages() 
    {
        try
        {
            // Load red pics   
            redUp = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/pieceredup.png")));
            redUp.setDescription("red");
            redDown = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass() 
                        .getResource("images/piecereddown.png")));
            redDown.setDescription("reddown");
            redLeft = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/pieceredleft.png")));
            redLeft.setDescription("redleft");
            redRight = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/pieceredright.png")));
            redRight.setDescription("redright");

            // Load green pics
            greenUp = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecegreenup.png")));
            greenUp.setDescription("green");
            greenDown = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecegreendown.png")));
            greenDown.setDescription("greendown");
            greenLeft = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecegreenleft.png")));
            greenLeft.setDescription("greenleft");
            greenRight = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecegreenright.png")));
            greenRight.setDescription("greenright");

            // Loads dead pics
            deadUp = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecedeadup.png")));
            deadUp.setDescription("deadup");
            deadDown = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecedeaddown.png")));
            deadDown.setDescription("deaddown");
            deadRight = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecedeadright.png")));
            deadRight.setDescription("deadright");
            deadLeft = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecedeadleft.png")));
            deadLeft.setDescription("deadleft");

            trailDot = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/piecedot.png")));
            trailDot.setDescription("piecedot");

            // Load background
            background = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/backgroundbkgd.png")));
        }
        catch (Exception ex)
        {
            System.out.println("Couldn't find images.");
        }
    }

    /** Our custom JTable has special features for displaying images and 
     *  a background.
     */
    private class ImageJTable extends JTable
    {
        /**
         * Default constructor for the ImageJTable.
         */
        public ImageJTable(Object[][] rowData, Object[] columnNames)
        {
            super(rowData, columnNames);
        }
        /**
         *  Tell JTable it should expect each column to contain IconImages,
         *  and should select the corresponding renderer for displaying it.
         */
        public Class getColumnClass(int column)
        {
            return ImageIcon.class;
        }
        /**
         * Allow the background to be displayed.
         */  
        public Component prepareRenderer(TableCellRenderer renderer, int row,
        int column)
        {
            Component component = super.prepareRenderer(renderer, row, column);
            // We want renderer component to be
            // transparent so background image is visible
            if (component instanceof JComponent)
            {
                ((JComponent) component).setOpaque(false);
            }
            return component;
        }

        /**
         * Override paint so as to show the table background.
         */ 
        public void paint(Graphics gfx)
        {
            // paint an image in the table background
            if (background != null)
            {
                gfx.drawImage(background.getImage(), 0, 0, null, null);
            }
            // Now let the paint do its usual work
            super.paint(gfx);
        }

    } // end ImageJTable

    /** 
     * Prints the Win message to the dialog dispplayed on top of the gui. 
     */
    private void printWin()
    {
        ArrayList < String > allMoves = gameBoard.getMoves();
        StringBuilder moves = new StringBuilder();
        for (String move : allMoves)
        {
            moves.append(move);
        }
        if (this.isFocused()) 
        {
            int n = JOptionPane.showConfirmDialog(
                    this,
                    "You won game " + regulator.getBoardNumber() + "!\n" +
                    moves.toString() + "\n" +
                    "Save your win? (y/n)",
                    "Game Won Notification",
                    JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION)
            {
                gameBoard.addEntryToHallOfFame(moves);
            }
        }
    }

     /**
     * Called whenever the model is changed. 
     * Updates the gui view. 
     */
    public void update(Observable ob, Object o)
    {   
        if (gameBoard.getBothViewUp() && gameBoard.getAboutFlag())
        {
            JOptionPane.showMessageDialog(null,
                        kAboutGame,
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
            gameBoard.setAboutFlagWithoutNotifying(false);
        }
        
        if (gameBoard.getBothViewUp() && gameBoard.getHallFlag())
        {
            hof.makeVisibleDialog();
            gameBoard.setHallFlagWithoutNotifying(false);
        }
        GameBoard.MyTable grid = gameBoard.getGrid();
        setTitle("Roundup - board " + regulator.getBoardNumber() + " " 
            + regulator.getBoardDifficulty());
        myStatus.setText("Moves: " + gameBoard.getMoveCount());
        background = new ImageIcon(
            Toolkit.getDefaultToolkit()
                .getImage(this.getClass()
                    .getResource("images/backgroundbkgd.png")));

        int lastDir = gameBoard.getLastDir();
        for (int i = 0; i < grid.getRowCount(); i++)
        {
            for (int j = 0; j < grid.getColumnCount(); j++)
            {
                if (grid.getValueAt(i, j) == (Object)GameBoard.kRed) 
                {
                    if (i == gameBoard.getLastMovedRow() 
                        && j == gameBoard.getLastMovedCol())
                    {
                        if (lastDir == KeyEvent.VK_DOWN || 
                            lastDir == KeyEvent.VK_D) 
                        {
                            if (gameBoard.getLoseFlag())
                            {
                                grid.setSpace(i, j, deadDown);
                            }
                            else
                            {
                                grid.setSpace(i, j, redDown);
                            }
                        }
                        else if (lastDir == KeyEvent.VK_RIGHT ||
                            lastDir == KeyEvent.VK_F)
                        {
                            if (gameBoard.getLoseFlag())
                            {
                                grid.setSpace(i, j, deadRight);
                            }
                            else
                            {
                                grid.setSpace(i, j, redRight);
                            }
                        }
                        else if (lastDir == KeyEvent.VK_LEFT || 
                            lastDir == KeyEvent.VK_S)
                        {
                            if (gameBoard.getLoseFlag())
                            {
                                grid.setSpace(i, j, deadLeft);
                            }
                            else
                            {
                                grid.setSpace(i, j, redLeft);
                            }
                        }
                        else if (lastDir == KeyEvent.VK_UP || 
                            lastDir == KeyEvent.VK_E)
                        {
                            if (gameBoard.getLoseFlag())
                            {
                                grid.setSpace(i, j, deadUp);
                            }
                            else
                            {
                                grid.setSpace(i, j, redUp);
                            }
                        }
                    }
                    else 
                    {
                        grid.setSpace(i, j, redUp);
                    }
                }
                else if (grid.getValueAt(i, j) == (Object)GameBoard.kGreen)
                {
                    if (i == gameBoard.getLastMovedRow() && 
                        j == gameBoard.getLastMovedCol())
                    {
                        if (lastDir == KeyEvent.VK_DOWN || 
                            lastDir == KeyEvent.VK_D)
                        {
                            if (gameBoard.getLoseFlag())
                            {
                                grid.setSpace(i, j, deadDown);
                            }
                            else
                            {
                                grid.setSpace(i, j, greenDown);
                            }
                        }
                        else if (lastDir == KeyEvent.VK_RIGHT || 
                            lastDir == KeyEvent.VK_F)
                        {
                            if (gameBoard.getLoseFlag())
                            {
                                grid.setSpace(i, j, deadRight);
                            }
                            else
                            {
                                grid.setSpace(i, j, greenRight);
                            }
                        }
                        else if (lastDir == KeyEvent.VK_LEFT || 
                            lastDir == KeyEvent.VK_S)
                        {
                            if (gameBoard.getLoseFlag())
                            {
                                grid.setSpace(i, j, deadLeft);
                            }
                            else
                            {
                                grid.setSpace(i, j, greenLeft);
                            }
                        }
                        else if (lastDir == KeyEvent.VK_UP || 
                            lastDir == KeyEvent.VK_E)
                        {
                            if (gameBoard.getLoseFlag())
                            {
                                grid.setSpace(i, j, deadUp);
                            }
                            else
                            {
                                grid.setSpace(i, j, greenUp);
                            }
                        }
                    }
                    else 
                    {
                        grid.setSpace(i, j, greenUp);
                    }
                }
                else if (grid.getValueAt(i, j) == (Object)GameBoard.kTrail)
                {
                    grid.setSpace(i, j, trailDot);
                }
            }
        }
        repaint();
        
        if (gameBoard.getLoseFlag())
        {
            background = new ImageIcon(
                Toolkit.getDefaultToolkit()
                    .getImage(this.getClass()
                        .getResource("images/losebkgd.png")));
            myStatus.setText("LOSE");
        }
        else if (gameBoard.getWinFlag())
        {
            printWin();
        }
        
        gameBoard.resetMovedRowCol();
    }
}  // end RoundUpGUI