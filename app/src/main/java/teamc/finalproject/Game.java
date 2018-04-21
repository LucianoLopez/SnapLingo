package teamc.finalproject;

// Created by Andrew

import java.util.ArrayList;
import java.util.List;

public class Game {
    private int joinID;
    private List<Player> playerList;
    private String name;

    public Game(Player creator, String customName, int joinID) {
        playerList = new ArrayList<>();
        playerList.add(creator);

        customName = name;
        this.joinID = joinID;
    }

    public int getJoinID() {
        return joinID;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public String getName() {
        return name;
    }
}
