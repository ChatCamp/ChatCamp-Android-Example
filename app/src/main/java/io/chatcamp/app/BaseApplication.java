package io.chatcamp.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.Message;
import io.chatcamp.sdk.OpenChannel;
import io.chatcamp.sdk.User;

import static io.chatcamp.app.ChatCampAppFirebaseMessagingService.sendNotification;

/**
 * Created by shubhamdhabhai on 08/02/18.
 */

public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private String groupId = "";

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        Log.e("BaseApplication", "on Create");
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(this);
//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                .build());
    }

    private void setupChatcamp() {
        ChatCamp.addChannelListener("NOTIFICATION", new ChatCamp.ChannelListener() {
            @Override
            public void onOpenChannelMessageReceived(OpenChannel openChannel, Message message) {

            }

            @Override
            public void onGroupChannelMessageReceived(GroupChannel groupChannel, Message message) {
                Log.e("Base Application", "push notification");
                if (!BaseApplication.getInstance().getGroupId().equals(groupChannel.getId())) {
                    sendNotification(BaseApplication.this, groupChannel.getId(),
                            BaseChannel.ChannelType.GROUP.name(), message, "chatcamp");
                }
            }

            @Override
            public void onGroupChannelUpdated(GroupChannel groupChannel) {

            }

            @Override
            public void onGroupChannelTypingStatusChanged(GroupChannel groupChannel) {

            }

            @Override
            public void onOpenChannelTypingStatusChanged(OpenChannel groupChannel) {

            }

            @Override
            public void onGroupChannelReadStatusUpdated(GroupChannel groupChannel) {

            }

            @Override
            public void onOpenChannelReadStatusUpdated(OpenChannel groupChannel) {

            }
        });
        if (!TextUtils.isEmpty(LocalStorage.getInstance().getUserId())
                && !TextUtils.isEmpty(LocalStorage.getInstance().getUsername())
                && ChatCamp.getConnectionState() != ChatCamp.ConnectionState.OPEN)  {
            ChatCamp.init(this, "6346990561630613504");
            ChatCamp.connect(LocalStorage.getInstance().getUserId(), new ChatCamp.ConnectListener() {
                @Override
                public void onConnected(User user, ChatCampException e) {
                    // do nothing
                    Log.e("BaseAplication", "chatcamp connected in base application");
                }
            });
            return;
        }
    }
    public static BaseApplication getInstance() {
        return instance;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        setupChatcamp();
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
