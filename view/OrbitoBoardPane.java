package view;

import javax.swing.*;

import controller.OrbitoController;

import java.awt.*;
import java.awt.event.*;

import model.OrbitoBoardChangedEvent;
import model.OrbitoGameEndedEvent;
import model.OrbitoModel;
import model.OrbitoModelListener;
import model.OrbitoStone;

public class OrbitoBoardPane extends JLayeredPane implements OrbitoModelListener {

    private final int VIRTUEL_BOARD_PANEL_WIDTH = 1000;
    private final int VIRTUEL_BOARD_PANEL_HEIGHT = 1000;

    private final int CELL_START_X = 90;
    private final int CELL_START_Y = 90;
    private final int CELL_SIZE = 205;

    private final Object LAYER_BOARD = Integer.valueOf(10);
    private final Object LAYER_STONES = Integer.valueOf(20);
    private final Object LAYER_CELLS = Integer.valueOf(30);

    private JLabel boardLabel;
    private Image boardImage;
    private OrbitoBoardCell cells[][] = new OrbitoBoardCell[4][4];
    private OrbitoStoneLabel stones[][] = new OrbitoStoneLabel[4][4];

    private OrbitoController controller;
    private OrbitoModel model;

    public OrbitoBoardPane(OrbitoController controller) {
        this.controller = controller;

        model = this.controller.getModel();
        model.addBoardChangedListener(this);

        this.setLayout(null);
        boardImage = new ImageIcon(getClass().getResource("/resources/OrbitoBoard.png")).getImage();
        boardLabel = new JLabel(new ImageIcon(boardImage));
        boardLabel.setOpaque(true);
        boardLabel.setBackground(Color.RED);
        add(boardLabel, LAYER_BOARD);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cells[i][j] = new OrbitoBoardCell(this, i, j);
                add(cells[i][j], LAYER_CELLS);
            }
        }
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateBoardImage();
            }
        });
    }

    private void updateBoardImage() {
        // you can use coordinates of a 1000x1000 pane. Scale will take care to convert
        // them
        double scaleX = (double) getWidth() / VIRTUEL_BOARD_PANEL_WIDTH;
        double scaleY = (double) getHeight() / VIRTUEL_BOARD_PANEL_HEIGHT;
        double scale = Math.min(scaleX, scaleY);

        // display boardLabel
        boardLabel.setBounds(0, 0, (int) (1000 * scale), (int) (1000 * scale));
        Image scaledImage = boardImage.getScaledInstance((int) (1000 * scale), (int) (1000 * scale),
                Image.SCALE_SMOOTH);
        boardLabel.setIcon(new ImageIcon(scaledImage));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cells[i][j].setBounds((int) ((CELL_START_X + j * CELL_SIZE) * scale),
                        (int) ((CELL_START_Y + i * CELL_SIZE) * scale), (int) (CELL_SIZE * scale),
                        (int) (CELL_SIZE * scale));
                if (stones[i][j] != null) {
                    stones[i][j].setBounds((int) ((CELL_START_X + 15 + j * CELL_SIZE) * scale),
                            (int) ((CELL_START_Y + i * CELL_SIZE) * scale), (int) (CELL_SIZE * scale),
                            (int) (CELL_SIZE * scale));
                }
            }
        }
    }

    void updateStones(OrbitoModel model) {
        OrbitoStone[][] board = model.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (stones[i][j] != null) {
                    remove(stones[i][j]);
                    stones[i][j] = null;
                }
                if (board[i][j] != null) {
                    stones[i][j] = new OrbitoStoneLabel(board[i][j].getColor());
                    add(stones[i][j], LAYER_STONES);
                }
            }
        }
    }

    @Override
    public void boardChanged(OrbitoBoardChangedEvent e) {
        updateStones((OrbitoModel) e.getSource());
        updateBoardImage();
    }

    @Override
    public void gameEnded(OrbitoGameEndedEvent e) {
        // outputGameEnded((OrbitoModel) e.getSource());
    }

}
