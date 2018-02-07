package io.chatcamp.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.Message;
import io.chatcamp.sdk.OpenChannel;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.PreviousMessageListQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;

public class ConversationActivity extends AppCompatActivity {

    private MessagesList mMessagesList;
    private MessagesListAdapter<ConversationMessage> messageMessagesListAdapter;
    private ImageLoader imageLoader;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupChannelListQuery.ParticipantState groupFilter;

    private void groupInit(GroupChannel groupChannel) {
        final GroupChannel g = groupChannel;
        final MessageInput input = (MessageInput) findViewById(R.id.edit_conversation_input);
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                g.sendMessage(input.toString(), new GroupChannel.SendMessageListener() {
                    @Override
                    public void onSent(Message message, ChatCampException e) {
//                        input.setText("");
                    }
                });

                return true;
            }
        });

        PreviousMessageListQuery previousMessageListQuery = g.createPreviousMessageListQuery();
        previousMessageListQuery.load(10, true, new PreviousMessageListQuery.ResultListener() {
            @Override
            public void onResult(List<Message> messageList, ChatCampException e) {
                final List<Message> m = messageList;
                System.out.println("MESSSAGE HISTORY:");
                System.out.println(m);

//                mAdapter = new ConversationMessageListAdapter(m, new ConversationMessageListAdapter.RecyclerViewClickListener() {
//                    @Override
//                    public void onClick(View view, int position) {
//                        Message messageElement = m.get(position);
//                        Toast.makeText(getApplicationContext(), "Element " + messageElement.getText(), Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });
//                mRecyclerView.setAdapter(mAdapter);
                List<ConversationMessage> conversationMessages = new ArrayList<ConversationMessage>();
                for(Message message: messageList) {
                    ConversationMessage conversationMessage = new ConversationMessage(message);
                    conversationMessages.add(conversationMessage);
                }
                messageMessagesListAdapter.addToEnd(conversationMessages, false);


            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_conversation);

        mMessagesList = (MessagesList) findViewById(R.id.messagesList);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(ConversationActivity.this).load(url).into(imageView);
            }
        };
        messageMessagesListAdapter = new MessagesListAdapter<>("1", imageLoader);
        mMessagesList.setAdapter(messageMessagesListAdapter);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String channelType = getIntent().getStringExtra("channelType");
        String channelId = getIntent().getStringExtra("channelId");

        if(channelType.equals("open")) {
            OpenChannel.get(channelId, new OpenChannel.GetListener() {
                @Override
                public void onResult(OpenChannel openChannel, ChatCampException e) {
                    final OpenChannel o = openChannel;
                    getSupportActionBar().setTitle(o.getName());
                    final MessageInput input = (MessageInput) findViewById(R.id.edit_conversation_input);
                    input.setInputListener(new MessageInput.InputListener() {
                        @Override
                        public boolean onSubmit(CharSequence input) {

                            o.sendMessage(input.toString(), new OpenChannel.SendMessageListener() {
                                @Override
                                public void onSent(Message message, ChatCampException e) {
//                                    input.setText("");
                                }
                            });

                            return true;
                        }
                    });
                    openChannel.join(new OpenChannel.JoinListener() {
                        @Override
                        public void onResult(ChatCampException e) {
                            PreviousMessageListQuery previousMessageListQuery = o.createPreviousMessageListQuery();
                            previousMessageListQuery.load(20, true, new PreviousMessageListQuery.ResultListener() {
                                @Override
                                public void onResult(List<Message> messageList, ChatCampException e) {
                                    final List<Message> m = messageList;
                                    System.out.println("MESSSAGE HISTORY:");
                                    System.out.println(m);

                                    mAdapter = new ConversationMessageListAdapter(m, new ConversationMessageListAdapter.RecyclerViewClickListener() {
                                        @Override
                                        public void onClick(View view, int position) {
                                            Message messageElement = m.get(position);
                                            Toast.makeText(getApplicationContext(), "Element " + messageElement.getText(), Toast.LENGTH_SHORT).show();


                                        }
                                    });
                                    mRecyclerView.setAdapter(mAdapter);

                                }
                            });
                        }
                    });
                }
            });

        }
        else {
            groupFilter = GroupChannelListQuery.ParticipantState.valueOf(getIntent().getStringExtra("participantState"));
            GroupChannel.get(channelId, new GroupChannel.GetListener() {
                @Override
                public void onResult(final GroupChannel groupChannel, ChatCampException e) {

                getSupportActionBar().setTitle(groupChannel.getName());
                groupChannel.sync(new GroupChannel.SyncListener() {
                    @Override
                    public void onResult(ChatCampException e) {

                    }
                });
                if (groupFilter == GroupChannelListQuery.ParticipantState.INVITED) {
                    groupChannel.acceptInvitation(new GroupChannel.AcceptInvitationListener() {
                        @Override
                        public void onResult(GroupChannel groupChannel, ChatCampException e) {
                            groupInit(groupChannel);
                        }
                    });
                } else {
                    groupInit(groupChannel);
                }

                }
            });
        }
        ChatCamp.addChannelListener("test", new ChatCamp.ChannelListener() {
            @Override
            public void onOpenChannelMessageReceived(OpenChannel openChannel, Message message) {
                final Message m = message;

                Toast.makeText(getApplicationContext(), m.getText(), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onGroupChannelMessageReceived(GroupChannel groupChannel, Message message) {
                final Message m = message;
                final ConversationMessage conversationMessage = new ConversationMessage(m);
                messageMessagesListAdapter.addToStart(conversationMessage, true);
                Toast.makeText(getApplicationContext(), m.getText(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
