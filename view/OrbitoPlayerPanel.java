package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.*;

public class OrbitoPlayerPanel extends JPanel implements OrbitoModelListener {
    
    private Player player;
    private OrbitoModel model;
    private JLabel backgroundLabel;
    private JLabel nameLabel;
    private JLabel turnIndicatorLabel;
    private JLabel trophyLabel;
    private int panelWidth;
    private int panelHeight;
    
    public OrbitoPlayerPanel(Player player, OrbitoModel model) {
        this.player = player;
        this.model = model;
        
        setLayout(null);
        setOpaque(false);
        
        // Load and set background image
        String colorSuffix = player.getColor() == OrbitoStoneColor.WHITE ? "White" : "Black";
        ImageIcon backgroundIcon = new ImageIcon("resources/PlayerPanel_" + colorSuffix + ".png");
        backgroundLabel = new JLabel(backgroundIcon);
        add(backgroundLabel);
        
        // Create name label with white text
        nameLabel = new JLabel(player.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setOpaque(false);
        add(nameLabel);
        
        // Add mouse listener for name change
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String newName = JOptionPane.showInputDialog(
                    SwingUtilities.getWindowAncestor(OrbitoPlayerPanel.this),
                    "Wie heißt " + player.getName() + "?",
                    player.getName()
                );
                if (newName != null && !newName.trim().isEmpty()) {
                    player.setName(newName);
                    updatePlayerName();
                }
            }
        });
        
        // Register as listener
        model.addBoardChangedListener(this);
        
        // Initial update
        updateTurnIndicator();
    }
    
    public void updateSize(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
        
        setSize(width, height);
        
        // Scale and position background
        Image scaledBackground = ((ImageIcon)backgroundLabel.getIcon()).getImage()
            .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaledBackground));
        backgroundLabel.setBounds(0, 0, width, height);
        
        // Position name label (85% width, 73% height from original PlayerPanel)
        int nameLabelWidth = (int)(width * 0.85);
        int nameLabelHeight = (int)(height * 0.73);
        int nameLabelX = (int)(width * 0.07);
        int nameLabelY = (int)(height * 0.05);
        nameLabel.setBounds(nameLabelX, nameLabelY, nameLabelWidth, nameLabelHeight);
        
        // Update font size dynamically
        updateNameLabelFont();
        
        // Update turn indicator position if visible
        if (turnIndicatorLabel != null) {
            updateTurnIndicatorPosition();
        }
        
        // Update trophy position if visible
        if (trophyLabel != null) {
            updateTrophyPosition();
        }
        
        revalidate();
        repaint();
    }
    
    private void updateNameLabelFont() {
        String text = nameLabel.getText();
        int labelWidth = nameLabel.getWidth();
        int labelHeight = nameLabel.getHeight();
        
        if (labelWidth <= 0 || labelHeight <= 0) return;
        
        // Find appropriate font size
        for (int fontSize = 1; fontSize < 200; fontSize++) {
            Font testFont = new Font("Arial", Font.BOLD, fontSize);
            FontMetrics metrics = nameLabel.getFontMetrics(testFont);
            
            if (metrics.stringWidth(text) > labelWidth || metrics.getHeight() > labelHeight) {
                nameLabel.setFont(new Font("Arial", Font.BOLD, Math.max(1, fontSize - 1)));
                return;
            }
        }
    }
    
    private void updatePlayerName() {
        nameLabel.setText(player.getName());
        updateNameLabelFont();
        repaint();
    }
    
    private void updateTurnIndicator() {
        // Remove existing indicator
        if (turnIndicatorLabel != null) {
            remove(turnIndicatorLabel);
            turnIndicatorLabel = null;
        }
        
        // Check if it's this player's turn
        if (model.getCurrentPlayer() == player && 
            model.getStatus() != OrbitoModelStatus.GAME_ENDED_SINGLE_PAYER_WON &&
            model.getStatus() != OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON &&
            model.getStatus() != OrbitoModelStatus.GAME_ENDED_NO_STONES_LEFT) {
            
            String colorSuffix = player.getColor() == OrbitoStoneColor.WHITE ? "White" : "Black";
            ImageIcon turnIcon = new ImageIcon("resources/TurnIndicator_" + colorSuffix + ".png");
            turnIndicatorLabel = new JLabel(turnIcon);
            add(turnIndicatorLabel);
            updateTurnIndicatorPosition();
        }
        
        revalidate();
        repaint();
    }
    
    private void updateTurnIndicatorPosition() {
        if (turnIndicatorLabel == null || panelWidth == 0) return;
        
        // Position based on original PlayerPanel logic (ratio 167/207)
        double indicatorWidth = panelWidth / 4.0;
        double indicatorHeight = indicatorWidth * (167.0 / 207.0);
        
        // Scale the icon
        ImageIcon icon = (ImageIcon)turnIndicatorLabel.getIcon();
        Image scaledImage = icon.getImage().getScaledInstance(
            (int)indicatorWidth, (int)indicatorHeight, Image.SCALE_SMOOTH);
        turnIndicatorLabel.setIcon(new ImageIcon(scaledImage));
        
        // Position: for white player on right, for black player on left
        int x;
        if (player.getColor() == OrbitoStoneColor.WHITE) {
            x = (int)(panelWidth - (indicatorWidth / 2.0));
        } else {
            x = (int)(-(indicatorWidth / 2.0));
        }
        
        turnIndicatorLabel.setBounds(x, 46, (int)indicatorWidth, (int)indicatorHeight);
    }
    
    private void updateTrophy() {
        // Remove existing trophy
        if (trophyLabel != null) {
            remove(trophyLabel);
            trophyLabel = null;
        }
        
        // Check if player has won
        if (player.getHasWon()) {
            String imageName;
            
            // Check if both players won (draw)
            if (model.getStatus() == OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON) {
                String colorSuffix = player.getColor() == OrbitoStoneColor.WHITE ? "White" : "Black";
                imageName = "resources/Trophy_" + colorSuffix + ".png";
            } else {
                // Single winner gets gold trophy
                imageName = "resources/Trophy_Gold.png";
            }
            
            ImageIcon trophyIcon = new ImageIcon(imageName);
            trophyLabel = new JLabel(trophyIcon);
            add(trophyLabel);
            updateTrophyPosition();
        }
        
        revalidate();
        repaint();
    }
    
    private void updateTrophyPosition() {
        if (trophyLabel == null || panelWidth == 0 || panelHeight == 0) return;
        
        // Calculate trophy size (88% of space below panel)
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        if (frame == null) return;
        
        int availableHeight = frame.getHeight() - panelHeight;
        double trophyHeight = availableHeight * 0.88;
        double trophyWidth;
        
        // Different ratios for gold vs colored trophy
        if (model.getStatus() == OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON) {
            trophyWidth = trophyHeight * (280.0 / 1318.0);
        } else {
            trophyWidth = trophyHeight * (561.0 / 1318.0);
        }
        
        // Scale the icon
        ImageIcon icon = (ImageIcon)trophyLabel.getIcon();
        Image scaledImage = icon.getImage().getScaledInstance(
            (int)trophyWidth, (int)trophyHeight, Image.SCALE_SMOOTH);
        trophyLabel.setIcon(new ImageIcon(scaledImage));
        
        // Position below the panel
        int x;
        if (model.getStatus() == OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON) {
            x = (int)(trophyWidth / 2.0);
        } else {
            x = 0;
        }
        int y = (int)(panelHeight * 1.025);
        
        trophyLabel.setBounds(x, y, (int)trophyWidth, (int)trophyHeight);
    }
    
    @Override
    public void boardChanged(OrbitoBoardChangedEvent e) {
        updateTurnIndicator();
    }
    
    @Override
    public void gameEnded(OrbitoGameEndedEvent e) {
        updateTurnIndicator();
        updateTrophy();
    }
}