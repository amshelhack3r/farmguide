package com.example.hack3r.farmguide.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MainFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    Context c;
    TextView period, month_heading, month_info, variant1, variant2, variant1_info, variant2_info;
    ImageView variant1_image, variant2_image;
    Button change_period;
    public final String TAG = MainActivity.class.getSimpleName();
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
        getJsonFromServer(url);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getJsonFromServer(url);
            }
        };
        period.addTextChangedListener(watcher);

        change_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment datePickerDialog = new DatePickerFragment();
                ((DatePickerFragment) datePickerDialog).setListener(MainFragment.this);
                datePickerDialog.show(getChildFragmentManager(), "date picker");
                }
        });
        return view;
    }

    //get information from the internet
    public void getJsonFromServer(String url) {
        //Make a new Json ArrayRequest from server
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int month_num = getMonthNum();
                try {
                    JSONObject object = response.getJSONObject(month_num);
                    String month_name = MONTHS[month_num];
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                showToast(error.getMessage());
            }
        });

        VolleyController.getInstance().addRequestQueue(jsonArrayRequest);
    }

    //helper function to show toast notifications
    public void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Toasty.success(getContext(), "fragment called", Toast.LENGTH_LONG).show();
       //The calender class compiles months as arrays so months run from 0-11
        int newMonth = month + 1;
        String myDate = day+"/"+newMonth+"/"+year;
        period.setText(myDate);
   }
}
