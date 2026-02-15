package model;

import java.util.EventListener;

public interface OrbitoBoardChangedListener extends EventListener {
    void boardChanged(OrbitoBoardChangedEvent e); 
}
