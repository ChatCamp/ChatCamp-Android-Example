package io.chatcamp.app;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.FirebaseInstanceId;
import android.util.Log;

/**
 * Created by ChatCamp Team on 07/12/17.
 */

public class ChatCampAppFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("CHATCAMP APP", "Refreshed token: " + refreshedToken);
    }
}
