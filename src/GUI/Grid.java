package GUI;

import main.GameEngine;

import javax.swing.*;
import java.awt.*;

public class Grid {
    public JButton[][] button;
    private int size;
    private JPanel panel;

    public Grid(JPanel panel, int size){
        this.size = size;
        this.panel = panel;
        this.button = new JButton[size][size];

        for(Integer i = 0; i < 3; i++){
            for(Integer j = 0; j < 3; j++){
                button[i][j] = new JButton();
                button[i][j].setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 50));
                button[i][j].setForeground(Color.black);
                button[i][j].addActionListener(new GameEngine(button[i][j], i, j));
                this.panel.add(button[i][j]);
            }
        }
    }

    public void clear(){
        for(Integer i = 0; i < this.size; i++){
            for(Integer j = 0; j < this.size; j++){
                button[i][j].setText("");
                button[i][j].setForeground(Color.black);
            }
        }
    }
}
