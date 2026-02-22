package model;

public class OrbitoModel {
    Player[] player;
    OrbitoStone[][] board;
    int currentPlayerIdx = 0;
    boolean currentPlayerHasMoved = false;
    boolean outOfStones = false;
    int outOfStonesOrbitoButtonCounter = 5;

    private java.util.List<OrbitoModelListener> modelListeners = new java.util.ArrayList<>();
    private boolean gameHasWinner = false;
    private OrbitoModelStatus status = OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE;

    public OrbitoModel() {
        this.board = new OrbitoStone[4][4];
        this.player = new Player[2];
        player[0] = new Player("Spieler 1", OrbitoStoneColor.WHITE);
        player[1] = new Player("Spieler 2", OrbitoStoneColor.BLACK);
    }

    private OrbitoModelStatus setModelStatus(OrbitoModelStatus status) {
        // System.out.println("Status changed to: " + status + " (previous: " +
        // this.status + ")");
        this.status = status;
        return status;
    }

    public OrbitoModelStatus getStatus() {
        return status;
    }

    public void addBoardChangedListener(OrbitoModelListener listener) {
        modelListeners.add(listener);
    }

    public Player getCurrentPlayer() {
        return player[currentPlayerIdx];
    }

    public boolean isOutOfStones() {
        return outOfStones;
    }

    public void setOutOfStones(boolean outOfStones) {
        this.outOfStones = outOfStones;
    }

    public int getOutOfStonesOrbitoButtonCounter() {
        return outOfStonesOrbitoButtonCounter;
    }

    public void placeStone(int row, int column) {
        if (status != OrbitoModelStatus.PLAYER_PLACE_STONE && status != OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE) {
            throw new IllegalArgumentException(
                    "Invalid status for placing stone! Expected: " + OrbitoModelStatus.PLAYER_PLACE_STONE + " or "
                            + OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE + ", Got: " + status);
        }
        if (row < 0 || row > 3 || column < 0 || column > 3 || board[row][column] != null) {
            throw new IllegalArgumentException("Invalid Position!");
        }
        if (board[row][column] != null) {
            throw new IllegalArgumentException("Cell already occupied!");
        }
        if (currentPlayerHasMoved) {
            throw new IllegalArgumentException("Current player has already moved!");
        }
        board[row][column] = getCurrentPlayer().getStone();
        currentPlayerHasMoved = true;
        setModelStatus(OrbitoModelStatus.PUSH_ORBITO_BUTTON);
        notifyBoardChanged();
    }

