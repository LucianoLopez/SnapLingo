package teamc.finalproject;

import java.io.Serializable;

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

    public void foundWord() {
        this.numFound += 1;
    }
}
