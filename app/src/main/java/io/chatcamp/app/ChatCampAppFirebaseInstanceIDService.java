package io.chatcamp.app;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.FirebaseInstanceId;
import android.util.Log;

import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.User;

/**
 * Created by ChatCamp Team on 07/12/17.
 */

public class ChatCampAppFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("CHATCAMP APP", "Refreshed token: " + refreshedToken);
        if(FirebaseInstanceId.getInstance().getToken() != null && ChatCamp.getConnectionState() == ChatCamp.ConnectionState.OPEN) {
            ChatCamp.updateUserPushToken(FirebaseInstanceId.getInstance().getToken(), new ChatCamp.UserPushTokenUpdateListener() {
                @Override
                public void onUpdated(User user, ChatCampException e) {
                    Log.d("CHATCAMP_APP", "PUSH TOKEN REGISTERED");

                }
            });
        }
    }
}
