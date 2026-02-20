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
        Image scaledImage = stoneImage.getScaledInstance((int)(1000*0.2), (int)(1000*0.2), Image.SCALE_SMOOTH);
        //setIcon(new ImageIcon(stoneImage));
        setOpaque(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });
    }
    
    private void resizeImage() {
        Image scaledImage = stoneImage.getScaledInstance((int)(getWidth()*0.9), (int)(getHeight()*0.9), Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));
    }

}    

