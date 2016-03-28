package com.example.shuweizhao.wokba;

import okhttp3.OkHttpClient;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class MyHttpClient {

    private static OkHttpClient myHttpClient;
    public static OkHttpClient getClient() {
        if (myHttpClient == null) {
            myHttpClient = new OkHttpClient();
        }
        return myHttpClient;
    }
}
