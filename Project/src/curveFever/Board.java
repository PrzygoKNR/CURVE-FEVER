package curveFever;

import java.util.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Board {
    public int[][] boardArray;
    private int width;
    private int height;
    private Canvas canvas;

    List<Player> players;

    public Board(int width, int height, final List<Player> players, final GraphicsContext gc) {
        this.width = width;
        this.height = height;
        boardArray = new int[width][height];
        this.players = players;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boardArray[i][j] = 0;
            }
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean checkSpace(Point position, int size) {           // sprawdza kilka punktow na graczu i czy nie jest za blisko ściany
        int x = (int) position.x - 1;                               // bo w tablicy bedzie to przesuniete
        int y = (int) position.y - 1;
        int r = size / 2;
        if(x > r && x < width - r && y > r && y < height - r) {
            if (boardArray[x][y] == 1) {
                return true;
            } else if (boardArray[x - r][y] == 1) {
                return true;
            } else if (boardArray[x][y - r] == 1) {
                return true;
            } else if (boardArray[x + r][y] == 1) {
                return true;
            } else if (boardArray[x][y + r] == 1) {
                return true;
            } else if (position.x - (double) size / 2.0 <= 0) {
                return true;
            } else if (position.y - (double) size / 2.0 <= 0) {
                return true;
            } else if (position.x + (double) size + 5 >= width) {
                return true;
            } else if (position.y + (double) size / 2.0 + 3 >= height) {
                return true;
            }
            return false;
        }
        return true;
    }

    public void addTrace(Point position, int size) {                           // dodaje slad do tablicy
        int x = (int) position.x - 1;  // bo w tablicy bedzie to przesuniete
        int y = (int) position.y - 1;
        for (int i = 0; i < (size * 0.6); i++) {
            for (int j = 0; j < (size * 0.6); j++) {
                boardArray[x - (int) (size * 0.4) + i][y - (int) (size * 0.4) + j] = 1;
            }
        }
    }
}
