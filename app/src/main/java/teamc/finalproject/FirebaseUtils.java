package teamc.finalproject;

// Created by Andrew

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FirebaseUtils {

    public static final FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static final DatabaseReference usersRef = DB.getReference("users");
    public static final DatabaseReference gamesRef = DB.getReference("games");

    public FirebaseUtils() {
        // Nothing here.
    }

    public static int getNewGameID() {
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }

    public static boolean doesUsernameExist(String username) {
        return false;
    }

    public static boolean doesGameExist(int gameID) {
        return true;
    }

    public static int getNewPlayerUID() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    public static void createUser(int uid, String name, String email) {
        DatabaseReference userRef = usersRef.child(Integer.toString(uid));
        userRef.child("email").setValue(email);
        userRef.child("game").setValue(0); // 0 means not currently in a game
        userRef.child(name).setValue(name);
    }

    public static List<Word> getNewWordList() {
        return Word.getDummyData();
    }

    public static void createGame(Game game) {
        // Firebase only likes String dbref names
        String gameKey = Integer.toString(game.getJoinID());
        String creatorKey = Integer.toString(game.getCreatorUID());

        // Put the game in the "games" section
        DatabaseReference gameRef = gamesRef.child(gameKey);
        gameRef.child("words").setValue(game.getWordList());

        DatabaseReference playerListRef = gameRef.child("player_list");

        DatabaseReference playerRef;
        for (int playerUID : game.getPlayerList()) {
            playerRef = playerListRef.child(Integer.toString(playerUID));
            playerRef.child("points").setValue(game.getPlayerScore(playerUID));
        }

        // Set player's game attribute to new game id
        DatabaseReference userRef = usersRef.child(creatorKey);
        userRef.child("game").setValue(game.getJoinID());
    }

    public static void playerJoinGame(int playerUID, int gameID) {
        System.out.println("Attempting to add player " + playerUID + " to game " + gameID + ".");

        DatabaseReference gameRef = getGameRefFromID(gameID);
        DatabaseReference newPlayerRefInGame = gameRef.child("player_list").child(Integer.toString(playerUID));
        newPlayerRefInGame.child("points").setValue(0);

        usersRef.child(Integer.toString(playerUID)).child("game").setValue(gameID);
    }

    public static void setWordsFound(int playerUID, int gameID, List<Word> words) {
        DatabaseReference gameRef = getGameRefFromID(gameID);
        DatabaseReference playerRef = gameRef.child("player_list").child(Integer.toString(playerUID));
        DatabaseReference wordsFoundRef = playerRef.child("words_found");
        wordsFoundRef.setValue(words);
    }

    public static DatabaseReference getGameRefFromID(int gameID) {
        return gamesRef.child(Integer.toString(gameID));
    }

    public static String getUsernameFromUID(int uid) {
        DatabaseReference userRef = usersRef.child(Integer.toString(uid));
        return "andrew";
    }

}
