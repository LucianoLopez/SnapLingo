package teamc.finalproject;

// Created by Andrew

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtils {

    public static FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static DatabaseReference usersRef = DB.getReference("users");
    public static DatabaseReference gamesRef = DB.getReference("games");

    public FirebaseUtils() {
        // Nothing here.
    }

    public static int getNewGameID() {
        return 314;
    }

    public static boolean doesUsernameExist(String username) {
        return false;
    }

    public static boolean doesGameExist(int id) {
        return false;
    }

    public static void createUser(String username) {
        DatabaseReference userRef = usersRef.child(username);
        userRef.child("score").setValue(0);
    }

    public static List<Word> getNewWordList() {
        Word tree = new Word("tree", "arbol");
        ArrayList<Word> wordList = new ArrayList<>();
        wordList.add(tree);
        return wordList;
    }

    public static void createGame(Game game) {
        DatabaseReference gameRef = gamesRef.child(Integer.toString(game.getJoinID()));
        gameRef.child("player_list").setValue(game.getPlayerList());
    }

    public static void playerJoinGame(String player, int gameID) {
        if (!doesGameExist(gameID)) {
            System.out.println("Game does not exist.");
            return;
        }
    }

}
