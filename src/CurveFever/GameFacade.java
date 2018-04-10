package CurveFever;

import java.util.*;
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
