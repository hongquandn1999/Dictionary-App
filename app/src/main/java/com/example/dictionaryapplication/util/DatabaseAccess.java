package com.example.dictionaryapplication.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dictionaryapplication.model.Word;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all words from anh_viet dictionary
     *
     * @return a List of word from dictionary
     */
    public List<String> getWords() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet limit 30", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public ArrayList<String> getWords(String filter) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where word like '"+ filter +"%' limit 10", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getDefinition(String word) {
        String definition = "";
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where word='"+ word +"'", null);
        cursor.moveToFirst();

        definition  = cursor.getString(2);

        cursor.close();
        return definition;
    }

    public int getIdByWord(String word) {
        int id = 0;
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where word='"+ word +"'", null);
        cursor.moveToFirst();

        id  = cursor.getInt(0);

        cursor.close();
        return id;
    }

    public long addNewWord(String word, String def){
        ContentValues contentValues = new ContentValues();
        contentValues.put("word", word);
        contentValues.put("content", def);
        long d = database.insert("anh_viet",null,contentValues);
        database.close();
        return d;
    }

    public long addFavo(int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        long d = database.insert("favorite",null,contentValues);
        database.close();
        return d;
    }

    public List<Integer> getFavos() {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM favorite", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void deleteFavo(int id){
        database.execSQL("delete from favorite where id='"+id+"'");
    }

    public Word getWordById(int id) {
        String word = "", def = "";
        int i = 0;
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where id='"+ id +"'", null);
        cursor.moveToFirst();
        i = cursor.getInt(0);
        word  = cursor.getString(1);
        def = cursor.getString(2);
        Word d = new Word(i, word, def);
        cursor.close();
        return d;
    }
}