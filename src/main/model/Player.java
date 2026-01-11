package main.model;

import java.awt.Color;

public class Player 
{
    private final String name;
    private final Color color;
    private int horizontalLocation;
    private int verticalLocation;
    private Direction direction;

    /**
     * Constructor
     * @param name name
     * @param color color
     * @param horizontalLocation horizontal coordinate
     * @param verticalLocation vertical coordinate
     * @param direction direction
     */
    public Player(String name, Color color, int horizontalLocation, int verticalLocation, Direction direction) 
    {
        this.name = name;
        this.color = color;
        this.horizontalLocation = horizontalLocation;
        this.verticalLocation = verticalLocation;
        this.direction = direction;
    }

    /** Updates the player position based on the direction field */
    public void move() 
    {
        switch (direction) 
        {
            case UP -> verticalLocation--;
            case DOWN -> verticalLocation++;
            case LEFT -> horizontalLocation--;
            case RIGHT -> horizontalLocation++;
        }
    }
    
    /** Getters and Setters */
    public String getName() 
    {
        return name;
    }

    public Color getColor() 
    {
        return color;
    }

    public int getHorizontalLocation() 
    {
        return horizontalLocation;
    }

    public int getVerticalLocation() 
    {
        return verticalLocation;
    }

    public void setDirection(Direction direction) 
    {
        this.direction = direction;
    }
}