package io.chatcamp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.chatcamp.uikit.customview.LoadingView;
import com.chatcamp.uikit.user.SelectableUserList;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.OpenChannel;

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
        EditText searchUserEt =findViewById(R.id.et_search_user);
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
                    String [] userIds = new String [userList.getSelectedUsers().size()];
                    for(int i = 0; i < userList.getSelectedUsers().size(); ++i) {
                        userIds[i] = userList.getSelectedUsers().get(i).getId();
                    }
                    GroupChannel.create(channelName.getText().toString(), userIds,
                            false, new GroupChannel.CreateListener() {
                        @Override
                        public void onResult(BaseChannel groupChannel, ChatCampException e) {
                            Intent intent = new Intent(CreateGroupChannelActivity.this, ConversationActivity.class);
                            intent.putExtra("channelType", "group");
                            intent.putExtra("participantState", GroupChannelListQuery.ParticipantState.ALL.name());
                            intent.putExtra("channelId", groupChannel.getId());
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }


}

