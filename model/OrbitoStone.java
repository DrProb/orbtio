package model;

public class OrbitoStone
{
    OrbitoStoneColor color;
    Player player;    
    OrbitoStone(OrbitoStoneColor color, Player player)
    {
        this.color = color;
        this.player = player;
    }
    public OrbitoStoneColor getColor() {
        return color;
    }
    public Player getPlayer() {
        return player;
    }
}
