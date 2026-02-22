import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class PlayerPanel extends Funktionen
{
    String color;
    Textfeld textfeld;
    JFrame frame;
    int width, height, x, y, screenWidth, screenHeight;
    JLabel amZugIndicator;
    public PlayerPanel(JFrame frame, String color, int screenWidth, int screenHeight, int x, int y, String name, spieler spieler) {
        this.color = color;
        this.frame = frame;
        this.x = x;
        this.y = y;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        Image image = new ImageIcon("player_panel_" + color + ".png").getImage();
        this.width = (int) ((screenWidth-(screenHeight*0.9)-40.0)/2.0);
        this.height = (int) ((width/1202.0)*788.0);
        //System.out.print(color + ": " + x + " " + y + " " + width + " " + height);
        JLabel panel = addImageToJFrame(image, frame, x, y, width, height, 0); 
        textfeld = new Textfeld(frame, (int) (width*0.85), (int) (height*0.73), (int) (x+(width*0.07)), (int) (y+(height*0.05)), name);
        
        panel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                //frame.setAlwaysOnTop(true);
                String updt = JOptionPane.showInputDialog(frame, "Wie heißt " + name + "?");
                //frame.setAlwaysOnTop(false);
                if (updt == null || updt.equals("")) {
                    updt = textfeld.text; 
                } else {
                    textfeld.setText(updt);
                    spieler.name = updt;
                } 
                
            }
        });
    }
    void addAmZugIndicator(String color){
        Image zug = new ImageIcon("amZug_"+color+".png").getImage();
        double zugWidth = width/4.0;
        double zugHeight = (zugWidth*(167.0/207.0));
        int m = 0;
        if (color.equals("s")) {m = width;} 
        
        amZugIndicator = addImageToJFrame(zug, frame, (int) ((x+width-(zugWidth/2.0))-m), 46, (int)zugWidth, (int) zugHeight, 4567893);
        frame.repaint();
    }
    void removeAmZugIndicator() {
        if (amZugIndicator != null) {
        frame.getLayeredPane().remove(amZugIndicator);
        amZugIndicator = null;
        frame.repaint();
    }
    }
    public void showDraw(String color) {
        Image pokal = new ImageIcon("pokal2_"+color+".png").getImage();
        double heightPokal = ((screenHeight-height)*0.88);
        double widthPokal = (heightPokal*(280.0/1318.0));
        JLabel pokalLabel = addImageToJFrame (pokal, frame, (int) (x+(widthPokal/2.0)), (int) (y+((double)height*1.025)), (int) widthPokal, (int) heightPokal, 14714424);
    }
    public void showWin() {
        Image pokal = null;
        try {
            pokal = new ImageIcon("pokal2.png").getImage();
        } catch (Exception e){
            //pokal = new ImageIcon("pokal.png").getImage();
            //System.out.println("Fehler bei getImage()");
        }
            
        
        double heightPokal = ((screenHeight-height)*0.88);
        JLabel pokalLabel = addImageToJFrame (pokal, frame, x, (int) (y+((double)height*1.025)), (int) (heightPokal*(561.0/1318.0)), (int) heightPokal, 14714424);
    }
}
