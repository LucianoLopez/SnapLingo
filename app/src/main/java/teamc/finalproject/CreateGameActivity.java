package teamc.finalproject;

// Created by Andrew

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Map;

public class CreateGameActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ProgressDialog mProgressDialog;
    private Game game;
    private Context context;
    private String creatorUID;
    private String username;
    private TextView code;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.create_game_layout);
        code = findViewById(R.id.joinCodeTextView);

        getSupportActionBar().setTitle("Create Game");

        // Create the game
        creatorUID = getIntent().getStringExtra("uid");
        final String username = getIntent().getStringExtra("username");

        System.out.println("CreateGameActivity got extra uid " + creatorUID);
        new CreateGame().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, creatorUID);

        //FIXME: THIS WAS JUST COPIED AND PASTED INTO onPostExecte of CreateGame()
//        final Game game = new Game(creatorUID);
//
//        TextView code = findViewById(R.id.joinCodeTextView);
//        code.setText(Integer.toString(game.getJoinID()));
//
//        // Push the game to the database
//        FirebaseUtils.createGame(game);
//
//        // Show players who have joined the game
//
//        final List<String> playerList = new ArrayList<>();
//
//        for (String playerID : game.getPlayerList()) {
//            playerList.add(playerID);
//        }
//
//        final ListView playerListView = findViewById(R.id.playerListView);
//
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerList);
//
//        playerListView.setAdapter(adapter);
//
//        List<Word> dummyData = Word.getDummyData();
//
//        List<Word> fakeFoundWords = new ArrayList<>();
//        fakeFoundWords.add(dummyData.get(0));
//        fakeFoundWords.add(dummyData.get(1));
//
//        FirebaseUtils.setWordsFound(creatorUID, game.getJoinID(), fakeFoundWords);
//
//        DatabaseReference gameRef = FirebaseUtils.getGameRefFromID(game.getJoinID());
//        DatabaseReference playerListRef = gameRef.child("player_list");
//
//        playerListRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String joinedPlayer = dataSnapshot.getKey();
//                updateAdapter(joinedPlayer);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                // TODO: Implement leaving the game
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        Button startButton = findViewById(R.id.startGameButton);
//        startButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Start game
//                FirebaseUtils.startGame(game.getJoinID());
//
//                // Join game
//                Intent joinGameIntent = new Intent(CreateGameActivity.this, MainActivity.class);
//                joinGameIntent.putExtra("uid", creatorUID);
//                joinGameIntent.putExtra("username", username);
//                joinGameIntent.putExtra("gameID", game.getJoinID());
//
//                startActivity(joinGameIntent);
//            }
//        });

    }

    public class CreateGame extends AsyncTask<String, Void, Game> {

        private ArrayList<String[]> list = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            showProgressDialog("Please wait while your game loads...");
        }

        @Override
        protected Game doInBackground(String...params) {
            return new Game(params[0]);
        }
        @Override
        protected void onPostExecute(Game lame) {
            super.onPostExecute(lame);
            game = lame;
            hideProgressDialog();
//            TextView code = findViewById(R.id.joinCodeTextView);
            code.setText(Integer.toString(game.getJoinID()));

            // Push the game to the database
            FirebaseUtils.createGame(game);

            // Show players who have joined the game

            final List<String> playerList = new ArrayList<>();

            for (String playerID : game.getPlayerList()) {
                playerList.add(playerID);
            }

            final ListView playerListView = findViewById(R.id.playerListView);

            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, playerList);

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
                    updateAdapter(joinedPlayer);
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

                    // Join game
                    Intent joinGameIntent = new Intent(CreateGameActivity.this, MainActivity.class);
                    joinGameIntent.putExtra("uid", creatorUID);
                    joinGameIntent.putExtra("username", username);
                    joinGameIntent.putExtra("gameID", game.getJoinID());

                    startActivity(joinGameIntent);
                }
            });

        }
        }

    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void updateAdapter(String newUserUID) {
        FirebaseUtils.usersRef.child(newUserUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> result = (HashMap<String, String>) dataSnapshot.getValue();
                System.out.println(result);
                String newUsername = result.get("name");
                adapter.add(newUsername);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
