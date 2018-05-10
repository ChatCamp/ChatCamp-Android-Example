package io.chatcamp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stfalcon.chatkit.messages.ConversationViewHelper;
import com.stfalcon.chatkit.messages.HeaderView;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.messagetypes.FileMessageFactory;
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

public class ConversationActivity extends AppCompatActivity implements AttachmentSender.UploadListener {

    private MessagesList mMessagesList;
    private String channelType;
    private String channelId;
    private MessageInput input;
    private MaterialProgressBar progressBar;
    private PreviousMessageListQuery previousMessageListQuery;
    private BaseChannel channel;
    private HeaderView headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mMessagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.edit_conversation_input);
        progressBar = findViewById(R.id.progress_bar);
        headerView = findViewById(R.id.header_view);

        // use a linear layout manager

        setSupportActionBar(headerView.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        channelType = getIntent().getStringExtra("channelType");
        channelId = getIntent().getStringExtra("channelId");
        if (channelType.equals("open")) {
            OpenChannel.get(channelId, new OpenChannel.GetListener() {
                @Override
                public void onResult(OpenChannel openChannel, ChatCampException e) {
                    final OpenChannel o = openChannel;
                    getChannel(o);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent dataFile) {
        input.onActivityResult(requestCode, resultCode, dataFile);
        mMessagesList.onActivityResult(requestCode, resultCode, dataFile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        input.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mMessagesList.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getChannel(BaseChannel channel) {
        headerView.setChannel(channel, LocalStorage.getInstance().getUserId());
        input.setChannel(channel);
        mMessagesList.init();
        mMessagesList.setSenderId(LocalStorage.getInstance().getUserId());
        MessageFactory[] messageFactories = new MessageFactory[4];
        messageFactories[0] = new TextMessageFactory();
        messageFactories[1] = new ImageMessageFactory(this);
        messageFactories[2] = new VideoMessageFactory(this);
        messageFactories[3] = new FileMessageFactory(this);
        mMessagesList.addMessageFactories(messageFactories);
        mMessagesList.setChannel(channel);
        mMessagesList.setTypingFactory(new DefaultTypingFactory());
        FileAttachmentSender fileAttachmentSender = new FileAttachmentSender(this, channel, "FILE", R.drawable.ic_document);
        fileAttachmentSender.setUploadListener(this);
        GalleryAttachmentSender galleryAttachmentSender = new GalleryAttachmentSender(this, channel, "Gallery", R.drawable.ic_gallery);
        galleryAttachmentSender.setUploadListener(this);
        CameraAttachmentSender cameraAttachmentSender = new CameraAttachmentSender(this, channel, "Camera", R.drawable.ic_camera);
        cameraAttachmentSender.setUploadListener(this);
        List<AttachmentSender> attachmentSenders = new ArrayList<>();
        attachmentSenders.add(fileAttachmentSender);
        attachmentSenders.add(cameraAttachmentSender);
        attachmentSenders.add(galleryAttachmentSender);

        input.setAttachmentSenderList(attachmentSenders);
    }

    @Override
    public void onUploadProgress(int progress) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(progress);
    }

    @Override
    public void onUploadSuccess() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onUploadFailed() {
        progressBar.setVisibility(View.GONE);
    }
}
