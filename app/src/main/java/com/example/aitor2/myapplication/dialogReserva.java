package com.example.aitor2.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by aitor2 on 28/04/2015.
 */
public class dialogReserva extends Activity {

    //// GCM ////*****************************************************
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_idd";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCMRelated";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;
    String SENDER_ID = "790751637625";
    String defaultTlf="";
    String tlfCeder="";
    ///////////////////////********************************************

    final Context context = this;
    private Button button;
    private EditText result;
    TextView et_destino, et_fecha, et_salida, nOcu, nLib;
    ImageView sit1, sit2, sit3, sit4;
    String rIdViaje, rPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reserva);

        /// GCM ////////******************************************

        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        new getTlf().execute();
        ////////////////******************************************
        button = (Button) findViewById(R.id.bReservar);
        et_fecha = (TextView) findViewById(R.id.et_fecha);
        et_destino = (TextView) findViewById(R.id.et_destino);
        et_salida = (TextView) findViewById(R.id.et_salida);
        nOcu = (TextView) findViewById(R.id.nOcu);
        nLib = (TextView) findViewById(R.id.nLib);
        sit1= (ImageView)findViewById((R.id.sit1));
        sit2= (ImageView)findViewById((R.id.sit2));
        sit3= (ImageView)findViewById((R.id.sit3));
        sit4= (ImageView)findViewById((R.id.sit4));
        rIdViaje = String.valueOf(getIntent().getIntExtra("idviaje",0));
        new sacarPersonas().execute();

