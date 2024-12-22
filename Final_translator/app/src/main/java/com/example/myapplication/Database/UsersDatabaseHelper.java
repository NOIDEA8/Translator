package com.example.myapplication.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USERS="create table Users ("+
        "id integer primary key autoincrement," +
            "account text," +
            "password text," +
            "name text)";
    private Context mcontext;
    public UsersDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mcontext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USERS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
