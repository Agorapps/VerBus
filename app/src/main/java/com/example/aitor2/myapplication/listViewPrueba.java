package com.example.aitor2.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

/**
 * Created by aitor2 on 11/04/2015.
 */
public class listViewPrueba extends Activity {
    ListView listView ;
    public View row;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.list_view_prueba);
        listView = (ListView) findViewById(R.id.lv1);
        String[] values = new String[] { "Brazos",
                "Piernas",
                "Cadera",
                "brazos2",
                "Todos",
                "Ninguno",
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_activated_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemChecked(0, false);
        listView.setItemChecked(1, false);
        listView.setItemChecked(2, false);
        listView.setItemChecked(3, false);
        listView.setItemChecked(4, false);
        listView.setItemChecked(5, false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                view.setBackgroundColor(Color.parseColor("#33B5E5"));
                if (position == 4) {
                    listView.setItemChecked(0, false);
                    listView.setItemChecked(1, false);
                    listView.setItemChecked(2, false);
                    listView.setItemChecked(5, false);
                    listView.setItemChecked(3, false);

                }else if (position != 4 && position != 5) {
                    listView.setItemChecked(4, false);
                    listView.setItemChecked(5, false);
                }else if (position == 5) {
                    listView.setItemChecked(0, false);
                    listView.setItemChecked(1, false);
                    listView.setItemChecked(2, false);
                    listView.setItemChecked(3, false);
                    listView.setItemChecked(4, false);
                }
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                int size = checked.size(); // number of name-value pairs in the array
                for (int i = 0; i < size; i++) {
                    int key = checked.keyAt(i);
                    boolean value = checked.get(key);
                    if (value) {
                        row = listView.getChildAt(i);
                        row.setBackgroundColor(Color.parseColor("#33B5E5"));
                    }else{
                        row = listView.getChildAt(i);
                        row.setBackgroundColor(Color.parseColor("#F0F0F0"));
                    }
                }
        }
        });
    }

   public void probar(View view)
   {
       ArrayList <String> seleccionados = new ArrayList<String>();
       SparseBooleanArray checked = listView.getCheckedItemPositions();
       int size = checked.size(); // number of name-value pairs in the array
       for (int i = 0; i < size; i++) {
           int key = checked.keyAt(i);
           boolean value = checked.get(key);
           if (value) {

               seleccionados.add(listView.getItemAtPosition(key).toString());
           }
       }

       for(int i=0;i<seleccionados.size();i++)
       {

           Log.d("TAAG", seleccionados.get(i));
       }
   }
}
