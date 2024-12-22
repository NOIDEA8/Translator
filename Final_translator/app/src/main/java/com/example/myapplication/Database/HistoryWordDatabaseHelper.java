package com.example.myapplication.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HistoryWordDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_HISTORYWORDS = "create table HistoryWords(" +
            "id integer primary key autoincrement," +
            "origin text," +
            "fromLanguage text," +
            "translated text," +
            "toLanguage text," +
            "account text)";
    private Context mcontext;

    public HistoryWordDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORYWORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
