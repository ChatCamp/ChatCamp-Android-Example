package io.chatcamp.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.chatcamp.app.setting.SettingFragment;

public class ListActivity extends AppCompatActivity {


    private BottomNavigationView navigation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        handleNavigationGroupChannels();
    }


    private void handleNavigationGroupChannels() {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new GroupChannelHomeFragment()).commit();
    }

    private void handleNavigationOpenChannels() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new OpenChannelListFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_open_channels:
                    handleNavigationOpenChannels();
                    return true;
                case R.id.navigation_group_channels:
                    handleNavigationGroupChannels();
                    return true;
                case R.id.navigation_user_list:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserListFragment()).commit();
                    return true;
                case R.id.navigation_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingFragment()).commit();
                    return true;
            }
            return false;
        }
    };
}
