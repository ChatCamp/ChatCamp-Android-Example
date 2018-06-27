package io.chatcamp.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chatcamp.uikit.channel.ChannelAdapter;
import com.chatcamp.uikit.channel.ChannelList;

import java.util.Timer;
import java.util.TimerTask;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.GroupChannelListQuery;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChannelListFragment extends Fragment {
    private TabLayout tabLayout;
    private ChannelList channelList;
    private GroupChannelListQuery.ParticipantState groupFilter;
    private ProgressBar progressBar;

    public GroupChannelListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Group Channel");
        View view = inflater.inflate(R.layout.fragment_group_channel_list, container, false);
        tabLayout = view.findViewById(R.id.group_navigation);
        progressBar = view.findViewById(R.id.progress_bar);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.group_all_channels));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.group_invited_channels));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.group_accepted_channels));
        tabLayout.addOnTabSelectedListener(tabLayoutOnClickListener);
        channelList = view.findViewById(R.id.channel_list);
        channelList.setChannelClickListener(new ChannelAdapter.ChannelClickedListener() {
            @Override
            public void onClick(BaseChannel baseChannel) {
                Intent intent = new Intent(getActivity(), ConversationActivity.class);
                intent.putExtra("channelType", "group");
                intent.putExtra("participantState", groupFilter.name());
                intent.putExtra("channelId", baseChannel.getId());
                startActivity(intent);
            }
        });
        channelList.setOnChannelsLoadedListener(new ChannelList.OnChannelsLoadedListener() {
            @Override
            public void onChannelsLoaded() {
                progressBar.setVisibility(View.GONE);
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

    private TabLayout.OnTabSelectedListener tabLayoutOnClickListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    groupFilter = GroupChannelListQuery.ParticipantState.ALL;
                    channelList.setChannelType(BaseChannel.ChannelType.GROUP, GroupChannelListQuery.ParticipantState.ALL);
                    return;
                case 1:
                    groupFilter = GroupChannelListQuery.ParticipantState.INVITED;
                    channelList.setChannelType(BaseChannel.ChannelType.GROUP, GroupChannelListQuery.ParticipantState.INVITED);
                    return;
                case 2:
                    groupFilter = GroupChannelListQuery.ParticipantState.ACCEPTED;
                    channelList.setChannelType(BaseChannel.ChannelType.GROUP, GroupChannelListQuery.ParticipantState.ACCEPTED);
                    return;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if(groupFilter == null) {
            groupFilter = GroupChannelListQuery.ParticipantState.ALL;
        }
        channelList.setChannelType(BaseChannel.ChannelType.GROUP, groupFilter);
    }
}
