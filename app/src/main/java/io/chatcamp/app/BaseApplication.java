package io.chatcamp.app;

import android.app.Application;

/**
 * Created by shubhamdhabhai on 08/02/18.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                .build());
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
