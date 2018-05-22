package io.chatcamp.app;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stfalcon.chatkit.messages.RecyclerScrollMoreListener;

import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.User;
import io.chatcamp.sdk.UserListQuery;

public class UserListFragment extends Fragment implements UserListAdapter.UserClickListener,
        RecyclerScrollMoreListener.OnLoadMoreListener {

    private UserListQuery query;
    private UserListAdapter userListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        query = new UserListQuery();
        userListAdapter = new UserListAdapter(getActivity());
        userListAdapter.setUserClickListener(this);
        getActivity().setTitle("Users");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerScrollMoreListener recyclerScrollMoreListener = new RecyclerScrollMoreListener(manager, this);
        recyclerView.addOnScrollListener(recyclerScrollMoreListener);
        recyclerView.setAdapter(userListAdapter);
        loadUsers();
        return view;
    }

    private void loadUsers() {
        query.get(new UserListQuery.ResultHandler() {
            @Override
            public void onResult(List<User> userList, ChatCampException e) {
                for(User user : userList) {
                    if(user.getId().equals(LocalStorage.getInstance().getUserId())) {
                        userList.remove(user);
                        break;
                    }
                }
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
                Intent intent = new Intent(getActivity(), ConversationActivity.class);
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