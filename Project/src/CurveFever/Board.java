package CurveFever;
import java.util.*;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

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

    void handleKey(KeyCode key) {
        for(Player player: players) {
            player.handleKey(key);
            player.draw(new Point2D(1,2), 2);
        }
    }

}
