package io.chatcamp.app;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.chatcamp.app.webview.FilePath;
import io.chatcamp.app.webview.WebViewActivity;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.Message;
import io.chatcamp.sdk.OpenChannel;
import io.chatcamp.sdk.Participant;
import io.chatcamp.sdk.PreviousMessageListQuery;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static io.chatcamp.app.ConversationMessage.TYPING_TEXT_ID;
import static io.chatcamp.app.GroupDetailActivity.KEY_GROUP_ID;

public class ConversationActivity extends AppCompatActivity {

    public static final String GROUP_CONNECTION_LISTENER = "group_channel_connection";
    public static final String CHANNEL_LISTENER = "group_channel_listener";
    private static final int PICK_MEDIA_RESULT_CODE = 111;
    private static final int PICKFILE_RESULT_CODE = 120;
    private static final int CAPTURE_MEDIA_RESULT_CODE = 121;
    private static final int PREVIEW_FILE_RESULT_CODE = 112;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_MEDIA = 113;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_DOCUMENT = 114;
    private static final int PERMISSIONS_REQUEST_CAMERA = 115;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 116;
    private static final String DOCUMENT = "document";
    private static final String MEDIA = "media";
    private MessagesList mMessagesList;
    private MessagesListAdapter<ConversationMessage> messageMessagesListAdapter;
    private ImageLoader imageLoader;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupChannelListQuery.ParticipantState groupFilter;
    private String channelType;
    private String channelId;
    private MessageInput input;
    private MessageTextWatcher textWatcher;
    private GroupChannel g;
    private MaterialProgressBar progressBar;
    private TextView groupTitleTv;
    private ImageView groupImageIv;
    private Toolbar toolbar;
    private boolean isOneToOneConversation;
    private Participant otherParticipant = null;
    private MessageHolders holder;
    private String currentPhotoPath;
    private MessageContentType.Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mRecyclerView = findViewById(R.id.rv_conversation);
        mMessagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.edit_conversation_input);
        progressBar = findViewById(R.id.progress_bar);
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(ConversationActivity.this).load(url)
                        .placeholder(R.drawable.icon_default_contact)
                        .error(R.drawable.icon_default_contact).into(imageView);
            }
        };

        holder = new MessageHolders();
        holder.setOnActionItemClickedListener(new MessageHolders.OnActionItemClickedListener() {
            @Override
            public void onActionItemClicked(String url) {
                g.markAsRead();
                Intent intent = new Intent(BaseApplication.getInstance(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, url);
                startActivity(intent);
            }
        });
        holder.setOnVideoItemClickedListener(new MessageHolders.OnVideoItemClickedListener() {
            @Override
            public void onVideoItemClicked(String url) {
                g.markAsRead();
                Intent intent = new Intent(ConversationActivity.this, MediaPreviewActivity.class);
                intent.putExtra(MediaPreviewActivity.VIDEO_URI, url);
                startActivity(intent);
            }
        });
        holder.setOnDocumentItemClickedListener(new MessageHolders.OnDocumentItemClickedListener() {
            @Override
            public void onDocumentItemClicked(MessageContentType.Document message) {
                g.markAsRead();
                downloadAndOpenDocument(message);

            }
        });

        messageMessagesListAdapter = new MessagesListAdapter<>(LocalStorage.getInstance().getUserId(), holder, imageLoader);
        mMessagesList.setAdapter(messageMessagesListAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groupImageIv = toolbar.findViewById(R.id.iv_group_image);
        groupTitleTv = toolbar.findViewById(R.id.tv_group_name);

        channelType = getIntent().getStringExtra("channelType");
        channelId = getIntent().getStringExtra("channelId");
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_CAMERA);
                return;
            }
        }
        openCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChannelDetails();

    }

    @Override
    protected void onPause() {
        removeChannelListener();
        removeConnectionListener();
        removeTextWatcher();
        super.onPause();
    }

    private void getChannelDetails() {

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
    }

    private void groupInit(final GroupChannel groupChannel) {
        g = groupChannel;
        g.markAsRead();
        if (g.getParticipants().size() <= 2 && g.isDistinct()) {
            isOneToOneConversation = true;
        }
        addConnectionListener(g);
        setInputListener(g);
        addTextWatcher(g);
        addChannelListener(g);

        if (isOneToOneConversation) {
            List<Participant> participants = g.getParticipants();
            for (Participant participant : participants) {
                if (!participant.getId().equals(LocalStorage.getInstance().getUserId())) {
                    otherParticipant = participant;
                }
            }
            populateToobar(otherParticipant.getAvatarUrl(), otherParticipant.getDisplayName());
        } else {
            populateToobar(g.getAvatarUrl(), g.getName());
        }
        OnTitleClickListener titleClickListener = new OnTitleClickListener();
        groupTitleTv.setOnClickListener(titleClickListener);
        groupImageIv.setOnClickListener(titleClickListener);
        PreviousMessageListQuery previousMessageListQuery = g.createPreviousMessageListQuery();
        previousMessageListQuery.load(10, true, new PreviousMessageListQuery.ResultListener() {
            @Override
            public void onResult(List<Message> messageList, ChatCampException e) {
                final List<Message> m = messageList;
                System.out.println("MESSSAGE HISTORY:");
                System.out.println(m);
                List<ConversationMessage> conversationMessages = new ArrayList<ConversationMessage>();
                for (Message message : messageList) {
                    ConversationMessage conversationMessage = new ConversationMessage(message);
                    conversationMessages.add(conversationMessage);
                }
                messageMessagesListAdapter.clear();
                messageMessagesListAdapter.addToEnd(conversationMessages, false);
            }
        });
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

    private void addConnectionListener(final GroupChannel groupChannel) {
        ChatCamp.addConnectionListener(GROUP_CONNECTION_LISTENER, new ChatCamp.ConnectionListener() {
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
    }

    private void setInputListener(final GroupChannel groupChannel) {
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                groupChannel.sendMessage(input.toString(), new GroupChannel.SendMessageListener() {
                    @Override
                    public void onSent(Message message, ChatCampException e) {
//                        input.setText("");
                        groupChannel.markAsRead();
                    }
                });

                return true;
            }
        });
        input.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // inflate the custom popup layout
                final View inflatedView = layoutInflater.inflate(R.layout.layout_attachment, null, false);
                LinearLayout galleryLl = inflatedView.findViewById(R.id.ll_gallery);
                LinearLayout cameraLl = inflatedView.findViewById(R.id.ll_camera);
                LinearLayout documentLl = inflatedView.findViewById(R.id.ll_document);
                // get device size
                Display display = getWindowManager().getDefaultDisplay();
                final Point size = new Point();
                display.getSize(size);
                final PopupWindow popupWindow = new PopupWindow(inflatedView, size.x - 50, (WindowManager.LayoutParams.WRAP_CONTENT), true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());//This has no meaning than dismissal of the Pop Up Noni!!
                popupWindow.setOutsideTouchable(true);

                popupWindow.showAtLocation(input, Gravity.BOTTOM, 0, 2 * input.getHeight());
                documentLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        checkReadPermission(DOCUMENT);
                    }
                });
                galleryLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        checkReadPermission(MEDIA);
                    }
                });
                cameraLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        checkCameraPermission();
                    }
                });
            }
        });
    }

    private void checkReadPermission(String type) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (type.equalsIgnoreCase(DOCUMENT)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_DOCUMENT);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_MEDIA);
                }
                return;
            }
        }
        chooseFile(type);
    }

    private void chooseFile(String type) {
        if (type.equalsIgnoreCase(DOCUMENT)) {
            Intent intent = new Intent();
            intent.setType("application/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select files"), PICKFILE_RESULT_CODE);
        } else if (type.equalsIgnoreCase(MEDIA)) {
            if (Build.VERSION.SDK_INT < 19) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/* video/*");
                startActivityForResult(Intent.createChooser(intent, "Select Media"), PICK_MEDIA_RESULT_CODE);
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                startActivityForResult(intent, PICK_MEDIA_RESULT_CODE);
            }
        }
    }

    private void addTextWatcher(final GroupChannel groupChannel) {
        textWatcher = new MessageTextWatcher(groupChannel);
        input.getInputEditText().addTextChangedListener(textWatcher);
        input.getInputEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (g != null) {
                        g.markAsRead();
                    }
                }
                return false;
            }
        });
    }

    private void addChannelListener(final GroupChannel groupChannel) {
        ChatCamp.addChannelListener(CHANNEL_LISTENER, new ChatCamp.ChannelListener() {
            @Override
            public void onOpenChannelMessageReceived(OpenChannel openChannel, Message message) {
                final Message m = message;
                Toast.makeText(getApplicationContext(), m.getText(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onGroupChannelMessageReceived(GroupChannel channel, Message message) {
                if(channel.getId().equals(groupChannel.getId())) {
                    final Message m = message;
                    final ConversationMessage conversationMessage = new ConversationMessage(m);
                    messageMessagesListAdapter.addToStart(conversationMessage, true);
                    Toast.makeText(getApplicationContext(), m.getText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGroupChannelTypingStatusChanged(GroupChannel channel) {
                if (channel.isTyping()) {
                    List<Participant> participants = channel.getTypingParticipants();
                    List<Participant> otherPaticipants = new ArrayList<>(participants);
                    Iterator<Participant> participantIterator = otherPaticipants.iterator();
                    while (participantIterator.hasNext()) {
                        Participant participant = participantIterator.next();
                        if (participant.getId().equals(LocalStorage.getInstance().getUserId())) {
                            participantIterator.remove();
                            break;
                        }
                    }
                    List<ConversationMessage> toBeRemovedMessage = new ArrayList<>();
                    List<ConversationMessage> conversationMessages = messageMessagesListAdapter.getMessageList();
                    if (conversationMessages != null
                            && conversationMessages.size() > 0) {

                        for (int i = 0; i < conversationMessages.size(); ++i) {
                            if (conversationMessages.get(i) instanceof ConversationMessage) {
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
                        if (toBeRemovedMessage.size() > 0) {
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

            //
            @Override
            public void onGroupChannelReadStatusUpdated(GroupChannel groupChannel) {
                Map<Integer, Long> readReceipt = groupChannel.getReadReceipt();
                if (readReceipt.size() == groupChannel.getParticipants().size()) {
                    Long lastRead = 0L;
                    for (Map.Entry<Integer, Long> entry : readReceipt.entrySet()) {
                        if (lastRead == 0L || entry.getValue() < lastRead) {
                            lastRead = entry.getValue();
                        }
                    }
                    holder.setLastTimeRead(lastRead * 1000);
                    messageMessagesListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onOpenChannelReadStatusUpdated(OpenChannel groupChannel) {

            }
        });
    }

    private void removeConnectionListener() {
        ChatCamp.removeConnectionListener(GROUP_CONNECTION_LISTENER);
    }

    private void removeTextWatcher() {
        input.getInputEditText().removeTextChangedListener(textWatcher);
    }

    private void removeChannelListener() {
        ChatCamp.removeChannelListener(CHANNEL_LISTENER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataFile) {
        if (resultCode == RESULT_OK) {
            // TODO Auto-generated method stub
            switch (requestCode) {
                case PICK_MEDIA_RESULT_CODE:
                    if (resultCode == RESULT_OK) {
                        Uri uri = dataFile.getData();
                        Intent intent = new Intent(ConversationActivity.this, MediaPreviewActivity.class);
                        intent.putExtra(MediaPreviewActivity.IMAGE_URI, uri.toString());
                        startActivityForResult(intent, PREVIEW_FILE_RESULT_CODE);
                    }
                    break;
                case PREVIEW_FILE_RESULT_CODE:
                    String uriMedia = dataFile.getExtras().getString(MediaPreviewActivity.IMAGE_URI);
                    uploadFile(Uri.parse(uriMedia));

                    break;
                case PICKFILE_RESULT_CODE:
                    Uri uriFile = dataFile.getData();
                    uploadFile(uriFile);
                    break;
                case CAPTURE_MEDIA_RESULT_CODE:
                    Uri uri;
                    if (dataFile == null || dataFile.getData() == null) {
                        uri = Uri.parse(currentPhotoPath);
                    } else {
                        uri = dataFile.getData();
                    }
                    uploadFile(uri);
                    break;

            }
        }
    }

    private void uploadFile(Uri uri) {
        String path = FilePath.getPath(this, uri);
        String fileName = "";
        String contentType = "";
        File file;
        if (path == null) {
            path = uri.toString();
            fileName = new File(path).getName();
            contentType = "image/*";
        } else {
            fileName = FilePath.getFileName(ConversationActivity.this, uri);
            contentType = getContentResolver().getType(uri);
        }
        if(contentType.contains("image")) {
            file = new File (path);
            try {
                File compressedFile = createImageFile();
                Bitmap bitmap = decodeSampledBitmapFromFile(path, 1280, 800);
                bitmap.compress (Bitmap.CompressFormat.JPEG, 100, new FileOutputStream (compressedFile));
                file  = compressedFile;
            }
            catch (Throwable t) {
                Log.e("ERROR", "Error compressing file." + t.toString ());
                t.printStackTrace ();
            }
        } else {
            file = new File(path);
        }
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        g.sendAttachment(file, fileName, contentType
                , new GroupChannel.UploadAttachmentListener() {
                    @Override
                    public void onUploadProgress(int progress) {
                        progressBar.setProgress(progress);
                    }

                    @Override
                    public void onUploadSuccess() {
                        progressBar.setVisibility(View.GONE);
                        g.markAsRead();
                        Snackbar.make(progressBar, "File Uploaded Successfully", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onUploadFailed(Throwable error) {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(progressBar, "Failed to upload File", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_DOCUMENT) {
            chooseFile(DOCUMENT);
        } else if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_MEDIA) {
            chooseFile(MEDIA);
        } else if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            downloadAndOpenDocument(document);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File file = createImageFile();
            Uri photoURI = FileProvider.getUriForFile(this,
                    "io.chatcamp.app.fileprovider",
                    file);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            Intent chooserIntent = Intent.createChooser(takePictureIntent, "Capture Image or Video");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takeVideoIntent});
            startActivityForResult(chooserIntent, CAPTURE_MEDIA_RESULT_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class MessageTextWatcher implements TextWatcher {

        private final GroupChannel groupChannel;

        public MessageTextWatcher(GroupChannel groupChannel) {
            this.groupChannel = groupChannel;
        }

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
    }

    class OnTitleClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (isOneToOneConversation) {
                Intent intent = new Intent(ConversationActivity.this, UserProfileActivity.class);
                if (otherParticipant != null) {
                    intent.putExtra(UserProfileActivity.KEY_PARTICIPANT_ID, otherParticipant.getId());
                    intent.putExtra(UserProfileActivity.KEY_GROUP_ID, g.getId());
                }
                startActivity(intent);
            } else {
                Intent intent = new Intent(ConversationActivity.this, GroupDetailActivity.class);
                intent.putExtra(KEY_GROUP_ID, g.getId());
                startActivity(intent);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void downloadAndOpenDocument(final MessageContentType.Document message) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                document = message;
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                return;
            }
        }
        new Thread(new Runnable() {
            public void run() {
                Uri path = FileProvider.getUriForFile(ConversationActivity.this,
                        "io.chatcamp.app.fileprovider",
                        downloadFile(message.getDocumentUrl()));
//                Uri path = Uri.fromFile(downloadFile(message.getDocumentUrl()));
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (message instanceof ConversationMessage) {
                        intent.setDataAndType(path, ((ConversationMessage) message).getMessage().getAttachment().getType());
                    } else {
                        intent.setDataAndType(path, "application/*");
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {

                }
            }
        }).start();

    }

    private File downloadFile(String downloadFilePath) {

        File file = null;
        try {
            File SDCardRoot = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file1 = new File(downloadFilePath);
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, file1.getName());
            if (file.exists()) {
                return file;
            }

            URL url = new URL(downloadFilePath);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
//            urlConnection.setDoOutput(true);
//
            // connect
            urlConnection.connect();

            // set the path where we want to save the file


            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            int totalsize = urlConnection.getContentLength();
            int downloadedSize = 0;

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                final float per = ((float) downloadedSize / totalsize) * 100;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress((int) per);
                    }
                });

            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    progressBar.setProgress(0);
                }
            });

            // close the output stream when complete //
            fileOutput.close();

        } catch (final MalformedURLException e) {
            Log.e("document", e.getMessage());
        } catch (final IOException e) {
            Log.e("document", e.getMessage());
        } catch (final Exception e) {
            Log.e("document", e.getMessage());
        }
        return file;
    }

    public Bitmap decodeSampledBitmapFromFile(String path, int reqHeight,
                                                     int reqWidth) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
}
