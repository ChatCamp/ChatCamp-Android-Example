package io.chatcamp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.chatcamp.app.setting.SettingActivity;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.OpenChannel;
import io.chatcamp.sdk.OpenChannelListQuery;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TabLayout mTabLayout;
    private TextView mTextMessage;
    private GroupChannelListQuery.ParticipantState groupFilter;
    private FloatingActionButton mChannelCreate;

    private TabLayout.OnTabSelectedListener mTabLayoutOnClickListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.d("APP", "Hello: " + tab.getPosition());
            switch (tab.getPosition()) {
                case 0:
                    groupFilter = GroupChannelListQuery.ParticipantState.ALL;
                    handleNavigationGroupChannels();
                    return;
                case 1:
                    groupFilter = GroupChannelListQuery.ParticipantState.INVITED;
                    handleNavigationGroupChannels();
                    return;
                case 2:
                    groupFilter = GroupChannelListQuery.ParticipantState.ACCEPTED;
                    handleNavigationGroupChannels();
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_open_channels:
//                    mTextMessage.setText(R.string.title_open_channels);
                    handleNavigationOpenChannels();
                    return true;
                case R.id.navigation_group_channels:
//                    mTextMessage.setText(R.string.title_group_channels);
                    mTabLayout.setVisibility(View.VISIBLE);
                    mTabLayout.removeAllTabs();
                    mTabLayout.addTab(mTabLayout.newTab().setText(R.string.group_all_channels));
                    mTabLayout.addTab(mTabLayout.newTab().setText(R.string.group_invited_channels));
                    mTabLayout.addTab(mTabLayout.newTab().setText(R.string.group_accepted_channels));
                    getSupportActionBar().setTitle("Group Channels");
                    handleNavigationGroupChannels();
                    return true;
                case R.id.navigation_settings:
//                    mTextMessage.setText(R.string.title_notifications);
                    startActivity(new Intent(ListActivity.this, SettingActivity.class));
                    return true;
            }
            return false;
        }
    };

    private FloatingActionButton.OnClickListener mChannelCreateClickListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ChannelCreateActivity.class);
            startActivity(intent);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Initializing the tablayout
        mTabLayout = (TabLayout) findViewById(R.id.group_navigation);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_channels_list);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mChannelCreate = (FloatingActionButton) findViewById(R.id.floating_button_create);
        mChannelCreate.setOnClickListener(mChannelCreateClickListener);
        mTabLayout.addOnTabSelectedListener(mTabLayoutOnClickListener);
        handleNavigationOpenChannels();
    }

    private void handleNavigationGroupChannels() {

//        GroupChannel.create("iflychat 12", new String[]{"2", "3"}, false, new GroupChannel.CreateListener() {
//            @Override
//            public void onResult(GroupChannel groupChannel) {
//                System.out.println("GROUP CREATED: " + groupChannel.getName());
//            }
//        });

        GroupChannelListQuery groupChannelListQuery = GroupChannel.createGroupChannelListQuery();
        groupChannelListQuery.setParticipantState(groupFilter);
        groupChannelListQuery.get(new GroupChannelListQuery.ResultHandler() {
            @Override
            public void onResult(List<GroupChannel> groupChannelList, ChatCampException e) {

                final List<GroupChannel> g = groupChannelList;


                mAdapter = new GroupChannelListAdapter(g, new GroupChannelListAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        GroupChannel groupChannelElement = g.get(position);
                        Toast.makeText(getApplicationContext(), "Element " + groupChannelElement.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                        intent.putExtra("channelType", "group");
                        intent.putExtra("participantState", groupFilter.name());
                        intent.putExtra("channelId", groupChannelElement.getId());
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);



//                final String groupChannelId = groupChannelList.get(0).getId();
//                GroupChannel.get(groupChannelId, new GroupChannel.GetListener() {
//                    @Override
//                    public void onResult(GroupChannel groupChannel) {
//                        System.out.println("GET GROUP CHANNEL : " + groupChannel.getName());
//                    }
//                });
            }
        });
    }

    private void handleNavigationOpenChannels() {
        OpenChannelListQuery openChannelListQuery = OpenChannel.createOpenChannelListQuery();
        openChannelListQuery.get(new OpenChannelListQuery.ResultHandler() {
            @Override
            public void onResult(List<OpenChannel> openChannelList, ChatCampException e) {
                final List<OpenChannel> o = openChannelList;


                getSupportActionBar().setTitle("Open Channels");
                mTabLayout.setVisibility(View.GONE);
                mAdapter = new OpenChannelListAdapter(o, new OpenChannelListAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        OpenChannel openChannelElement = o.get(position);
                        Toast.makeText(getApplicationContext(), "Element " + openChannelElement.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                        intent.putExtra("channelType", "open");
                        intent.putExtra("channelId", openChannelElement.getId());
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);

            }
        });
    }

}
