package teamc.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PremiseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premise);

        final String username = getIntent().getStringExtra("username");
        final String uid = getIntent().getStringExtra("uid");

        Button acceptButton = findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCreateOrJoin = new Intent(PremiseActivity.this, CreateOrJoinActivity.class);
                goToCreateOrJoin.putExtra("username", username);
                goToCreateOrJoin.putExtra("uid", uid);

                startActivity(goToCreateOrJoin);
            }
        });
    }
}
