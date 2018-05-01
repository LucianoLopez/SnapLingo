package teamc.finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Score implements Serializable {

    private String userName;
    private int score;

    public Score() {
    }

    public Score(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

}
