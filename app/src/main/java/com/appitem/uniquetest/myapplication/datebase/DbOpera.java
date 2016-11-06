package com.appitem.uniquetest.myapplication.datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by HUSTy on 2016/11/5.
 */
public class DbOpera {
    public static ListDb mListDb;
    public static SQLiteDatabase mReader,mWriter;
    public DbOpera(Context context){
        mListDb=new ListDb(context);
        mReader=mListDb.getReadableDatabase();
        mWriter=mListDb.getWritableDatabase();
    }

    public static void deleteItem(String ctime){
        mWriter.delete(ListDb.TABNAME,ListDb.CTIME+"=?",new String[]{ctime});
        mWriter.delete(ListDb.UFTABNAME,ListDb.CTIME+"=?",new String[]{ctime});
        mWriter.delete(ListDb.FTABNAME,ListDb.CTIME+"=?",new String[]{ctime});
    }

    public static void deleteUFItem(String ctime){
        mWriter.delete(ListDb.UFTABNAME,ListDb.CTIME+"=?",new String[]{ctime});
    }

    public static Integer searchItem(String str){
        Cursor cursor=mReader.query(ListDb.TABNAME,null,null,null,null,null,null);
        String name;
        int length=cursor.getCount(),i;
        for(i=0;i<length;i++){
            cursor.moveToPosition(i);
            name=cursor.getString(cursor.getColumnIndex(ListDb.TITLE));
            if(name.equals(str))
                return i;
        }
        return null;
    }

    public static void copyToOtherList(int position){
        Cursor cursor=mReader.query(ListDb.TABNAME,null,null,null,null,null,null);
        cursor.moveToPosition(position);
        ContentValues values=new ContentValues();
        values.put(ListDb.TITLE,cursor.getString(cursor.getColumnIndex(ListDb.TITLE)));
        values.put(ListDb.RANK,cursor.getString(cursor.getColumnIndex(ListDb.RANK)));
        values.put(ListDb.CTIME,cursor.getString(cursor.getColumnIndex(ListDb.CTIME)));
        values.put(ListDb.FTIME,cursor.getString(cursor.getColumnIndex(ListDb.FTIME)));
        values.put(ListDb.FINISH,cursor.getString(cursor.getColumnIndex(ListDb.FTIME)));
        values.put(ListDb.CONTENT,cursor.getString(cursor.getColumnIndex(ListDb.CONTENT)));
        mWriter.insert(ListDb.FTABNAME,null,values);
    }

    public static void setFinish(int position){
        Cursor cursor=mReader.query(ListDb.TABNAME,null,null,null,null,null,null);
        cursor.moveToPosition(position);
        String ctime=cursor.getString(cursor.getColumnIndex(ListDb.CTIME));
        ContentValues values=new ContentValues();
        values.put(ListDb.FINISH,"已完成");
        mWriter.update(ListDb.TABNAME,values,ListDb.CTIME+"=?",new String[]{ctime});
    }
}
