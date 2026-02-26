package view;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;
import controller.OrbitoController;
import model.*;

//based on Funktion.java and Bildschirm.java
public class OrbitoGUI implements OrbitoModelListener {

    OrbitoController controller;
    OrbitoModel model;
    JFrame frame;
    OrbitoBoardPane boardPanel;
    JLabel statusLabel;
    OrbitoPlayerPanel player1Panel;
    OrbitoPlayerPanel player2Panel;
    JLabel trophy1Label;
    JLabel trophy2Label;
    JButton newGameButton;
    ImageIcon originalNewGameIcon; // Store original icon to prevent double-scaling

    public OrbitoGUI(OrbitoController controller) {
        this.controller = controller;
        model = controller.getModel();
        frame = new JFrame("Orbito");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1400, 900);

        frame.setLocationRelativeTo(null);

        statusLabel = new JLabel("Orbito Game");
        updateStatusLabel();
        frame.add(statusLabel);

        boardPanel = new OrbitoBoardPane(controller);
        frame.add(boardPanel);

        // Create player panels
        Player[] players = model.getPlayers();
        player1Panel = new OrbitoPlayerPanel(players[0], model);
        player2Panel = new OrbitoPlayerPanel(players[1], model);
        frame.add(player1Panel);
        frame.add(player2Panel);

