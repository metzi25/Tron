package main.view;

import main.model.Direction;
import main.model.GameModel;
import main.model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Objects;

public class GameMenu extends JPanel 
{
    private final int TILE_SIZE = 40;
    private final Timer timer;
    private int time;
    private final JLabel timeLabel;
    private JLabel[][] gameArea;
    private GameModel game;
    private final ImageIcon motorSprite;
    private final ImageIcon deathSprite;
    private final GameView parent;

    /**
     * Constructor
     * @param parent the parent object
     */
    public GameMenu(GameView parent) 
    {
        this.parent = parent;

        ImageIcon motorIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("../resources/helmet.png")));
        motorSprite = new ImageIcon(motorIcon.getImage().getScaledInstance(TILE_SIZE - 5, TILE_SIZE - 5, java.awt.Image.SCALE_SMOOTH));

        ImageIcon deathIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("../resources/skull.png")));
        deathSprite = new ImageIcon(deathIcon.getImage().getScaledInstance(TILE_SIZE - 5, TILE_SIZE - 5, java.awt.Image.SCALE_SMOOTH));

        /** Player 1 controls */
        getInputMap().put(KeyStroke.getKeyStroke("W"), "Player1 Up");
        getActionMap().put("Player1 Up", new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { game.setPlayerDirection(Direction.UP,0);}
        });

        getInputMap().put(KeyStroke.getKeyStroke("D"), "Player1 Right");
        getActionMap().put("Player1 Right", new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { game.setPlayerDirection(Direction.RIGHT,0);}
        });

        getInputMap().put(KeyStroke.getKeyStroke("S"), "Player1 Down");
        getActionMap().put("Player1 Down", new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { game.setPlayerDirection(Direction.DOWN,0);}
        });

        getInputMap().put(KeyStroke.getKeyStroke("A"), "Player1 Left");
        getActionMap().put("Player1 Left", new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { game.setPlayerDirection(Direction.LEFT,0);}
        });

      
        /** Player 2 controls */
        getInputMap().put(KeyStroke.getKeyStroke("UP"), "Player2 Up");
        getActionMap().put("Player2 Up", new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { game.setPlayerDirection(Direction.UP,1);}
        });
        
        getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "Player2 Right");
        getActionMap().put("Player2 Right", new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { game.setPlayerDirection(Direction.RIGHT,1);}
        });
        
        getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "Player2 Down");
        getActionMap().put("Player2 Down", new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { game.setPlayerDirection(Direction.DOWN,1);}
        });

        getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "Player2 Left");
        getActionMap().put("Player2 Left", new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) { game.setPlayerDirection(Direction.LEFT,1);}
        });

        setLayout(new BorderLayout());

        time = 0;
        timer = new Timer(1000, new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    if (game != null) 
                    {
                        updateLabel();
                        clearPlayerIcon();

                        GameState gameState = game.doRound();
                        paintPlayerLocation();

                        if (gameState != GameState.IN_PROGRESS) 
                        {
                            timer.stop();
                            String gameEndMessage = "";
                            switch (gameState) 
                            {
                                case DRAW -> 
                                {
                                    gameEndMessage = "Draw.";
                                    paintDeadIcon(0);
                                    paintDeadIcon(1);
                                }
                                case PLAYER1WON -> 
                                {
                                    gameEndMessage = game.getPlayerName(0) + " won.";
                                    paintDeadIcon(1); // change other player icon
                                }
                                case PLAYER2WON -> 
                                {
                                    gameEndMessage = game.getPlayerName(1) + " won.";
                                    paintDeadIcon(0); // change other player icon
                                }
                            }
                            JOptionPane.showMessageDialog(GameMenu.this, gameEndMessage, "Game Over", JOptionPane.INFORMATION_MESSAGE);

                            exitGame();
                        }
                    }
                } 
                catch (SQLException exception) 
                {
                    JOptionPane.showMessageDialog(GameMenu.this, "Database error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
                    GameMenu.this.exitGame();
                }
            }
        });

        timeLabel = new JLabel("00:00", SwingConstants.CENTER);
        timeLabel.setFont(timeLabel.getFont().deriveFont(40f));
        add(timeLabel, BorderLayout.NORTH);
    }

    /**
     * Start a new game
     * @param player1Name name of player 1
     * @param player1Color color of player 1
     * @param player2Name name of player 2      
     * @param player2Color color of player 2
     * @param mapSize map size
     * @throws IllegalArgumentException if any input is invalid
     */
    public void newGame(String player1Name, Color player1Color, String player2Name, Color player2Color, int mapSize) throws IllegalArgumentException 
    {
        game = new GameModel(player1Name, player1Color, player2Name, player2Color, mapSize);

        CardLayout cardLayout = (CardLayout) (parent.getContentPane().getLayout());
        cardLayout.show(parent.getContentPane(), "Game");

        requestFocus(); 

        drawMap();
        
        timer.restart();

        timeLabel.setPreferredSize(new Dimension(mapSize * TILE_SIZE, 40));
    }

    /** End game */
    private void exitGame() 
    {
        game = null;
        timer.stop();
        time = 0;    
        timeLabel.setText("00:00");
        remove(((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER));
        parent.returnToMenu();
    }

    /** Drawing the map */
    private void drawMap() 
    {
        int mapSize = game.getMapSize();
     
        Dimension gamePanelDimension = new Dimension(mapSize * TILE_SIZE, mapSize * TILE_SIZE + 100);
  
        setPreferredSize(gamePanelDimension);
        parent.setSize(gamePanelDimension);

        JPanel gameAreaPanel = new JPanel();
        gameAreaPanel.setLayout(new GridLayout(mapSize, mapSize));
        gameAreaPanel.setPreferredSize(new Dimension(mapSize * TILE_SIZE, mapSize * TILE_SIZE));

        add(gameAreaPanel, BorderLayout.CENTER);

        gameArea = new JLabel[mapSize][mapSize];

        for (int row = 0; row < mapSize; row++) 
        {
            for (int column = 0; column < mapSize; column++) 
            {
                JLabel tile = new JLabel();
                tile.setOpaque(true);

                tile.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                tile.setBackground(game.getColor(row, column));
                tile.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 100), 1));

                gameAreaPanel.add(tile);
                gameArea[row][column] = tile;
            }
        }
        
        paintPlayerLocation();
        }

    /** Paints the corresponding tiles to the correct color */
    private void paintPlayerLocation() 
    {
        JLabel player1Location = gameArea[game.getVerticalPlayerLocation(0)][game.getHorizontalPlayerLocation(0)];
        JLabel player2Location = gameArea[game.getVerticalPlayerLocation(1)][game.getHorizontalPlayerLocation(1)];

        player1Location.setBackground(game.getPlayerColor(0));
        player2Location.setBackground(game.getPlayerColor(1));

        player1Location.setIcon(motorSprite);
        player2Location.setIcon(motorSprite);
    }

    /** Clears the player icon from the board */
    private void clearPlayerIcon() 
    {
        gameArea[game.getVerticalPlayerLocation(0)][game.getHorizontalPlayerLocation(0)].setIcon(null);
        gameArea[game.getVerticalPlayerLocation(1)][game.getHorizontalPlayerLocation(1)].setIcon(null);
    }

    /**
     * Draws the skull icon at the location of death
     * @param player player index
     */
    private void paintDeadIcon(int player) 
    {
        gameArea[game.getVerticalPlayerLocation(player)][game.getHorizontalPlayerLocation(player)].setIcon(deathSprite);
    }

    /** Updates the time label */
    private void updateLabel() 
    {
        time++;

        int seconds = time % 60;
        int minutes = time / 60;

        StringBuilder stringBuilder = new StringBuilder();

        if (minutes < 10) 
        {
            stringBuilder.append(0);
        }

        stringBuilder.append(minutes);
        stringBuilder.append(':');

        if (seconds < 10) 
        {
            stringBuilder.append(0);
        }

        stringBuilder.append(seconds);

        timeLabel.setText(stringBuilder.toString());
    }
}