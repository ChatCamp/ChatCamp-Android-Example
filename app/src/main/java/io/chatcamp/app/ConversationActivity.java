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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConversationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupChannelListQuery.ParticipantState groupFilter;

    private void groupInit(GroupChannel groupChannel) {
        final GroupChannel g = groupChannel;
        final TextView input = (TextView) findViewById(R.id.edit_conversation_input);
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            g.sendMessage(input.getText().toString(), new GroupChannel.SendMessageListener() {
                                @Override
                                public void onSent(Message message, ChatCampException e) {
                                    input.setText("");
                                }
                            });
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        PreviousMessageListQuery previousMessageListQuery = g.createPreviousMessageListQuery();
        previousMessageListQuery.load(10, true, new PreviousMessageListQuery.ResultListener() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_conversation);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String channelType = getIntent().getStringExtra("channelType");
        String channelId = getIntent().getStringExtra("channelId");

        if(channelType.equals("open")) {
            OpenChannel.get(channelId, new OpenChannel.GetListener() {
                @Override
                public void onResult(OpenChannel openChannel, ChatCampException e) {
                    final OpenChannel o = openChannel;
                    getSupportActionBar().setTitle(o.getName());
                    final TextView input = (TextView) findViewById(R.id.edit_conversation_input);
                    input.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                switch (keyCode) {
                                    case KeyEvent.KEYCODE_DPAD_CENTER:
                                    case KeyEvent.KEYCODE_ENTER:
                                        o.sendMessage(input.getText().toString(), new OpenChannel.SendMessageListener() {
                                            @Override
                                            public void onSent(Message message, ChatCampException e) {
                                                input.setText("");
                                            }
                                        });
                                        return true;
                                    default:
                                        break;
                                }
                            }
                            return false;
                        }
                    });
                    openChannel.join(new OpenChannel.JoinListener() {
                        @Override
                        public void onResult(ChatCampException e) {
                            PreviousMessageListQuery previousMessageListQuery = o.createPreviousMessageListQuery();
                            previousMessageListQuery.load(10, true, new PreviousMessageListQuery.ResultListener() {
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

                Toast.makeText(getApplicationContext(), m.getText(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
