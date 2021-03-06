package io.chatcamp.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chatcamp.uikit.messages.HeaderView;
import com.chatcamp.uikit.messages.MessageInput;
import com.chatcamp.uikit.messages.MessagesList;
import com.chatcamp.uikit.messages.messagetypes.FileMessageFactory;
import com.chatcamp.uikit.messages.messagetypes.ImageMessageFactory;
import com.chatcamp.uikit.messages.messagetypes.MessageFactory;
import com.chatcamp.uikit.messages.messagetypes.TextMessageFactory;
import com.chatcamp.uikit.messages.messagetypes.VideoMessageFactory;
import com.chatcamp.uikit.messages.sender.AttachmentSender;
import com.chatcamp.uikit.messages.sender.CameraAttachmentSender;
import com.chatcamp.uikit.messages.sender.FileAttachmentSender;
import com.chatcamp.uikit.messages.sender.GalleryAttachmentSender;
import com.chatcamp.uikit.messages.typing.DefaultTypingFactory;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.OpenChannel;
import io.chatcamp.sdk.PreviousMessageListQuery;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestConversationFragment extends Fragment

        implements AttachmentSender.UploadListener {

    private MessagesList mMessagesList;
    private String channelType;
    private String channelId;
    private MessageInput input;
    private MaterialProgressBar progressBar;
    private PreviousMessageListQuery previousMessageListQuery;
    private BaseChannel channel;
    private HeaderView headerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_test_conversation, container, false);

        mMessagesList = view.findViewById(R.id.messagesList);
        input = view.findViewById(R.id.edit_conversation_input);
        progressBar = view.findViewById(R.id.progress_bar);
        headerView = view.findViewById(R.id.header_view);
        setHasOptionsMenu(true);

        // use a linear layout manager

        ((AppCompatActivity) getActivity()).setSupportActionBar(headerView.getToolbar());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        channelType = getArguments().getString("channelType");
        channelId = getArguments().getString("channelId");
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
            final GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter groupFilter = GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_ACCEPTED;//GroupChannelListQuery.ParticipantState.valueOf(getIntent().getStringExtra("participantState"));
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
                    if (groupFilter == GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_INVITED) {
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
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        input.onActivityResult(requestCode, resultCode, data);
        mMessagesList.onActivityResult(requestCode, resultCode, data);
        //requestPermissions(new String[]{}, 111);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        input.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mMessagesList.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getChannel(BaseChannel channel) {
        headerView.setChannel(channel);
        input.setChannel(channel);
        MessageFactory[] messageFactories = new MessageFactory[4];
        messageFactories[0] = new TextMessageFactory();
        messageFactories[1] = new ImageMessageFactory(this);
        messageFactories[2] = new VideoMessageFactory(this);
        messageFactories[3] = new FileMessageFactory(this);
        mMessagesList.addMessageFactories(messageFactories);
        mMessagesList.setChannel(channel);
        mMessagesList.setTypingFactory(new DefaultTypingFactory(getContext()));
        FileAttachmentSender fileAttachmentSender = new FileAttachmentSender(this, channel, "File", R.drawable.ic_document);
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
    public void onUploadFailed(ChatCampException error) {
        progressBar.setVisibility(View.GONE);
    }


    public TestConversationFragment() {
        // Required empty public constructor
    }

}