    public OrbitoStone[][] getBoard() {
        OrbitoStone[][] copy = new OrbitoStone[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    public void pushOrbitoButton() {
        if (status != OrbitoModelStatus.PUSH_ORBITO_BUTTON) {
            throw new IllegalArgumentException("Invalid status for pushing Orbito button! Expected: "
                    + OrbitoModelStatus.PUSH_ORBITO_BUTTON + ", Got: " + status);
        }

        if (!outOfStones && !currentPlayerHasMoved) {
            throw new IllegalArgumentException("Current player has not moved!");
        }

        OrbitoStone f = board[0][0];
        for (int i = 0; i < 3; i++) {
            OrbitoStone o = board[i + 1][0];
            board[i + 1][0] = f;
            f = o;
        }
        for (int i = 0; i < 3; i++) {
            OrbitoStone o = board[3][i + 1];
            board[3][i + 1] = f;
            f = o;
        }
        for (int i = 3; i > 0; i--) {
            OrbitoStone o = board[i - 1][3];
            board[i - 1][3] = f;
            f = o;
        }
        for (int i = 3; i > 0; i--) {
            OrbitoStone o = board[0][i - 1];
            board[0][i - 1] = f;
            f = o;
        }
        f = board[1][1];
        board[1][1] = board[1][2];
        board[1][2] = board[2][2];
        board[2][2] = board[2][1];
        board[2][1] = f;

        currentPlayerIdx = (currentPlayerIdx + 1) % 2;
        currentPlayerHasMoved = false;
        setModelStatus(OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE);
        // notifyBoardChanged();
        checkForFourInARow();

        if (gameHasWinner) {
            notifyBoardChanged();
            return;
        }

        if (!outOfStones) {
            updateOutOfStones();
        } else {
            setModelStatus(OrbitoModelStatus.PUSH_ORBITO_BUTTON);
            outOfStonesOrbitoButtonCounter--;
            if (outOfStonesOrbitoButtonCounter == 0) {
                setModelStatus(OrbitoModelStatus.GAME_ENDED_NO_STONES_LEFT);
                notifyGameEnded();
            }
        }

        notifyBoardChanged();
    }

    public void moveStone(int startRow, int startColumn, int targetRow, int targetColumn) {
        if (status != OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE) {
            throw new IllegalArgumentException("Invalid status for moving stone! Expected: "
                    + OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE + ", Got: " + status);
        }
        if (startRow < 0 || startRow > 3 || startColumn < 0 || startColumn > 3 ||
                targetRow < 0 || targetRow > 3 || targetColumn < 0 || targetColumn > 3) {
            throw new IllegalArgumentException("Invalid Position!");
        }
        if (board[startRow][startColumn] == null) {
            throw new IllegalArgumentException("No stone at start position!");
        }
        if (board[startRow][startColumn].getPlayer() == getCurrentPlayer()) {
            throw new IllegalArgumentException("You can only move opponent's stones!");
        }
        if (board[targetRow][targetColumn] != null) {
            throw new IllegalArgumentException("Target cell is already occupied!");
        }
        if (Math.abs(startRow - targetRow) + Math.abs(startColumn - targetColumn) != 1) {
            throw new IllegalArgumentException("You can only move to adjacent cells!");
        }
        setModelStatus(OrbitoModelStatus.PLAYER_PLACE_STONE);
        board[targetRow][targetColumn] = board[startRow][startColumn];
        board[startRow][startColumn] = null;
        notifyBoardChanged();
    }

    void notifyBoardChanged() {
        OrbitoBoardChangedEvent event = new OrbitoBoardChangedEvent(this);
        for (OrbitoModelListener listener : modelListeners) {
            listener.boardChanged(event);
        }
    }

    void notifyGameEnded() {
        OrbitoGameEndedEvent event = new OrbitoGameEndedEvent(this);
        for (OrbitoModelListener listener : modelListeners) {
            listener.gameEnded(event);
        }
    }

    void setWinner(Player winner) {
        gameHasWinner = true;
        winner.setHasWon(true);
    }

    void updateOutOfStones() {
        if (getCurrentPlayer().steinAnzahl == 0) {
            outOfStones = true;
            setModelStatus(OrbitoModelStatus.PUSH_ORBITO_BUTTON);
        }
    }

    void checkForFourInARow() {
        // Spiel.checkWin
        for (int i = 0; i < 4; i++) {
            if (board[i][0] == null)
                continue;

            Player c = board[i][0].getPlayer();
            boolean won = true;

            for (int j = 1; j < 4; j++) {
                if (board[i][j] == null ||
                        !board[i][j].getPlayer().equals(c)) {
                    won = false;
                    break;
                }
            }
            if (won) {
                setWinner(c);
            }
        }

        for (int i = 0; i < 4; i++) {
            if (board[0][i] == null)
                continue;

            Player c = board[0][i].getPlayer();
            boolean won = true;

            for (int j = 1; j < 4; j++) {
                if (board[j][i] == null ||
                        !board[j][i].getPlayer().equals(c)) {
                    won = false;
                    break;
                }
            }
            if (won) {
                setWinner(c);
            }
        }

        if (board[0][0] != null) {
            Player c = board[0][0].getPlayer();
            boolean won = true;

            for (int i = 1; i < 4; i++) {
                if (board[i][i] == null ||
                        !board[i][i].getPlayer().equals(c)) {
                    won = false;
                    break;
                }
            }
            if (won) {
                setWinner(c);
            }
        }

        if (board[0][3] != null) {
            Player c = board[0][3].getPlayer();
            boolean won = true;

            for (int i = 1; i < 4; i++) {
                if (board[i][3 - i] == null ||
                        !board[i][3 - i].getPlayer().equals(c)) {
                    won = false;
                    break;
                }
            }
            if (won) {
                setWinner(c);
            }
        }

        if (gameHasWinner) {
            if (player[0].getHasWon() && player[1].getHasWon() ) {
                setModelStatus(OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON);
            } else {
                setModelStatus(OrbitoModelStatus.GAME_ENDED_SINGLE_PAYER_WON);
            }
            notifyGameEnded();
        }

    }

    public Player[] getPlayers() {
        return player;
    }

    public void initializeGame() {
        this.board = new OrbitoStone[4][4];
        player[0].setHasWon(false);
        player[0].steinAnzahl =8;
        player[1].setHasWon(false);
        player[1].steinAnzahl =8;
        currentPlayerIdx = 0;
        currentPlayerHasMoved = false;
        outOfStones = false;
        gameHasWinner = false;
        outOfStonesOrbitoButtonCounter = 5;
        setModelStatus(OrbitoModelStatus.PLAYER_MOVE_OR_PLACE_STONE);
        notifyBoardChanged();
    }    
}
