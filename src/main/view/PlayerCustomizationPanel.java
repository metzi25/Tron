package main.view;

import javax.swing.*;
import java.awt.*;

public class PlayerCustomizationPanel extends JPanel 
{ 
    private final JTextField playerNameInput;
    private final JLabel playerColorLabel;
    private static int playerNumber = 1;

    /**
     * Constructor.
     * @param defaultColor default color for the player
     */
    public PlayerCustomizationPanel(Color defaultColor) 
    {
        setPreferredSize(new Dimension(150, 350));

        JLabel nameLabel = new JLabel("Name", SwingConstants.CENTER);
        nameLabel.setFont(nameLabel.getFont().deriveFont(16f));
        add(nameLabel);

        playerNameInput = new JTextField("Player" + PlayerCustomizationPanel.playerNumber);
        playerNameInput.setPreferredSize(new Dimension(120, 25));
        add(this.playerNameInput);

        JLabel colorLabel = new JLabel("Color", SwingConstants.CENTER);
        colorLabel.setPreferredSize(new Dimension(150,18));
        colorLabel.setFont(colorLabel.getFont().deriveFont(16f));
        add(colorLabel);

        playerColorLabel = new JLabel();
        playerColorLabel.setOpaque(true);
        playerColorLabel.setPreferredSize(new Dimension(100,100));
        playerColorLabel.setBackground(defaultColor);
        add(playerColorLabel);

        JButton playerColorPickerButton = new JButton("Select");
        playerColorPickerButton.setPreferredSize(new Dimension(100,25));
        add(playerColorPickerButton);
        
        playerColorPickerButton.addActionListener(e -> playerColorLabel.setBackground(JColorChooser.showDialog(null,"Choose a color!", playerColorLabel.getBackground())));

        PlayerCustomizationPanel.playerNumber++;
    }

    /** Getters */
    public String getPlayerName() 
    {
        return playerNameInput.getText();
    }

    public Color getPlayerColor() 
    {
        return playerColorLabel.getBackground();
    }
}