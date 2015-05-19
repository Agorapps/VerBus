package com.example.aitor2.myapplication;

/**
 * Created by aitor2 on 07/04/2015.
 */ import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

    public class drawer_adapter extends ArrayAdapter{
        String[] color_names;
        Integer[] image_id;
        Context context;
        public drawer_adapter(Activity context,Integer[] image_id, String[] text){
            super(context, R.layout.drawer_items, text);
            // TODO Auto-generated constructor stub
            this.color_names = text;
            this.image_id = image_id;
            this.context = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View single_row = inflater.inflate(R.layout.drawer_items, null,
                    true);
            TextView textView = (TextView) single_row.findViewById(R.id.rowText);
            ImageView imageView = (ImageView) single_row.findViewById(R.id.rowIcon);
            textView.setText(color_names[position]);
            imageView.setImageResource(image_id[position]);

            return single_row;
        }
    }

