package CurveFever;

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
    static List<KeyCode> pressedKeys;

    public GameFacade(int numberOfPlayers, List<KeyCode> pressedKeys1, final GraphicsContext gc){

        GameFacade.pressedKeys = pressedKeys1;
        boardObject = new Board(400, players, gc);
        handlingObject = new Handling(boardObject, gc);
        scoreCounterObject = new ScoreCounter();
        Timer timer = new Timer();

        //Create static keys, colors, starting points and angles
        KeyCode[][] keys = {{KeyCode.LEFT, KeyCode.RIGHT}, {KeyCode.Z, KeyCode.X}, {KeyCode.B, KeyCode.N}, {KeyCode.DIGIT1, KeyCode.DIGIT2}};
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE};
        Point [] startingPosition = {new Point(200,300), new Point(300 , 500), new Point(1000,400), new Point(700,200)};
        Double [] startingAngle = {200.0, 100.0, 0.0, 300.0};

       if(numberOfPlayers>4){
           throw new IllegalArgumentException();
       }
       else{
           for(int i=0;i<numberOfPlayers;i++){
               players.add(new Player(colors[i], keys[i][0], keys[i][1], startingPosition[i], startingAngle[i]));
           }
       }

       timer.scheduleAtFixedRate(new TimerTask() {
           @Override
           public void run() {
               for(Player player: players){
                   handlingObject.handleKeys(GameFacade.pressedKeys,player);
                   player.draw(gc);
                   player.makeStep();
               }
           }
       },0,10);
    }

    public Dictionary<Integer, Integer> getScores(){

        return null;
    }
}
