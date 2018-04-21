package teamc.finalproject;

// Created by Andrew

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private int joinID;
    private List<String> playerList;
    private Map<String, Integer> scores;
    private String name;

    public Game(String creator, String customName, int joinID) {
        playerList = new ArrayList<>();
        playerList.add(creator);

        scores = new HashMap<>();
        setPlayerScore(creator, 0);

        customName = name;
        this.joinID = joinID;
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

    public String getName() {
        return name;
    }

}
