package CurveFever;

import java.awt.*;
import java.util.*;
import java.util.List;

import javafx.scene.input.KeyCode;

public class GameFacade {
    Handling handlingObject;
    Board boardObject;
    ScoreCounter scoreCounterObject;

    List<Player> players = new ArrayList<Player>();
    List<Bonus> bonuses = new ArrayList<Bonus>();

    public GameFacade(int numberOfPlayers){
        boardObject = new Board(400, players);
        handlingObject = new Handling(boardObject);
        scoreCounterObject = new ScoreCounter();

        //Create static keys
        KeyCode[][] keys = {{KeyCode.LEFT, KeyCode.RIGHT}, {KeyCode.A, KeyCode.D}, {KeyCode.J, KeyCode.L}, {KeyCode.NUMPAD4, KeyCode.NUMPAD6}};
        Color[] colors = {Color.red, Color.green, Color.blue, Color.orange};
       if(numberOfPlayers>4){
           throw new IllegalArgumentException();
       }
       else{
           for(int i=0;i<numberOfPlayers;i++){
               players.add(new Player(colors[i], keys[i][0],keys[i][1]));
           }
       }
    }

    void HandleKey(KeyCode key){
        for(Player player: players){
            player.handleKey(key);
        }
    }

    Dictionary<Integer, Integer> getScores(){

        return null;
    }

}
