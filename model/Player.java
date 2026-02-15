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

    public String getName() {
        return name;
    }

    void zug() {
        // System.out.println(name + " ist am Zug."); //Ich muss noch was basteln was anzeigt wer dran ist
        
        // //while (!moved) {
        // if (!(spiel.spieler[0].steinAnzahl == 8 && spiel.spieler[1].steinAnzahl == 8)) {
                
        // } 
            //System.out.println("Möchtest du einen Stein deines Gegners bewegen? Gib seine Position ein oder schreibe 'skip'!");
            //String pos = sc.nextLine();
            //if (pos.equalsIgnoreCase("skip")) {
                //moved = true;
                //show = false;
            //} else {
                //moved = spielfeld.move(pos, color);
            //}
        //}
        //*/
        
        // finishedPlacing=false;
        // spiel.bildschirm.startPlacing(this);   
        
    }
    
    public void beendeZug() {
        // this.steinAnzahl--;
        // spielfeld.showBoard();
        // spiel.bildschirm.showOrbitoKnopf();
        //System.out.println("Gedrehtes Spielfeld:");
        //
    }
}
