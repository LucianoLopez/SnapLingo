package teamc.finalproject;

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

        String userUID = getIntent().getStringExtra("uid");
        String username = getIntent().getStringExtra("username");
        int gameID = getIntent().getIntExtra("gameID", 0);

        DatabaseReference gameRef = FirebaseUtils.getGameRefFromID(gameID);
        gameRef.child("started").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean gameStarted = (boolean) dataSnapshot.getValue();
                System.out.println(gameStarted);
                if (gameStarted) {
                    System.out.println("Join game");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
