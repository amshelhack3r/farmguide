package com.example.hack3r.farmguide.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.hack3r.farmguide.MainActivity;
import com.example.hack3r.farmguide.R;
import com.example.hack3r.farmguide.helpers.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainFragment extends Fragment{

    Context c;
    TextView period, month_heading, month_info, variant1, variant2, variant1_info, variant2_info;
    ImageView variant1_image, variant2_image;
    Button change_period;
    public final String TAG = MainActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    public final String url = "http://mutall.co.ke/sam_json/planting_seasons.json";
    String[] MONTHS = {
            "JANUARY",
            "FEBRUARY",
            "MARCH",
            "APRIL",
            "MAY",
            "JUNE",
            "JULY",
            "AUGUST",
            "SEPTEMBER",
            "OCTOBER",
            "NOVEMBER",
            "DECEMBER"
    };
   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        period = view.findViewById(R.id.date);
        month_heading = view.findViewById(R.id.month);
        month_info = view.findViewById(R.id.month_info);
        variant1 = view.findViewById(R.id.variant1);
        variant1_info = view.findViewById(R.id.variant1_info);
        variant1_image = view.findViewById(R.id.variant1_image);
        variant2 = view.findViewById(R.id.variant2);
        variant2_info = view.findViewById(R.id.variant2_info);
        variant2_image = view.findViewById(R.id.variant2_image);
        change_period = view.findViewById(R.id.change_period);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        period.setText(dateFormat.format(date));
        getJsonFromServer(url, getMonthNum());
        change_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                }
        });
        return view;
    }

    //get information from the internet
    public void getJsonFromServer(String url, final int Month) {
       showProgress("Please Wait");
        //Make a new Json ArrayRequest from server
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object = response.getJSONObject(Month);
                    String month_name = MONTHS[Month];
                    month_heading.setText(month_name);
                    String intro = object.getString("intro");
                    month_info.setText(intro);
                    JSONArray jsonArray = object.getJSONArray("variant");

                    JSONObject object1 = jsonArray.getJSONObject(0);
                    String v1 = object1.getString("title");
                    variant1.setText(v1);
                    String v1_info = object1.getString("info");
                    variant1_info.setText(v1_info);
                    Glide.with(getContext())
                            .load("http://mutall.co.ke/sam_json/images/"+v1.toLowerCase()+".jpg")
                            .into(variant1_image);


                    JSONObject object2 = jsonArray.getJSONObject(1);
                    String v2 = object2.getString("title");
                    variant2.setText(v2);
                    String v2_info = object2.getString("info");
                    variant2_info.setText(v2_info);
                    Glide.with(getContext())
                            .load("http://mutall.co.ke/sam_json/images/"+v2.toLowerCase()+".jpg")
                            .into(variant2_image);
                }catch (JSONException e){
                    e.printStackTrace();
                }

                dismissProgress();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                showToast(error.getMessage());
                dismissProgress();
            }
        });

        VolleyController.getInstance().addRequestQueue(jsonArrayRequest);
    }

    //helper function to show toast notifications
    public void showToast(String message){
        Toasty.info(getContext(), message, Toast.LENGTH_LONG).show();
    }

    //show notifications on the notification bar
    public void systemNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("PLANT PLANT NOW")
                .setContentText("Get into the app to know more")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(1, mBuilder.build());
    }

    //Get the current month number
    public int getMonthNum(){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = format.parse(period.getText().toString());
            cal.setTime(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        int num = cal.get(Calendar.MONTH);
        return num;
    }

    void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("SELECT MONTH")
                .setItems(R.array.MONTHS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getJsonFromServer(url, which);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
