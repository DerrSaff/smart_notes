package com.test.smartnotes;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by saff on 29.06.17.
 */

public class SmartNotesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
