package curveFever;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.lang.*;


public class Bonus {

    private static int maxId = 0;
    private int bonusID;
    private BonusType type;
    public Point position;

    public Bonus(int x, int y, BonusType type) {
        this.position = new Point(x, y);
        this.type = type;
        this.bonusID = this.maxId;
        this.maxId++;
    }

    public void draw(GraphicsContext gc) {
        switch (type) {
            case slowDown:
                gc.drawImage(CurveFeverConsts.BONUS_IMAGE_SPEED,this.position.x - CurveFeverConsts.BONUS_IMAGE_SIZE / 2,
                        this.position.y - CurveFeverConsts.BONUS_IMAGE_SIZE / 2,
                        CurveFeverConsts.BONUS_IMAGE_SIZE,
                        CurveFeverConsts.BONUS_IMAGE_SIZE);
                break;
            case changeSize:
                gc.drawImage(CurveFeverConsts.BONUS_IMAGE_CHANGE_SIZE,this.position.x - CurveFeverConsts.BONUS_IMAGE_SIZE / 2,
                        this.position.y - CurveFeverConsts.BONUS_IMAGE_SIZE / 2,
                        CurveFeverConsts.BONUS_IMAGE_SIZE,
                        CurveFeverConsts.BONUS_IMAGE_SIZE);

                break;
            default:
                break;
        }

    }

    public void checkPlayers(List<Player> players) {
        for (Player player : players) {
            Point playerPosition = player.getPositions();
            if (playerPosition.x > position.x && playerPosition.x < position.x + CurveFeverConsts.BONUS_IMAGE_SIZE
                    && playerPosition.y > position.y && playerPosition.y < position.y + CurveFeverConsts.BONUS_IMAGE_SIZE) {
                player.catchBonus(this.type);
            }
        }
    }

    public boolean handleBonus(List<Player> list) {
        return true;
    }

}
