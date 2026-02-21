package model;

public class OrbitoModel {
    Player[] player;
    OrbitoStone[][] board;
    int currentPlayerIdx = 0;
    boolean currentPlayerHasMoved = false;
    boolean outOfStones = false;
    int outOfStonesOrbitoButtonCounter = 0;
    private java.util.List<OrbitoModelListener> modelListeners = new java.util.ArrayList<>();
    private boolean gameEnded = false;
    private OrbitoModelStatus status = OrbitoModelStatus.PLAYER_PLACE_STONE;

    public OrbitoModel() {
        this.board = new OrbitoStone[4][4];
        this.player = new Player[2];
        player[0] = new Player("Spieler 1", OrbitoStoneColor.WHITE);
        player[1] = new Player("Spieler 2", OrbitoStoneColor.BLACK);
    }

    public OrbitoModelStatus getStatus() {
        return status;
    }

    public void addBoardChangedListener(OrbitoModelListener listener) {
        modelListeners.add(listener);
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

    public Player getCurrentPlayer() {
        return player[currentPlayerIdx];
    }

    public void placeStone(int x, int y) {
        if (x < 0 || x > 3 || y < 0 || y > 3 || board[x][y] != null) {
            throw new IllegalArgumentException("Invalid Position!");
        }
        if (board[x][y] != null) {
            throw new IllegalArgumentException("Cell already occupied!");
        }
        if (currentPlayerHasMoved) {
            throw new IllegalArgumentException("Current player has already moved!");
        }
        board[x][y] = getCurrentPlayer().getStone();
        currentPlayerHasMoved = true;
        status = OrbitoModelStatus.PUSH_ORBITO_BUTTON;
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
            throw new IllegalArgumentException("Invalid status for pushing Orbito button! Expected: " + OrbitoModelStatus.PUSH_ORBITO_BUTTON + ", Got: " + status);
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
        status = OrbitoModelStatus.PLAYER_PLACE_STONE;
        notifyBoardChanged();
        checkForFourInARow();

        if (!outOfStones) {
            checkForNoMoreStones();
        } else {
            outOfStonesOrbitoButtonCounter++;
            if (outOfStonesOrbitoButtonCounter == 5) {
                status = OrbitoModelStatus.GAME_ENDED_NO_STONES_LEFT;
                notifyGameEnded();
            }
        }
    }

    void setWinner(Player winner) {
        gameEnded = true;
        if(status != OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON) {
            status = OrbitoModelStatus.GAME_ENDED_SINGLE_PAYER_WON;
        } else {
            status = OrbitoModelStatus.GAME_ENDED_BOTH_PLAYERS_WON;
        }
        winner.setHasWon(true);
    }

    void checkForNoMoreStones() {
        if (getCurrentPlayer().steinAnzahl == 0) {
            outOfStones = true;
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

        if (gameEnded) {
            notifyGameEnded();
        }

    }

    public Player[] getPlayers() {
        return player;
    }
    /*
     * int[] parsePos(String pos) {
     * if (pos.length()!=2) {
     * System.out.println("Ungültige Eingabe, versuche es erneut!");
     * return null;
     * }
     * int clm = Character.toUpperCase(pos.charAt(0)) - 'A';
     * int row = pos.charAt(1) - '1';
     * if (row < 0 || row > 3 || clm < 0 || clm > 3) {
     * System.out.println("Dieses Feld existiert nicht, versuche es erneut!");
     * return null;
     * }
     * return new int[] {row,clm};
     * }
     * private int[] getDirOffset(String dir) {
     * switch (dir) {
     * case "l":
     * return new int [] {0, -1};
     * case "r":
     * return new int [] {0, 1};
     * case "u":
     * return new int [] {-1, 0};
     * case "d":
     * return new int [] {1, 0};
     * default:
     * System.out.print("Ungültige Eingabe - Versuche es erneut!");
     * return null;
     * }
     * }
     * public boolean move(String pos, String color) {
     * int[] p = parsePos(pos);
     * if (p==null) {
     * return false;
     * }
     * int row = p[0];
     * int clm = p[1];
     * if (board[row][clm] == null) {
     * System.out.println("Hier ist kein Stein! Gib ein anderes Feld ein!");
     * return false;
     * }
     * if (board[row][clm].color == color) {
     * System.out.
     * println("Du kannst nur Figuren deines Gegners bewegen! Gib ein anderes Feld ein!"
     * );
     * return false;
     * }
     * while (true) {
     * System.out.
     * println("In welche Richtung möchtest du den Stein bewegen? (l / r / u / d / exit)"
     * );
     * String dir = sc.nextLine();
     * if (dir == "exit") {
     * return false;
     * }
     * int[] o = getDirOffset(dir);
     * int rowO = o[0];
     * int clmO = o[1];
     * try {
     * if (board[row+rowO][clm+clmO] != null) {
     * System.out.println("Dieses Feld ist belegt!");
     * break;
     * }
     * board[row+rowO][clm+clmO] = board[row][clm];
     * board[row][clm] = null;
     * return true;
     * } catch (Exception ArrayIndexOutOfBoundsException) {
     * System.out.println("Du kannst die Steine nicht aus dem Feld rausverschieben!"
     * );
     * }
     * }
     * return true;
     * }
     */}
