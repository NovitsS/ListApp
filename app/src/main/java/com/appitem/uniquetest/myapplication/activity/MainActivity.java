package com.appitem.uniquetest.myapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appitem.uniquetest.myapplication.R;
import com.appitem.uniquetest.myapplication.datebase.DbOpera;
import com.appitem.uniquetest.myapplication.datebase.ListDb;
import com.appitem.uniquetest.myapplication.recyclerviewadapter.RecyclerViewAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RecyclerViewAdapter.OnRecyclerViewItemListener {

    private RecyclerView recyclerView;
    private DbOpera mDbOpera;
    private RecyclerViewAdapter adapter;
    private Button mSearchbtn;
    private EditText mSearchEdit;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDbOpera=new DbOpera(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecyclerViewAdapter(this,DbOpera.mReader.query(ListDb.TABNAME,null,null,null,null,null,null));
        adapter.setOnRecyclerViewItemListener(this);
        recyclerView.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback mCallback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position=viewHolder.getAdapterPosition();
                Cursor cursor=DbOpera.mReader.query(ListDb.TABNAME,null,null,null,null,null,null);
                cursor.moveToPosition(position);
                String ctime=cursor.getString(cursor.getColumnIndex(ListDb.CTIME));
                if(direction==ItemTouchHelper.RIGHT){
                    DbOpera.deleteItem(ctime);
                    adapter.notifyItemRemoved(position);
                    onResume();
                }else{
                    DbOpera.deleteUFItem(ctime);
                    DbOpera.setFinish(position);
                    DbOpera.copyToOtherList(position);
                    onResume();

                }
            }
        };
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        mSearchbtn=(Button)findViewById(R.id.content_button);
        mSearchEdit=(EditText)findViewById(R.id.content_edittext);
        mSearchbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Integer position;
                str=mSearchEdit.getText().toString();
                position=DbOpera.searchItem(str);
                mSearchEdit.setText("");
                if(position==null) {
                    Toast.makeText(MainActivity.this, "搜索的item不存在", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent=new Intent(MainActivity.this,ShowItemActivity.class);
                    intent.putExtra("position",(int)position);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.item_time:
                adapter=new RecyclerViewAdapter(this,DbOpera.mReader.query(ListDb.TABNAME,null,null,null,null,null,null));
                recyclerView.setAdapter(adapter);
                return true;
            case R.id.item_rank:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            Intent intent=new Intent(this,AddActivity.class);
            startActivity(intent);
        }  else if (id == R.id.nav_finished) {
            adapter=new RecyclerViewAdapter(this,DbOpera.mReader.query(ListDb.FTABNAME,null,null,null,null,null,null));
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_unfinished) {
            adapter=new RecyclerViewAdapter(this,DbOpera.mReader.query(ListDb.UFTABNAME,null,null,null,null,null,null));
            recyclerView.setAdapter(adapter);
        } else if (id == R.id.nav_all) {
            adapter=new RecyclerViewAdapter(this,DbOpera.mReader.query(ListDb.TABNAME,null,null,null,null,null,null));
            recyclerView.setAdapter(adapter);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onResume(){
        super.onResume();
        adapter=new RecyclerViewAdapter(this,DbOpera.mReader.query(ListDb.TABNAME,null,null,null,null,null,null));
        adapter.setOnRecyclerViewItemListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent=new Intent(this,ShowItemActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }

}
