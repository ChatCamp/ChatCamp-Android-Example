package io.chatcamp.app;

import android.app.Application;

/**
 * Created by shubhamdhabhai on 08/02/18.
 */

public class BaseApplication extends Application{

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
