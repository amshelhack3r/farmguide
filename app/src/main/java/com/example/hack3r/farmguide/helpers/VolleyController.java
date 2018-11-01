package com.example.hack3r.farmguide.helpers;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Create a singleton class to handle requests for the android volley library
 * we use a singleton class because we want to handle multiple requests at a time and not
 * having to create a new requestQueue each time we create a request
 * From the android developer site using the singleton approach prevents memory leaks
 * We extend the application object so as to tell the operating system to generate the object even
 * before the first activity(MainAcivity) is created
 * Invoke this instance in the manifest so that the singleton instance is created when the application
 * is created
 */

public class VolleyController extends Application {
    private static VolleyController ourInstance = new VolleyController();
    public static final String TAG = VolleyController.class.getSimpleName();
    private RequestQueue requestQueue;

   public static VolleyController getInstance() {
        return ourInstance;
    }

    public VolleyController() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    /**
     * we need three methods for handling the requests
     * 1:getRequestQueue which replaces the Volley.newRequestQueue
     * 2:addRequestQueue which adds a request to a queue
     * 2:cancelRequestQueue which cancels a request
     */

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addRequestQueue(Request<T> requestQueue){
        requestQueue.setTag(requestQueue);
        getRequestQueue().add(requestQueue);
    }

    public void cancelRequestQueue(){
        requestQueue.cancelAll(TAG);
    }
}
