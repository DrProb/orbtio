package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OrbitoBoardPanel extends JPanel {

    private final int VIRTUEL_BOARD_PANEL_WIDTH = 1000;
    private final int VIRTUEL_BOARD_PANEL_HEIGHT = 1000;

    private JLabel boardLabel;
    private Image boardImage;

    public OrbitoBoardPanel() {
        this.setLayout(null);
        boardImage = new ImageIcon(getClass().getResource("/resources/OrbitoBoard.png")).getImage();
        boardLabel = new JLabel(new ImageIcon(boardImage));
        boardLabel.setOpaque(true);
        boardLabel.setBackground(Color.RED);
        add(boardLabel);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateBoardImage();
            }
        });
    }

    private void updateBoardImage() {
        double scaleX = (double) getWidth() / VIRTUEL_BOARD_PANEL_WIDTH;
        double scaleY = (double) getHeight() / VIRTUEL_BOARD_PANEL_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        int virtualX = 0;
        int virtualY = 0;
        int virtualWidth = 1000;
        int virtualHeight = 1000;

        int realX = (int) (virtualX * scale);
        int realY = (int) (virtualY * scale);
        int realWidth = (int) (virtualWidth * scale);
        int realHeight = (int) (virtualHeight * scale);

        boardLabel.setBounds(realX, realY, realWidth, realHeight);

        Image scaledImage = boardImage.getScaledInstance(realWidth, realHeight, Image.SCALE_SMOOTH);
        boardLabel.setIcon(new ImageIcon(scaledImage));
    }
    
}
