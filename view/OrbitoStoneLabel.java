package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.OrbitoStoneColor;

public class OrbitoStoneLabel extends JLabel {

    private Image stoneImage;

    public OrbitoStoneLabel(OrbitoStoneColor color) {
        if (color == OrbitoStoneColor.BLACK) {
            stoneImage = new ImageIcon(getClass().getResource("/resources/sphere_black.png")).getImage();
        } else {
             stoneImage = new ImageIcon(getClass().getResource("/resources/sphere_white.png")).getImage();
        }
        setOpaque(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });
    }
    
    private void resizeImage() {
        Image scaledImage = stoneImage.getScaledInstance((int)(getWidth()), (int)(getHeight()), Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));
    }

}    

