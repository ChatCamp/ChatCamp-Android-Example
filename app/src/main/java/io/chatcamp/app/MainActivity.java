package io.chatcamp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.PreviousMessageListQuery;
import io.chatcamp.sdk.User;

public class MainActivity extends AppCompatActivity {

    private EditText userId;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(LocalStorage.getInstance().getUserId())
                && !TextUtils.isEmpty(LocalStorage.getInstance().getUsername())) {
            connectToChatSdk(LocalStorage.getInstance().getUserId(),
                    LocalStorage.getInstance().getUsername(), false);
            return;
        }
        setUpView();
    }

    private void setUpView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        ChatCamp.init(this, "6365171677000626176");
//        ChatCamp.init(this, "6359014142933725184");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button bConnect = (Button) findViewById(R.id.button_connect);
        userId = (EditText) findViewById(R.id.edit_view_id);
        userName = (EditText) findViewById(R.id.edit_view_name);
//        Log.d("CHATCAMP APP", FirebaseInstanceId.getInstance().getToken());


        bConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userId.getText().toString().length() == 0) {
                    userId.setError("Cannot be empty");
                } else if (userName.getText().toString().length() == 0) {
                    userName.setError("Cannot be empty");
                } else {
                    connectToChatSdk(userId.getText().toString(), userName.getText().toString(), true);
                }


//                Snackbar.make(view, "Hello".concat("Replace with your own action"), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });
    }

    public void connectToChatSdk(final String userId, final String username, final boolean buttonClicked) {
        ChatCamp.init(this, Constant.APP_ID);
        ChatCamp.connect(userId, new ChatCamp.ConnectListener() {
            @Override
            public void onConnected(User user, ChatCampException e) {
                if (e != null) {
                    if (!buttonClicked) {
                        LocalStorage.getInstance().setUserId("");
                        LocalStorage.getInstance().setUsername("");
                        setUpView();
                    } else {
                        Snackbar.make(MainActivity.this.userId, "Something went wrong", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    System.out.println("CONNECTED");
                    LocalStorage.getInstance().setUserId(user.getId());
                    LocalStorage.getInstance().setUsername(username);
                    ChatCamp.updateUserDisplayName(username, new ChatCamp.UserUpdateListener() {
                        //                            ChatCamp.updateUserProfileUrl("https://iflychat.com", new ChatCamp.UserUpdateListener() {
                        @Override
                        public void onUpdated(User user, ChatCampException e) {
                            System.out.println("UPDATE DISPLAY NAME" + user.getDisplayName());

                            Intent intent = new Intent(MainActivity.this, ListActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                            //Log.d("CHATCAMP APP", FirebaseInstanceId.getInstance().getToken());


                        }
                    });
                    Map map = new HashMap();
                    map.put("key", "value");
                    ChatCamp.updateUserMetadata(map, new ChatCamp.UserUpdateListener() {
                        @Override
                        public void onUpdated(User user, ChatCampException e) {
                            Log.d("CHATCAMP_APP", "meta data updated");
                        }
                    });
                    if (FirebaseInstanceId.getInstance().getToken() != null) {
                        ChatCamp.updateUserPushToken(FirebaseInstanceId.getInstance().getToken(), new ChatCamp.UserPushTokenUpdateListener() {
                            @Override
                            public void onUpdated(User user, ChatCampException e) {
                                Log.d("CHATCAMP_APP", "PUSH TOKEN REGISTERED");

                            }
                        });
                    }
                }
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
