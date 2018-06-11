package io.chatcamp.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chatcamp.uikit.channel.ChannelAdapter;
import com.chatcamp.uikit.channel.ChannelList;

import java.util.Timer;
import java.util.TimerTask;

import io.chatcamp.sdk.BaseChannel;


public class OpenChannelListFragment extends Fragment {

    private ChannelList channelList;

    public OpenChannelListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open_channel_list, container, false);
        getActivity().setTitle("Open Channel");
        channelList = view.findViewById(R.id.rv_open_channels_list);
        channelList.setChannelType(BaseChannel.ChannelType.OPEN, null);
        channelList.setChannelClickListener(new ChannelAdapter.ChannelClickedListener() {
            @Override
            public void onClick(BaseChannel baseChannel) {
                Intent intent = new Intent(getActivity(), ConversationActivity.class);
                intent.putExtra("channelType", "open");
                intent.putExtra("participantState", "");
                intent.putExtra("channelId", baseChannel.getId());
                startActivity(intent);
            }
        });
        return view;
    }
}
