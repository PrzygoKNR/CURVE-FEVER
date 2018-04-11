package CurveFever;

import javafx.geometry.Point2D;
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

    public GameFacade(int numberOfPlayers, List<KeyCode> pressedKeys1, GraphicsContext gc){

        GameFacade.pressedKeys = pressedKeys1;
        boardObject = new Board(400, players, gc);
        handlingObject = new Handling(boardObject);
        scoreCounterObject = new ScoreCounter();
        Timer timer = new Timer();

        //Create static keys
        KeyCode[][] keys = {{KeyCode.LEFT, KeyCode.RIGHT}, {KeyCode.A, KeyCode.D}, {KeyCode.J, KeyCode.L}, {KeyCode.NUMPAD4, KeyCode.NUMPAD6}};
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE};
       if(numberOfPlayers>4){
           throw new IllegalArgumentException();
       }
       else{
           for(int i=0;i<numberOfPlayers;i++){
               players.add(new Player(colors[i], keys[i][0],keys[i][1]));
           }
       }
       /*timer.scheduleAtFixedRate(new TimerTask() {
           @Override
           public void run() {
               testHandleKey();
               handlingObject.handleKeys(GameFacade.pressedKeys,players);
           }
       },0,100);

       przerzucone do Boardu
       */
    }

    Dictionary<Integer, Integer> getScores(){

        return null;
    }
    public void handleKey(List<KeyCode> keys){
        handlingObject.handleKeys(keys, players);
    }
}
