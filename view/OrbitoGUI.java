package view;

import javax.swing.*;
import java.awt.*;
import controller.OrbitoController;

//based on Funktion.java and Bildschirm.java
public class OrbitoGUI {

    OrbitoController controller;
    JFrame frame;

    public OrbitoGUI(OrbitoController controller) {
        this.controller = controller;
        frame = startJFrame();    

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width  = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        
        int brettM = (int) (height * 0.9);
        
        //Image brett = new ImageIcon("resources/OrbitoBoard.png").getImage();
        int widthI = brettM;
        int heightI = brettM;
        addImageToJFrame("resources/OrbitoBoard.png", (width-widthI)/2, (height-heightI)/2, widthI, heightI, 10);

        frame.repaint();
    } 

    JFrame startJFrame(){
        JFrame frame = new JFrame("Orbito");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        //frame.setUndecorated(true);   // kein Rahmen, keine Buttons
        frame.setVisible(true);
        frame.setLayout(null);
        return frame;
    }
    
    JLabel addImageToJFrame (String filename, int xPosition, int yPosition, int imageWide, int imageHight, int z){
        Image imageToAdd = new ImageIcon(filename).getImage();
        imageToAdd = scaleImage(imageToAdd, imageWide, imageHight);
        ImageIcon icon = new ImageIcon(imageToAdd);
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(xPosition, yPosition,imageWide, imageHight);
        frame.getLayeredPane().add(imageLabel, Integer.valueOf(z));
        return imageLabel;
    }

    public Image scaleImage(Image originalPicture, int wideImage,int hightImage){
        //Image originalPicture = new ImageIcon(pathImage).getImage();
        System.out.println(wideImage);
        System.out.println(hightImage);
        Image scaledPicture = originalPicture.getScaledInstance(wideImage, hightImage, Image.SCALE_SMOOTH);
        return scaledPicture;
    }    

}
