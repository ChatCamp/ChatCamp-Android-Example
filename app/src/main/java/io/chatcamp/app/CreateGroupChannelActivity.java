package io.chatcamp.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chatcamp.uikit.customview.LoadingView;
import com.chatcamp.uikit.user.SelectableUserList;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelParams;
import io.chatcamp.sdk.PublicGroupChannelListQuery;

/**
 * A login screen that offers login via email/password.
 */
public class CreateGroupChannelActivity extends AppCompatActivity {

    private EditText channelName;
    private SelectableUserList userList;
    private LoadingView loadingView;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_group_channel);

        channelName = findViewById(R.id.channel_name);
        loadingView = findViewById(R.id.loading_view);
        Button mButtonCreate = findViewById(R.id.button_channel_create);
        userList = findViewById(R.id.selectable_user_list);
        EditText searchUserEt = findViewById(R.id.et_search_user);
        userList.setLoadingView(loadingView);
        final List<String> userIds = new ArrayList<>();
        userIds.add(ChatCamp.getCurrentUser().getId());
        userList.search(null, userIds);


        searchUserEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                userList.search(editable.toString(), userIds);
            }
        });

        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (channelName.getText().toString().length() > 0 && userList.getSelectedUsers().size() > 0) {
                    final List<String> userIds = new ArrayList<>();
                    for (int i = 0; i < userList.getSelectedUsers().size(); ++i) {
                        userIds.add(userList.getSelectedUsers().get(i).getId());
                    }
                    userIds.add(ChatCamp.getCurrentUser().getId());
                    GroupChannelParams params = new GroupChannelParams();
                    params.setName(channelName.getText().toString());
                    List<String> customFilter = new ArrayList<>();
                    customFilter.add("custom");
                    customFilter.add("filter");
                    customFilter.add("abs");
                    params.setCustomFilter(customFilter);
                    params.setDistinct(false);
                    params.setParticipantIds(userIds);
                    params.setData("sample data");
                    params.setPublic(false);
                    List<String> moderators = new ArrayList<>();
                    moderators.add(ChatCamp.getCurrentUser().getId());
                    params.setModeratorIds(moderators);
                    GroupChannel.create(params, new GroupChannel.CreateListener() {
                        @Override
                        public void onResult(final BaseChannel groupChannel, ChatCampException e) {
//                            if(channelName.getText().toString().equals("inactive1 only")) {
//                                groupChannel.setActive(false, new BaseChannel.SetActiveListener() {
//                                    @Override
//                                    public void onResult(ChatCampException e) {
//                                        Log.d("asc", "sd");
//                                    }
//                                });
//                            }

//                            groupChannel.createParticipantListQuery().load(20, new UserListQuery.ResultHandler() {
//                                @Override
//                                public void onResult(List<User> userList, ChatCampException e) {
//                                    Log.d("asc", "sd");
//                                }
//                            });


//                            groupChannel.unmuteParticipant(userIds.get(0), new BaseChannel.UnmuteParticipantListener() {
//                                @Override
//                                public void onResult(ChatCampException e) {
//                                    ((GroupChannel)groupChannel).sync(new GroupChannel.SyncListener() {
//                                        @Override
//                                        public void onResult(GroupChannel groupChannel, ChatCampException e) {
//                                            groupChannel.ifMuted();
//                                        }
//                                    });
//                                    Log.d("asc", "sd");
//                                }
//                            });

                          /*  groupChannel.banParticipant(userIds.get(0), new BaseChannel.BanParticipantListener() {
                                @Override
                                public void onResult(ChatCampException e) {
                                    Log.d("asc", "sd");
                                }
                            });

                            groupChannel.createBannedParticipantListQuery().load(20, new UserListQuery.ResultHandler() {
                                @Override
                                public void onResult(List<User> userList, ChatCampException e) {
                                    Log.d("asc", "sd");
                                }
                            });*/
//                            groupChannel.unbanParticipant(userIds.get(0), new BaseChannel.UnbanParticipantListener() {
//                                @Override
//                                public void onResult(ChatCampException e) {
//                                    Log.d("asc", "sd");
//                                }
//                            });
//                            groupChannel.delete(new BaseChannel.DeleteChannelListener() {
//                                @Override
//                                public void onResult(ChatCampException exception) {
//                                    Log.d("asc", "sd");
//                                }
//                            });
//                            ((GroupChannel)groupChannel).archive(new GroupChannel.OnArchiveListener() {
//                                @Override
//                                public void onResult(ChatCampException e) {
//                                    Log.d("asc", "sd");
//                                }
//                            });
//
//                            ((GroupChannel)groupChannel).unarchive(new GroupChannel.OnUnarchiveListener() {
//                                @Override
//                                public void onResult(ChatCampException e) {
//                                    Log.d("asc", "sd");
//                                }
//                            });

//                            PublicGroupChannelListQuery query = new PublicGroupChannelListQuery();
//                            query.load(new PublicGroupChannelListQuery.ResultHandler() {
//                                @Override
//                                public void onResult(List<GroupChannel> groupChannelList, ChatCampException e) {
//                                    Log.d("asc", "sd");
//                                }
//                            });
















                           /* Intent intent = new Intent(CreateGroupChannelActivity.this, ConversationActivity.class);
                            intent.putExtra("channelType", "group");
                            intent.putExtra("participantState", GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_ALL.name());
                            intent.putExtra("channelId", groupChannel.getId());
                            startActivity(intent);
                            finish();*/
                        }
                    });
                }
            }
        });
    }


}

