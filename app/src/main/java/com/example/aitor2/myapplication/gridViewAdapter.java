package com.example.aitor2.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by aitor2 on 08/04/2015.
 */
public class gridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public gridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.gNombreEvento = (TextView) row.findViewById(R.id.gNombreEvento);
            holder.gFecha = (TextView) row.findViewById(R.id.gFecha);
            holder.gSalida= (TextView) row.findViewById(R.id.gSalida);
            holder.gLlegada= (TextView) row.findViewById(R.id.gLlegada);
            holder.tv_tipo=(TextView) row.findViewById(R.id.tv_tipo);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ImageItem item = (ImageItem) data.get(position);
        holder.gNombreEvento.setText(item.getgNombreEvento());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        holder.gFecha.setText(formatter.format(item.getgFecha()));
        holder.gSalida.setText(item.getgHoraSalida());
        holder.gLlegada.setText(item.getgHoraLlegada());
        if(Pattern.compile("l").matcher(item.getTipo()).find())
        {
            holder.tv_tipo.setVisibility(View.VISIBLE);
        }
        return row;
    }

    static class ViewHolder {
        TextView gNombreEvento;
        TextView gFecha;
        TextView gSalida;
        TextView gLlegada;
        TextView tv_tipo;
    }
}
