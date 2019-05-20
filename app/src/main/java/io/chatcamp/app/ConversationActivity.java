package io.chatcamp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chatcamp.uikit.customview.LoadingView;
import com.chatcamp.uikit.messages.HeaderView;
import com.chatcamp.uikit.messages.MessageInput;
import com.chatcamp.uikit.messages.MessagesList;
import com.chatcamp.uikit.messages.messagetypes.FileMessageFactory;
import com.chatcamp.uikit.messages.messagetypes.ImageMessageFactory;
import com.chatcamp.uikit.messages.messagetypes.MessageFactory;
import com.chatcamp.uikit.messages.messagetypes.TextMessageFactory;
import com.chatcamp.uikit.messages.messagetypes.VideoMessageFactory;
import com.chatcamp.uikit.messages.messagetypes.VoiceMessageFactory;
import com.chatcamp.uikit.messages.sender.AttachmentSender;
import com.chatcamp.uikit.messages.sender.CameraAttachmentSender;
import com.chatcamp.uikit.messages.sender.FileAttachmentSender;
import com.chatcamp.uikit.messages.sender.GalleryAttachmentSender;
import com.chatcamp.uikit.messages.sender.VoiceSender;
import com.chatcamp.uikit.messages.typing.DefaultTypingFactory;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.GroupChannelParams;
import io.chatcamp.sdk.Message;
import io.chatcamp.sdk.MessageParams;
import io.chatcamp.sdk.OpenChannel;
import io.chatcamp.sdk.Participant;
import io.chatcamp.sdk.PreviousMessageListQuery;
import io.chatcamp.sdk.PublicGroupChannelListQuery;
import io.chatcamp.sdk.TotalCountFilterParams;
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
    private ProgressBar loadMessagePb;
    private LoadingView loadingView;
    private TextView connectionState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Conversation Activity", "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mMessagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.edit_conversation_input);
        progressBar = findViewById(R.id.progress_bar);
        headerView = findViewById(R.id.header_view);
        loadMessagePb = findViewById(R.id.load_message_pb);
        loadingView = findViewById(R.id.loading_view);
        mMessagesList.setLoadingView(loadingView);
        connectionState = findViewById(R.id.tv_connection);

        ChatCamp.addConnectionListener("conversation", new ChatCamp.ConnectionListener() {
            @Override
            public void onConnectionChanged(ChatCamp.NetworkState networkState) {
                if(networkState == ChatCamp.NetworkState.CONNECTED) {
                    connectionState.setVisibility(View.GONE);
                    Log.e("connected", "in connected");
                } else if(networkState == ChatCamp.NetworkState.DISCONNECTED) {
                    connectionState.setText("No Network");
                    connectionState.setVisibility(View.VISIBLE);
                    Log.e("connected", "in disconnected");
                } else if(networkState == ChatCamp.NetworkState.CONNECTING) {
                    connectionState.setText("Connecting");
                    connectionState.setVisibility(View.VISIBLE);
                    Log.e("connected", "in connecting");
                }
            }
        });
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
                    setChannel(o);
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
            final GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter groupFilter
                    = GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_ACCEPTED;//GroupChannelListQuery.ParticipantState.valueOf(getIntent().getStringExtra("participantState"));
            GroupChannel.get(channelId, new GroupChannel.GetListener() {
                @Override
                public void onResult(final GroupChannel groupChannel, ChatCampException e) {
                    setChannel(groupChannel);
                    groupChannel.unbanParticipant("sandesh",  new BaseChannel.UnbanParticipantListener() {
                        @Override
                        public void onResult(ChatCampException e) {
                            Log.d("sdf", "sf");
                        }
                    });
//                    GroupChannelParams params = new GroupChannelParams();
//                    params.setDistinct(false);
//                    groupChannel.update(params, new BaseChannel.UpdateListener() {
//                        @Override
//                        public void onResult(BaseChannel channel, ChatCampException e) {
//                            Log.d("sdf", "sf");
//                        }
//                    });

//                    PreviousMessageListQuery a = groupChannel.createPreviousMessageListQuery();
//                    a.setDirection(PreviousMessageListQuery.PreviousMessageListQueryFilterDirection.NEXT);
//                    a.setReference("6511125190171160576");
                   // a.setTimestamp(1552373219);
//                    a.setTextSearch("7");

//                    a.setSortBy(PreviousMessageListQuery.PreviousMessageListQueryFilterSortBy.SORT_BY_INSERTED_AT);

//                    a.setLimit(20);
//                    a.setUserId("10101");
//                    a.load(new PreviousMessageListQuery.ResultListener() {
//                        @Override
//                        public void onResult(List<Message> messageList, ChatCampException e) {
//                            Log.d("sd", "sf");
//                        }
//                    });
//                    groupChannel.deleteMessage("6507858293761765376", new BaseChannel.DeleteMessageListener() {
//                        @Override
//                        public void onResult(ChatCampException exception) {
//                            Log.d("sdf", "Sdf");
//                        }
//                    });
//                    PublicGroupChannelListQuery query = GroupChannel.createPublicGroupChannelListQuery();
//                    query.load(new PublicGroupChannelListQuery.ResultHandler() {
//                        @Override
//                        public void onResult(List<GroupChannel> groupChannelList, ChatCampException e) {
//                            Log.d("sdf", "Sdf");
//                        }
//                    });

//                    groupChannel.getTotalMessageCount(new BaseChannel.OnGetTotalMessageCountListener() {
//                        @Override
//                        public void onGetTotalMessageCount(int count, TotalCountFilterParams params, ChatCampException chatCampException) {
//                            Log.d("sdf", "Sdf");
//                        }
//                    });
//                    groupChannel.setActive(false, new BaseChannel.SetActiveListener() {
//                        @Override
//                        public void onResult(ChatCampException e) {
//                            groupChannel.sync(new GroupChannel.SyncListener() {
//                                @Override
//                                public void onResult(GroupChannel groupChannela, ChatCampException e) {
//                                    groupChannela.ifActive();
//                                }
//                            });
//                        }
//                    });
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

    public void setChannel(BaseChannel channel) {
        mMessagesList.setOnMessagesLoadedListener(new MessagesList.OnMessagesLoadedListener() {
            @Override
            public void onMessagesLoaded() {
                loadMessagePb.setVisibility(View.GONE);
            }
        });
        headerView.setChannel(channel);
        invalidateOptionsMenu();
        input.setChannel(channel);
        input.setOnSendClickListener(new MessageInput.OnSendCLickedListener() {
            @Override
            public void onSendClicked(String text) {
                String a = text;
            }
        });
        MessageFactory[] messageFactories = new MessageFactory[5];
        messageFactories[0] = new TextMessageFactory();
        messageFactories[1] = new ImageMessageFactory(this);
        messageFactories[2] = new VideoMessageFactory(this);
        messageFactories[3] = new VoiceMessageFactory(this);
        messageFactories[4] = new FileMessageFactory(this);
        mMessagesList.addMessageFactories(messageFactories);
        mMessagesList.setChannel(channel);
        mMessagesList.setTypingFactory(new DefaultTypingFactory(this));
//        mMessagesList.setAvatarImageLoader(new ImageLoader() {
//            @Override
//            public void loadImage(ImageView imageView, String url) {
//                // add loading image logic here
//            }
//        });
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
        VoiceSender voiceSender = new VoiceSender(this, channel);
        voiceSender.setUploadListener(this);
        input.setVoiceSender(voiceSender);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_block) {
            headerView.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_conversation, menu);
        headerView.onCreateOptionMenu(menu.findItem(R.id.action_block));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        headerView.onPrepareOptionsMenu(menu.findItem(R.id.action_block));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(channelId)) {
            BaseApplication.getInstance().setGroupId(channelId);
        }
    }

    @Override
    protected void onPause() {
        Log.e("conversation", "on pause");
        BaseApplication.getInstance().setGroupId("");
        super.onPause();
    }

}
