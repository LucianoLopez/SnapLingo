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

        creator = "andrew";
        FirebaseUtils.createUser(creator);

        Game game = playerCreateGame();

        String newPlayer = "luciano";
        FirebaseUtils.createUser("luciano");
        FirebaseUtils.playerJoinGame("luciano", 314);
    }

    private Game playerCreateGame() {
        int id = FirebaseUtils.getNewGameID();
        Game game = new Game(creator, "Game 1", id);
        FirebaseUtils.createGame(game);
        return game;
    }
}
