package com.example.x230.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = (EditText)findViewById(R.id.emailAddress);

        SharedPreferences prefs = getSharedPreferences("MyNewSaveFile", Context.MODE_PRIVATE);
        String userString = prefs.getString("DefaultEmail","email@domain.com");

        email.setText(userString);

        Button login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener( click ->
        {
            String input = email.getText().toString();

            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("DefaultEmail", input);


            edit.commit();//write to disk

            Intent intent = new Intent(LoginActivity.this, StartActivity.class);
            startActivity(intent);

        });
    }


    protected void onResume(){
        Log.i(ACTIVITY_NAME, "In onResume");
        super.onResume();
    }
    protected void onStart(){
        Log.i(ACTIVITY_NAME, "In onStart");
        super.onStart();
    }
    protected void onPause(){
        Log.i(ACTIVITY_NAME, "In onPause");
        super.onPause();
    }
    protected void onStop(){
        Log.i(ACTIVITY_NAME, "In onStop");
        super.onStop();
    }
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "In onDestroy");
        super.onDestroy();
    }
}
