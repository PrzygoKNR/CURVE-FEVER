package CurveFever;
import java.awt.geom.Point2D;
import java.util.*;

public class Board {
    public int[][] boardArray;
    private int size;
    List<Player> players;

    public Board(int size, List<Player> players) {
        this.players = players;
    }
    
    public int getSize() {
        return this.size;
    }   
    
    public boolean checkSpace(Point2D position, int size) {
        return false; //TODO
    }
}
