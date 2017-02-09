package com.rajat.gopetting;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by priyanka on 2/9/2017.
 */

public class APIManager {

    private static final String TAG = "APIManager";
    RequestQueue mQueue;
    private Context context;
    private ResponseInterface responseInterface;
    StringRequest jsonRequest;
    StringRequest jsonRequest1;

    String url;
    String url1;
    private static final String ALLOWED_URI_CHARS = "utf-8";
    Response.Listener<String> defaultResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            responseInterface.parseResponse(response);
        }
    };
    Response.ErrorListener defaultErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            responseInterface.parseResponse("ERROR: " + error.toString());
        }


    };

    public APIManager(Context context, ResponseInterface responseInterface) {
        this.context = context;
        this.responseInterface = responseInterface;
    }

    private void callGetAPI(Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        mQueue = Volley.newRequestQueue(context);
        url = "https://guidebook.com/service/v2/upcomingGuides/";

        jsonRequest = new StringRequest(Request.Method.GET, url,
                responseListener, errorListener);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jsonRequest);
    }

    private void callGetAPI() {
        callGetAPI(defaultResponseListener, defaultErrorListener);
    }
    public void callAPI(){
        callGetAPI();
    }
}
