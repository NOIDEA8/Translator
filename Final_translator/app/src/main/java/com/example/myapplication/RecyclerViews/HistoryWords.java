package com.example.myapplication.RecyclerViews;

public class HistoryWords {
    private String origin;
    private String translated;
    private String fromLanguage;
    private String toLanguage;

    public HistoryWords() {
    }

    public HistoryWords(String origin, String translated, String fromLanguage, String toLanguage) {
        this.origin = origin;
        this.translated = translated;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
}
