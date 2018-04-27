package teamc.finalproject;

// Created by Andrew

import android.support.v4.util.ArrayMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {

    private int creatorUID;
    private int joinID;
    private Map<Integer, Integer> scores; // Acts as the player list
    private List<Word> wordList;

    public Game(int creatorUID, int joinID, List<Word> wordList) {
        this.creatorUID = creatorUID;
        this.joinID = joinID;
        this.wordList = wordList;

        scores = new ArrayMap<>();
        // setPlayerScore(creatorUID, 0);
    }

    public Game(int creatorUID) {
        this(creatorUID, FirebaseUtils.getNewGameID(), FirebaseUtils.getNewWordList());
    }

    public void givePlayerPoints(int playerUID, int points) {
        int newPlayerScore = scores.get(playerUID) + points;
        setPlayerScore(playerUID, newPlayerScore);
    }

    public void setPlayerScore(int playerUID, int score) {
        scores.put(playerUID, score);
    }

    public void playerJoinGame(int playerUID) {
        setPlayerScore(playerUID, 0);
    }

    // Getters

    public int getJoinID() {
        return joinID;
    }

    public List<Integer> getPlayerList() {
        return new ArrayList<>(scores.keySet());
    }

    public int getCreatorUID() {
        return creatorUID;
    }

    public List<Word> getWordList() {
        return this.wordList;
    }

    public int getPlayerScore(int playerUID) {
        return scores.get(playerUID);
    }

}
