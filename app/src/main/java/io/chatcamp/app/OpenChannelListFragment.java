package io.chatcamp.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chatcamp.uikit.channel.ChannelAdapter;
import com.chatcamp.uikit.channel.ChannelList;
import com.chatcamp.uikit.customview.LoadingView;
import com.chatcamp.uikit.database.DbBaseChannelWrapper;

import io.chatcamp.sdk.BaseChannel;


public class OpenChannelListFragment extends Fragment {

    private ChannelList channelList;
    private LoadingView loadingView;
    private FloatingActionButton mChannelCreate;

    public OpenChannelListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open_channel_list, container, false);
        getActivity().setTitle("Open Channel");
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        final TextView placeHolderText = view.findViewById(R.id.tv_place_holder);
        channelList = view.findViewById(R.id.rv_open_channels_list);
        loadingView = view.findViewById(R.id.loading_view);
        channelList.setLoadingView(loadingView);
        mChannelCreate = (FloatingActionButton) view.findViewById(R.id.floating_button_create);
        mChannelCreate.setOnClickListener(mChannelCreateClickListener);
        channelList.setChannelType(BaseChannel.ChannelType.OPEN, null);
        channelList.setChannelClickListener(new ChannelAdapter.ChannelClickedListener() {
            @Override
            public void onClick(DbBaseChannelWrapper baseChannel) {
                Intent intent = new Intent(getActivity(), ConversationActivity.class);
                intent.putExtra("channelType", "open");
                intent.putExtra("participantState", "");
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

        return view;
    }

    private FloatingActionButton.OnClickListener mChannelCreateClickListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), CreateOpenChannelActivity.class);
            startActivity(intent);
        }
    };
}
