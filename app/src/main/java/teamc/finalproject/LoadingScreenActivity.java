package teamc.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class LoadingScreenActivity extends Activity {
    /**
    Right now this class is useless...
     */

    private final int WAIT_TIME = 5000;
    private String username;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("LoadingScreenActivity screen started");
        setContentView(R.layout.loading_screen);
        findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        userUID = intent.getStringExtra("uid");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                Intent intent = new Intent(LoadingScreenActivity.this, CreateGameActivity.class);
//                intent.putExtra("username", username);
//                intent.putExtra("uid", userUID);
//
//                LoadingScreenActivity.this.startActivity(intent);
                LoadingScreenActivity.this.finish();
            }
        }, WAIT_TIME);
    }
}
