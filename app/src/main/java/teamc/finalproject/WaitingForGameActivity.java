package teamc.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class WaitingForGameActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_start);

        final String userUID = getIntent().getStringExtra("uid");
        final String username = getIntent().getStringExtra("username");
        final int gameID = getIntent().getIntExtra("gameID", 0);

        DatabaseReference gameRef = FirebaseUtils.getGameRefFromID(gameID);
        gameRef.child("started").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null && (boolean) dataSnapshot.getValue()) {
                    // Join game
                    Intent joinGameIntent = new Intent(WaitingForGameActivity.this, MainActivity.class);
                    joinGameIntent.putExtra("uid", userUID);
                    joinGameIntent.putExtra("username", username);
                    joinGameIntent.putExtra("gameID", gameID);

                    startActivity(joinGameIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
