package io.chatcamp.app;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.chatcamp.app.customContent.IncomingActionMessageViewHolder;
import io.chatcamp.app.customContent.IncomingImageMessageViewHolder;
import io.chatcamp.app.customContent.IncomingTextMessageViewHolder;
import io.chatcamp.app.customContent.IncomingTypingMessageViewHolder;
import io.chatcamp.app.customContent.OutcomingActionMessageViewHolder;
import io.chatcamp.app.customContent.OutcomingImageMessageViewHolder;
import io.chatcamp.app.customContent.OutcomingTextMessageViewHolder;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.Message;
import io.chatcamp.sdk.OpenChannel;
import io.chatcamp.sdk.Participant;
import io.chatcamp.sdk.PreviousMessageListQuery;

public class ConversationActivity extends AppCompatActivity {

    public static final byte CONTENT_TYPE_ACTION = Byte.valueOf("101");
    public static final byte CONTENT_TYPE_TEXT = Byte.valueOf("102");
    public static final byte CONTENT_TYPE_IMAGE = Byte.valueOf("103");
    public static final byte CONTENT_TYPE_TYPING = Byte.valueOf("104");
    public static final String TYPING_TEXT_ID = "chatcamp_typing_id";
    private MessagesList mMessagesList;
    private MessagesListAdapter<ConversationMessage> messageMessagesListAdapter;
    private ImageLoader imageLoader;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupChannelListQuery.ParticipantState groupFilter;

    private void groupInit(final GroupChannel groupChannel) {
        final GroupChannel g = groupChannel;
        ChatCamp.addConnectionListener("1", new ChatCamp.ConnectionListener() {
            @Override
            public void onConnectionChanged(boolean b) {
                if (b) {
                    groupChannel.sync(new GroupChannel.SyncListener() {
                        @Override
                        public void onResult(ChatCampException e) {
                            if (mMessagesList != null) {
                                Snackbar.make(mMessagesList, "Group sync successful",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
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

        input.getInputEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    groupChannel.startTyping();
                } else {
                    groupChannel.stopTyping();
                }
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
                for (Message message : messageList) {
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

        ContentCheckerAction contentChecker = new ContentCheckerAction();

        MessageHolders holders = new MessageHolders()
                .registerContentType(
                        CONTENT_TYPE_TYPING,
                        IncomingTypingMessageViewHolder.class,
                        R.layout.layout_item_incoming_typing_message,
                        //we dont need this but this is required in the argument
                        IncomingTypingMessageViewHolder.class,
                        R.layout.layout_item_incoming_typing_message,
                        contentChecker);


        holders.registerContentType(
                CONTENT_TYPE_ACTION,
                IncomingActionMessageViewHolder.class,
                R.layout.layout_incoming_action,
                OutcomingActionMessageViewHolder.class,
                R.layout.layout_outcoming_action,
                contentChecker);

        holders.registerContentType(
                CONTENT_TYPE_TEXT,
                IncomingTextMessageViewHolder.class,
                R.layout.layout_item_incoming_text_message,
                OutcomingTextMessageViewHolder.class,
                R.layout.layout_item_outcoming_text_message,
                contentChecker);

        holders.registerContentType(
                CONTENT_TYPE_IMAGE,
                IncomingImageMessageViewHolder.class,
                R.layout.layout_item_incoming_image_message,
                OutcomingImageMessageViewHolder.class,
                R.layout.layout_item_outcoming_image_message,
                contentChecker);

        messageMessagesListAdapter = new MessagesListAdapter<>(LocalStorage.getInstance().getUserId(), holders, imageLoader);
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


        if (channelType.equals("open")) {
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

        } else {
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

            @Override
            public void onGroupChannelTypingStatusChanged(GroupChannel groupChannel) {
                if (groupChannel.isTyping()) {
                    List<Participant> participants = groupChannel.getTypingParticipants();
                    List<Participant> otherPaticipants = new ArrayList<>(participants);
                    Iterator<Participant> participantIterator = otherPaticipants.iterator();
                    while (participantIterator.hasNext()) {
                        Participant participant = participantIterator.next();
                        if(participant.getId().equals(LocalStorage.getInstance().getUserId())) {
                            participantIterator.remove();
                        }
                    }
                    List<ConversationMessage> toBeRemovedMessage = new ArrayList<>();
                    List<ConversationMessage> conversationMessages = messageMessagesListAdapter.getMessageList();
                    if (conversationMessages != null
                            && conversationMessages.size() > 0) {

                        for (int i = 0; i < conversationMessages.size(); ++i) {
                            if(conversationMessages.get(i) instanceof ConversationMessage) {
                                ConversationMessage message = conversationMessages.get(i);
                                if (message.getId().contains(TYPING_TEXT_ID)) {
                                    boolean isAbsent = true;
                                    for (Participant participant : otherPaticipants) {

                                        if (message.getUser().getId().equals(participant.getId())) {
                                            isAbsent = false;
                                        }
                                    }
                                    if (isAbsent) {
                                        toBeRemovedMessage.add(message);
                                    }
                                }
                            }
                        }
                        for (Participant participant : otherPaticipants) {
                            ConversationMessage message = new ConversationMessage();
                            message.setAuthor(new ConversationAuthor(participant));
                            message.setId(TYPING_TEXT_ID + participant.getId());
                            messageMessagesListAdapter.addToStart(message, true);
                        }
                        if(toBeRemovedMessage.size() > 0) {
                            messageMessagesListAdapter.delete(toBeRemovedMessage);
                        }
                    }
                } else {
                    messageMessagesListAdapter.deleteAllTypingMessages();
                }


            }

            @Override
            public void onOpenChannelTypingStatusChanged(OpenChannel groupChannel) {

            }
        });
    }


    public class ContentCheckerAction implements MessageHolders.ContentChecker<ConversationMessage> {

        @Override
        public boolean hasContentFor(ConversationMessage message, byte type) {
            if (type == CONTENT_TYPE_TYPING) {
                return message.getId().contains(TYPING_TEXT_ID);
            } else if (type == CONTENT_TYPE_ACTION) {
                return message.getMessage().getType().equals("text")
                        && message.getMessage().getCustomType().equals("action_link");
            } else if (type == CONTENT_TYPE_TEXT) {
                return message.getMessage().getType().equals("text");
            } else if (type == CONTENT_TYPE_IMAGE) {
                return message.getMessage().getType().equals("attachment");
            }
            return false;
        }

    }

}
