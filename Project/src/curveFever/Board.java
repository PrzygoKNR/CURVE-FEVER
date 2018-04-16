package curveFever;
import java.util.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Board {
    public int[][] boardArray = new int[2000][1000];
    private int size;
    private Canvas canvas;

    List<Player> players;

    public Board(int size, final List<Player> players, final GraphicsContext gc) {
        this.players = players;
        for(int i = 0;i < 2000; i++){
            for( int j = 0; j<1000; j++){
                boardArray[i][j] = 0;
            }
        }
    }
    
    public int getSize() {
        return this.size;
    }   
    
    public boolean checkSpace(Point position, int size) {
        int x = (int)position.x-1;  // bo w tablicy bedzie to przesuniete
        int y = (int)position.y-1;
        int r = size/2;
        if(boardArray[x][y] == 1 ){return true;}
        else if (boardArray[x-r][y] == 1) {return true;}
        else if (boardArray[x][y-r] == 1) {return true;}
        else if (boardArray[x+r][y] == 1) {return true;}
        else if (boardArray[x][y+r] == 1) {return true;}
        else if (position.x - (double)size - 5<= 0) {return true;}
        else if (position.y - (double)size - 5<= 0) {return true;}
        else if (position.x + (double)size + 5>= 2000) {return true;}
        else if (position.y + (double)size + 5 >= 1000) {return true;}
        return false;
    }
    public void addTrace(Point position, int size) {
        int x = (int)position.x-1;  // bo w tablicy bedzie to przesuniete
        int y = (int)position.y-1;
        for(int i = 0; i < (size * 0.6); i++) {
            for(int j = 0; j < (size * 0.6); j++) {
                boardArray [x - (int)(size*0.4) + i][y - (int)(size*0.4) + j] = 1;
            }
        }
    }
}
