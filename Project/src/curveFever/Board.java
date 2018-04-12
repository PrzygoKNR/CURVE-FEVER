package curveFever;
import java.util.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Board {
    public int[][] boardArray;
    private int size;
    private Canvas canvas;

    List<Player> players;

    public Board(int size, final List<Player> players, final GraphicsContext gc) {
        this.players = players;
    }
    
    public int getSize() {
        return this.size;
    }   
    
    public boolean checkSpace(Point position, int size) {
        return false; //TODO
    }
}
