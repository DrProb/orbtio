package view;

import javax.swing.*;

import controller.OrbitoController;

import java.awt.*;
import java.awt.event.*;

import model.OrbitoBoardChangedEvent;
import model.OrbitoGameEndedEvent;
import model.OrbitoModel;
import model.OrbitoModelListener;
import model.OrbitoModelStatus;
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
    private final Object LAYER_TURN_BUTTON = Integer.valueOf(40);

    private JLabel boardLabel;
    private JLabel turnButtonLabel;
    private Image boardImage;
    private OrbitoBoardCell cells[][] = new OrbitoBoardCell[4][4];
    private OrbitoStoneLabel stones[][] = new OrbitoStoneLabel[4][4];
    private OrbitoBoardCell moveSourceCell = null;
    private OrbitoBoardPaneStatus status = OrbitoBoardPaneStatus.NORMAL;

    OrbitoController controller;
    OrbitoModel model;

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

        turnButtonLabel = new TurnButtonLabel(this);
        add(turnButtonLabel, LAYER_TURN_BUTTON);

        updateCellStates();
        updateTurnButton();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleAndPlaceAllControls();
            }
        });
    }

    public OrbitoBoardPaneStatus getStatus() {
        return status;
    }

    private void setCellState(OrbitoBoardCell orbitoBoardCell) {
        if (status == OrbitoBoardPaneStatus.SELECT_MOVE_TARGET) {
            setCellStateSelectMoveTarget(orbitoBoardCell);
        } else {
            setCellStateNormal(orbitoBoardCell);
        }
    }

    private void setCellStateNormal(OrbitoBoardCell orbitoBoardCell) {
        OrbitoStone[][] board = model.getBoard();
        OrbitoModelStatus modelStatus = model.getStatus();

        if (modelStatus == OrbitoModelStatus.PLAYER_PLACE_STONE
                || modelStatus == OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE) {
            OrbitoStone stoneInCell = board[orbitoBoardCell.getColumn()][orbitoBoardCell.getRow()];
            if (stoneInCell == null) {
                orbitoBoardCell.setStatus(OrbitoBoardCellStatus.PLACE_STONE);
            } else if (stoneInCell.getPlayer() != model.getCurrentPlayer()
                    && emptyNeighboringCellExists(orbitoBoardCell)
                    && modelStatus == OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE) {
                orbitoBoardCell.setStatus(OrbitoBoardCellStatus.MOVE_STONE_SELECT_SOURCE);
            } else {
                orbitoBoardCell.setStatus(OrbitoBoardCellStatus.DISABLED);
            }
        } else {
            orbitoBoardCell.setStatus(OrbitoBoardCellStatus.DISABLED);
        }
    }

    private void setCellStateSelectMoveTarget(OrbitoBoardCell orbitoBoardCell) {
        OrbitoStone[][] board = model.getBoard();

        if (model.getStatus() == OrbitoModelStatus.PLAYER_PLACE_STONE
                || model.getStatus() == OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE) {
            OrbitoStone stoneInCell = board[orbitoBoardCell.getColumn()][orbitoBoardCell.getRow()];
            if (stoneInCell == null && isNeighborOfMoveSource(orbitoBoardCell)) {
                orbitoBoardCell.setStatus(OrbitoBoardCellStatus.MOVE_STONE_TARGET);
            } else if (orbitoBoardCell == moveSourceCell) {
                orbitoBoardCell.setStatus(OrbitoBoardCellStatus.MOVE_STONE_DESELECT_SOURCE);
            } else {
                orbitoBoardCell.setStatus(OrbitoBoardCellStatus.DISABLED);
            }
        } else {
            orbitoBoardCell.setStatus(OrbitoBoardCellStatus.DISABLED);
        }
    }

    private boolean isNeighborOfMoveSource(OrbitoBoardCell orbitoBoardCell) {
        return Math.abs(orbitoBoardCell.getColumn() - moveSourceCell.getColumn())
                + Math.abs(orbitoBoardCell.getRow() - moveSourceCell.getRow()) == 1;
    }

    public void scaleAndPlaceAllControls() {
        // you can use coordinates of a 1000x1000 pane. Scale will take care to convert
        // them
        double scaleX = (double) getWidth() / VIRTUEL_BOARD_PANEL_WIDTH;
        double scaleY = (double) getHeight() / VIRTUEL_BOARD_PANEL_HEIGHT;
        double scale = Math.min(scaleX, scaleY);

        //System.out.println("Board image updated with scale: " + scale);

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

        turnButtonLabel.setBounds((int) (455 * scale), (int) (450 * scale), (int) (100 * scale),
                (int) (100 * scale));
        // revalidate();
        // repaint();
    }

    void setMoveSourceCell(OrbitoBoardCell orbitoBoardCell) {
        // this is called from OrbitoBoardCell when a cell is clicked in MOVE_STONE mode
        // we need to switch to PLACE_STONE mode and remember the source cell for the
        // move
        moveSourceCell = orbitoBoardCell;
        moveSourceCell.setStatus(OrbitoBoardCellStatus.MOVE_STONE_DESELECT_SOURCE);
        moveSourceCell.repaint();
        status = OrbitoBoardPaneStatus.SELECT_MOVE_TARGET;
        updateCellStates();
    }

    void setMoveTargetCell(OrbitoBoardCell targetCell) {
        OrbitoBoardCell sourceCell = moveSourceCell;
        status = OrbitoBoardPaneStatus.NORMAL;
        moveSourceCell = null;

        controller.moveStone(sourceCell.getColumn(), sourceCell.getRow(), targetCell.getColumn(), targetCell.getRow());
        // updateCellStates(); // will be called from boardChanged after the move is
        // executed
    }

    void cancelMoveCell() {
        status = OrbitoBoardPaneStatus.NORMAL;
        moveSourceCell.setStatus(OrbitoBoardCellStatus.MOVE_STONE_SELECT_SOURCE);
        moveSourceCell.repaint();
        moveSourceCell = null;
        updateCellStates();
    }

    private boolean emptyNeighboringCellExists(OrbitoBoardCell orbitoBoardCell) {
        OrbitoStone[][] board = model.getBoard();
        int col = orbitoBoardCell.getColumn();
        int row = orbitoBoardCell.getRow();
        // Check all 4 neighboring cells (up, down, left, right)
        if (col > 0 && board[col - 1][row] == null)
            return true;
        if (col < 3 && board[col + 1][row] == null)
            return true;
        if (row > 0 && board[col][row - 1] == null)
            return true;
        if (row < 3 && board[col][row + 1] == null)
            return true;
        return false;
    }

    private void updateCellStates() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                setCellState(cells[i][j]);
            }
        }
    }

    private void updateStones() {
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

    private void updateTurnButton() {
        if (model.getStatus() == OrbitoModelStatus.PUSH_ORBITO_BUTTON) {
            turnButtonLabel.setVisible(true);
        } else {
            turnButtonLabel.setVisible(false);
        }
    }

    @Override
    public void boardChanged(OrbitoBoardChangedEvent e) {
        updateBoardChanged();
    }

    private void updateBoardChanged() {
        updateStones();
        updateCellStates();
        updateTurnButton();
        scaleAndPlaceAllControls();
    }

    @Override
    public void gameEnded(OrbitoGameEndedEvent e) {
        updateBoardChanged();
        // outputGameEnded((OrbitoModel) e.getSource());
    }

}
