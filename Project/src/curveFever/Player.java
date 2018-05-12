package curveFever;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;

import java.lang.Math;
import java.util.Random;

public class Player implements IDrawable {
    private static int maxId = 0;
    private int playerID;
    private Color color;
    private int size = CurveFeverConsts.PLAYER_DEFAULT_SIZE;
    private double speed = CurveFeverConsts.PLAYER_DEFAULT_SPEED;
    private boolean isDead = false;
    private KeyCode leftKey;
    private KeyCode rightKey;
    private double angle;
    private double rotateStep = CurveFeverConsts.PLAYER_DEFAULT_ROTATE;
    private Point[] positions = new Point[CurveFeverConsts.NUMBER_OF_POINTS_TO_STORE];
    private int msCounter = 0;                              //licznik milisekund, do zmiany na rysowanie/nierysowanie śladu
    public boolean isLineDrawing = true;
    private int randomDeltaToDrawingLine = 0;

    public Player(Color color, KeyCode leftKey, KeyCode rightKey, Point startingPosition, double startingAngle) {
        this.playerID = maxId;
        maxId++;
        this.color = color;
        this.leftKey = leftKey;
        this.rightKey = rightKey;

        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Point(startingPosition.x, startingPosition.y);
        }


        this.angle = startingAngle;
        setNewRandomDeltaToDrawingLine();
    }

    public void setStartingLocation(Point startingPosition) {
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Point(startingPosition.x, startingPosition.y);
        }
    }

    public KeyCode getLeftKey(){
        return leftKey;
    }

    public   KeyCode getRightKey() {
        return rightKey;
    }
    private void setNewRandomDeltaToDrawingLine() {
        final double MAX_PERCENTAGE_TO_DELTA = 0.8;
        randomDeltaToDrawingLine = (new Random()).nextInt((int) (CurveFeverConsts.MS_TO_DRAW_BREAK * MAX_PERCENTAGE_TO_DELTA));
    }

    private void rotate(double angle) {
        this.angle += angle;
        if (this.angle > 360.0) this.angle -= 360.0;
        if (this.angle < 0.0) this.angle += 360;
    }

    public int getSize() {
        return this.size;
    }

    public boolean getIsLineDrawing() {
        return this.isLineDrawing;
    }

    public void handleKey(KeyCode key) {
        if (key == leftKey) rotate(rotateStep);
        else if (key == rightKey) rotate(-rotateStep);
    }

    public Color getColor() {
        return this.color;
    }

    public int getId() {
        return this.playerID;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean getIsDead() {
        return this.isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public double getAngle() {
        return angle;
    }

    public Point getPositionForTrace() {
        return new Point(positions[(int) ((double) (CurveFeverConsts.PLAYER_MARGIN_BEETWEN_PLAYER_AND_LINE + this.size / 2) * 2.0 / speed)].x,
                positions[(int) ((double) (CurveFeverConsts.PLAYER_MARGIN_BEETWEN_PLAYER_AND_LINE + this.size / 2) * 2.0 / speed)].y); // pozycja w której rysujemy slad
    }

    public Point getPositions() {
        return positions[0];                             // aktualna pozycja gracza
    }

    public void makeStep() {
        for (int i = positions.length - 1; i > 0; i--) {
            positions[i].x = positions[i - 1].x;
            positions[i].y = positions[i - 1].y;
        }

        positions[0].x += speed * Math.sin(Math.toRadians(angle));
        positions[0].y += speed * Math.cos(Math.toRadians(angle));

        this.msCounter += CurveFeverConsts.TIME_OF_REFRESH_GRAPHICS;
        if (this.msCounter > CurveFeverConsts.MS_TO_DRAW_BREAK - this.randomDeltaToDrawingLine) {
            this.isLineDrawing = false;
        }
        if (this.msCounter > CurveFeverConsts.MS_TO_DRAW_BREAK + CurveFeverConsts.MS_TO_START_DRAWING - this.randomDeltaToDrawingLine) {
            this.isLineDrawing = true;
            this.msCounter = 0;
            setNewRandomDeltaToDrawingLine();
        }
        //positionTest();
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(CurveFeverConsts.EMPTY_BOARD_COLOR);                                            //zakrywa stara pozycje gracza
        gc.fillOval(this.positions[1].x, this.positions[1].y, size, size);

        gc.setFill(CurveFeverConsts.PLAYER_POINT_COLOR);                                             //rysuje pozycje gracza
        gc.fillOval(this.positions[0].x, this.positions[0].y, size, size);

        if (this.isLineDrawing) {
            gc.setFill(this.color);                                             //rysuje slad
            gc.fillOval(
                    this.positions[(int) ((double) (CurveFeverConsts.PLAYER_MARGIN_BEETWEN_PLAYER_AND_LINE + this.size / 2) * 2.0 / speed)].x,
                    this.positions[(int) ((double) (CurveFeverConsts.PLAYER_MARGIN_BEETWEN_PLAYER_AND_LINE + this.size / 2) * 2.0 / speed)].y,
                    size,
                    size);
        }

        gc.restore();
    }

    private void positionTest() {
        for (int i = 0; i < positions.length; i++) {
            System.out.print(i);
            System.out.print(" ");
            System.out.print(positions[i].x);
            System.out.print(" ");
            System.out.print(positions[i].y);
            System.out.print("\n");
        }
        System.out.print("\n\n\n");
    }
}
