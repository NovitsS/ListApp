package com.appitem.uniquetest.myapplication.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appitem.uniquetest.myapplication.R;
import com.appitem.uniquetest.myapplication.datebase.DbOpera;
import com.appitem.uniquetest.myapplication.datebase.ListDb;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HUSTy on 2016/11/5.
 */
public class ShowItemActivity extends AppCompatActivity
        implements View.OnClickListener,DatePickerDialog.OnDateSetListener,DialogInterface.OnClickListener{
    private EditText mTitle,mText;
    private Button mReverseBtn,mRetuBtn,mReRankbtn,mReTimebtn;
    private int position;
    private Cursor cursor;
    private String rank,title,cTime,fTime,content;
    private String[] ranks=new String[]{"A","B","C"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showitem);
        mTitle=(EditText) findViewById(R.id.show_title);
        mText=(EditText) findViewById(R.id.show_text);
        mReverseBtn=(Button)findViewById(R.id.reverse);
        mRetuBtn=(Button)findViewById(R.id.retu);
        mReverseBtn.setOnClickListener(this);
        mRetuBtn.setOnClickListener(this);
        Bundle bundle=getIntent().getExtras();
        position=bundle.getInt("position");
        cursor= DbOpera.mReader.query(ListDb.TABNAME,null,null,null,null,null,null);
        cursor.moveToPosition(position);
        mTitle.setText(cursor.getString(cursor.getColumnIndex(ListDb.TITLE)));
        mText.setText(cursor.getString(cursor.getColumnIndex(ListDb.CONTENT)));
        rank=cursor.getString(cursor.getColumnIndex(ListDb.RANK));
        mReRankbtn=(Button)findViewById(R.id.reRank);
        mReTimebtn=(Button)findViewById(R.id.reTime);
        mReRankbtn.setOnClickListener(this);
        mReTimebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reverse:
                addDB(DbOpera.mWriter);
                finish();
                break;
            case R.id.retu:
                finish();
                break;
            case R.id.reRank:
                int num;
                if(rank.equals("A"))
                    num=0;
                else {
                    if(rank.equals("B"))
                        num=1;
                    else num=2;
                }
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setSingleChoiceItems(ranks,num,this);
                dialog.setPositiveButton("确定",null);
                dialog.show();
                break;
            case R.id.reTime:
                Calendar d=Calendar.getInstance(Locale.CHINA);
                Date mDate=new Date();
                d.setTime(mDate);
                int year=d.get(Calendar.YEAR);
                int month=d.get(Calendar.MONTH);
                int day=d.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dlg=new DatePickerDialog(ShowItemActivity.this,ShowItemActivity.this,year,month,day);
                dlg.show();
                break;
        }
    }

    private void addDB(SQLiteDatabase writer){
        ContentValues values=new ContentValues();
        title=mTitle.getText().toString();
        content=mText.getText().toString();
        cTime=cursor.getString(cursor.getColumnIndex(ListDb.CTIME));
        values.put(ListDb.TITLE,title);
        values.put(ListDb.FTIME,fTime);
        values.put(ListDb.CONTENT,content);
        values.put(ListDb.RANK,rank);
        String[] str={cTime};
        writer.update(ListDb.TABNAME,values,ListDb.CTIME+"=?",str);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        fTime=Integer.toString(year)+"年"+Integer.toString(monthOfYear)+"月"+Integer.toString(dayOfMonth)+"日";
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        rank=ranks[which];
    }
}
