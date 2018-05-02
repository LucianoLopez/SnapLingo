package teamc.finalproject;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Word implements Serializable {

    private String englishTranslation;
    private String foreignTranslation;
    public int numFound = 0;
    public String gameID;
    public int firebaseWordIndex;
    public Uri imageURI;

    public Word() {

    }

    public Word(String englishTranslation, String foreignTranslation) {
        this.englishTranslation = englishTranslation;
        this.foreignTranslation = foreignTranslation;
        numFound = 0;
    }
    public Uri getImageURI() {
        return imageURI;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    public String getForeignTranslation() {
        return foreignTranslation;
    }

    public static List<Word> getDummyData() {
        List<Word> dummyData = new ArrayList<>();

        Word word1 = new Word("tree", "arbol");
        Word word2 = new Word("apple", "manzana");
        Word word3 = new Word("plate", "plato");
        Word word4 = new Word("house", "casa");
        Word word5 = new Word("dog", "perro");

        dummyData.add(word1);
        dummyData.add(word2);
        dummyData.add(word3);
        dummyData.add(word4);
        dummyData.add(word5);

        return dummyData;
    }

    public void foundWord() {
        this.numFound += 1;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("games").child(gameID).child("words").child(String.valueOf(firebaseWordIndex)).child("numFound").setValue(this.numFound);
    }

    public void setImageURI(Uri uri) {
        imageURI = uri;
    }


    @Override
    public String toString() {
        return this.englishTranslation + "/" + this.foreignTranslation;
    }
}
