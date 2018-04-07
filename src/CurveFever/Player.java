package CurveFever;

import java.awt.*;

public class Player {
    private static int maxId = 0;
    public int playerID;
    private Color color;
    private int size;

    Player(Color color, int leftKey, int rightKey){
        this.playerID = maxId;
        maxId++;
    }
}
