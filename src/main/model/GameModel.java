package main.model;

import main.database.HighScores;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Objects;

public class GameModel 
{   
    private final Tile[][] map;
    private final Player[] players;

    /**
     * Constructor
     * @param player1Name player 1 name
     * @param player1Color player 1 color
     * @param player2Name player 2 name      
     * @param player2Color player 2 color
     * @param mapSize map size
     * @throws IllegalArgumentException if any input is invalid
     */
    public GameModel(String player1Name, Color player1Color, String player2Name, Color player2Color,  int mapSize) throws IllegalArgumentException 
    {        
        StringBuilder errorMessage = new StringBuilder();

        if (player1Name.trim().length() == 0 || player2Name.trim().length() == 0) 
        {
            errorMessage.append("Player names cannot be empty!\n");
        }

        if (Objects.equals(player1Name.trim(), player2Name.trim())) 
        {
            errorMessage.append("Player names must be unique!\n");
        }

        if (Objects.equals(player1Color, player2Color)) 
        {
            errorMessage.append("Player colors must be unique!\n");
        }

        if (player1Color.equals(Tile.SAFE_COLOR) || player2Color.equals(Tile.SAFE_COLOR)) 
        {
            errorMessage.append("RGB(70, 70, 70) color is reserved for the game.");
        }

        if (errorMessage.length() != 0) {
            throw new IllegalArgumentException(errorMessage.toString());
        }

        int fullSize = mapSize + 2;

        players = new Player[2];

        int verticalStart = fullSize / 2;

        int player1HorizontalStart = 1;
        players[0] = new Player(player1Name, player1Color, player1HorizontalStart, verticalStart, Direction.RIGHT);

        int player2HorizontalStart = fullSize - 2;
        players[1] = new Player(player2Name, player2Color, player2HorizontalStart, verticalStart, Direction.LEFT);

        // loading the map
        map = new Tile[fullSize][fullSize];

        for (int row = 0; row < fullSize; row++) 
        {
            for (int column = 0; column < fullSize; column++) 
            {
                if (row == 0 || column == 0 || column == fullSize - 1 || row == fullSize - 1)
                {
                    map[row][column] = new Tile(Color.BLACK);
                } 
                else 
                {
                    map[row][column] = new Tile();
                }
            }
        }

        int mapIndex = 1 + (int) (Math.random() * 10); 
        String mapFileName = "../resources/input" + mapIndex + ".txt";

        InputStream inputStream = getClass().getResourceAsStream(mapFileName);
        if (inputStream == null) 
        {
            throw new IllegalArgumentException("Map file not found: " + mapFileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) 
        {
            for (int row = 1; row <= mapSize; row++) 
            {
                String line = reader.readLine();
                if (line == null || line.length() < mapSize) 
                {
                    throw new IllegalArgumentException("Invalid map file: " + mapFileName);
                }
                for (int column = 1; column <= mapSize; column++) 
                {
                    char c = line.charAt(column - 1);
                    switch (c) 
                    {
                        case '1':
                            this.map[row][column].setColor(Color.BLACK);
                            break;
                        case '0':
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown character in map file (" + mapFileName + "): " + c);
                    }
                }
            }
        } 
        catch (IOException e) 
        {
            throw new IllegalArgumentException("Error while reading map file (" + mapFileName + "): " + e.getMessage(), e);
        }

        map[verticalStart][player1HorizontalStart].setColor(players[0].getColor());
        map[verticalStart][player2HorizontalStart].setColor(players[1].getColor());
    }

    /**
     * Simulates one round (1s)
     * @return
     * {@code GameState.IN_PROGRESS} if no collision
     * {@code GameState.PLAYER2WIN} if player 1 collides
     * {@code GameState.PLAYER1WIN} if player 2 collides
     * {@code GameState.DRAW} if both players collide
     * @throws SQLException database error
     */
    public GameState doRound() throws SQLException 
    {
        for (Player player : players) 
        {
            player.move();
        }

        Player player1 = players[0];
        Player player2 = players[1];

        if (player1.getHorizontalLocation() == player2.getHorizontalLocation() && player1.getVerticalLocation() == player2.getVerticalLocation()) 
        {
            return GameState.DRAW;
        }

        Tile location1 = map[player1.getVerticalLocation()][player1.getHorizontalLocation()];
        Tile location2 = map[player2.getVerticalLocation()][player2.getHorizontalLocation()];

        boolean player1Dead = !location1.isSafe();
        boolean player2Dead = !location2.isSafe();

        if (player1Dead && player2Dead) 
        {
            return GameState.DRAW;
        }

        if (player1Dead) 
        {
            HighScores.instance().insertScore(player2.getName());
            return GameState.PLAYER2WON;
        }

        if (player2Dead) 
        {
            HighScores.instance().insertScore(player1.getName());
            return GameState.PLAYER1WON;
        }

        location1.setColor(player1.getColor());
        location2.setColor(player2.getColor());

        return GameState.IN_PROGRESS;
    }

    /** Getters and Setters */
    public String getPlayerName(int playerIndex) 
    {
        return players[playerIndex].getName();
    }

    public Color getPlayerColor(int playerIndex) 
    {
        return players[playerIndex].getColor();
    }

    public int getHorizontalPlayerLocation(int playerIndex) 
    {
        return players[playerIndex].getHorizontalLocation();
    }

    public int getVerticalPlayerLocation(int playerIndex) 
    {
        return players[playerIndex].getVerticalLocation();
    }

    public void setPlayerDirection(Direction direction, int playerIndex) 
    {
        players[playerIndex].setDirection(direction);
    }

    public int getMapSize() 
    {
        return map.length;
    }

    public Color getColor(int row, int column) 
    {
        return map[row][column].getColor();
    }
}