package curveFever;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import java.lang.Math;

public class Player implements IDrawable{
    private static int maxId = 0;
    private int playerID;
    private Color color;
    private static int sizeDefault = 10;
    private static int speedDefault = 2;
    private static int rotateDefault = 2;
    private int size;
    private int speed;
    private boolean isDead;
    private KeyCode leftKey;
    private KeyCode rightKey;
    private double angle;
    private double rotateStep;
    private static int positionLenght = 40;
    private Point[] position = new Point[positionLenght];

    public Player(Color color, KeyCode leftKey, KeyCode rightKey, Point startingPosition, double startingAngle) {

        this.playerID = maxId;
        maxId++;
        this.color = color;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        for(int i = 0; i < positionLenght; i++) {
            position[i] = new Point( startingPosition.x, startingPosition.y);
        }
        this.angle = startingAngle;
        size = sizeDefault;
        speed = speedDefault;
        rotateStep = rotateDefault;
    }

    private void rotate(double angle){
        this.angle += angle;
        if(this.angle > 360.0) this.angle -= 360.0;
        if(this.angle < 0.0) this.angle += 360;
    }

    public int getSize(){
        return this.size;
    }

    public void handleKey(KeyCode key){
        if (key == leftKey) rotate(rotateStep);
        else if (key == rightKey) rotate(-rotateStep);
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

    public double getAngle() {
        return angle;
    }

    public void makeStep()
    {
        for(int i = positionLenght - 1; i > 0; i--){
            position[i].x = position[i-1].x;
            position[i].y = position[i-1].y;
        }
        position[0].x += speed * Math.sin(Math.toRadians(angle));
        position[0].y += speed * Math.cos(Math.toRadians(angle));

        //positionTest();
    }
    public void draw(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillOval(this.position[1].x,this.position[1].y, size+5, size+5);
        gc.restore();
        gc.setFill(Color.GRAY);
        gc.fillOval(this.position[0].x,this.position[0].y, size, size);
        gc.restore();
        gc.setFill(this.color);
        gc.fillOval(this.position[7].x,this.position[7].y, size, size);
        gc.restore();
    }
    private void positionTest() {
        for (int i = 0; i < positionLenght; i++) {
            System.out.print(i);
            System.out.print(" ");
            System.out.print(position[i].x);
            System.out.print(" ");
            System.out.print(position[i].y);
            System.out.print("\n");
        }
        System.out.print("\n\n\n");
    }
}
