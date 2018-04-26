package teamc.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WordListGenerator wlg = new WordListGenerator();
        System.out.println("starting");
        HashMap<String, String> words = wlg.getWordList(10);
        System.out.println(words);
        System.out.println("finished");
    }
}
