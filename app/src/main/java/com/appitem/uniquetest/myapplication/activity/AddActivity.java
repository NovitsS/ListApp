package com.appitem.uniquetest.myapplication.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appitem.uniquetest.myapplication.R;
import com.appitem.uniquetest.myapplication.datebase.DbOpera;
import com.appitem.uniquetest.myapplication.datebase.ListDb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HUSTy on 2016/11/5.
 */
public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private EditText mEditTextTitle,mEditTextText;
    private TextView mTextView;
    private Button mButtonSave,mButtonDelete,mButtonFTime;
    private RadioGroup rg;
    private String rank,title,finish="未完成",cTime,fTime=null,content;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add);
        mEditTextTitle=(EditText)findViewById(R.id.add_title);
        mEditTextText=(EditText)findViewById(R.id.add_text);
        mTextView=(TextView)findViewById(R.id.add_ftime);
        mButtonSave=(Button)findViewById(R.id.add_save);
        mButtonDelete=(Button)findViewById(R.id.add_delete);
        mButtonFTime=(Button)findViewById(R.id.add_fbutton);

        mButtonFTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Calendar d=Calendar.getInstance(Locale.CHINA);
                Date mDate=new Date();
                d.setTime(mDate);
                int year=d.get(Calendar.YEAR);
                int month=d.get(Calendar.MONTH);
                int day=d.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dlg=new DatePickerDialog(AddActivity.this,AddActivity.this,year,month,day);
                dlg.show();
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                title=mEditTextTitle.getText().toString();
                cTime=getTime();
                content=mEditTextText.getText().toString();
                addDB(DbOpera.mWriter);
                finish();
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rg=(RadioGroup)findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbtn_A:
                        rank="A";
                        break;
                    case R.id.rbtn_B:
                        rank="B";
                        break;
                    case R.id.rbtn_C:
                        rank="C";
                        break;
                }
            }
        });
    }

    private void addDB(SQLiteDatabase writer){
        ContentValues values=new ContentValues();
        values.put(ListDb.TITLE,title);
        values.put(ListDb.RANK,rank);
        values.put(ListDb.FINISH,finish);
        values.put(ListDb.CTIME,cTime);
        values.put(ListDb.FTIME,fTime);
        values.put(ListDb.CONTENT,content);
        writer.insert(ListDb.TABNAME,null,values);
        writer.insert(ListDb.UFTABNAME,null,values);
    }

    private String getTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date=new Date();
        String time=format.format(date);
        return time;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        fTime=Integer.toString(year)+"年"+Integer.toString(monthOfYear)+"月"+Integer.toString(dayOfMonth)+"日";
        mTextView.setText(fTime);
    }
}
