package view;

import javax.swing.*;

import model.OrbitoStoneColor;

import java.awt.*;
import java.awt.event.*;

//based on cell.java
public class OrbitoBoardCell extends JLabel {

    private int column;
    private int row;
    private OrbitoBoardCellStatus status = OrbitoBoardCellStatus.DISABLED;
    private OrbitoBoardPane board;

    private boolean hovered = false;
    private OrbitoBoardCell myself;

    public OrbitoBoardCell(OrbitoBoardPane board, int column, int row) {
        this.board = board;
        this.column = column;
        this.row = row;
        this.setOpaque(false);
        myself = this;

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (status == OrbitoBoardCellStatus.DISABLED)
                    return;
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

                System.out.println("Clicked cell " + column + ", " + row + " with status " + status);

                switch (status) {
                    case DISABLED:
                        return;
                    case PLACE_STONE:
                        board.controller.placeStone(column, row);
                        break;
                    case MOVE_STONE_DESELECT_SOURCE:
                        board.cancelMoveCell();
                        break;
                    case MOVE_STONE_SELECT_SOURCE:
                        board.setMoveSourceCell(myself);
                        break;
                    case MOVE_STONE_TARGET:
                        board.setMoveTargetCell(myself);
                        break;
                }
            }
        });

    }

    public OrbitoBoardCellStatus getStatus() {
        return status;
    }

    public void setStatus(OrbitoBoardCellStatus status) {
        this.status = status;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (status == OrbitoBoardCellStatus.DISABLED) {
            return;
        }

        if (hovered) {
            Graphics2D g2 = (Graphics2D) g.create();

            // fill cell with semi-transparent color based on status
            switch (status) {
                case PLACE_STONE:
                    g2.setColor(new Color(150, 150, 150, 80));
                    break;
                case MOVE_STONE_SELECT_SOURCE:
                    g2.setColor(new Color(0, 255, 0, 80));
                    break;
                case MOVE_STONE_DESELECT_SOURCE:
                    g2.setColor(new Color(0, 0, 255, 80));
                    break;
                case MOVE_STONE_TARGET:
                    g2.setColor(new Color(255, 255, 0, 80));
                    break;
                case DISABLED:
            }
            g2.fillRect(0, 0, getWidth(), getHeight());

            // draw opaque border based on status
            // switch (status) {
            //     case PLACE_STONE:
            //         g2.setColor(new Color(120, 120, 120));
            //         break;
            //     case MOVE_STONE_SELECT_SOURCE:
            //         g2.setColor(new Color(0, 255, 0));
            //         break;
            //     case MOVE_STONE_DESELECT_SOURCE:
            //         g2.setColor(new Color(0, 0, 255));
            //         break;
            //     case MOVE_STONE_TARGET:
            //         g2.setColor(new Color(255, 255, 0));
            //         break;
            //     case DISABLED:
            // }
            // set boarder color based on player color - better usability than status-based color for border
            if(board.model.getCurrentPlayer().getColor() == OrbitoStoneColor.WHITE) {
                g2.setColor(new Color(255, 255, 255));
            } else {
                g2.setColor(new Color(0, 0, 0));
            }
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);

            g2.dispose();
        }
    }

}
