package io.chatcamp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chatcamp.uikit.customview.LoadingView;
import com.chatcamp.uikit.messages.RecyclerScrollMoreListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.User;
import io.chatcamp.sdk.UserListQuery;

public class UserListFragment extends Fragment implements UserListAdapter.UserClickListener,
        RecyclerScrollMoreListener.OnLoadMoreListener {

    private UserListQuery query;
    private UserListAdapter userListAdapter;
    ProgressBar progressBar;
    TextView placeHolderText;
    private boolean isFirstTime = true;
    private LoadingView loadingView;
    private EditText searchUserEt;
    private Timer timer = new Timer();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        query = ChatCamp.createUserListQuery();
        userListAdapter = new UserListAdapter(getActivity());
        userListAdapter.setUserClickListener(this);
        getActivity().setTitle("Users");
        progressBar = view.findViewById(R.id.progress_bar);
        placeHolderText = view.findViewById(R.id.tv_place_holder);
        loadingView = view.findViewById(R.id.loading_view);
        searchUserEt = view.findViewById(R.id.et_search_user);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerScrollMoreListener recyclerScrollMoreListener = new RecyclerScrollMoreListener(manager, this);
        recyclerView.addOnScrollListener(recyclerScrollMoreListener);
        recyclerView.setAdapter(userListAdapter);
        loadUsers("", false);
        searchUserEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loadUsers(editable.toString(), true);
            }
        });
        return view;
    }

    private void loadUsers(String searchText, boolean hasTextChanged) {
        if (!TextUtils.isEmpty(searchText) || hasTextChanged) {
            query = ChatCamp.createUserListQuery();
            query.setDisplayNameSearch(searchText);
            isFirstTime = true;
        }
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                query.load(20, new UserListQuery.ResultHandler() {
                    @Override
                    public void onResult(List<User> userList, ChatCampException e) {
                        if (isFirstTime) {
                            isFirstTime = false;
                            progressBar.setVisibility(View.GONE);
                            userListAdapter.clear();
                            if (userList.size() == 0) {
                                placeHolderText.setVisibility(View.VISIBLE);
                            } else {
                                placeHolderText.setVisibility(View.GONE);
                            }
                        }
                        for (User user : userList) {
                            if (user.getId().equals(ChatCamp.getCurrentUser().getId())) {
                                userList.remove(user);
                                break;
                            }
                        }
                        loadingView.setVisibility(View.GONE);
                        userListAdapter.addAll(userList);
                    }
                });
            }
        }, 500);
//            query.load(20, new UserListQuery.ResultHandler() {
//                @Override
//                public void onResult(List<User> userList, ChatCampException e) {
//                    if (isFirstTime) {
//                        isFirstTime = false;
//                        progressBar.setVisibility(View.GONE);
//                        userListAdapter.clear();
//                        if (userList.size() == 0) {
//                            placeHolderText.setVisibility(View.VISIBLE);
//                        } else {
//                            placeHolderText.setVisibility(View.GONE);
//                        }
//                    }
//                    for (User user : userList) {
//                        if (user.getId().equals(ChatCamp.getCurrentUser().getId())) {
//                            userList.remove(user);
//                            break;
//                        }
//                    }
//                    loadingView.setVisibility(View.GONE);
//                    userListAdapter.addAll(userList);
//                }
//            });
    }

    @Override
    public void onUserClicked(User user) {
        GroupChannel.create("OneToOne", new String[]{ChatCamp.getCurrentUser().getId(), user.getId()}, true, new BaseChannel.CreateListener() {
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
        loadingView.setVisibility(View.VISIBLE);
        loadUsers("", false);
    }
}
