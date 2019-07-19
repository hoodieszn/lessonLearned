package com.example.lessonlearned.Services;
import android.content.Context;

import com.example.lessonlearned.BuildConfig;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.entity.StringEntity;

public class RESTClient {

    private static final String BASE_URL = "https://llbackend.herokuapp.com/api/v1/";
    private static final String AUTH_TOKEN = BuildConfig.AuthToken;


    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", AUTH_TOKEN);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", AUTH_TOKEN);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", AUTH_TOKEN);
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
