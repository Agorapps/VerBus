package com.example.aitor2.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.view.Gravity.START;

public class misReservas extends Activity {


    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    GoogleCloudMessaging gcm;
    String regid;
    String SENDER_ID = "790751637625";
    private boolean flipped;
    private GridView gridView;
    TextView tv_telefono;
    Date date2;
    private gridViewAdapter gridAdapter;
    String titles[] = {"Inicio","Mis reservas"};
    Integer icons[] = {R.drawable.ic_launcher,R.drawable.ic_launcher};
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_reservas);
        tv_telefono=(TextView) findViewById(R.id.tv_tlf);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();
        drawer_adapter adapter = new drawer_adapter(this, icons, titles);
        ListView lv = (ListView) findViewById(R.id.drawer_listview);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer.closeDrawer(START);
                if(position==0)
                {
                    Intent intent = new Intent(misReservas.this, DrawerArrowSample.class);
                    startActivity(intent);
                }else if (position==1)
                {
                    Intent intent = new Intent(misReservas.this, misReservas.class);
                    startActivity(intent);
                }
            }
        });
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
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
            @Override public void onClick(View v) {
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

            @Override public void onClick(View v) {
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

        /////////////Gridview

        gridView = (GridView) findViewById(R.id.gridView);

        ////////////////////////

        new numeroDefault().execute();
    }


    class llenarGridView extends AsyncTask<Void, Void, Void>
    {

        ArrayList<ImageItem> imageItems = new ArrayList<>();
        protected Void doInBackground(Void... params)
        {
            String tlf=tv_telefono.getText().toString();
            try{

                //////////////////////////JSON Cabeceras
                String txt;

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://agorapps.com/llenargrid.php");
                List<NameValuePair> parame = new ArrayList<NameValuePair>();
                parame.add(new BasicNameValuePair("dato" , tlf));
                httppost.setEntity(new UrlEncodedFormEntity(parame));
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity ent = resp.getEntity();
                txt = EntityUtils.toString(ent);
                JSONObject jsonObject = new JSONObject(txt);
                JSONArray eventos = jsonObject.getJSONArray("eventos");

                for (int i = 0; i < eventos.length(); i++) {
                    JSONObject city = eventos.getJSONObject(i);
                    String dtStart = city.getString("bd_fecha");
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        date2 = format.parse(dtStart);
                        System.out.println("Date ->" + date2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    imageItems.add(new ImageItem(city.getString("bd_destinos"),city.getString("bd_salida"),date2, city.getString("bd_llegada"), city.getString("idreser"), city.getInt("id_propia"), city.getString("bd_personas")));

                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result) {

            Collections.sort(imageItems, new FishNameComparator());
            gridAdapter = new gridViewAdapter(misReservas.this, R.layout.grid_items, imageItems);
            gridView.setAdapter(gridAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String tv_fecha = formatter.format(imageItems.get(position).getgFecha());
                    Intent i = new Intent (misReservas.this, dialogReserva.class);
                    i.putExtra("aux", 1);
                    i.putExtra("idviaje",imageItems.get(position).getGidViaje());
                    i.putExtra("destino", imageItems.get(position).getgNombreEvento());
                    i.putExtra("salida", imageItems.get(position).getgHoraSalida());
                    i.putExtra("plazas", Integer.parseInt(imageItems.get(position).getgPersonas()));
                    i.putExtra("fecha", tv_fecha);
                    i.putExtra("tlf_reserva", tv_telefono.getText().toString());
                    Log.d("IKJHFKJD","JKSFHKSDJ");
                    startActivity(i);
                }
            });
        }
    }

    public void buscarReserva(View view)
    {
        new llenarGridView().execute();
    }

    public class FishNameComparator implements Comparator<ImageItem> {
        public int compare(ImageItem left, ImageItem right) {
            return left.getgFecha().compareTo(right.getgFecha());

        }

    }

    class numeroDefault extends AsyncTask<Void, Void, Void>
    {
        String defaultTlf;
        protected Void doInBackground(Void... params) {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            try {
                regid = gcm.register(SENDER_ID);
            } catch (Exception e) {
                Log.d("PENE", String.valueOf(e));
            }
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://agorapps.com/recibir_tlf.php");
                List<NameValuePair> parame = new ArrayList<NameValuePair>();
                parame.add(new BasicNameValuePair("regId", regid));
                httppost.setEntity(new UrlEncodedFormEntity(parame));
                HttpResponse resp = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(resp.getEntity());
                try {
                    JSONObject jresponse = new JSONObject(responseBody);
                    defaultTlf = jresponse.getString("error");
                } catch (Exception e) {
                    Log.d("PENE", "horror2");
                }
            } catch (Exception e) {
                Log.d("PENE", "horror");
            }
            return null;
        }
        protected void onPostExecute(Void result) {

            tv_telefono.setText(defaultTlf);
        }
    }
    public void onBackPressed() {
        finish();
    }
}
