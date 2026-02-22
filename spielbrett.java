import java.util.Scanner;
public class spielbrett {
    public spieler[] spieler;
    public stein[][] spielfeld;
    Scanner sc = new Scanner(System.in);
    orbitoKnopf orbito;
    Bildschirm bildschirm;
    public spielbrett() {
        this.spielfeld = new stein[4][4];
        //spielfeld[0][0] = new stein("s");
        this.orbito = new orbitoKnopf();
    }
    public void showBoard() {
        bildschirm.resetKugel();
        for (int i = 0; i < 4; i++) {
            
            for (int j = 0; j < 4; j++) {
            if (spielfeld[i][j] != null) {
                    bildschirm.addStone(j, i, spielfeld[i][j]); 
            }
            //System.out.println(zeile);
        }
        /**System.out.println();
        System.out.println("  A B C D");
        String zeile;
        
        for (int i = 0; i < 4; i++) {
            zeile = "" + (i+1);
            for (int j = 0; j < 4; j++) {
                if (spielfeld[i][j] == null) {
                    zeile = zeile + " .";                    
                } else {
                    zeile = zeile + " " + spielfeld[i][j].color;  
                    bildschirm.addStone(i, j, spielfeld[i][j]);
                }
            }
            System.out.println(zeile);
        }
        
        System.out.println();
        
        **/
        }
        bildschirm.bildschirm.repaint();
        bildschirm.disableOcc(spielfeld);
    
    }
    public void setBildschirm(Bildschirm bildschirm) {
        this.bildschirm = bildschirm;
    }
    void place(int x, int y, stein stein) {
        //int[] p = parsePos(pos);    
        spielfeld[y][x] = stein;
        //return true;
    }   
    int[] parsePos(String pos) {
        if (pos.length()!=2) {
            //System.out.println("Ungültige Eingabe, versuche es erneut!");
            return null;
        }
        int clm = Character.toUpperCase(pos.charAt(0)) - 'A';
        int row = pos.charAt(1) - '1';
        if (row < 0 || row > 3 || clm < 0 || clm > 3) {
            //System.out.println("Dieses Feld existiert nicht, versuche es erneut!");
            return null;
        }
        return new int[] {row,clm};
    }
    private int[] getDirOffset(String dir) {
        switch (dir) {
            case "l":
                return new int [] {0, -1};
            case "r":
                return new int [] {0, 1};
            case "u":
                return new int [] {-1, 0};
            case "d":
                return new int [] {1, 0};
            default:
                //System.out.print("Ungültige Eingabe - Versuche es erneut!");
                return null;
        }
    }
    public boolean move(String pos, String color) {
        int[] p = parsePos(pos);
        if (p==null) {
            return false;
        }
        int row = p[0];
        int clm = p[1];  
        if (spielfeld[row][clm] == null) {
            //System.out.println("Hier ist kein Stein! Gib ein anderes Feld ein!");
            return false;
        }
        if (spielfeld[row][clm].color == color) {
            //System.out.println("Du kannst nur Figuren deines Gegners bewegen! Gib ein anderes Feld ein!");
            return false;
        }
        while (true) {
            //System.out.println("In welche Richtung möchtest du den Stein bewegen? (l / r / u / d / exit)");
            String dir = sc.nextLine();
            if (dir == "exit") {
                return false;
            }
            int[] o = getDirOffset(dir);
            int rowO = o[0];
            int clmO = o[1];
            try {
                if (spielfeld[row+rowO][clm+clmO] != null) {
                    //System.out.println("Dieses Feld ist belegt!");
                    break;
                }
                spielfeld[row+rowO][clm+clmO] = spielfeld[row][clm];
                spielfeld[row][clm] = null;
                return true;
            } catch (Exception ArrayIndexOutOfBoundsException) {
                //System.out.println("Du kannst die Steine nicht aus dem Feld rausverschieben!");
            }     
        }
        return true;
    }
}