        // Initialize new game button (initially hidden)
        newGameButton = new JButton();
        // Use JLabel instead of JButton for better control
        JLabel tempButton = new JLabel();
        try {
            originalNewGameIcon = new ImageIcon("new.png");
            if (originalNewGameIcon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                tempButton.setIcon(originalNewGameIcon);
                tempButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                tempButton.setOpaque(false);
                tempButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        controller.startNewGame();
                    }
                });
            } else {
                tempButton.setText("New Game");
                tempButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
                tempButton.setOpaque(true);
                tempButton.setBackground(java.awt.Color.LIGHT_GRAY);
            }
        } catch (Exception ex) {
            tempButton.setText("New Game");
            tempButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
            tempButton.setOpaque(true);
            tempButton.setBackground(java.awt.Color.LIGHT_GRAY);
        }
        newGameButton = new JButton(); // Keep as JButton type but copy from label
        newGameButton.setIcon(tempButton.getIcon());
        newGameButton.setText(tempButton.getText());
        newGameButton.setFont(tempButton.getFont());
        newGameButton.setBorderPainted(false);
        newGameButton.setContentAreaFilled(false);
        newGameButton.setFocusPainted(false);
        newGameButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newGameButton.setOpaque(false);
        newGameButton.addActionListener(e -> controller.startNewGame());
        newGameButton.setVisible(false); // Start hidden, show only when game ends
        frame.getLayeredPane().add(newGameButton, JLayeredPane.MODAL_LAYER);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeFrame();
            }
        });

        model.addBoardChangedListener(this);

        frame.setVisible(true);
        
        // Initial resize to set up trophies
        resizeFrame();
    }

    private void resizeFrame() {
        int boardSize = (int) (frame.getHeight() * 0.9);
        int boardX = (frame.getWidth() - boardSize) / 2;
        int boardY = (frame.getHeight() - boardSize) / 2;
        
        boardPanel.setBounds(boardX, boardY, boardSize, boardSize);
        statusLabel.setBounds((int) (boardX + boardSize * 0.1), 0, boardSize, (int) (boardSize * 0.09));
        
        // Calculate player panel dimensions (based on original PlayerPanel)
        int panelWidth = (int) ((frame.getWidth() - boardSize - 40.0) / 2.0);
        int panelHeight = (int) ((panelWidth / 1202.0) * 788.0);
        
        // Position player 1 panel (left side, White player)
        int player1X = 20;
        int player1Y = boardY;
        player1Panel.setBounds(player1X, player1Y, panelWidth, panelHeight);
        player1Panel.updateSize(panelWidth, panelHeight);
        
        // Position player 2 panel (right side, Black player)
        int player2X = boardX + boardSize + 20;
        int player2Y = boardY;
        player2Panel.setBounds(player2X, player2Y, panelWidth, panelHeight);
        player2Panel.updateSize(panelWidth, panelHeight);
        
        // Position new game button (centered, overlaying the board)
        if (newGameButton != null && originalNewGameIcon != null) {
            // Scale the button to fit in the center of the board
            double scale = 0.25; // Scale to 25% of original size
            int buttonWidth = (int)(originalNewGameIcon.getIconWidth() * scale);
            int buttonHeight = (int)(originalNewGameIcon.getIconHeight() * scale);
            
            // Position centered on the board
            int buttonX = boardX + (boardSize - buttonWidth) / 2;
            int buttonY = boardY + (boardSize - buttonHeight) / 2;
            
            // Scale the icon from the ORIGINAL to prevent double-scaling
            java.awt.Image scaledImage = originalNewGameIcon.getImage().getScaledInstance(
                buttonWidth, buttonHeight, java.awt.Image.SCALE_SMOOTH);
            newGameButton.setIcon(new ImageIcon(scaledImage));
            
            newGameButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        } else if (newGameButton != null) {
            // Fallback for text button - center on board
            int buttonY = boardY + (boardSize - 40) / 2;
            newGameButton.setBounds(boardX + (boardSize - 150) / 2, buttonY, 150, 40);
        }
        
        // Update trophy positions if they exist
        updateTrophyPositions(player1X, player1Y, player2X, player2Y, panelWidth, panelHeight);
    }

    private void updateStatusLabel() {
        String text = "";
        switch (model.getStatus()) {
            case PLAYER_MOVE_OR_PLACE_STONE:
                text = String.format("%s: You must move an opponent's stone or place an own stone",
                        model.getCurrentPlayer().getName());
                break;
            case PLAYER_PLACE_STONE:
                text = String.format("%s: You must place a stone on the board", model.getCurrentPlayer().getName());
                break;
            case PUSH_ORBITO_BUTTON:
                if (model.isOutOfStones()) {
                    text = String.format("Board is full! Orbito Button must be finally pressed %d times!",
                            model.getOutOfStonesOrbitoButtonCounter());
                } else {
                    text = String.format("%s: You must push the Orbito Button!", model.getCurrentPlayer().getName());
                }
                // text = "Push the Orbito Button!";
                break;
            case GAME_ENDED_SINGLE_PAYER_WON:
                for (Player p : model.getPlayers()) {
                    if (p.getHasWon()) {
                        text = String.format("%s: Congratulations. You won the game! ", p.getName());
                    }
                }
                break;
            case GAME_ENDED_BOTH_PLAYERS_WON:
                text = "Both players have won!";
                break;
            case GAME_ENDED_NO_STONES_LEFT:
                text = "Game ended in a draw! No winner after 5 final pushes!";
                break;
            default:
                text = model.getStatus().toString();
        }
        statusLabel.setText(text);
    }

    @Override
    public void boardChanged(OrbitoBoardChangedEvent e) {
        updateStatusLabel();
        
        // Hide the button only if the game is NOT in an ended state
        // (to prevent hiding it after gameEnded() when there's a final board change)
        OrbitoModelStatus status = model.getStatus();
        boolean isGameEnded = (status == OrbitoModelStatus.GAME_ENDED_SINGLE_PAYER_WON ||
                              status == OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON ||
                              status == OrbitoModelStatus.GAME_ENDED_NO_STONES_LEFT);
        
        if (newGameButton != null && newGameButton.isVisible() && !isGameEnded) {
            newGameButton.setVisible(false);
        }
        
        // Remove trophies when a new game starts (game is no longer in ended state)
        if (!isGameEnded && (trophy1Label != null || trophy2Label != null)) {
            if (trophy1Label != null) {
                frame.remove(trophy1Label);
                trophy1Label = null;
            }
            if (trophy2Label != null) {
                frame.remove(trophy2Label);
                trophy2Label = null;
            }
            frame.revalidate();
            frame.repaint();
        }
    }

    @Override
    public void gameEnded(OrbitoGameEndedEvent e) {
        updateStatusLabel();
        updateTrophies();
        
        // Show the new game button when the game ends
        if (newGameButton != null) {
            newGameButton.setVisible(true);
            frame.getLayeredPane().moveToFront(newGameButton);
            newGameButton.repaint();
            frame.getLayeredPane().repaint();
        }
        frame.revalidate();
        frame.repaint();
    }
    
    private void updateTrophies() {
        // Remove existing trophies
        if (trophy1Label != null) {
            frame.remove(trophy1Label);
            trophy1Label = null;
        }
        if (trophy2Label != null) {
            frame.remove(trophy2Label);
            trophy2Label = null;
        }
        
        Player[] players = model.getPlayers();
        
        // Check if player 1 won
        if (players[0].getHasWon()) {
            String imageName;
            if (model.getStatus() == OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON) {
                imageName = "resources/Trophy_White.png";
            } else {
                imageName = "resources/Trophy_Gold.png";
            }
            trophy1Label = new JLabel(new ImageIcon(imageName));
            frame.add(trophy1Label);
        }
        
        // Check if player 2 won
        if (players[1].getHasWon()) {
            String imageName;
            if (model.getStatus() == OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON) {
                imageName = "resources/Trophy_Black.png";
            } else {
                imageName = "resources/Trophy_Gold.png";
            }
            trophy2Label = new JLabel(new ImageIcon(imageName));
            frame.add(trophy2Label);
        }
        
        // Force a resize to position the trophies
        resizeFrame();
        frame.revalidate();
        frame.repaint();
    }
    
    private void updateTrophyPositions(int player1X, int player1Y, int player2X, int player2Y, 
                                       int panelWidth, int panelHeight) {
        // Calculate available space below the panels
        int availableHeight = frame.getHeight() - (player1Y + panelHeight) - 20; // 20px margin
        
        // Use 90% of available height for trophy
        double trophyHeight = availableHeight * 0.9;
        
        // Update player 1 trophy
        if (trophy1Label != null) {
            double trophyWidth;
            if (model.getStatus() == OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON) {
                trophyWidth = trophyHeight * (280.0 / 1318.0);
            } else {
                trophyWidth = trophyHeight * (561.0 / 1318.0);
            }
            
            ImageIcon icon = (ImageIcon)trophy1Label.getIcon();
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(
                (int)trophyWidth, (int)trophyHeight, java.awt.Image.SCALE_SMOOTH);
            trophy1Label.setIcon(new ImageIcon(scaledImage));
            
            int x = player1X + (panelWidth - (int)trophyWidth) / 2;
            int y = player1Y + panelHeight + 10;
            trophy1Label.setBounds(x, y, (int)trophyWidth, (int)trophyHeight);
        }
        
        // Update player 2 trophy
        if (trophy2Label != null) {
            double trophyWidth;
            if (model.getStatus() == OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON) {
                trophyWidth = trophyHeight * (280.0 / 1318.0);
            } else {
                trophyWidth = trophyHeight * (561.0 / 1318.0);
            }
            
            ImageIcon icon = (ImageIcon)trophy2Label.getIcon();
            java.awt.Image scaledImage = icon.getImage().getScaledInstance(
                (int)trophyWidth, (int)trophyHeight, java.awt.Image.SCALE_SMOOTH);
            trophy2Label.setIcon(new ImageIcon(scaledImage));
            
            int x = player2X + (panelWidth - (int)trophyWidth) / 2;
            int y = player2Y + panelHeight + 10;
            trophy2Label.setBounds(x, y, (int)trophyWidth, (int)trophyHeight);
        }
    }

}