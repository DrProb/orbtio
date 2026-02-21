package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TurnButtonLabel extends JLabel {

    private Image buttonImage;
    OrbitoBoardPane board;

    public TurnButtonLabel(OrbitoBoardPane board) {
        this.board = board;
        buttonImage = new ImageIcon(getClass().getResource("/resources/TurnButton.png")).getImage();
        setOpaque(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                board.controller.pushOrbitoButton();
            }
        });
    }

    private void resizeImage() {
        Image scaledImage = buttonImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));
    }

}
