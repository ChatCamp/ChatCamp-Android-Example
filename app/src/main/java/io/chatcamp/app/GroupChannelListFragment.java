package io.chatcamp.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chatcamp.uikit.channel.ChannelAdapter;
import com.chatcamp.uikit.channel.ChannelList;
import com.chatcamp.uikit.customview.LoadingView;
import com.chatcamp.uikit.database.DbBaseChannelWrapper;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChannelListFragment extends Fragment {
//    private TabLayout tabLayout;
    private ChannelList channelList;
    private GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter state;
    private ProgressBar progressBar;
    private TextView placeHolderText;
    private LoadingView loadingView;
    private FloatingActionButton mChannelCreate;

    public GroupChannelListFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_channel_list, container, false);
//        tabLayout = view.findViewById(R.id.group_navigation);
        progressBar = view.findViewById(R.id.progress_bar);
        placeHolderText = view.findViewById(R.id.tv_place_holder);
        loadingView = view.findViewById(R.id.loading_view);
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.group_all_channels));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.group_invited_channels));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.group_accepted_channels));
//        tabLayout.addOnTabSelectedListener(tabLayoutOnClickListener);
        channelList = view.findViewById(R.id.channel_list);
        channelList.setLoadingView(loadingView);
        mChannelCreate = (FloatingActionButton) view.findViewById(R.id.floating_button_create);
        mChannelCreate.setOnClickListener(mChannelCreateClickListener);
        channelList.setChannelClickListener(new ChannelAdapter.ChannelClickedListener() {
            @Override
            public void onClick(DbBaseChannelWrapper baseChannel) {
                Intent intent = new Intent(getActivity(), ConversationActivity.class);
                intent.putExtra("channelType", "group");
                intent.putExtra("participantState", state.name());
                intent.putExtra("channelId", baseChannel.getId());
                startActivity(intent);
            }
        });
        channelList.setOnChannelsLoadedListener(new ChannelList.OnChannelsLoadedListener() {
            @Override
            public void onChannelsLoaded() {
                progressBar.setVisibility(View.GONE);
                if(channelList.getAdapter().getItemCount() == 0) {
                    placeHolderText.setVisibility(View.VISIBLE);
                } else {
                    placeHolderText.setVisibility(View.GONE);
                }
            }
        });
//        channelList.setAvatarImageLoader(new ImageLoader() {
//            @Override
//            public void loadImage(ImageView imageView, String url) {
//                // add image loading logic here
//            }
//        });

        return view;
    }

//    private TabLayout.OnTabSelectedListener tabLayoutOnClickListener = new TabLayout.OnTabSelectedListener() {
//        @Override
//        public void onTabSelected(TabLayout.Tab tab) {
//            switch (tab.getPosition()) {
//                case 0:
//                    state = GroupChannelListQuery.ParticipantState.ALL;
//                    channelList.setChannelType(BaseChannel.ChannelType.GROUP, GroupChannelListQuery.ParticipantState.ALL);
//                    return;
//                case 1:
//                    state = GroupChannelListQuery.ParticipantState.INVITED;
//                    channelList.setChannelType(BaseChannel.ChannelType.GROUP, GroupChannelListQuery.ParticipantState.INVITED);
//                    return;
//                case 2:
//                    state = GroupChannelListQuery.ParticipantState.ACCEPTED;
//                    channelList.setChannelType(BaseChannel.ChannelType.GROUP, GroupChannelListQuery.ParticipantState.ACCEPTED);
//                    return;
//            }
//        }
//
//        @Override
//        public void onTabUnselected(TabLayout.Tab tab) {
//
//        }
//
//        @Override
//        public void onTabReselected(TabLayout.Tab tab) {
//
//        }
//    };

    @Override
    public void onResume() {
        super.onResume();
        if(state == null) {
            state = GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_ALL;
        }
        if(state == GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_ALL) {
//            GroupChannelListQuery listQuery = GroupChannel.createGroupChannelListQuery();
//            listQuery.setViewOnlyFilter(GroupChannelListQuery.GroupChannelListQueryViewOnlyFilter.VIEW_ONLY);
//            ;
//            listQuery.load(new GroupChannelListQuery.ResultHandler() {
//                @Override
//                public void onResult(List<GroupChannel> groupChannelList, ChatCampException e) {
//                    Log.d("df", "sdf");
//                }
//            });
//            GroupChannelListQuery listQueryAll = GroupChannel.createGroupChannelListQuery();
//            List<String> customFilter = new ArrayList<>();
//            listQueryAll.setCustomFilterSearch("custom");
//
//            listQueryAll.load(new GroupChannelListQuery.ResultHandler() {
//                @Override
//                public void onResult(List<GroupChannel> groupChannelList, ChatCampException e) {
//                    Log.d("df", "sdf");
//                }
//            });
            GroupChannelListQuery listQueryWrite = GroupChannel.createGroupChannelListQuery();
            listQueryWrite.setViewOnlyFilter(GroupChannelListQuery.GroupChannelListQueryViewOnlyFilter.WRITE);
//            ;
            listQueryWrite.load(new GroupChannelListQuery.ResultHandler() {
                @Override
                public void onResult(List<GroupChannel> groupChannelList, ChatCampException e) {
                    Log.d("df", "sdf");
                }
            });
        }
        channelList.setChannelType(BaseChannel.ChannelType.GROUP, state);
    }

    private FloatingActionButton.OnClickListener mChannelCreateClickListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), CreateGroupChannelActivity.class);
            startActivity(intent);
        }
    };

    public void setParticipantState(GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter state) {
        this.state = state;
    }
}
