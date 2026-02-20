package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.OrbitoController;

//based on Funktion.java and Bildschirm.java
public class OrbitoGUI {

    OrbitoController controller;
    JFrame frame;
    OrbitoBoardPane boardPanel;

    public OrbitoGUI(OrbitoController controller) {
        this.controller = controller;
        frame = new JFrame("Orbito");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1400, 900);

        frame.setLocationRelativeTo(null);
        
        JLabel label = new JLabel("Orbito Game");
        label.setBounds(300, 10, 200, 30);
        frame.add(label);

        JLabel label2 = new JLabel("Player 1: 0 points");
        label2.setBounds(10, 10, 200, 30);
        frame.add(label2);

        boardPanel = new OrbitoBoardPane(controller);
        frame.add(boardPanel);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeFrame();
            }
        });
    
        frame.setVisible(true);
    }

    private void resizeFrame() {
        int boardSize = (int) (frame.getHeight() * 0.9);
        boardPanel.setBounds((frame.getWidth()-boardSize)/2, (frame.getHeight()-boardSize)/2, boardSize, boardSize);
    }

}
