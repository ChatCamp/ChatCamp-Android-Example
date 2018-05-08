package io.chatcamp.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
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
import com.stfalcon.chatkit.messages.messagetypes.DocumentMessageFactory;
import com.stfalcon.chatkit.messages.messagetypes.ImageMessageFactory;
import com.stfalcon.chatkit.messages.messagetypes.MessageFactory;
import com.stfalcon.chatkit.messages.messagetypes.TextMessageFactory;
import com.stfalcon.chatkit.messages.messagetypes.VideoMessageFactory;
import com.stfalcon.chatkit.messages.sender.AttachmentSender;
import com.stfalcon.chatkit.messages.sender.CameraAttachmentSender;
import com.stfalcon.chatkit.messages.sender.FileAttachmentSender;
import com.stfalcon.chatkit.messages.sender.GalleryAttachmentSender;
import com.stfalcon.chatkit.messages.typing.DefaultTypingFactory;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.OpenChannel;
import io.chatcamp.sdk.Participant;
import io.chatcamp.sdk.PreviousMessageListQuery;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static io.chatcamp.app.GroupDetailActivity.KEY_GROUP_ID;

public class ConversationActivity extends AppCompatActivity {

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
    private PreviousMessageListQuery previousMessageListQuery;
    private BaseChannel channel;

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
//        helper = new ConversationViewHelper(this, mMessagesList, input, progressBar,
//                channelType, channelId, LocalStorage.getInstance().getUserId());
//        helper.init();
//        helper.setOnGetChannelListener(this);
        if (channelType.equals("open")) {
            OpenChannel.get(channelId, new OpenChannel.GetListener() {
                @Override
                public void onResult(OpenChannel openChannel, ChatCampException e) {
                    final OpenChannel o = openChannel;
                    getChannel(o);
//                    getSupportActionBar().setTitle(o.getName());
                    openChannel.join(new OpenChannel.JoinListener() {
                        @Override
                        public void onResult(ChatCampException e) {
                            previousMessageListQuery = o.createPreviousMessageListQuery();
                            channel = o;
                        }
                    });

                }
            });

        } else {
            //TODO check the participant state - INVITED, ALL,  ACCEPTED
            final GroupChannelListQuery.ParticipantState groupFilter = GroupChannelListQuery.ParticipantState.ACCEPTED;//GroupChannelListQuery.ParticipantState.valueOf(getIntent().getStringExtra("participantState"));
            GroupChannel.get(channelId, new GroupChannel.GetListener() {
                @Override
                public void onResult(final GroupChannel groupChannel, ChatCampException e) {
                    getChannel(groupChannel);
//                    groupChannel.sync(new GroupChannel.SyncListener() {
//                        @Override
//                        public void onResult(ChatCampException e) {
//
//                        }
//                    });
                    if (groupFilter == GroupChannelListQuery.ParticipantState.INVITED) {
                        groupChannel.acceptInvitation(new GroupChannel.AcceptInvitationListener() {
                            @Override
                            public void onResult(GroupChannel groupChannel, ChatCampException e) {
                                channel = groupChannel;
                            }
                        });
                    } else {
                        channel = groupChannel;
                    }

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        helper.onResume();
    }

    @Override
    protected void onPause() {
//        helper.onPause();
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
        input.onActivityResult(requestCode, resultCode,dataFile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        input.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getChannel(BaseChannel channel) {
        input.setChannel(channel);
        mMessagesList.init();
        mMessagesList.setSenderId(LocalStorage.getInstance().getUserId());
        MessageFactory [] messageFactories = new MessageFactory[4];
        messageFactories[0] = new TextMessageFactory();
        messageFactories[1] = new ImageMessageFactory(this);
        messageFactories[2] = new VideoMessageFactory();
        messageFactories[3] = new DocumentMessageFactory(this);
        mMessagesList.addMessageFactories(messageFactories);
        mMessagesList.setChannel(channel);
        mMessagesList.setTypingFactory(new DefaultTypingFactory());
        FileAttachmentSender fileAttachmentSender = new FileAttachmentSender(this, channel, "FILE", R.drawable.ic_document);
        GalleryAttachmentSender galleryAttachmentSender = new GalleryAttachmentSender(this, channel, "Gallery", R.drawable.ic_gallery);
        CameraAttachmentSender cameraAttachmentSender = new CameraAttachmentSender(this, channel, "Camera", R.drawable.ic_camera);
        List<AttachmentSender> attachmentSenders = new ArrayList<>();
        attachmentSenders.add(fileAttachmentSender);
        attachmentSenders.add(cameraAttachmentSender);
        attachmentSenders.add(galleryAttachmentSender);

        input.setAttachmentSenderList(attachmentSenders);
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
}
