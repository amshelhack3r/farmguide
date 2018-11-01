package com.example.hack3r.farmguide.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

public class MainFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    TextView period, month_heading, variant1, variant2, variant1_info, variant2_info;
    public final String TAG = MainActivity.class.getSimpleName();
    public final String url = "http://mutall.co.ke/sam_json/planting_seasons.json";
    Switch aSwitch;
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
            "NOVEMBER",
            "DECEMBER"
    };
   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

//        period = view.findViewById(R.id.date);
//        month_heading = view.findViewById(R.id.month);
//        variant1 = view.findViewById(R.id.variant1);
//        variant1_info = view.findViewById(R.id.variant1_info);
//        variant2 = view.findViewById(R.id.variant2);
//        variant2_info = view.findViewById(R.id.variant2_info);
//        aSwitch = view.findViewById(R.id.switch1);

//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    systemNotification();
//                }
//            }
//        });
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = new Date();
//        period.setText(dateFormat.format(date));
//
//        getJsonFromServer(url);
//        TextWatcher watcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                getJsonFromServer(url);
//            }
//        };
//
//        period.addTextChangedListener(watcher);
         return view;
    }

    //
    public void changePeriod(View view){
        android.support.v4.app.DialogFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.show(getChildFragmentManager(), "date picker");
    }

    //action when you set a date
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //The calender class compiles months as arrays so months run from 0-11
        int newMonth = month + 1;
        String myDate = day+"/"+newMonth+"/"+year;
        period.setText(myDate);
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

                    JSONArray jsonArray = object.getJSONArray("variant");

                    JSONObject object1 = jsonArray.getJSONObject(0);
                    String v1 = object1.getString("title");
                    variant1.setText(v1);
                    String v1_info = object1.getString("info");
                    variant1_info.setText(v1_info);


                    JSONObject object2 = jsonArray.getJSONObject(1);
                    String v2 = object2.getString("title");
                    variant2.setText(v2);
                    String v2_info = object2.getString("info");
                    variant2_info.setText(v2_info);
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

}
