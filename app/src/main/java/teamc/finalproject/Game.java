package teamc.finalproject;

// Created by Andrew

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private String creator;
    private int joinID;
    private List<String> playerList;
    private Map<String, Integer> scores;

    public Game(String creator, int joinID) {
        this.creator = creator;
        this.joinID = joinID;

        playerList = new ArrayList<>();
        playerList.add(creator);

        scores = new HashMap<>();
        setPlayerScore(creator, 0);
    }

    public int getJoinID() {
        return joinID;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerScore(String username, int score) {
        scores.put(username, score);
    }

}
