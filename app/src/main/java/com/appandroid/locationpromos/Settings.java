package com.appandroid.locationpromos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.appandroid.locationpromos.utils.SharedPref;

public class Settings extends AppCompatActivity {

    AudioManager audioManager;
    SwitchCompat switc ;
    Button logout , returnbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switc = (SwitchCompat) findViewById(R.id.silent);

        logout= (Button)findViewById(R.id.logoutt) ;
        returnbtn = (Button)findViewById(R.id.returnid);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }

        Boolean Check = Boolean.valueOf(SharedPref.readSharedSetting(Settings.this,"silent","False"));
        if(Check){
            switc.setChecked(true);
        }else{
            switc.setChecked(false);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.saveSharedSetting(Settings.this,"userconnect","False");
                Intent logout = new Intent(Settings.this,LoginActivity.class);
                startActivity(logout);
                finish();


            }
        });
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent logout = new Intent(Settings.this,UserActivity.class);
                startActivity(logout);
                finish();
            }
        });


        switc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean switchState = switc.isChecked();

                if(switchState){
                    state();
             //       Toast.makeText(getApplicationContext(),"mode silent activated",Toast.LENGTH_SHORT).show();
                    SharedPref.saveSharedSetting(Settings.this,"silent","True");
                }else{
                    state();
             //      Toast.makeText(getApplicationContext(),"mode silent disactivated",Toast.LENGTH_SHORT).show();
                    SharedPref.saveSharedSetting(Settings.this,"silent","False");
                }
            }
        });
    }


    @Override

    protected void onStart(){
        state();
        super.onStart();
    }


    @Override

    protected void onResume(){
        state();
        super.onResume();
    }
    private void state(){
        if(audioManager.getRingerMode() == audioManager.RINGER_MODE_SILENT){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
         //   silent.setText("Silent");
            Toast.makeText(Settings.this, "Normal-Mode", Toast.LENGTH_SHORT).show();
        }else{
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
         //   silent.setText("Normal");
            Toast.makeText(Settings.this, "Silent-Mode", Toast.LENGTH_SHORT).show();

        }
    }

}