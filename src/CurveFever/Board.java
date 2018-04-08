package CurveFever;
import java.awt.geom.Point2D;

public class Board {
    public int[][] boardArray;
    private int size;
    
    public void Board(int size) {
    
    }
    
    public int getSize() {
        return this.size;
    }   
    
    public boolean checkSpace(Point2D position, int size) {
        return false; //TODO
    }
}
