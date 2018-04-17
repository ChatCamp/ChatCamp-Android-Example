package io.chatcamp.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.ConversationViewHelper;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;

import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.Participant;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static io.chatcamp.app.GroupDetailActivity.KEY_GROUP_ID;

public class ConversationActivity extends AppCompatActivity implements ConversationViewHelper.OnGetChannelListener {

    private MessagesList mMessagesList;
    private String channelType;
    private String channelId;
    private MessageInput input;
    private MaterialProgressBar progressBar;
    private TextView groupTitleTv;
    private ImageView groupImageIv;
    private Toolbar toolbar;
    private Participant otherParticipant = null;
    private ConversationViewHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mMessagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.edit_conversation_input);
        progressBar = findViewById(R.id.progress_bar);

        // use a linear layout manager
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groupImageIv = toolbar.findViewById(R.id.iv_group_image);
        groupTitleTv = toolbar.findViewById(R.id.tv_group_name);

        channelType = getIntent().getStringExtra("channelType");
        channelId = getIntent().getStringExtra("channelId");
        helper = new ConversationViewHelper(this, mMessagesList, input, progressBar,
                channelType, channelId, LocalStorage.getInstance().getUserId());
        helper.init();
        helper.setOnGetChannelListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.onResume();
    }

    @Override
    protected void onPause() {
        helper.onPause();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_conversation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_conversation) {
            //TODO add click to opn details
//            titleClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateToobar(String imageUrl, String title) {
        Picasso.with(this).load(imageUrl)
                .placeholder(R.drawable.icon_default_contact)
                .error(R.drawable.icon_default_contact)
                .into(groupImageIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) groupImageIv.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        groupImageIv.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        groupImageIv.setImageResource(R.mipmap.ic_launcher_round);
                    }
                });

        groupTitleTv.setText(title);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataFile) {
        helper.onActivityResult(requestCode, resultCode, dataFile);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        helper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void getChannel(BaseChannel channel) {
        boolean isOneToOneConversation = false;
        if (channel instanceof GroupChannel) {
            GroupChannel groupChannel = (GroupChannel) channel;
            if (groupChannel.getParticipants().size() <= 2 && groupChannel.isDistinct()) {
                isOneToOneConversation = true;
            }
        }
        if (isOneToOneConversation) {
            List<Participant> participants = ((GroupChannel) channel).getParticipants();
            for (Participant participant : participants) {
                if (!participant.getId().equals(LocalStorage.getInstance().getUserId())) {
                    otherParticipant = participant;
                }
            }
            populateToobar(otherParticipant.getAvatarUrl(), otherParticipant.getDisplayName());
        } else {
            populateToobar(channel.getAvatarUrl(), channel.getName());
        }
        OnTitleClickListener titleClickListener = new OnTitleClickListener(channel, isOneToOneConversation);
        groupTitleTv.setOnClickListener(titleClickListener);
        groupImageIv.setOnClickListener(titleClickListener);
    }

    class OnTitleClickListener implements View.OnClickListener {

        private final BaseChannel channel;
        private final boolean isOneToOneConversation;

        public OnTitleClickListener(BaseChannel channel, boolean isOnToOneConversation) {
            this.channel = channel;
            this.isOneToOneConversation = isOnToOneConversation;
        }

        @Override
        public void onClick(View view) {
            if (isOneToOneConversation) {
                Intent intent = new Intent(ConversationActivity.this, UserProfileActivity.class);
                if (otherParticipant != null) {
                    intent.putExtra(UserProfileActivity.KEY_PARTICIPANT_ID, otherParticipant.getId());
                    intent.putExtra(UserProfileActivity.KEY_GROUP_ID, channel.getId());
                }
                startActivity(intent);
            } else {
                Intent intent = new Intent(ConversationActivity.this, GroupDetailActivity.class);
                intent.putExtra(KEY_GROUP_ID, channel.getId());
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        helper.onDestroy();
        super.onDestroy();
    }
}
