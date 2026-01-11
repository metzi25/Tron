package main.view;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameView extends JFrame 
{
    public final Dimension FRAME_DIMENSION  = new Dimension(480, 260);
    private final MainMenu mainMenu;
    private final GameMenu gameMenu;
    private final HighScoreMenu highScoreMenu;

    /**
     * Constructor, establishing connections
     */
    public GameView() 
    { 
        setTitle("Tron");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_DIMENSION);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("../resources/helmet.png"))).getImage());

        // create card layout
        getContentPane().setLayout(new CardLayout());

        mainMenu = new MainMenu(this);
        gameMenu = new GameMenu(this);
        highScoreMenu = new HighScoreMenu(this);

        getContentPane().add(mainMenu, "Main menu");
        getContentPane().add(gameMenu, "Game");
        getContentPane().add(highScoreMenu, "Score menu");

        setVisible(true);
    }

    /**
     * Returning to the main menu from the game
     */
    public void returnToMenu() 
    {
        ((CardLayout) (getContentPane().getLayout())).show(getContentPane(), "Main menu");
        setSize(FRAME_DIMENSION);
    }

    /**
     * Creating a new game with error handling
     * @param player1Name player 1 name
     * @param player1Color player 1 color
     * @param player2Name player 2 name      
     * @param player2Color player 2 color
     */
    public void newGame(String player1Name, Color player1Color, String player2Name, Color player2Color) 
    {
        try 
        {
            int mapSize = 15;
            gameMenu.newGame(player1Name, player1Color, player2Name, player2Color, mapSize);

        } catch (IllegalArgumentException exception) 
        {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Requesting database data and displaying it graphically
     */
    public void requestScores() 
    {
        highScoreMenu.updateTable();
    }
}