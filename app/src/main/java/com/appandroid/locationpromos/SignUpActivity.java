package com.appandroid.locationpromos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.appandroid.locationpromos.database.dbhelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    dbhelper dbhelper;
    MaterialButton btnadd ;
    TextInputEditText usernameedt,emailedt,phoneedt, passwordedt,passwordedt2 ;
    CheckBox useredt,retaileredt ;




    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dbhelper = new dbhelper(getApplicationContext());

        btnadd = (MaterialButton) findViewById(R.id.adduserbtn);
        usernameedt = (TextInputEditText) findViewById(R.id.username_edit_text);
        passwordedt = (TextInputEditText) findViewById(R.id.password_edit_text);
        phoneedt = (TextInputEditText) findViewById(R.id.telephone_edit_text);
        emailedt = (TextInputEditText) findViewById(R.id.email_edit_text);
        passwordedt2 =(TextInputEditText)findViewById(R.id.password_edit_text2);
        useredt = (CheckBox) findViewById(R.id.checkbox_user);
        retaileredt=(CheckBox) findViewById(R.id.checkbox_retailer);


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean noErrors = true;
                String usernametst = usernameedt.getText().toString();
                String emailedtst = emailedt.getText().toString() ;
                String phoneedtst = phoneedt.getText().toString() ;
                String passwordedtst = passwordedt.getText().toString();
                String passwrodedtst2 = passwordedt2.getText().toString();
                String user_type = "";
                if(useredt.isChecked()){
                    user_type="user";
                }else if(retaileredt.isChecked()){
                    user_type="retailer";
                }
                if(!useredt.isChecked() && !retaileredt.isChecked()){
                    noErrors = false;
                    Toast.makeText(getApplicationContext(),"you need to check one of the checkbox",Toast.LENGTH_LONG).show();
                    retaileredt.setError("you need to check one of the checkbox");
                }

                if (usernametst.isEmpty()) {
                    usernameedt.setError("Please fill out all fields completely. ");
                    noErrors = false;
                } else {
                    usernameedt.setError(null);
                }
                if (emailedtst.isEmpty()) {
                    emailedt.setError("Please fill out all fields completely. ");
                    noErrors = false;
                } else {
                    emailedt.setError(null);
                }
                if (phoneedtst.isEmpty()) {
                    phoneedt.setError("Please fill out all fields completely. ");
                    noErrors = false;
                } else {
                    phoneedt.setError(null);
                }
                if (passwordedtst.isEmpty()) {
                    passwordedt.setError("Please fill out all fields completely. ");
                    noErrors = false;
                } else {
                    passwordedt.setError(null);
                }
                if (!passwordedtst.equals(passwrodedtst2)) {
                    passwordedt.setError("the two passwords not identical");
                    passwordedt2.setError("the two passwords not identical");
                    noErrors = false;
                } else {
                    passwordedt.setError(null);
                    passwordedt2.setError(null);
                }
                if(!emailValidator(emailedtst.toString())){
                    emailedt.setError("email non valid");
                    noErrors=false;
                }


                if (noErrors) {

               addNewUser us=    new addNewUser(getApplicationContext(), usernametst,emailedtst,passwordedtst,user_type,phoneedtst,dbhelper);
                us.execute();

                }

            }
        });


    }








    private class addNewUser extends AsyncTask<Void,Void,Boolean> {
        String username , password , email ,userphone, user_type;
        Context ct;
        dbhelper dbh;
        public addNewUser(Context applicationContext, String usernametst, String emailedtst, String passwordedtst, String user_type, String phoneedtst,dbhelper dbh) {
            this.username =usernametst;
            this.ct = applicationContext;
            this.password= passwordedtst;
            this.email = emailedtst;
            this.userphone=phoneedtst;
            this.user_type=user_type;
            this.dbh=dbh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(ct, "wait !!! ", Toast.LENGTH_LONG).show();
        }



        @Override
        protected Boolean doInBackground(Void... voids) {
            if(dbh.emailExist(email)){
            /*    new AlertDialog.Builder(SignUpActivity.this)
                        .setTitle("email already exist")
                        .setMessage("Email already exist" )
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/
              emailedt.setError("email already exist");
            return false;

            }else{

                ContentValues values = new ContentValues();
                values.put("user_name",username);
                values.put("user_email", email);
                values.put("user_password", password);
                values.put("user_phone", userphone);
                values.put("user_type", user_type);

                dbh.Insert(values,"user");

                return true;

            }
        }
        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if(s){
           //     Toast.makeText(getApplicationContext(),"user added",Toast.LENGTH_LONG);
                Intent login = new Intent(SignUpActivity.this,LoginActivity.class);
                  SignUpActivity.this.startActivity(login);
            }else{
                Toast.makeText(getApplicationContext(),"something wrong",Toast.LENGTH_LONG);
            }


        }




    }



    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }



    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        CheckBox c1 = (CheckBox) findViewById(R.id.checkbox_user) ;
        CheckBox c2 = (CheckBox) findViewById(R.id.checkbox_retailer) ;
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_user:
                if (checked){
                    c2.setChecked(false);
                }

                break;
            case R.id.checkbox_retailer:
                if (checked){
                c1.setChecked(false);
                }


                break;

        }
    }
}