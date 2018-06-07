package curvefever;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.List;

import javafx.scene.input.KeyCode;

public class GameFacade {
    Handling handlingObject;
    Board boardObject;
    ScoreCounter scoreCounterObject;

    GraphicsContext scoresGc;

    List<Player> players = new ArrayList<Player>();
    List<Bonus> bonuses = new ArrayList<Bonus>();
    static Set<KeyCode> pressedKeys;
    private boolean pause = true;
    boolean allDead = true;
    private int switchState = 0;
    private Random random;

    public GameFacade(int width, int height, Set<KeyCode> pressedKeys, final GraphicsContext gc,
                      KeyCode[][] playersControls, int maxNumberOfPlayers, Color[] colors, GraphicsContext scoresGc) {
        this.scoresGc = scoresGc;
        this.pressedKeys = pressedKeys;
        boardObject = new Board(width, height, players, gc);
        handlingObject = new Handling(boardObject, gc);
        scoreCounterObject = new ScoreCounter(maxNumberOfPlayers);
        Timer timer = new Timer();
        random = new Random();

        initPlayers(width, height, maxNumberOfPlayers, playersControls, random, colors, gc);

        Map<Integer, Integer> scores = scoreCounterObject.getScores();
        for (int i = 0; i < maxNumberOfPlayers; i++) {
            scores.put(i, 0);
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
                            if (switchState == 0) {
                                allDead = true;
                                for (Player player : players) {
                                    if (!player.getIsDead()) {
                                        allDead = false;
                                        handlingObject.handleKeys(GameFacade.pressedKeys, player);
                                        player.makeStep();

                                        if (boardObject.checkSpace(player.getPositions(), player.getSize()) == true) {
                                            player.setIsDead(true);
                                            for (Player player1 : players) {
                                                if (!player1.getIsDead()) {
                                                    scoreCounterObject.increseScore(player1.getId());
                                                }
                                            }
                                            refreshScoreList(scoresGc, height);
                                        }  // jak wykryje zderzenie to umarl a kto umarl ten nie żyje XD

                                        player.draw(gc);                                                                // rysowanie
                                        if (player.getIsLineDrawing()) {                                                //jeżeli linia jest w danym miejscu rysowana
                                            boardObject.addTrace(player.getPositionForTrace(), player.getSize());       // dodaje slad do tablicy kolizji
                                        }
                                    }
                                }

                                for (Bonus bonus : bonuses) {
                                    bonus.draw(gc);
                                    bonus.checkPlayers(players);
                                }

                                if (allDead) {
                                    pause = !pause;
                                    boardObject.clearBoard();
                                    gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                                    drawBorders(gc, (int) gc.getCanvas().getWidth(), (int) gc.getCanvas().getHeight());
                                    players.clear();
                                    initPlayers(width, height, maxNumberOfPlayers, playersControls, random, colors, gc);
                                }
                            }
                        }
                    }
                });
            }
        }, 0, CurveFeverConsts.TIME_OF_REFRESH_GRAPHICS);
    }

    private void initPlayers(int width, int height, int maxNumberOfPlayers, KeyCode[][] playersControls, Random random, Color[] colors, GraphicsContext gc) {
        players.clear();
        bonuses.clear();
        boolean tooClose = true;
        Point startingPoint = new Point(1,1);
        Player.clearIdCounter();

        for (int i = 0; i < maxNumberOfPlayers; i++) {
            if (playersControls[i][0] == null) {
                continue;
            }
            tooClose = true;
            while(tooClose) {
                tooClose = false;
                startingPoint = new Point(
                        random.nextInt(width - CurveFeverConsts.MARGIN_OF_BOUNDS * 2 - CurveFeverConsts.PLAYER_DEFAULT_SIZE * 2) + CurveFeverConsts.PLAYER_DEFAULT_SIZE + CurveFeverConsts.MARGIN_OF_BOUNDS,
                        random.nextInt(height - CurveFeverConsts.MARGIN_OF_BOUNDS * 2 - CurveFeverConsts.PLAYER_DEFAULT_SIZE * 2) + CurveFeverConsts.PLAYER_DEFAULT_SIZE + CurveFeverConsts.MARGIN_OF_BOUNDS);

                for (Player player : players) {
                    if (Math.sqrt((player.getPositions().x - startingPoint.x) * (player.getPositions().x - startingPoint.x) + (player.getPositions().y - startingPoint.y) * (player.getPositions().y - startingPoint.y)) < 50.0) {
                        tooClose = true;
                    }
                }
            }

            players.add(new Player(colors[i],
                    playersControls[i][0],
                    playersControls[i][1],
                    startingPoint,
                    random.nextInt(359)));
        }

        // generowanie bonusów
        for (int i = 0; i < CurveFeverConsts.NUMBER_OF_BONUSES_ON_BOARD; i++) {
            bonuses.add(new Bonus(random.nextInt(width - CurveFeverConsts.BONUS_IMAGE_SIZE * 2) + CurveFeverConsts.BONUS_IMAGE_SIZE,
                    random.nextInt(height - CurveFeverConsts.BONUS_IMAGE_SIZE * 2) + CurveFeverConsts.BONUS_IMAGE_SIZE,
                    BonusType.values()[random.nextInt(BonusType.values().length - 1) + 1]));
        }

        makeSteps(gc);
        drawBorders(gc, width, height);
    }

    private void makeSteps(final GraphicsContext gc) {
        for (int i = 0; i < 10; i++) {            // przed rozpoczęciem wykonuje kilka ruchów żeby nie wykryło zderzenia i było wiadomo w którą stronę skierowani są gracze
            for (Player player : players) {
                player.makeStep();
                player.draw(gc);
                boardObject.addTrace(player.getPositionForTrace(), player.getSize());
            }
        }
    }

    private void drawBorders(GraphicsContext gc, int width, int height) {
        gc.setStroke(Color.BLACK);
        gc.strokeLine(5.0, 5.0, width - 5.0, 5.0);
        gc.strokeLine(width - 5.0, 5.0, width - 5.0, height - 5.0);
        gc.strokeLine(width - 5.0, height - 5.0, 5.0, height - 5.0);
        gc.strokeLine(5.0, height - 5.0, 5.0, 5.0);
    }

    public void refreshScoreList(GraphicsContext gc, int height) {
        gc.clearRect(0, 0, CurveFeverConsts.SIZE_OF_SCORE_LIST, height);
        int verticalPosition = 0;

        Map<Integer, Integer> scores = scoreCounterObject.getScores();

        for (Map.Entry<Integer, Integer> entry : scores.entrySet()) {
            Integer playerId = entry.getKey();
            Integer score = entry.getValue();
            Color color = Color.WHITE;
            for (Player player : players) {
                if (player.getId() == playerId) {
                    color = player.getColor();
                    break;
                }
            }
            gc.setFill(color);
            gc.fillRoundRect(1.0,
                    verticalPosition,
                    CurveFeverConsts.PLAYER_COLOR_RECTANGLE_SIZE,
                    CurveFeverConsts.PLAYER_COLOR_RECTANGLE_SIZE,
                    0.5 * CurveFeverConsts.PLAYER_COLOR_RECTANGLE_SIZE,
                    0.5 * CurveFeverConsts.PLAYER_COLOR_RECTANGLE_SIZE);

            gc.fillText(score.toString(), CurveFeverConsts.PLAYER_COLOR_RECTANGLE_SIZE + 6, verticalPosition + CurveFeverConsts.PLAYER_COLOR_RECTANGLE_SIZE);
            verticalPosition += CurveFeverConsts.PLAYER_COLOR_RECTANGLE_SIZE + 3;
            gc.restore();


        }
    }
}
