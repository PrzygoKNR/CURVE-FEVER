package curveFever;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.security.Key;
import java.util.*;
import java.util.List;

import javafx.scene.input.KeyCode;

public class GameFacade {
    Handling handlingObject;
    Board boardObject;
    ScoreCounter scoreCounterObject;

    List<Player> players = new ArrayList<Player>();
    List<Bonus> bonuses = new ArrayList<Bonus>();
    static Set<KeyCode> pressedKeys;

    public GameFacade(int width, int height, Set<KeyCode> pressedKeys1, final GraphicsContext gc,
                      KeyCode[][] playersControls, int maxNumberOfPlayers) {
        GameFacade.pressedKeys = pressedKeys1;
        boardObject = new Board(width, height, players, gc);
        handlingObject = new Handling(boardObject, gc);
        scoreCounterObject = new ScoreCounter();
        Timer timer = new Timer();

        //Create static keys, colors, starting points and angles
        KeyCode[][] keys = {{KeyCode.LEFT, KeyCode.RIGHT}, {KeyCode.Z, KeyCode.X}, {KeyCode.B, KeyCode.N}, {KeyCode.DIGIT1, KeyCode.DIGIT2}};
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CORAL, Color.PINK};
        Point[] startingPosition = {new Point(200, 300), new Point(300, 500), new Point(1000, 400),
                                new Point(700, 200), new Point(500, 500), new Point(800, 800)};
        Double[] startingAngle = {200.0, 100.0, 0.0, 300.0, 100.0, 90.0};

        if (maxNumberOfPlayers > 6) {
            throw new IllegalArgumentException();
        } else {
            for (int i = 0; i < maxNumberOfPlayers; i++) {
                if(playersControls[i][0] == (null)) {
                    System.out.println("NUUUULLL");
                    continue;
                }
                players.add(new Player(colors[i], playersControls[i][0], playersControls[i][1], startingPosition[i], startingAngle[i]));
            }
        }

        for (int i = 0; i < 10; i++) {            // przed rozpoczęciem wykonuje kilka ruchów żeby nie wykryło zderzenia i było wiadomo w którą stronę skierowanie są gracze
            for (Player player : players) {
                player.makeStep();
                player.draw(gc);
                boardObject.addTrace(player.getPositionForTrace(), player.getSize());
            }
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : players) {
                            if (!player.getIsDead()) {
                                handlingObject.handleKeys(GameFacade.pressedKeys, player);
                                player.makeStep();

                                if (boardObject.checkSpace(player.getPositions(), player.getSize()) == true) {
                                    player.setIsDead(true);
                                }  // jak wykryje zderzenie to umarl a kto umarl ten nie żyje XD

                                player.draw(gc);                                                              // rysowanie
                                if (player.getIsLineDrawing()) {                                                //jeżeli linia jest w danym miejscu rysowana
                                    boardObject.addTrace(player.getPositionForTrace(), player.getSize());     // dodaje slad do tablicy kolizji
                                }
                            }
                        }
                    }
                });
            }
        }, 0, CurveFeverConsts.TIME_OF_REFRESH_GRAPHICS);
    }

    public Map<Integer, Integer> getScores() {

        return null;
    }
}
