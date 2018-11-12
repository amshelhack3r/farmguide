package com.example.hack3r.farmguide.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hack3r.farmguide.R;
import com.example.hack3r.farmguide.helpers.CustomRowAdapter;
import com.example.hack3r.farmguide.helpers.VolleyController;
import com.example.hack3r.farmguide.objects.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CropInfo extends Fragment {
    RecyclerView recyclerView;
    CustomRowAdapter rowAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    List<Crop> cropList;
    ProgressDialog progressDialog;
    public static final String TAG = CropInfo.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_crop_info, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiper);
        recyclerView = view.findViewById(R.id.recyclerView);
        getInfoFromServer();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfoFromServer();
            }
        });
        return view;
    }

    void populateView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        rowAdapter = new CustomRowAdapter(context, cropList);
        recyclerView.setAdapter(rowAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    void getInfoFromServer(){
        showProgress("FETCHING FROM INTERNET");
        cropList = new ArrayList();
        String url = "http://mutall.co.ke/sam_json/data.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i<response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String title = object.getString("crop");
                        String soil = object.getString("soil");
                        String rainfall = object.getString("rainfall");
                        String temperature = object.getString("temperature");
                        String image = object.getString("image");
                        Crop crop = new Crop(title, soil, rainfall, temperature, image);
                        cropList.add(crop);

                    }catch (JSONException e){
                    e.printStackTrace();
                }
                }

                populateView();
                dismissProgress();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.warning(getContext(), "No response: Is internet on??").show();
                dismissProgress();
                swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
            }
        });

        VolleyController.getInstance().addRequestQueue(jsonArrayRequest);
    }

    void showProgress(String message){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void dismissProgress(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
