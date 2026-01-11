package main.view;

import main.database.HighScore;
import main.database.HighScores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class HighScoreMenu extends JPanel 
{
    private final DefaultTableModel tableModel;
    private final GameView parent;

    /**
     * Constructor
     * @param parent the parent object
     */
    public HighScoreMenu(GameView parent)  
    {
        this.parent = parent;

        setLayout(new BorderLayout());
        setPreferredSize(parent.FRAME_DIMENSION);

        JLabel header  = new JLabel("Best Players", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(30f));
        add(header, BorderLayout.NORTH);
    
        tableModel = new DefaultTableModel(new String[]{"Name", "Score"}, 0);

        JTable highScoreTable = new JTable(tableModel);
        highScoreTable.setDefaultEditor(Object.class, null);
        add(new JScrollPane(highScoreTable), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> parent.returnToMenu());
        add(backButton, BorderLayout.SOUTH);
    }

    /** Updates the table content */
    public void updateTable() 
    {
        tableModel.setRowCount(0);
        try 
        {
            ArrayList<HighScore> data = HighScores.instance().getTopScores();

            for (HighScore highScore : data) 
            {
                Object[] row = {highScore.name(), highScore.score()};
                tableModel.addRow(row);
            }
        } 
        catch (SQLException exception) 
        {
            JOptionPane.showMessageDialog(this, "Database error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
            parent.returnToMenu();
        }
    }
}