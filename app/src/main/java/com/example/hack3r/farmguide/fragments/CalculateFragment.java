package com.example.hack3r.farmguide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hack3r.farmguide.R;

import es.dmoral.toasty.Toasty;


public class CalculateFragment extends Fragment {
    TextView result, converter, spacing, formula, total;
    EditText landsize, seedspacing;
    Button calculate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculate, container, false);
        result = view.findViewById(R.id.textView17);
        converter = view.findViewById(R.id.acre_inch_convert);
        spacing = view.findViewById(R.id.seed_spacing);
        formula = view.findViewById(R.id.formula);
        total = view.findViewById(R.id.total);
        landsize = view.findViewById(R.id.editText5);
        seedspacing = view.findViewById(R.id.editText6);
        calculate = view.findViewById(R.id.button);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allInputted()){
                    calculate();
                }else {
                    Toasty.error(container.getContext(), "Land Size Cannot be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    boolean allInputted(){
        boolean bool = true;
        if (landsize.getText().toString().matches("")){
            bool = false;
        }
        return bool;
    }

    void calculate(){
        int land = Integer.valueOf(landsize.getText().toString());
        int space;
        if (seedspacing.getText().toString().matches("")){
            space = 6;
        }else {
            space = Integer.valueOf(seedspacing.getText().toString());
        }

        result.setText("Convert Acres into inches: 1 acre = 6.273e+6 inches");
        double total_inches = land * 6.273e+6;
        converter.setText(land+" acres = "+total_inches+" inches");
        spacing.setText("Seed Spacing= "+space +"*"+space+" inches");
        double total_spacing = space * space;
        formula.setText("Formula: "+total_inches+"/"+total_spacing);
        double total_seedling = total_inches/total_spacing;
        total.setText("Approx Total Seedling: "+total_seedling);

    }
}
