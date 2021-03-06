package teamc.finalproject;

// Created by Andrew

import android.support.v4.util.ArrayMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {

    private String creatorUID;
    private int joinID;
    private Map<String, Integer> scores; // Acts as the player list
    private List<Word> wordList;
    private boolean started = false;

    public Game(String creatorUID, int joinID, List<Word> wordList, boolean started) {
        this.creatorUID = creatorUID;
        this.joinID = joinID;
        this.wordList = wordList;
        this.started = started;

        scores = new ArrayMap<>();
        setPlayerScore(creatorUID, 0);
    }

    public Game(String creatorUID) {
        this(creatorUID, FirebaseUtils.getNewGameID(), FirebaseUtils.getNewWordList(), false);
    }

    public void givePlayerPoints(String playerUID, int points) {
        int newPlayerScore = scores.get(playerUID) + points;
        setPlayerScore(playerUID, newPlayerScore);
    }

    public void setPlayerScore(String playerUID, int score) {
        scores.put(playerUID, score);
    }

    public void playerJoinGame(String playerUID) {
        setPlayerScore(playerUID, 0);
    }

    // Getters

    public int getJoinID() {
        return joinID;
    }

    public List<String> getPlayerList() {
        return new ArrayList<>(scores.keySet());
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public List<Word> getWordList() {
        return this.wordList;
    }

    public int getPlayerScore(String playerUID) {
        return scores.get(playerUID);
    }

}
