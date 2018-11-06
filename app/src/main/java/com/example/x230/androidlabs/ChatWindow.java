package com.example.x230.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.x230.androidlabs.ChatWindow.ChatDatabaseHelper.KEY_ID;
import static com.example.x230.androidlabs.ChatWindow.ChatDatabaseHelper.KEY_MESSAGE;
import static com.example.x230.androidlabs.ChatWindow.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends Activity {
    private static final String TAG = ChatWindow.class.getSimpleName();
    ListView listView;
    Button sendButton;
    EditText editText;
    ArrayList<String> chatMessages = new ArrayList<>();

    protected void onDestroy(Bundle savedInstanceState) {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_MESSAGE}, null, null, null, null, null, null);

        Log.i(TAG, "Cursor's column count=" + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(TAG, "Column Names: " + cursor.getColumnName(i));
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final String msg = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            chatMessages.add(msg);
            Log.i(TAG, "SQL MESSAGE: " + msg);

            cursor.moveToNext();
        }


        listView = (ListView) findViewById(R.id.listViewChat);
        sendButton = (Button) findViewById(R.id.sendButton);
        editText = (EditText) findViewById(R.id.editTextChat);

        ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(click ->
        {
            chatMessages.add(editText.getText().toString());
            messageAdapter.notifyDataSetChanged();

            ContentValues cv = new ContentValues();
            cv.put(KEY_MESSAGE, editText.getText().toString());
            db.insert(TABLE_NAME, "", cv);

            editText.setText("");
        });

    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return chatMessages.size();
        }

        public String getItem(int position) {
            return chatMessages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }

        public long getItemId(int position) {
            return position;  //for now we're not using SQL: we'll change this later.
        }


    }

    class ChatDatabaseHelper extends SQLiteOpenHelper {

        public final static String KEY_ID = "Integers";
        public final static String KEY_MESSAGE = "Message";
        public final static String DATABASE_NAME = "Messages.db";
        public final static int VERSION_NUM = 2;
        public final static String TABLE_NAME = "Messages";

        public ChatDatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.i("ChatDatabaseHelper", "Calling onCreate");
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_MESSAGE + " string not null);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion = " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

    }

}