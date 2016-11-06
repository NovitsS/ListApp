package com.appitem.uniquetest.myapplication.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HUSTy on 2016/11/5.
 */
public class ListDb extends SQLiteOpenHelper {
    public static final String ID="_id";
    public static final String TITLE="title";
    public static final String RANK="rank";
    public static final String FINISH="finish";
    public static final String CTIME="ctime";
    public static final String FTIME="ftime";
    public static final String CONTENT="content";
    public static final String TABNAME="ListDb";
    public static final String UFTABNAME="UFListDb";
    public static final String FTABNAME="FListDb";
    public Context context;
    public ListDb(Context context) {
        super(context, "ListDb", null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TABNAME+"("+
                        ID+" integer primary key autoincrement,"+
                        TITLE+" text,"+
                        RANK+" text,"+
                        FINISH+" text,"+
                        CTIME+" text,"+
                        FTIME+" text,"+
                        CONTENT+" text"+")"

        );

        db.execSQL(
                "CREATE TABLE "+UFTABNAME+"("+
                        ID+" integer primary key autoincrement,"+
                        TITLE+" text,"+
                        RANK+" text,"+
                        FINISH+" text,"+
                        CTIME+" text,"+
                        FTIME+" text,"+
                        CONTENT+" text"+")"

        );

        db.execSQL(
                "CREATE TABLE "+FTABNAME+"("+
                        ID+" integer primary key autoincrement,"+
                        TITLE+" text,"+
                        RANK+" text,"+
                        FINISH+" text,"+
                        CTIME+" text,"+
                        FTIME+" text,"+
                        CONTENT+" text"+")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
