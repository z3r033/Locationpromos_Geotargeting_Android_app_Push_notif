package com.appandroid.locationpromos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.appandroid.locationpromos.utils.SharedPref;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int time = 1000;
    ProgressBar mProgressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgressBar = (ProgressBar) findViewById(R.id.splashprogrssbar);
        new BackgroundSplashTask().execute();
    }
    private class BackgroundSplashTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mProgressBar.setVisibility(View.GONE);
            CheckSession();
            finish();
        }


    }

    private void CheckSession () {
        Boolean CheckSessionb = Boolean.valueOf(SharedPref.readSharedSetting(SplashScreenActivity.this,"userconnect","False"));

        Intent intoLogin = new Intent(SplashScreenActivity.this,LoginActivity.class);
        Intent intoUser = new Intent (SplashScreenActivity.this,UserActivity.class);
        Intent intoRetailer = new Intent (SplashScreenActivity.this,RetailerActivity.class);
        if(CheckSessionb){
            String user_type=SharedPref.readSharedSetting(SplashScreenActivity.this,"user_type","user");
            if(user_type.equals("user")){
                startActivity(intoUser);
                finish();
            }else if (user_type.equals("retailer")){
                startActivity(intoRetailer);
                finish();
            }

        }
        else
        {
            startActivity(intoLogin);
            finish();
        }

    }
}