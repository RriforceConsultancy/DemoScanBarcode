package com.example.denishaamrutiya.demoscanbarcode.compatibility;

import android.content.Context;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.denishaamrutiya.demoscanbarcode.helper.DbHelper;
import com.zookey.universalpreferences.UniversalPreferences;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class AppController extends MultiDexApplication {

    private static Context mAppContext;
    public static AppController application;

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    
    public AppController() {
        application = this;
    }

    public static AppController getApp() {
        if (application == null) {
            application = new AppController();
        }
        return application;
    }

    public static Context getAppContext() {
        if (application == null) {
            application = new AppController();
        }
        return application;
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate");
        super.onCreate();
        UniversalPreferences.initialize(this);
        MultiDex.install(this);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("note.realm")
                .build();
        Realm.setDefaultConfiguration(config);
        firstTime();
        application = this;
    }
    private void firstTime(){
        DbHelper dbHelper = new DbHelper(this,Realm.getDefaultInstance());
    }

    @Override
    public void onTerminate() {
        Log.e(TAG,"onTerminate");
        super.onTerminate();
    }

    public static synchronized AppController getInstance() {
        return application;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

 /*   public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }*/

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
