package com.appandroid.locationpromos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appandroid.locationpromos.adapter.MyAdapter;
import com.appandroid.locationpromos.database.dbhelper;
import com.appandroid.locationpromos.models.BrandModel;
import com.appandroid.locationpromos.utils.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RetailerActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    SwipeRefreshLayout refresh ;
    MyAdapter myAdapter;
    dbhelper dbhelper;
    public ArrayList<BrandModel> mDataList= new ArrayList<BrandModel>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_login) {
            SharedPref.saveSharedSetting(RetailerActivity.this,"userconnect","False");
            Intent logout = new Intent(RetailerActivity.this,LoginActivity.class);
            startActivity(logout);
            finish();
            return true;


        }else if (id == R.id.nav_settings) {

            Intent settings = new Intent(RetailerActivity.this,Settings.class);
            startActivity(settings);
            //  finish();
            return true;


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer);
      //  refresh = findViewById(R.id.swiperefreshretailer);
        dbhelper = new dbhelper(getApplicationContext());
        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewretailer);
        int user_id = SharedPref.readSharedSettingint(RetailerActivity.this,"user_id",0);
        mDataList = dbhelper.getbrandsByuser( user_id);
        fab  = findViewById(R.id.fabretailer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Snackbar.make(v, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
                startActivity(new Intent(getApplicationContext(),addCompany.class));
            }
        });
        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefreshretailer);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int user_id = SharedPref.readSharedSettingint(RetailerActivity.this,"user_id",0);
                mDataList = dbhelper.getbrandsByuser( user_id);
                myAdapter = new MyAdapter(RetailerActivity.this, mDataList,dbhelper);
                recyclerView.setAdapter(myAdapter);
                LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getApplicationContext());
                mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagert);
                refresh.setRefreshing(false);
            }
        });
       myAdapter = new MyAdapter(RetailerActivity.this, mDataList,dbhelper);
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagert);
        TextInputEditText searchcontact = (TextInputEditText) findViewById(R.id.searchretailer);
        searchcontact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (RetailerActivity.this).myAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("are you sure want to exit the application?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}