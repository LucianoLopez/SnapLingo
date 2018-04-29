package teamc.finalproject;

// Created by Andrew

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class CreateOrJoinActivity extends AppCompatActivity {

    private Game game;
    private List<String> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_join_layout);

        Button createGameButton = findViewById(R.id.create_game_button);
        Button joinGameButton = findViewById(R.id.join_game_button);

        final String username = getIntent().getStringExtra("username");
        final String userUID = getIntent().getStringExtra("uid");

        System.out.println("CreateOrJoinActivity got extra uid " + userUID);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoinActivity.this, CreateGameActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("uid", userUID);

                startActivity(intent);
            }
        });

        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoinActivity.this, JoinGameActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("uid", userUID);

                startActivity(intent);
            }
        });
    }
}
