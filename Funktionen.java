/**
 * Beschreiben Sie hier die Klasse Funktionen.
 * 
 * @author (Aaron) 
 * @funktionn (This class is used to help creating objects and images with Swing)
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Funktionen
{
    int rows = 4, cols = 4, cellSize;
    
    public Image scaleImage(Image originalPicture, int wideImage,int hightImage){
        //Image originalPicture = new ImageIcon(pathImage).getImage();
        //System.out.println(wideImage);
        //System.out.println(hightImage);
        Image scaledPicture = originalPicture.getScaledInstance(wideImage, hightImage, Image.SCALE_SMOOTH);
        return scaledPicture;
    }
    public Image processImage(String pathImage){
        Image originalPicture = new ImageIcon(pathImage).getImage();
        return originalPicture;
    }
    public JLabel addImageToJFrame (Image imageToAdd, JFrame frameToAddTo, int xPosition, int yPosition, int imageWide, int imageHight, int z){
        frameToAddTo.setLayout(null);
        //System.out.println(imageWide);
        //System.out.println(imageHight);
        imageToAdd = scaleImage(imageToAdd, imageWide, imageHight);
        ImageIcon icon = new ImageIcon(imageToAdd);
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(xPosition, yPosition,imageWide, imageHight);
        frameToAddTo.getLayeredPane().add(imageLabel, Integer.valueOf(z));
        frameToAddTo.repaint();
        return imageLabel;
    }
    //public void addClickListener(JComponent object, Runnable action){
      //  object.addMouseListener(new MouseAdapter(){
        //    @Override
          //  public void mouseClicked(MouseEvent e){
            //    action.run();
           // }
       // });
    //}
    public JFrame startJFrame(String displayedName){
        JFrame frame = new JFrame(displayedName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1535, 1000);
        frame.setLayout(null);
        frame.setVisible(true);
        return frame;
    }
    public JFrame startJFrame(String displayedName, int width, int hight){
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        JFrame frame = new JFrame(displayedName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(width, hight);
        

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);   // kein Rahmen, keine Buttons
        frame.setVisible(true);
        return frame;
    }
    public void displayText(String textToDisplay, JFrame frameToAddTo, int xPosition, int yPosition){
        JLabel label = new JLabel(textToDisplay);
        label.setBounds(xPosition, yPosition, 200, 30);
        frameToAddTo.setLayout(null);
        frameToAddTo.add(label);
        frameToAddTo.repaint();
    }
    public void test(){
        
    }
    public int calculate(JFrame frame){
        int frameWidth  = frame.getWidth();
        int frameHeight = frame.getHeight();
        int gridStartX = (frameWidth  - rows)  / 2;
        int gridStartY = (frameHeight - cols) / 2;
        return gridStartX;
        
    }
    public void addStoneWhite(int gridX, int gridY, JFrame frame){
        Image stone = new ImageIcon("Hintergrund.png").getImage();

        Image scaledStone = stone.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledStone);
        JLabel imageLabel = new JLabel(icon);
        
        int frameWidth  = frame.getWidth();
        int frameHeight = frame.getHeight();
        int gridWidth  = cols * cellSize;
        int gridHeight = rows * cellSize;

        int gridStartX = (frameWidth  - gridWidth)  / 2;
        int gridStartY = (frameHeight - gridHeight) / 2;
        int pixelX = gridStartX + gridX * cellSize;
        int pixelY = gridStartY + gridX * cellSize;

        imageLabel.setBounds(pixelX, pixelY, cellSize, cellSize);
        frame.add(imageLabel);
        frame.repaint();
    }
    
}

