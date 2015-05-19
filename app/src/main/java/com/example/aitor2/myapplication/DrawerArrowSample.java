/*
 * Copyright (C) 2014 Chris Renke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.aitor2.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;


//import com.melnykov.fab.FloatingActionButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kd.dynamic.calendar.generator.ImageGenerator;

import java.util.Calendar;
import java.util.regex.Pattern;

import static android.view.Gravity.START;

public class DrawerArrowSample extends Activity {
    String titles[] = {"Inicio", "Mis reservas"};
    Integer icons[] = {R.drawable.ic_launcher, R.drawable.ic_launcher};
    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    SimpleDateFormat fmt;
    private boolean flipped;
    String group_names[];
    Calendar mCurrentDate;
    Date group_namesFecha[];
    Integer group_separador[];
    Integer id_viaje[];
    String child_destinos[];
    String child_bdSalida[];
    String child_bdLlegada[];
    String child_bdPersonas[];
    int child_color[];
    ArrayList<Group> list;
    ArrayList<Group> list2;
    String fechaComp;
    SwipeRefreshLayout swipeRefreshLayout;
    // FloatingActionButton fab;
    private ExpandableListAdapter ExpAdapter;
    private ArrayList<Group> ExpListItems;
    private ExpandableListView ExpandList;
    FloatingActionButton actionB;
    meterEnCola mec = new meterEnCola();
    //////////calendar


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.home_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();
        drawer_adapter adapter = new drawer_adapter(this, icons, titles);
        ListView lv = (ListView) findViewById(R.id.drawer_listview);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer.closeDrawer(START);
                if (position == 0) {
                    Intent intent = new Intent(DrawerArrowSample.this, DrawerArrowSample.class);
                    startActivity(intent);
                    finish();
                } else if (position == 1) {
                    Intent intent = new Intent(DrawerArrowSample.this, misReservas.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        findViewById(R.id.pink_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawerArrowSample.this, "Clicked pink Floating Action Button", Toast.LENGTH_SHORT).show();
            }
        });
        actionB = (FloatingActionButton) findViewById(R.id.pink_icon);
        actionB.setStrokeVisible(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.GREEN, Color.BLACK, Color.GREEN);

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));


        imageView.setImageDrawable(drawerArrowDrawable);


        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
                }
            }
        });

        final TextView styleButton = (TextView) findViewById(R.id.indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {

            boolean rounded = false;

            @Override
            public void onClick(View v) {

                styleButton.setText(rounded //
                        ? resources.getString(R.string.rounded) //
                        : resources.getString(R.string.squared));

                rounded = !rounded;

                drawerArrowDrawable = new DrawerArrowDrawable(resources, rounded);
                drawerArrowDrawable.setParameter(offset);
                drawerArrowDrawable.setFlip(flipped);
                drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));

                imageView.setImageDrawable(drawerArrowDrawable);
            }
        });

        /////////////////////create expandable listview
        new expandable().execute();
////////////////////calendario


        // Pop up Date picker on pressing the editText


    }

    /////////////////////// refresco del expandible listview

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

///// aqui va las acciones a actualizar
                    new expandable().execute();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);

        }
    };


    //////////////////////////// asynctask Recbir JSON
    class expandable extends AsyncTask<Void, Void, Void> {

        String ou;

        @Override
        protected Void doInBackground(Void... params) {
            /////////////////////////JSON Hijos

            try {
                String txt2;

                HttpClient httpclient2 = new DefaultHttpClient();
                HttpPost httppost2 = new HttpPost("http://agorapps.com/childs.php");
                HttpResponse resp2 = httpclient2.execute(httppost2);
                HttpEntity ent2 = resp2.getEntity();
                txt2 = EntityUtils.toString(ent2);
                JSONObject jsonObject2 = new JSONObject(txt2);
                JSONArray childs = jsonObject2.getJSONArray("childs");
                child_destinos = new String[childs.length()];
                child_bdSalida = new String[childs.length()];
                child_bdLlegada = new String[childs.length()];
                child_bdPersonas = new String[childs.length()];
                id_viaje = new Integer[childs.length()];
                for (int i = 0; i < childs.length(); i++) {
                    JSONObject city2 = childs.getJSONObject(i);
                    child_destinos[i] = city2.getString("bd_destinos");
                    child_bdSalida[i] = city2.getString("bd_salida");
                    child_bdLlegada[i] = city2.getString("bd_llegada");
                    child_bdPersonas[i] = city2.getString("bd_personas");
                    id_viaje[i] = city2.getInt("id_propia");
                }

                //////////////////////////JSON Cabeceras
                String txt;

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://agorapps.com/padres.php");
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity ent = resp.getEntity();
                txt = EntityUtils.toString(ent);
                JSONObject jsonObject = new JSONObject(txt);
                JSONArray eventos = jsonObject.getJSONArray("eventos");
                group_names = new String[eventos.length()];
                group_namesFecha = new Date[eventos.length()];
                group_separador = new Integer[eventos.length()];
                for (int i = 0; i < eventos.length(); i++) {
                    JSONObject city = eventos.getJSONObject(i);
                    group_names[i] = city.getString("bd_evento");
                    String str = city.getString("bd_fecha");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                    try {

                        Date date = formatter.parse(str);
                        group_namesFecha[i] = date;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int u = Integer.parseInt(city.getString("numero"));
                    group_separador[i] = u;

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        protected void onPostExecute(Void result) {
            try {

                ////llenado de los arrays e insercion en adapter
                list = new ArrayList<Group>();

                ArrayList<Child> ch_list;

                int j = 0;
                int size = 0;

                for (int i = 0; i < group_names.length; i++) {
                    size = size + group_separador[i];
                    Group gru = new Group();
                    gru.setTv_evento(group_names[i]);
                    gru.setTv_fecha(group_namesFecha[i]);

                    ch_list = new ArrayList<Child>();
                    for (; j < size; j++) {
                        Child ch = new Child();
                        ch.setTv_destino(child_destinos[j]);
                        ch.setTv_salida(child_bdSalida[j]);
                        ch.setTv_llegada(child_bdLlegada[j]);
                        ch.setTv_personas(child_bdPersonas[j]);
                        ch.setId_viaje(id_viaje[j]);
                        ch_list.add(ch);
                    }
                    gru.setItems(ch_list);
                    list.add(gru);
                }

                Collections.sort(list, new FishNameComparator());
                ExpandList = (ExpandableListView) findViewById(R.id.exp_list);


                actionB.setOnClickListener(setDate);
                ExpListItems = list;
                ExpAdapter = new ExpandableListAdapter(DrawerArrowSample.this, ExpListItems);
                ExpandList.setAdapter(ExpAdapter);


                ExpandList.setOnChildClickListener(new OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        //get the group header
                        Group group = ExpListItems.get(groupPosition);
                        //get the child info
                        Child child = group.getItems().get(childPosition);
                        //display it or do something with it
                        Intent i = new Intent(DrawerArrowSample.this, dialogReserva.class);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String tv_fecha = formatter.format(group.getTv_fecha());

                        i.putExtra("destino", child.getTv_destino());
                        i.putExtra("fecha", tv_fecha);
                        i.putExtra("salida", child.getTv_salida());
                        i.putExtra("idviaje", child.getId_viaje());
                        i.putExtra("plazas", Integer.parseInt(child.getTv_personas()));
                        startActivity(i);
                        finish();
                        return false;
                    }
                });

                ExpandList.setOnScrollListener(new AbsListView.OnScrollListener() {


                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }


                    public void onScroll(AbsListView view, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {
                        if (ExpandList.getFirstVisiblePosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                            actionB.setVisibility(View.VISIBLE);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                            actionB.setVisibility(View.GONE);
                        }
                    }
                });
                super.onPostExecute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    } // Asyntask 4

    public class FishNameComparator implements Comparator<Group> {
        public int compare(Group left, Group right) {
            return left.getTv_fecha().compareTo(right.getTv_fecha());

        }

    }

    public class fixed extends SwipeRefreshLayout {

        public fixed(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

    View.OnClickListener setDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mCurrentDate = Calendar.getInstance();
            int mYear = mCurrentDate.get(Calendar.YEAR);
            int mMonth = mCurrentDate.get(Calendar.MONTH);
            int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(DrawerArrowSample.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                    // Update the editText to display the selected dat
                    String day = String.valueOf(selectedDay);
                    int mes = selectedMonth + 1;
                    String month = String.valueOf(mes);
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    fechaComp = (selectedYear + "-" + month + "-" + day);
                    ///////////////////////////asyntask
                    class expandable2 extends AsyncTask<Void, Void, Void> {
                        int bd_id;
                        String ou;

                        @Override
                        protected Void doInBackground(Void... params) {

                            try {

                                //////////////////////////JSON Cabeceras
                                String txt;

                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost("http://agorapps.com/padres2.php");
                                List<NameValuePair> parame = new ArrayList<NameValuePair>();
                                parame.add(new BasicNameValuePair("dato", fechaComp));
                                httppost.setEntity(new UrlEncodedFormEntity(parame));
                                HttpResponse resp = httpclient.execute(httppost);
                                HttpEntity ent = resp.getEntity();
                                txt = EntityUtils.toString(ent);
                                JSONObject jsonObject = new JSONObject(txt);
                                JSONArray eventos = jsonObject.getJSONArray("eventos");
                                group_names = new String[eventos.length()];
                                group_namesFecha = new Date[eventos.length()];
                                group_separador = new Integer[eventos.length()];
                                for (int i = 0; i < eventos.length(); i++) {
                                    JSONObject city = eventos.getJSONObject(i);
                                    group_names[i] = city.getString("bd_evento");
                                    bd_id = city.getInt("id");
                                    String str = city.getString("bd_fecha");
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                    try {

                                        Date date = formatter.parse(str);
                                        group_namesFecha[i] = date;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                String bd_id2 = String.valueOf(bd_id);
                                String txt2;

                                HttpClient httpclient2 = new DefaultHttpClient();
                                HttpPost httppost2 = new HttpPost("http://agorapps.com/childs2.php");
                                List<NameValuePair> parame2 = new ArrayList<NameValuePair>();
                                parame2.add(new BasicNameValuePair("dato2", bd_id2));
                                httppost2.setEntity(new UrlEncodedFormEntity(parame2));
                                HttpResponse resp2 = httpclient2.execute(httppost2);
                                HttpEntity ent2 = resp2.getEntity();
                                txt2 = EntityUtils.toString(ent2);
                                JSONObject jsonObject2 = new JSONObject(txt2);
                                JSONArray childs = jsonObject2.getJSONArray("childs");
                                child_destinos = new String[childs.length()];
                                child_bdSalida = new String[childs.length()];
                                child_bdLlegada = new String[childs.length()];
                                child_bdPersonas = new String[childs.length()];
                                for (int i = 0; i < childs.length(); i++) {
                                    JSONObject city2 = childs.getJSONObject(i);
                                    child_destinos[i] = city2.getString("bd_destinos");
                                    child_bdSalida[i] = city2.getString("bd_salida");
                                    child_bdLlegada[i] = city2.getString("bd_llegada");
                                    child_bdPersonas[i] = city2.getString("bd_personas");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            return null;
                        }

                        protected void onPostExecute(Void result) {
                            try {

                                ////llenado de los arrays e insercion en adapter
                                list2 = new ArrayList<Group>();

                                ArrayList<Child> ch_list;


                                for (int i = 0; i < group_names.length; i++) {

                                    Group gru = new Group();
                                    gru.setTv_evento(group_names[i]);
                                    gru.setTv_fecha(group_namesFecha[i]);

                                    ch_list = new ArrayList<Child>();
                                    for (int j = 0; j < child_bdLlegada.length; j++) {
                                        Child ch = new Child();
                                        ch.setTv_destino(child_destinos[j]);
                                        ch.setTv_salida(child_bdSalida[j]);
                                        ch.setTv_llegada(child_bdLlegada[j]);
                                        ch.setTv_personas(child_bdPersonas[j]);
                                        ch_list.add(ch);
                                    }
                                    gru.setItems(ch_list);
                                    list2.add(gru);
                                }

                                if (list2.isEmpty()) {
                                    Toast toast1 =
                                            Toast.makeText(getApplicationContext(),
                                                    "Ningún evento en la fecha seleccionada", Toast.LENGTH_SHORT);

                                    toast1.show();
                                } else {
                                    Collections.sort(list2, new FishNameComparator());
                                    ExpListItems = list2;
                                    ExpAdapter = new ExpandableListAdapter(DrawerArrowSample.this, ExpListItems);
                                    ExpandList.setAdapter(ExpAdapter);
                                }
                                super.onPostExecute(result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("TAAAAG", String.valueOf(bd_id));
                        }
                    }
                    new expandable2().execute();
                }
            }, mYear, mMonth, mDay);
            mDatePicker.show();
        }
    };

    // Listview on child click listener
    /////////////////////////////////////////////////////////
//    class cancelarReservaCola extends AsyncTask<Void, Void, Void> {
//        String msj;
//        protected Void doInBackground(Void... params) {
//
//            return null;
//        }
//        private void sendRegistrationIdToBackend() {
//
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost("http://agorapps.com/register.php");
//                List<NameValuePair> parame = new ArrayList<NameValuePair>();
//                parame.add(new BasicNameValuePair("regId", regid));
//                parame.add(new BasicNameValuePair("tlf",tlf));
//                httppost.setEntity(new UrlEncodedFormEntity(parame));
//                HttpResponse resp = httpclient.execute(httppost);
//            }catch(Exception e){Log.d("PENE", "horror");}
//        }
//        protected void onPostExecute(Void result) {
//
//        }
//    }
    public void onBackPressed() {
        finish();
    }
}
