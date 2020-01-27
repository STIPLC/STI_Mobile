package com.sti.sti_mobile;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import co.paystack.android.PaystackSdk;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);


        //PaystackSdk.initialize(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        PaystackSdk.initialize(getApplicationContext());


        Map configCloudinary = new HashMap();
        configCloudinary.put("cloud_name", "stiplc");
        configCloudinary.put("api_key", "568485972771544");
        configCloudinary.put("api_secret", "uQSxwMqLY0c5Kd3JSjHCB8I9M90");
        configCloudinary.put("upload_preset", "z2uab1xl");
        MediaManager.init(this, configCloudinary);


    }
}
