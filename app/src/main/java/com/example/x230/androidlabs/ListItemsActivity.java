package com.example.x230.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {
    protected static final String ACTIVITY_NAME="ListItemsActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        ImageButton cameraButton = (ImageButton)findViewById(R.id.camera);

        cameraButton.setOnClickListener( click ->
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        });

        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener((e,f)->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            //user clicked OK button
                            Intent resultIntent = new Intent();
                            String response = getString(R.string.response);
                            resultIntent.putExtra("Response",response);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            //user cancelled the dialog

                        }
                    })
                    .show();
        });

        Switch switchOff = (Switch)findViewById(R.id.switchOff);
        switchOff.setOnCheckedChangeListener((u,v) ->
        {
            if(switchOff.isChecked()){
                String text = getString(R.string.switch_On);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, (CharSequence)text, duration);
                toast.show();
            }
            else{
                String text = getString(R.string.switch_Off);
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(this, (CharSequence)text, duration);
                toast.show();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageButton cameraButton = (ImageButton)findViewById(R.id.camera);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            cameraButton.setImageBitmap(imageBitmap);
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
