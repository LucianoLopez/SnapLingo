package teamc.finalproject;

// Created by Andrew

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreateGameActivity extends AppCompatActivity {

    private String creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the game
        creator = "andrew";
        int joinID = FirebaseUtils.getNewGameID();

        Game game = new Game(creator, joinID);

        // Push the game to the database

    }
}
