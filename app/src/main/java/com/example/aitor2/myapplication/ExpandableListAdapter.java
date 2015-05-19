package com.example.aitor2.myapplication;
/**
 * Created by Aitor on 07/03/2015.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;


public class ExpandableListAdapter extends BaseExpandableListAdapter
{

   private Context context;
   private ArrayList<Group> groups;

   public ExpandableListAdapter(Context context, ArrayList<Group> groups) {
      this.context = context;
      this.groups = groups;
   }



   public Object getChild(int groupPosition, int childPosition) {
      ArrayList<Child> chList = groups.get(groupPosition).getItems();
      return chList.get(childPosition);
   }


   public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
   }


   public View getChildView(int groupPosition, int childPosition,
           boolean isLastChild, View convertView, ViewGroup parent) {

      Child child = (Child) getChild(groupPosition, childPosition);
      if (convertView == null) {
         LayoutInflater infalInflater = (LayoutInflater) context
                 .getSystemService(context.LAYOUT_INFLATER_SERVICE);
         convertView = infalInflater.inflate(R.layout.list_item, null);
      }
      TextView tv_destino = (TextView) convertView.findViewById(R.id.tv_destino);
      TextView tv_personas = (TextView) convertView.findViewById(R.id.tv_personas);
      TextView tv_salida = (TextView) convertView.findViewById(R.id.tv_salida);
      TextView tv_llegada = (TextView) convertView.findViewById(R.id.tv_llegada);
      LinearLayout fondo = (LinearLayout) convertView.findViewById(R.id.fondo);
      final NumberProgressBar bnp = (NumberProgressBar) convertView.findViewById(R.id.numberbar1);

      tv_destino.setText(child.getTv_destino().toString());
      tv_personas.setText(child.getTv_personas().toString());
      tv_salida.setText(child.getTv_salida().toString());
      tv_llegada.setText(child.getTv_llegada().toString());


      bnp.setProgress(Integer.parseInt(child.getTv_personas()));
      String str=child.getTv_destino();
//      if(str.substring(0,5).equals("Palma"))
//      {
//          fondo.setBackgroundColor(Color.parseColor("#ff00adff"));
//      }else{
//          fondo.setBackgroundColor(Color.parseColor("#fff3ff00"));
//      }

      return convertView;
   }
   @Override
   public boolean isChildSelectable(int groupPosition, int childPosition)
   {

      return true;
   }


   public int getChildrenCount(int groupPosition) {
      ArrayList<Child> chList = groups.get(groupPosition).getItems();
      return chList.size();
   }


   public Object getGroup(int groupPosition) {
      return groups.get(groupPosition);
   }


   public int getGroupCount() {
      return groups.size();
   }


   public long getGroupId(int groupPosition) {
      return groupPosition;
   }


   public View getGroupView(int groupPosition, boolean isExpanded,
           View convertView, ViewGroup parent) {
      Group group = (Group) getGroup(groupPosition);
      if (convertView == null) {
         LayoutInflater inf = (LayoutInflater) context
                 .getSystemService(context.LAYOUT_INFLATER_SERVICE);
         convertView = inf.inflate(R.layout.list_group, null);
      }
      TextView tv_evento = (TextView) convertView.findViewById(R.id.tv_evento);
      TextView tv_fecha = (TextView) convertView.findViewById(R.id.tv_fecha);
      LinearLayout bevGroup = (LinearLayout) convertView.findViewById(R.id.groupLinear);
      tv_evento.setText(group.getTv_evento());
       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
       tv_fecha.setText(formatter.format(group.getTv_fecha()));
       if(isExpanded)
       {
           bevGroup.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.myshapeg3));
       }
       else
       {
           bevGroup.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.myshapeg));
       }
      return convertView;
   }


   public boolean hasStableIds() {
      return true;
   }

}
