package main.view;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel 
{
    private final PlayerCustomizationPanel player1Panel;
    private final PlayerCustomizationPanel player2Panel;
    private final GameView parent;

    /**
     * Constructor
     * @param parent the parent object
     */
    public MainMenu(GameView parent) 
    {
        this.parent = parent;
        
        setLayout(new BorderLayout());
        setPreferredSize(parent.FRAME_DIMENSION);

        player1Panel = new PlayerCustomizationPanel(Color.BLUE);
        add(player1Panel, BorderLayout.WEST);

        player2Panel = new PlayerCustomizationPanel(Color.RED);
        add(player2Panel, BorderLayout.EAST);

        JPanel navigationMenu = new JPanel();
        Dimension buttonDimension = new Dimension(150,40);

        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(buttonDimension);
        startButton.addActionListener(e -> {
            parent.newGame(player1Panel.getPlayerName(), player1Panel.getPlayerColor(), player2Panel.getPlayerName(), player2Panel.getPlayerColor());
        });

        JButton scoreButton = new JButton("High Scores");
        scoreButton.setPreferredSize(buttonDimension);
        scoreButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) (parent.getContentPane().getLayout());
            cardLayout.show(parent.getContentPane(), "Score menu");
            parent.requestScores();
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(buttonDimension);
        exitButton.addActionListener(e -> System.exit(0));

        navigationMenu.add(startButton);
        navigationMenu.add(scoreButton);
        navigationMenu.add(exitButton);

        add(navigationMenu, BorderLayout.CENTER);
    }
}