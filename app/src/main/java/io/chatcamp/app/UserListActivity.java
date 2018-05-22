package io.chatcamp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stfalcon.chatkit.messages.RecyclerScrollMoreListener;

import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.User;
import io.chatcamp.sdk.UserListQuery;

public class UserListActivity extends AppCompatActivity implements UserListAdapter.UserClickListener,
        RecyclerScrollMoreListener.OnLoadMoreListener {

    private UserListQuery query;
    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        query = new UserListQuery();
        userListAdapter = new UserListAdapter(this);
        userListAdapter.setUserClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerScrollMoreListener recyclerScrollMoreListener = new RecyclerScrollMoreListener(manager, this);
        recyclerView.addOnScrollListener(recyclerScrollMoreListener);
        recyclerView.setAdapter(userListAdapter);
        loadUsers();
    }

    private void loadUsers() {
        query.get(new UserListQuery.ResultHandler() {
            @Override
            public void onResult(List<User> userList, ChatCampException e) {
                userListAdapter.addAll(userList);
            }
        });
    }

    @Override
    public void onUserClicked(User user) {
        //TODO use userId from chatcamp
        GroupChannel.create("OneToOne", new String[]{LocalStorage.getInstance().getUserId(), user.getId()}, true, new BaseChannel.CreateListener() {
            @Override
            public void onResult(BaseChannel groupChannel, ChatCampException e) {
                Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                intent.putExtra("channelType", "group");
                intent.putExtra("participantState", GroupChannel.ChannelType.GROUP.name());
                intent.putExtra("channelId", groupChannel.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoadMore(int page, int total) {
        loadUsers();
    }
}
