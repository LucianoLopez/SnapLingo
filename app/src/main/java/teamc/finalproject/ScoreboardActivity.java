package teamc.finalproject;

// Created by Andrew

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {

    private Game game;
    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
