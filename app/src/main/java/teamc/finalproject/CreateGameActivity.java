package teamc.finalproject;

// Created by Andrew

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateGameActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_game_layout);

        // Create the game
        final String creatorUID = getIntent().getStringExtra("uid");
        final String username = getIntent().getStringExtra("username");

        System.out.println("CreateGameActivity got extra uid " + creatorUID);

        final Game game = new Game(creatorUID);

        TextView code = findViewById(R.id.joinCodeTextView);
        code.setText(Integer.toString(game.getJoinID()));

        // Push the game to the database
        FirebaseUtils.createGame(game);

        // Show players who have joined the game

        final List<String> playerList = new ArrayList<>();

        for (String playerID : game.getPlayerList()) {
            playerList.add(playerID);
        }

        final ListView playerListView = findViewById(R.id.playerListView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerList);

        playerListView.setAdapter(adapter);

        List<Word> dummyData = Word.getDummyData();

        List<Word> fakeFoundWords = new ArrayList<>();
        fakeFoundWords.add(dummyData.get(0));
        fakeFoundWords.add(dummyData.get(1));

        FirebaseUtils.setWordsFound(creatorUID, game.getJoinID(), fakeFoundWords);

        DatabaseReference gameRef = FirebaseUtils.getGameRefFromID(game.getJoinID());
        DatabaseReference playerListRef = gameRef.child("player_list");

        playerListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String joinedPlayer = dataSnapshot.getKey();
                adapter.add(joinedPlayer);
                adapter.notifyDataSetChanged();
                System.out.println("Adapter count is " + adapter.getCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // TODO: Implement leaving the game
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button startButton = findViewById(R.id.startGameButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start game
                FirebaseUtils.startGame(game.getJoinID());
                // TODO: Switch to playing the game
            }
        });

    }
}
