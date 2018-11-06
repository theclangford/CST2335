package com.example.x230.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


public class StartActivity extends Activity {
    protected static final String ACTIVITY_NAME="StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button button = findViewById(R.id.button);
        button.setOnClickListener( click ->
        {
            Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
            startActivityForResult(intent,50);
        });

        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(click->
        {
            Log.i(ACTIVITY_NAME, "User clicked Start Chat.");
            Intent intent = new Intent(StartActivity.this, ChatWindow.class);
            startActivityForResult(intent,50);
        });

        Button weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(click ->
        {
            Log.i(ACTIVITY_NAME,"User clicked Weather Forecast." );
            Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
            startActivityForResult(intent,60);
        });

    }
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        if((responseCode == Activity.RESULT_OK)&&(requestCode == 50)){
            Log.i(ACTIVITY_NAME,"Returned to StartActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            String text = "ListItemsActivity passed: ";
            text += messagePassed;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }


    @Override
    protected void onResume(){
        Log.i(ACTIVITY_NAME, "In onResume");
        super.onResume();

    }
    @Override
    protected void onStart(){
        Log.i(ACTIVITY_NAME, "In onStart");
        super.onStart();
    }
    @Override
    protected void onPause(){
        Log.i(ACTIVITY_NAME, "In onPause");
        super.onPause();
    }
    @Override
    protected void onStop(){
        Log.i(ACTIVITY_NAME, "In onStop");
        super.onStop();
    }
    @Override
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "In onDestroy");
        super.onDestroy();
    }
}
