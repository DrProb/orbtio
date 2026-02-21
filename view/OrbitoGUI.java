package view;

import javax.swing.*;
import java.awt.*;
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

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeFrame();
            }
        });

        model.addBoardChangedListener(this);

        frame.setVisible(true);
    }

    private void resizeFrame() {
        int boardSize = (int) (frame.getHeight() * 0.9);
        boardPanel.setBounds((frame.getWidth() - boardSize) / 2, (frame.getHeight() - boardSize) / 2, boardSize,
                boardSize);
        statusLabel.setBounds((int)((frame.getWidth() - boardSize) / 2 + boardSize*0.1), 0, boardSize, (int) (boardSize * 0.09));
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
                text = "Push the Orbito Button!";
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
                text = "Game ended in a draw! No stones left!";
                break;
            default:
                text = model.getStatus().toString();
        }
        statusLabel.setText(text);
    }

    @Override
    public void boardChanged(OrbitoBoardChangedEvent e) {
        updateStatusLabel();
    }

    @Override
    public void gameEnded(OrbitoGameEndedEvent e) {
        updateStatusLabel();
    }

}
