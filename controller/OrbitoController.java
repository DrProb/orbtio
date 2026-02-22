package controller;

import model.*;
import view.*;

public class OrbitoController {

    OrbitoModel model;

    private final boolean RUN_TESTS = false;
    private final boolean RUN_TESTS_WITH_GUI = true;
    private final boolean RUN_TESTS_WITH_CONSOLE = false;
    private final int RUN_TESTS_ACTION_DELAY_MS = 500;

    public OrbitoController() {
        model = new OrbitoModel();
        if (!RUN_TESTS) {
            new OrbitoConsole(model);
            new OrbitoGUI(this);

        } else {
            if (RUN_TESTS_WITH_CONSOLE) {
                new OrbitoConsole(model);
            }
            if (RUN_TESTS_WITH_GUI) {
                new OrbitoGUI(this);
            }
            runTests();
        }
    }

    public void placeStone(int row, int column) {
        runTestsWithDelay();
        model.placeStone(row, column);
    }

    public void pushOrbitoButton() {
        runTestsWithDelay();
        model.pushOrbitoButton();
    }

    public void moveStone(int fromRow, int fromColumn, int toRow, int toColumn) {
        runTestsWithDelay();
        model.moveStone(fromRow, fromColumn, toRow, toColumn);
    }

    public void startNewGame() {
        runTestsWithDelay();
        model.initializeGame();
    }
    
    public OrbitoModel getModel() {
        return model;
    }
    
    private void runTestsWithDelay() {
        if (RUN_TESTS_ACTION_DELAY_MS > 0) {
            try {
                Thread.sleep(RUN_TESTS_ACTION_DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void runTests() {
            testWinPlayer1();
            testDraw();
            testBothWin();
    }

    void testWinPlayer1() {
 
        placeAndPush(0, 1);
        placeAndPush(1, 1);
        placeAndPush(0, 0);
        placeAndPush(2, 1);
        placeAndPush(1, 0);
        placeAndPush(2, 1);
        placeAndPush(2, 0);

        if (!testGameCondition(OrbitoModelStatus.GAME_ENDED_SINGLE_PAYER_WON, true, false) ) {
            System.out.println("[NOK] Test testWinPlayer1 failed!");
        } else {
            System.out.println("[OK] Test testWinPlayer1 passed!");
        }

        startNewGame();
    }

    private boolean testGameCondition(OrbitoModelStatus expectedStatus, boolean player1Won, boolean player2Won) {
        OrbitoModelStatus actualStatus = model.getStatus();
        if (actualStatus != expectedStatus) {
            System.out.println("Expected game status: " + expectedStatus + ", but got: " + actualStatus);
            return false;
        }
        if(model.getPlayers()[0].getHasWon() != player1Won) {
            System.out.println("Expected Player 1 win status: " + player1Won + ", but got: " + model.getPlayers()[0].getHasWon());
            return false;
        }
        if(model.getPlayers()[1].getHasWon() != player2Won) {
            System.out.println("Expected Player 2 win status: " + player2Won + ", but got: " + model.getPlayers()[1].getHasWon());
            return false;
        }   
        return true;
    }

    void testDraw() {
        for (int i = 0; i < 12; i++) {
            placeAndPush(0, 0);
        }
        for (int i = 0; i < 4; i++) {
            placeAndPush(1, 2);
        }
        for (int i = 0; i < 5; i++) {
            pushOrbitoButton();
        }
        if (!testGameCondition(OrbitoModelStatus.GAME_ENDED_NO_STONES_LEFT, false, false) ) {
            System.out.println("[NOK] Test testDraw failed!");
        } else {
            System.out.println("[OK] Test testDraw passed!");
        }

        startNewGame();
    }

    void testBothWin() {
        for (int i = 0; i < 9; i++) {
            placeAndPush(0, 0);
        }
        placeAndPush(0, 1);
        placeAndPush(1, 0);
        placeAndPush(0, 0);
        placeAndPush(1, 2);
        placeAndPush(2, 1);
        placeAndPush(1, 1);
        placeAndPush(1, 1);
        pushOrbitoButton();

        if (!testGameCondition(OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON, true, true) ) {
            System.out.println("[NOK] Test testBothWin failed!");
        } else {
            System.out.println("[OK] Test testBothWin passed!");
        }

        startNewGame();
    }

    private void placeAndPush(int x, int y) {
        placeStone(x, y);
        pushOrbitoButton();
    }
}
