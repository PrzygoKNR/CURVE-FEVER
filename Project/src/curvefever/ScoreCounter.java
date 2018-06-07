package curvefever;

import java.util.*;

public class ScoreCounter {

    //<idOfPlayer, numberOfPoints
    public ScoreCounter(int numberOfPlayers) {
        scores = new HashMap<Integer, Integer>(numberOfPlayers);
    }

    private Map<Integer, Integer> scores;

    public void increseScore(int id) {
        if (scores.containsKey(id) == true) {
            scores.put(id, scores.get(id).intValue() + 1);
        } else if (scores.containsKey(id) == false) {
            scores.put(id, 0);
        }
    }

    public Map<Integer, Integer> getScores() {
        return scores;
    }


}
