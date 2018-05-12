package curveFever;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
    private boolean pause = true;
    boolean allDead = true;
    boolean restart = false;
    boolean bulDupy1 = false;
    boolean bulDupy2 = false;
    int bulDupy3 = 0;

    public GameFacade(int width, int height, Set<KeyCode> pressedKeys, final GraphicsContext gc,
                      KeyCode[][] playersControls, int maxNumberOfPlayers, Color[] colors) {
        this.pressedKeys = pressedKeys;
        boardObject = new Board(width, height, players, gc);
        handlingObject = new Handling(boardObject, gc);
        scoreCounterObject = new ScoreCounter();
        Timer timer = new Timer();

        Random random = new Random();

        for (int i = 0; i < maxNumberOfPlayers; i++) {
            if (playersControls[i][0] == null) {
                continue;
            }
            Point startingPoint = new Point(
                    random.nextInt(width - CurveFeverConsts.MARGIN_OF_BOUNDS * 2 - CurveFeverConsts.PLAYER_DEFAULT_SIZE * 2) + CurveFeverConsts.PLAYER_DEFAULT_SIZE + CurveFeverConsts.MARGIN_OF_BOUNDS,
                    random.nextInt(height - CurveFeverConsts.MARGIN_OF_BOUNDS * 2 - CurveFeverConsts.PLAYER_DEFAULT_SIZE * 2) + CurveFeverConsts.PLAYER_DEFAULT_SIZE + CurveFeverConsts.MARGIN_OF_BOUNDS);

            players.add(new Player(colors[i],
                    playersControls[i][0],
                    playersControls[i][1],
                    startingPoint,
                    random.nextInt(359)));
        }

        gc.setStroke(Color.BLACK);
        gc.strokeLine(5.0, 5.0, width - 5.0, 5.0);
        gc.strokeLine(width - 5.0, 5.0, width - 5.0, height - 5.0);
        gc.strokeLine(width - 5.0, height - 5.0, 5.0, height - 5.0);
        gc.strokeLine(5.0, height - 5.0, 5.0, 5.0);

        for (int i = 0; i < 10; i++) {            // przed rozpoczęciem wykonuje kilka ruchów żeby nie wykryło zderzenia i było wiadomo w którą stronę skierowani są gracze
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
                        if (pressedKeys.contains(KeyCode.SPACE)) {
                            pause = !pause;
                            pressedKeys.clear();
                        }

                        if (!pause) {
                            if(bulDupy3==2) {
                                for (Player player2 : players) {
                                    player2.setStartingLocation(new Point(
                                            random.nextInt(width - CurveFeverConsts.MARGIN_OF_BOUNDS * 2 - CurveFeverConsts.PLAYER_DEFAULT_SIZE * 2) + CurveFeverConsts.PLAYER_DEFAULT_SIZE + CurveFeverConsts.MARGIN_OF_BOUNDS,
                                            random.nextInt(height - CurveFeverConsts.MARGIN_OF_BOUNDS * 2 - CurveFeverConsts.PLAYER_DEFAULT_SIZE * 2) + CurveFeverConsts.PLAYER_DEFAULT_SIZE + CurveFeverConsts.MARGIN_OF_BOUNDS)
                                    );
                                    player2.isLineDrawing=true;
                                    boardObject.clearBoard();

                                    player2.draw(gc);
                                    player2.makeStep();
                                    player2.makeStep();
                                    player2.makeStep();
                                    player2.makeStep();
                                    player2.makeStep();
                                    player2.makeStep();

                                    player2.setIsDead(false);
                                }
                                pause = !pause;
                                bulDupy3=3;
                            }
                            if(bulDupy3==1){
                                boardObject.clearBoard();
                                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                                bulDupy3=2;
                                pause=!pause;
                            }
                            if(bulDupy3==0) {
                                allDead = true;
                                for (Player player : players) {
                                    if (!player.getIsDead()) {
                                        allDead = false;
                                        handlingObject.handleKeys(GameFacade.pressedKeys, player);
                                        player.makeStep();

                                        if (boardObject.checkSpace(player.getPositions(), player.getSize()) == true) {
                                            player.setIsDead(true);
                                            //player.
                                        }  // jak wykryje zderzenie to umarl a kto umarl ten nie żyje XD

                                        player.draw(gc);                                                                // rysowanie
                                        if (player.getIsLineDrawing()) {                                                //jeżeli linia jest w danym miejscu rysowana
                                            boardObject.addTrace(player.getPositionForTrace(), player.getSize());       // dodaje slad do tablicy kolizji
                                        }
                                    }
                                }
                                if(allDead) {bulDupy3=1; pause=!pause;}
                            }
                            if(bulDupy3==3) bulDupy3=0;
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
