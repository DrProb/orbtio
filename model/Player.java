package model;

public class Player
{
    String name;
    int steinAnzahl;
    OrbitoStoneColor color;

    boolean hasWon = false;
    /**
     * Konstruktor für Objekte der Klasse spieler
     */
    Player(String name, OrbitoStoneColor color)
    {
        this.name = name;
        this.steinAnzahl = 8;
        this.color = color;
    }

    OrbitoStone getStone() {
        if (steinAnzahl > 0) {
            steinAnzahl--;
            return new OrbitoStone(color, this);
        } else {
            throw new IllegalStateException("No stones left!");
        }
    }

    void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public boolean getHasWon() {
        return hasWon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public OrbitoStoneColor getColor() {
        return color;
    }

}
