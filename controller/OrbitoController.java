package controller;

import model.*;
import view.*;

public class OrbitoController {

    OrbitoModel model;

    public OrbitoController() {
        model = new OrbitoModel();
        OrbitoConsole console = new OrbitoConsole(model);
        OrbitoGUI gui = new OrbitoGUI(this);

        // testNoStonesLeft();
        // testWinPlayer1();
        // System.out.println("Test finished");

    }

    public void placeStone(int column, int row) {
        model.placeStone(column, row);
    }

    public void pushOrbitoButton() {
        model.pushOrbitoButton();
    }

    public void moveStone(int fromColumn, int fromRow, int toColumn, int toRow) {
        model.moveStone(fromColumn, fromRow, toColumn, toRow);
    }

    void testWinPlayer1() {
        placeAndPush(0, 1);
        placeAndPush(1, 1);
        placeAndPush(0, 0);
        placeAndPush(2, 1);
        placeAndPush(1, 0);
        placeAndPush(2, 1);
        placeAndPush(2, 0);
    }

    void testWinBoth() {
        for (int i = 0; i < 12; i++) {
            placeAndPush(0, 0);
        }
        for (int i = 0; i < 4; i++) {
            placeAndPush(1, 1);
        }
    }

    void testNoStonesLeft() {
        for (int i = 0; i < 12; i++) {
            placeAndPush(0, 0);
        }
        for (int i = 0; i < 4; i++) {
            placeAndPush(1, 2);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println("Final Pushing Orbito button " + (i + 1) + "/5");
            model.pushOrbitoButton();
        }
    }

    void placeAndPush(int x, int y) {
        model.placeStone(x, y);
        model.pushOrbitoButton();
    }

    public OrbitoModel getModel() {
        return model;
    }
}
