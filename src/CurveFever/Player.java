package CurveFever;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player {
    private static int maxId = 0;
    private int playerID;
    private Color color;
    private int size;
    private int speed;
    private boolean isDead;
    private int leftKey;
    private int rightKey;
    private double angle;
    private Point2D position;

    Player(Color color, int leftKey, int rightKey){
        this.playerID = maxId;
        maxId++;
        this.color = color;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
    }

    private void rotate(double angle){
        this.angle += angle;
    }

    public int getSize(){
        return this.size;
    }

    public void handleKey(int key){

    }

    public Color getColor(){
        return this.color;
    }

    public int getId(){
        return this.playerID;
    }

    public int getSpeed(){
        return this.speed;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public boolean getIsDead(){
        return this.isDead;
    }

    public void setIsDead(boolean isDead){
        this.isDead = isDead;
    }

    public void makeStep(){
        //???? xD
    }

}
