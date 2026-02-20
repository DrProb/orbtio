package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//based on cell.java
public class OrbitoBoardCell extends JLabel {

    private int column;
    private int row;
    public boolean enabled = false;
    public boolean occupied = false;
    private boolean hovered = false;

    public OrbitoBoardCell(OrbitoBoardPanel board, int column, int row) {
        this.column = column;
        this.row = row;
        this.enabled = true;
        this.setOpaque(false);
        //this.setLayout(null);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!enabled) return;
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                
                // if (enabled) {
                //     if (bildschirm.modus == bildschirm.MODUS_MOVE) {
                //         mouseClickedModusMove(e);
                //     }
                //     else if (bildschirm.modus == bildschirm.MODUS_PLACE) {
                //         mouseClickedModusPlace(e);
                //     }
                // }
            }
            public void mouseClickedModusPlace(MouseEvent e) {
                    
                    // if (bildschirm.amZug.spielfeld.spielfeld[y][x] == null) {
                    //     hovered = false;
                    //     stein stein = new stein(bildschirm.amZug.color);
                    //     bildschirm.amZug.spielfeld.place(x, y, stein);
                    //     bildschirm.amZug.hasMoved = false;
                    //     bildschirm.amZug.beendeZug();
                    // } else {
                    //     bildschirm.moveStein(x,y);
                    // }
                    // //bildschirm.amZug.finishedPlacing = true;
                    
                
            }
            public void mouseClickedModusMove(MouseEvent e) {
                    // spielbrett spielfeld = bildschirm.amZug.spielfeld;
                    // if (y == bildschirm.moveY && x == bildschirm.moveX) { //Cancel move
                    //     bildschirm.modus = bildschirm.MODUS_PLACE;
                    // bildschirm.amZug.spielfeld.showBoard();
                    // bildschirm.startPlacing(bildschirm.amZug);
                    // return;
                    // }
                    
                    // spielfeld.spielfeld[y][x] = spielfeld.spielfeld[bildschirm.moveY][bildschirm.moveX];
                    
                    // spielfeld.spielfeld[bildschirm.moveY][bildschirm.moveX] = null;
                    // bildschirm.amZug.hasMoved = true;
                    // bildschirm.modus = bildschirm.MODUS_PLACE;
                    // bildschirm.amZug.spielfeld.showBoard();
                    // bildschirm.startPlacing(bildschirm.amZug);
                    // hovered = false;
                    // //bildschirm.amZug.finishedPlacing = true;                    
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (hovered) {
            Graphics2D g2 = (Graphics2D) g.create();

            // transparente graue Fläche
            if (true) { // bildschirm.modus == bildschirm.MODUS_MOVE) {
                g2.setColor(new Color(255, 255, 0, 80));
            } // else if ((bildschirm.modus == bildschirm.MODUS_PLACE && occupied) {
              // g2.setColor(new Color(0, 0, 150, 80));
              // }
            else {
                g2.setColor(new Color(150, 150, 150, 80));
            }
            // g2.setColor(new Color(150, 150, 150, 80));
            if (occupied) {
                g2.setColor(new Color(0, 0, 255, 80));
            }
            g2.fillRect(0, 0, getWidth(), getHeight());

            // undurchsichtige graue Border
            if (true) { // bildschirm.modus == bildschirm.MODUS_MOVE) {
                g2.setColor(new Color(255, 255, 0));
            } // else if (bildschirm.modus == bildschirm.MODUS_PLACE && occupied) {
              // g2.setColor(new Color(0, 0, 120));
              // }
            else {
                g2.setColor(new Color(120, 120, 120));
            }
            if (occupied) {
                g2.setColor(new Color(0, 0, 255, 80));
            }
            // g2.setColor(new Color(120, 120, 120)); // voll opaque
            g2.setStroke(new BasicStroke(2)); // Border-Dicke
            g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);

            g2.dispose();
        }
    }

    

}
