package main.database;

import java.sql.*;
import java.util.ArrayList;

public class HighScores {

    private static HighScores instance;
    private final Connection connection;

    /**
     * Constructor
     * @throws SQLException if connection with the database is incorrect
     */
    private HighScores() throws SQLException 
    {       
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost/tron?serverTimezone=UTC&user=root&password=admin");
    }

    /**
     * @return singleton instance
     * @throws SQLException if connection with the database is incorrect
     */
    public static HighScores instance() throws SQLException 
    {
        if (HighScores.instance == null) 
        {
            HighScores.instance = new HighScores();
        }
        return instance;
    }

    /**
     * Inserts a player's score into the database. If the player already has a record, increases their score by one.
     * @param playerName player name
     * @throws SQLException if connection with the database is incorrect
     */
    public void insertScore(String playerName) throws SQLException 
    {
        PreparedStatement selectPlayerID = connection.prepareStatement("SELECT id FROM highscores WHERE Name = ?");
        selectPlayerID.setString(1, playerName);

        ResultSet resultSet = selectPlayerID.executeQuery();

        if (resultSet.next()) 
        {
            int id = resultSet.getInt(1);
            PreparedStatement updateRecord = connection.prepareStatement("UPDATE highscores SET score = score + 1 WHERE id = ?");
            updateRecord.setInt(1, id);
            updateRecord.executeUpdate();
        }
        else 
        {
            PreparedStatement insertRecord = connection.prepareStatement("INSERT INTO highscores (name, score) VALUES (?, 1)");
            insertRecord.setString(1, playerName);
            insertRecord.executeUpdate();
        }
    }

    /**
     * @return An ArrayList created from records containing the top 10 scores from the database table.
     * @throws SQLException if connection with the database is incorrect
     */
    public ArrayList<HighScore> getTopScores() throws SQLException 
    {
        ArrayList<HighScore> topScores = new ArrayList<>();

        PreparedStatement selectTop = this.connection.prepareStatement("SELECT name, score FROM highscores ORDER BY score DESC LIMIT 10");
        ResultSet resultSet = selectTop.executeQuery();

        while (resultSet.next()) 
        {
            topScores.add(new HighScore(resultSet.getString(1), resultSet.getInt(2)));
        }

        return topScores;
    }
}