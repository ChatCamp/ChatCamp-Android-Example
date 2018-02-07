package io.chatcamp.app;

import android.util.Log;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;

import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ChatCamp.init(this, "6346990561630613504");
//        ChatCamp.init(this, "6365171677000626176");
//        ChatCamp.init(this, "6359014142933725184");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button bConnect = (Button) findViewById(R.id.button_connect);
        final EditText userId = (EditText) findViewById(R.id.edit_view_id);
        final EditText userName = (EditText) findViewById(R.id.edit_view_name);
//        Log.d("CHATCAMP APP", FirebaseInstanceId.getInstance().getToken());


        bConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userId.getText().toString().length() == 0) {
                    userId.setError("Cannot be empty");
                }
                else if(userName.getText().toString().length() == 0) {
                    userName.setError("Cannot be empty");
                }
                else {
                    ChatCamp.connect(userId.getText().toString(), new ChatCamp.ConnectListener() {
                        @Override
                        public void onConnected(User user, ChatCampException e) {
                            System.out.println("CONNECTED");
                            ChatCamp.updateUserDisplayName(userName.getText().toString(), new ChatCamp.UserUpdateListener() {
//                            ChatCamp.updateUserProfileUrl("https://iflychat.com", new ChatCamp.UserUpdateListener() {
                                @Override
                                public void onUpdated(User user, ChatCampException e) {
                                    System.out.println("UPDATE DISPLAY NAME" + user.getDisplayName());

                                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                    startActivity(intent);

                                    Log.d("CHATCAMP APP", FirebaseInstanceId.getInstance().getToken());




                                }
                            });
                            ChatCamp.updateUserPushToken(FirebaseInstanceId.getInstance().getToken(), new ChatCamp.UserPushTokenUpdateListener() {
                                @Override
                                public void onUpdated(User user, ChatCampException e) {
                                    Log.d("CHATCAMP_APP", "PUSH TOKEN REGISTERED");

                                }
                            });
                        }
                    });
                }


//                Snackbar.make(view, "Hello".concat("Replace with your own action"), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
