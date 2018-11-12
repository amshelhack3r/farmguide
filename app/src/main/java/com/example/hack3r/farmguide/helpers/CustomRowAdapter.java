package com.example.hack3r.farmguide.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hack3r.farmguide.R;
import com.example.hack3r.farmguide.objects.Crop;

import java.util.List;

public class CustomRowAdapter extends RecyclerView.Adapter<CustomRowAdapter.MyViewHolder> {
        public List<Crop> cropList;
        Context context;

        public CustomRowAdapter(Context context, List<Crop> list) {
            this.cropList = list;
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView crop_title, rainfall, soil, temperature;
            ImageView crop_image;


            public MyViewHolder(View view){
                super(view);
                crop_image = view.findViewById(R.id.imageView2);
                crop_title = view.findViewById(R.id.crop_title);
                rainfall = view.findViewById(R.id.rainfall);
                soil = view.findViewById(R.id.soil_type);
                temperature = view.findViewById(R.id.temperature);

            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Crop crop = cropList.get(position);
            holder.crop_title.setText(crop.getCrop_title());
            holder.rainfall.setText(crop.getRainfall());
            holder.soil.setText(crop.getSoil());
            holder.temperature.setText(crop.getTemperature());
//            holder.crop_image.setImageResource(R.drawable.beans);

            Glide.with(context)
                    .load(crop.getImage())
                    .into(holder.crop_image);

        }

        @Override
        public int getItemCount() {
            return cropList.size();
        }
}
