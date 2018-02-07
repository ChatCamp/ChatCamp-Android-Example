package io.chatcamp.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shubhamdhabhai on 07/02/18.
 */

public class LocalStorage {

    private static final String USER_ID = "chat_camp_user_id";
    private static final String USERNAME = "chat_camp_username";
    private static final String SHARED_PREFERENCES_STORE = "shared_preference_chat_camp";
    private final Context context;

    private SharedPreferences preferences;
    private static LocalStorage instance;

    private LocalStorage(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static LocalStorage getInstance() {
        if(instance == null) {
            instance = new LocalStorage(BaseApplication.getInstance());
            return instance;
        } else {
            return instance;
        }
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public String getUserId() {
        return preferences.getString(USER_ID, "");
    }

    public String getUsername() {
        return preferences.getString(USERNAME, "");
    }
}