//            nOcu.setText(String.valueOf(4 - (getIntent().getIntExtra("plazas", 0))));
//            nLib.setText(String.valueOf(getIntent().getIntExtra("plazas", 0)));
            String []ar=getIntent().getStringExtra("destino").split("[/]");
            et_destino.setText(ar[1]);


            et_salida.setText(getIntent().getStringExtra("salida"));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            et_fecha.setText(getIntent().getStringExtra("fecha"));
           // rellenarAsientos();
        if(getIntent().getIntExtra("aux",0)!=1) {
            button.setText("Reservar");
        }else{
            button.setText("Cancelar reserva");
            String tlf_reserva=getIntent().getStringExtra("tlf_reserva");
        }
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(getIntent().getIntExtra("aux",0)!=1) {
                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.dialog_custom, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                    userInput.setText(defaultTlf);
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Confirmar Reserva",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            /////// GCM ////////**********************
                                            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

                                            new RegisterApp(dialogReserva.this, gcm, String.valueOf(getIntent().getIntExtra("idviaje", 0)), userInput.getText().toString(),1).execute();

                                            ////////////////////************************

                                        }
                                    })
                            .setNegativeButton("Cancelar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }else{
                    new SweetAlertDialog(dialogReserva.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿Estas seguro?")
                            .setCancelText("No")
                            .setConfirmText("Si")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    // reuse previous dialog instance, keep widget user state, reset them if you need
                                    Toast.makeText(context, "¡Reserva no cancelada!", Toast.LENGTH_SHORT).show();
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.setTitleText("¿Quieres ceder tu asiento?")
                                            .setConfirmText("Cancelar sin ceder")
                                            .showCancelButton(true)
                                            .setCancelText("Si")
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.cancel();
                                                    LayoutInflater li = LayoutInflater.from(context);
                                                    View promptsView = li.inflate(R.layout.dialog_custom2, null);

                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                                                    // set prompts.xml to alertdialog builder
                                                    alertDialogBuilder.setView(promptsView);

                                                    final EditText userInput = (EditText) promptsView.findViewById(R.id.tlfCeder);

                                                    // set dialog message
                                                    alertDialogBuilder
                                                            .setCancelable(false)
                                                            .setPositiveButton("Ceder asiento",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                            //////////
                                                                            tlfCeder = userInput.getText().toString();
                                                                            new cederAsiento().execute();
                                                                        }
                                                                    })
                                                            .setNegativeButton("Cancelar",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            Toast.makeText(context, "¡Reserva no cancelada!", Toast.LENGTH_SHORT).show();
                                                                            dialog.cancel();
                                                                        }
                                                                    });

                                                    // create alert dialog
                                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                                    // show it
                                                    alertDialog.show();
                                                }
                                            })
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    new cancelarReserva().execute();
                                                    Intent intent = new Intent(dialogReserva.this, DrawerArrowSample.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                }
                            })
                            .show();

                }
            }
        });
    }

    ///////////// GCM ////////////////////*******************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString("id_shared", "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(getApplicationContext());
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(dialogReserva.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    ///////////////////////////////////////**********************



    ///////////ASYNCTASK////////////////////

    class getTlf extends AsyncTask<Void, Void, Void>
    {

        String ou;
        @Override
        protected Void doInBackground(Void... params)
        {
                try {
                    regid = gcm.register(SENDER_ID);
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://agorapps.com/recibir_tlf.php");
                    List<NameValuePair> parame = new ArrayList<NameValuePair>();
                    parame.add(new BasicNameValuePair("regId", regid));
                    httppost.setEntity(new UrlEncodedFormEntity(parame));
                    HttpResponse resp = httpclient.execute(httppost);
                    String responseBody =EntityUtils.toString(resp.getEntity());
                    try {
                        JSONObject jresponse = new JSONObject(responseBody);
                        defaultTlf = jresponse.getString("error");
                    }catch(Exception e){}
                }catch(Exception e){}

            return null;
        }
        protected void onPostExecute(Void result) {

                super.onPostExecute(result);
        }

    } // Asyntask 4

    ///////////////////////////

    public void rellenarAsientos(String plazas)
    {
        int calculos=4-Integer.parseInt(plazas);
            switch(calculos)
            {
                case 4:
                    sit4.setImageResource(R.drawable.ocupado);
                    sit3.setImageResource(R.drawable.ocupado);
                    sit2.setImageResource(R.drawable.ocupado);
                    sit1.setImageResource(R.drawable.ocupado);
                    break;
                case 3:
                    sit3.setImageResource(R.drawable.ocupado);
                    sit2.setImageResource(R.drawable.ocupado);
                    sit1.setImageResource(R.drawable.ocupado);
                    break;
                case 2:
                    sit2.setImageResource(R.drawable.ocupado);
                    sit1.setImageResource(R.drawable.ocupado);
                    break;
                case 1:
                    sit1.setImageResource(R.drawable.ocupado);
                    break;
            }
            calculos--;
    }

    class cancelarReserva extends AsyncTask<Void, Void, Void>
    {
        String defaultTlf;
        protected Void doInBackground(Void... params) {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            try {
                regid = gcm.register(SENDER_ID);
            } catch (Exception e) {
            }
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://agorapps.com/cancelar_reserva.php");
                List<NameValuePair> parame = new ArrayList<NameValuePair>();

                parame.add(new BasicNameValuePair("idViaje", rIdViaje));
                parame.add(new BasicNameValuePair("tlf_reserva", getIntent().getStringExtra("tlf_reserva")));
                parame.add(new BasicNameValuePair("personas", rPersonas));
                httppost.setEntity(new UrlEncodedFormEntity(parame));
                HttpResponse resp = httpclient.execute(httppost);

            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Toast.makeText(context, "¡Reserva cancelada!", Toast.LENGTH_SHORT).show();
        }
    }

    class cederAsiento extends AsyncTask<Void, Void, Void>
    {
        String respuesta="";
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://agorapps.com/cederAsiento.php");
                List<NameValuePair> parame = new ArrayList<NameValuePair>();
                parame.add(new BasicNameValuePair("idViaje", rIdViaje));
                parame.add(new BasicNameValuePair("tlf_reserva",tlfCeder ));
                httppost.setEntity(new UrlEncodedFormEntity(parame));
                HttpResponse resp = httpclient.execute(httppost);
                String responseBody =EntityUtils.toString(resp.getEntity());
                try {
                    JSONObject jresponse = new JSONObject(responseBody);
                    respuesta = jresponse.getString("error");
                }catch(Exception e){
                    Log.d("HOOOORROR","HORROR");
                }
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Log.d("RESPUESTA",respuesta);
            if(Pattern.compile("1").matcher(respuesta).find())
            {
                Toast.makeText(context, "¡Reserva cedida!", Toast.LENGTH_SHORT).show();
            }else if(Pattern.compile("2").matcher(respuesta).find())
            {
                Toast.makeText(context, "¡Este número ya tiene una reserva para este viaje!", Toast.LENGTH_SHORT).show();
            }else if(Pattern.compile("3").matcher(respuesta).find())
            {
                Toast.makeText(context, "¡Lista de espera!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class sacarPersonas extends AsyncTask<Void, Void, Void>
    {
        String nPersonas;
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://agorapps.com/sacarPersonas.php");
                List<NameValuePair> parame = new ArrayList<NameValuePair>();

                parame.add(new BasicNameValuePair("idViaje", rIdViaje));
                httppost.setEntity(new UrlEncodedFormEntity(parame));
                HttpResponse resp = httpclient.execute(httppost);
                String responseBody =EntityUtils.toString(resp.getEntity());
                try {
                    JSONObject jresponse = new JSONObject(responseBody);
                    nPersonas = jresponse.getString("error");
                    Log.d("HOOOORROR",rPersonas);
                }catch(Exception e){
                }
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            nLib.setText(nPersonas);
            nOcu.setText(String.valueOf(4-Integer.parseInt(nPersonas)));
            rellenarAsientos(nPersonas);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(dialogReserva.this, DrawerArrowSample.class);
        startActivity(intent);
        finish();
    }
}
