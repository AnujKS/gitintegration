package com.android.githubintegration.Util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetworkUtil {
    public static OkHttpClient createClient() {

        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }
}
