package com.appandroid.locationpromos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appandroid.locationpromos.database.dbhelper;
import com.appandroid.locationpromos.models.UserModel;
import com.appandroid.locationpromos.utils.SharedPref;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {


    final private int REQUEST_CODE_ASK_PERMISSIONS = 18;
    Boolean autorisation = false;
    MaterialButton btnlogin,btnsign;
  dbhelper dbhelper;
    TextInputEditText usernametxt, passwordtxt;
    MaterialCheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CheckUserPermsions();
        dbhelper = new dbhelper(getApplicationContext());
        usernametxt = (TextInputEditText) findViewById(R.id.edtemail2);
        passwordtxt = (TextInputEditText) findViewById(R.id.edtpassword2);
        btnlogin = (MaterialButton) findViewById(R.id.btnlogin2);
        btnsign = (MaterialButton) findViewById(R.id.sign_button);
        mCheckBox = (MaterialCheckBox) findViewById(R.id.checkboxadmin2);



        mCheckBox.setChecked(true);

        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernametxt.getText().toString();
                String password = passwordtxt.getText().toString();

                if ((username.length() <= 0 || username == null) || (password.length() <= 0 || password == null)) {
                    usernametxt.setError("Please fill out all fields completely.");
                } else {
                    new LoginAuth(LoginActivity.this, usernametxt, passwordtxt,mCheckBox,dbhelper).execute();

                }

            }
        });
    }




    private class LoginAuth extends AsyncTask<Void,Void,String> {

        Context c;

        EditText usernametx, passwordtx;
        ProgressBar pd;
        UserModel mUser;
        CheckBox mCheckBox;
        dbhelper dbh;
        public LoginAuth(Context c, EditText username, EditText password, CheckBox mCheckBox,dbhelper dbh) {
this.dbh=dbh;
            this.c = c;

            this.usernametx = username;
            this.passwordtx = password;
            this.mCheckBox = mCheckBox;

            mUser = new UserModel();

            mUser.setUser_email(username.getText().toString());
            mUser.setUser_password(password.getText().toString());

        }


        @Override
        protected String doInBackground(Void... voids) {
            return this.login();
        }

        private String login() {

            UserModel m = dbh.UserExists(mUser.getUser_email(), mUser.getUser_password());
            if(m==null){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"email or password not correct , try another time",Toast.LENGTH_LONG).show();

                    }
                });
            }else{
            if (m.getUser_email().equals(mUser.getUser_email()) && m.getUser_password().equals(mUser.getUser_password())) {

                SharedPref.saveSharedSettingint(c, "user_id", m.getUser_id());
                SharedPref.saveSharedSetting(c, "user_name", String.valueOf(m.getUser_name()));
                SharedPref.saveSharedSetting(c, "user_type", String.valueOf(m.getUser_type()));
                SharedPref.saveSharedSetting(c, "user_email", String.valueOf(m.getUser_email()));

                if (mCheckBox.isChecked()) {
                    SharedPref.saveSharedSetting(c, "userconnect", "True");
                }
                if(m.getUser_type().equals("user")) {
                    c.startActivity(new Intent(c, UserActivity.class));
                }else{
                    c.startActivity(new Intent(c, RetailerActivity.class));
                }


            }
            }

            String response = "";


            return response;


        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            Toast.makeText(c, s + "", Toast.LENGTH_LONG).show();


        }


    }
    void CheckUserPermsions(){

        String[] autorisations={Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.INTERNET,  Manifest.permission.WAKE_LOCK
                ,Manifest.permission.FOREGROUND_SERVICE};
        if ( Build.VERSION.SDK_INT >= 23){

            for(String autorisation :autorisations) {
                if (ActivityCompat.checkSelfPermission(this, autorisation) !=
                        PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                    Manifest.permission.INTERNET  ,
                                    Manifest.permission.WAKE_LOCK ,
                                    Manifest.permission.FOREGROUND_SERVICE},
                            REQUEST_CODE_ASK_PERMISSIONS
                    );
                    return;
                }
            }
        }
        autorisation=true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);

                    startActivity(i);
                    autorisation=true;
                } else {
                    Toast.makeText( this,  "check permissions" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}