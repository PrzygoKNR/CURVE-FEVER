package CurveFever;
import java.util.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class Board {
    public int[][] boardArray;
    private int size;
    private Canvas canvas;

    List<Player> players;
    Timer timer = new Timer();

    public Board(int size, final List<Player> players, final GraphicsContext gc) {
        this.players = players;
        this.canvas = canvas;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for(Player player: players){
                    player.draw(gc);
                    player.makeStep();
                }
            }
        },0,100);
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
        }
    }

}
