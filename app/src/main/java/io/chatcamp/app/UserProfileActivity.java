package io.chatcamp.app;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.User;

public class UserProfileActivity extends AppCompatActivity {

    public static final String KEY_PARTICIPANT_ID = "key_participant_id";

    private TextView onlineStatusTv;
    private ImageView onlineIv;
    private TextView lastSeenTv;
    private ImageView toolbarIv;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onlineStatusTv = findViewById(R.id.tv_online_status);
        onlineIv = findViewById(R.id.iv_online);
        lastSeenTv = findViewById(R.id.tv_last_seen);
        toolbarIv = findViewById(R.id.toolbarImage);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        String participantId = getIntent().getStringExtra(KEY_PARTICIPANT_ID);
        if(!TextUtils.isEmpty(participantId)) {
            User.getUser(participantId, new User.OnGetUserListener() {
                @Override
                public void onGetuser(User user, ChatCampException ex) {
                    collapsingToolbarLayout.setTitle(user.getDisplayName());
                    Picasso.with(UserProfileActivity.this).load(user.getAvatarUrl()).into(toolbarIv);
                    if (user.isOnline()) {
                        onlineIv.setVisibility(View.VISIBLE);
                        lastSeenTv.setVisibility(View.GONE);
                        onlineStatusTv.setText("Online");
                    } else {
                        onlineIv.setVisibility(View.GONE);
                        lastSeenTv.setVisibility(View.VISIBLE);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(user.getLastSeen() * 1000);
                        lastSeenTv.setText(format.format(date));
                        onlineStatusTv.setText("Last Seen");
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
