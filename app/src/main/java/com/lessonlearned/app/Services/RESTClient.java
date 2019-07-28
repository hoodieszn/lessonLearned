package com.lessonlearned.app.Services;
import android.content.Context;

import com.lessonlearned.app.BuildConfig;
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
    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", AUTH_TOKEN);
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void put(Context context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler){
        client.addHeader("Authorization", AUTH_TOKEN);
        client.put(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }
    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.addHeader("Authorization", AUTH_TOKEN);
        client.delete(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void delete(Context context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler){
        client.addHeader("Authorization", AUTH_TOKEN);
        client.delete(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
