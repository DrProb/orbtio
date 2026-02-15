package view;

import model.*;

public class OrbitoConsole implements OrbitoBoardChangedListener {
    
    public OrbitoConsole(OrbitoModel model) {
        model.addBoardChangedListener(this);
    }

    @Override
    public void boardChanged(OrbitoBoardChangedEvent e) {
        System.out.println("Board changed!");
        outputBoard((OrbitoModel) e.getSource());  
    }

    void outputBoard(OrbitoModel model) {
        OrbitoStone[][] board = model.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(board[i][j].getColor() == OrbitoStoneColor.WHITE ? "W " : "B ");
                }
            }
            System.out.println();
        }
        System.out.println();
    } 
}
