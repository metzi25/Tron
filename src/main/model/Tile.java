package main.model;

import java.awt.Color;

public class Tile 
{   
    public static final Color SAFE_COLOR = new Color(70, 70, 70);
    private Color color;
    
    /** No-args constructor */
    public Tile() 
    {
        color = Tile.SAFE_COLOR;
    }

    /**
     * Parameterized constructor
     * @param color the color of the tile
     */
    public Tile(Color color) 
    {
        this.color = color;
    }
    
    /**
     * Checks if the tile is safe
     * @return true if safe
     */
    public boolean isSafe() 
    {
        return color == SAFE_COLOR;
    }

    /** Getters and setters */
    public Color getColor() 
    {
        return color;
    }

    public void setColor(Color color) 
    {
        this.color = color;
    }
}