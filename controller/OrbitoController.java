package controller;

import model.*;
import view.*;

public class OrbitoController
{

    OrbitoModel model;
    public OrbitoController()
    {
        model = new OrbitoModel();
        OrbitoConsole console = new OrbitoConsole(model);
        OrbitoGUI gui = new OrbitoGUI(this);

        //testNoStonesLeft();
        testWinPlayer1();
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

/*     Scanner sc = new Scanner(System.in);
    spielbrett spielfeld;
    spieler[] spieler;
    //orbitoKnopf orbito;
    Music music;
    Bildschirm bildschirm;
    int spIdx = 1;
    public OrbitoController()
    {
        
        this.music = new Music();
        this. spielfeld = new spielbrett();
        //System.out.println("Spiel gestartet. Wie heißt Spieler 1? ");
        //String name = sc.nextLine();
        spieler s1 = new spieler("Spieler 1", "w", spielfeld, this);
        //System.out.println("Hallo " + name + ", wie heißt dein Mitspieler? ");
        //name = sc.nextLine();
        
        spieler s2 = new spieler("Spieler 2", "s",spielfeld, this);
        this.spieler = new spieler[] {s1, s2};
        this.bildschirm = new Bildschirm(spieler);
        spielfeld.setBildschirm(this.bildschirm);
        naechsterSpieler();       
    }
    void reset() {
         bildschirm.bildschirm.dispose();
        spIdx = 1;
        this.music = new Music();
        this. spielfeld = new spielbrett();
        //System.out.println("Spiel gestartet. Wie heißt Spieler 1? ");
        //String name = sc.nextLine();
        spieler s1 = new spieler("Spieler 1", "w", spielfeld, this);
        //System.out.println("Hallo " + name + ", wie heißt dein Mitspieler? ");
        //name = sc.nextLine();
        
        spieler s2 = new spieler("Spieler 2", "s",spielfeld, this);
        this.spieler = new spieler[] {s1, s2};
        this.bildschirm = new Bildschirm(spieler);
        spielfeld.setBildschirm(this.bildschirm);
        naechsterSpieler();  
    }
    void closeGame() {
        System.exit(0);
    }
    public void naechsterSpieler() {
        spielfeld.showBoard();   
        String[] winC = checkWin();
         if (winC[0] != null) {
                if (winC[1] != null) {
                    end("beide");
                } else {
                    end(winC[0]);
                }   
                return;
            }
        spIdx = (spIdx+1)% 2;
        if(spieler[0].steinAnzahl > 0 || spieler[1].steinAnzahl > 0) {
            
            spieler[spIdx].zug();
                     
            
            //spieler winner = null;
           
        } else {
            for (int i=0; i < 5; i++) {
                spielfeld.orbito.drehen(spielfeld.spielfeld);
                winC = checkWin();
                spieler winner = null;
            if (winC[0] != null) {
                if (winC[1] != null) {
                    end("beide");
                } else {
                    end(winC[0]);
                }  
                return;
            }
            }
            end(null);
            return;
        }
     }   
    private void end(String winner) {
         if (winner == null) {
            System.out.println("Unentschieden!");
            bildschirm.unentschieden();
        } else if (winner.equals("beide")) {
            System.out.println("Beide haben gleichzeitig gewonnen! Unentschieden!");
            bildschirm.unentschieden();
        } else {
            for(spieler s: spieler) { 
                if(s.color.equals(winner)) { 
                               
                    System.out.println(s.name + " hat gewonnen!");
                    bildschirm.gewonnen(s.color);
                }
            }              
            
        }
        //while(true) {
            //System.out.println("Schreibe 'nochmal' um erneut zu spielen!");
            //String in = sc.nextLine();
            //if (in.equals("nochmal")) {
              //  new Spiel();
            //}
        //}
        bildschirm.newGame();
    }
    private String[] checkWin() {
    String winners[] = new String[2];
    int winIdx = 0;
    for (int i = 0; i < 4; i++) {
        if (spielfeld.spielfeld[i][0] == null) continue;

        String c = spielfeld.spielfeld[i][0].color;
        boolean won = true;

        for (int j = 1; j < 4; j++) {
            if (spielfeld.spielfeld[i][j] == null ||
                !spielfeld.spielfeld[i][j].color.equals(c)) {
                won = false;
                break;
            }
        }
        if (won) {
            if (winIdx < 2 && (winIdx == 0 || !winners[0].equals(c))) {
                winners[winIdx] = c;
                winIdx++;
            }
        }
    }

    
    for (int i = 0; i < 4; i++) {
        if (spielfeld.spielfeld[0][i] == null) continue;

        String c = spielfeld.spielfeld[0][i].color;
        boolean won = true;

        for (int j = 1; j < 4; j++) {
            if (spielfeld.spielfeld[j][i] == null ||
                !spielfeld.spielfeld[j][i].color.equals(c)) {
                won = false;
                break;
            }
        }
        if (won) {
            if (winIdx < 2 && (winIdx == 0 || !winners[0].equals(c))) {
                winners[winIdx] = c;
                winIdx++;
            }
        }
    }

    
    if (spielfeld.spielfeld[0][0] != null) {
        String c = spielfeld.spielfeld[0][0].color;
        boolean won = true;

        for (int i = 1; i < 4; i++) {
            if (spielfeld.spielfeld[i][i] == null ||
                !spielfeld.spielfeld[i][i].color.equals(c)) {
                won = false;
                break;
            }
        }
        if (won) {
            if (winIdx < 2 && (winIdx == 0 || !winners[0].equals(c))) {
                winners[winIdx] = c;
                winIdx++;
            }
        }
    }

    
        if (spielfeld.spielfeld[0][3] != null) {
            String c = spielfeld.spielfeld[0][3].color;
            boolean won = true;

            for (int i = 1; i < 4; i++) {
                if (spielfeld.spielfeld[i][3 - i] == null ||
                    !spielfeld.spielfeld[i][3 - i].color.equals(c)) {
                    won = false;
                    break;
                }
            }
            if (won) {
            if (winIdx < 2 && (winIdx == 0 || !winners[0].equals(c))) {
                winners[winIdx] = c;
                winIdx++;
            }
        }
    }

        return winners;
    } */
}
