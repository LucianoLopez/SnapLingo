package teamc.finalproject;

// Created by Andrew

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class JoinGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // final int userUID = getIntent().getIntExtra("userUID", 0);
        final int userUID = 987654;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game_layout);

        final EditText joinGameEntry = findViewById(R.id.code_entry);

        Button joinGameButton = findViewById(R.id.join_this_game);

        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int enteredCode = Integer.parseInt(joinGameEntry.getText().toString());
                if (FirebaseUtils.doesGameExist(enteredCode)) {
                    // Game exists. Join game.
                    FirebaseUtils.playerJoinGame(userUID, enteredCode);

                    System.out.println("Successfully joined game.");
                } else {
                    // Game does not exist
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinGameActivity.this);
                    builder.setTitle("Game does not exist.");
                    builder.setMessage("That game code doesn't seem to be right. Please try again.");
                    builder.setPositiveButton("OK", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
