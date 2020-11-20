package com.example.dictionaryapplication.model;

import java.io.Serializable;


public class Word implements Serializable {

    private int id;
    private String word;
    private String def;


    public Word(int id, String word, String def) {
        this.id = id;
        this.word = word;
        this.def = def;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }
}
