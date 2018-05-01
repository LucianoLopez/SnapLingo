package teamc.finalproject;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class Word implements Serializable {

    private String englishTranslation;
    private String foreignTranslation;
    private int numFound;

    public Word() {

    }

    public Word(String englishTranslation, String foreignTranslation) {
        this.englishTranslation = englishTranslation;
        this.foreignTranslation = foreignTranslation;
        this.numFound = 0;
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
    }

    @Override
    public String toString() {
        return this.englishTranslation + "/" + this.foreignTranslation;
    }
}
