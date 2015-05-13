import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * PreferencesDialog represents the GUI dialog that displays the choices
 * for different graphics. 
 * 
 * @author Eric Tran
 * @version 1 
 */
public class PreferencesDialog 
{
    private JPanel panel;
    private JRadioButton defaultButton;
    private JRadioButton sheepButton;
    private JRadioButton cowsButton;
    private JRadioButton rainbowButton;

    /**
     * Default constructor of the dialog. 
     */
    public PreferencesDialog() 
    {
        panel = new JPanel(new GridLayout(0, 1));
        defaultButton = new JRadioButton("default(robots)");    
        sheepButton = new JRadioButton("sheep");
        cowsButton = new JRadioButton("cows");
        rainbowButton = new JRadioButton("rainbow robots");
    } 

    /**
     * Makes the dialog visible and prompts the user for a choice.
     * @return string representing the preference
     */
    public String getResponse()
    {
        ButtonGroup choices = new ButtonGroup();
        choices.add(defaultButton);
        choices.add(sheepButton);
        choices.add(cowsButton);
        choices.add(rainbowButton);

        panel.add(new JLabel("Skin"));

        defaultButton.setSelected(true);
        panel.add(defaultButton);
        panel.add(sheepButton);
        panel.add(cowsButton);
        panel.add(rainbowButton);

        int choice = JOptionPane.showConfirmDialog(null,
                panel, 
                "Roundup Preferences", 
                JOptionPane.OK_CANCEL_OPTION);       

        // If the user wants the default        
        if (choice == JOptionPane.OK_OPTION && defaultButton.isSelected())
        {
            return "default";
        }
        // User wants sheep
        else if (choice == JOptionPane.OK_OPTION && sheepButton.isSelected())
        {
            return "sheep";
        }
        // User wants cow
        else if (choice == JOptionPane.OK_OPTION && cowsButton.isSelected())
        {
            return "cows";
        }
        // User wants rainbow
        else if (choice == JOptionPane.OK_OPTION && rainbowButton.isSelected())
        {
            return "rainbow";
        }
        else
        {
            return "cancel";
        }        
    }
}