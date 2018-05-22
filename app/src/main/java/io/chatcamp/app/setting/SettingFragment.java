package io.chatcamp.app.setting;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.chatcamp.app.LocalStorage;
import io.chatcamp.app.MainActivity;
import io.chatcamp.app.R;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;

public class SettingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        getActivity().setTitle("Setting");
        TextView logout = view.findViewById(R.id.tv_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalStorage.getInstance().setUsername("");
                LocalStorage.getInstance().setUserId("");
                ChatCamp.disconnect(new ChatCamp.DisconnectListener() {
                    @Override
                    public void onDisconnected(ChatCampException e) {
                        Intent logoutIntent = new Intent(getActivity(), MainActivity.class);
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logoutIntent);
                        getActivity().finish();
                    }
                });

            }
        });
        return view;
    }
}
