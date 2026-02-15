package model;

import java.util.EventListener;

public interface OrbitoModelListener extends EventListener {
    void boardChanged(OrbitoBoardChangedEvent e);
    void gameEnded(OrbitoGameEndedEvent e);
}
