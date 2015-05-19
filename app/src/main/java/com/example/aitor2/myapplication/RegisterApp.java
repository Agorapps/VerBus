package com.example.aitor2.myapplication;

/**
 * Created by aitor2 on 01/04/2015.
 */
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RegisterApp extends AsyncTask<Void, Void, String> {


    private static final String TAG = "GCMRelated";
    Context ctx;
    String id_viaje;
    String msj;
    String tlf;
    String nombre;
    String pass;
    SharedPreferences prefs;
    int aux;
    meterEnCola mec = new meterEnCola();
    GoogleCloudMessaging gcm;
    String SENDER_ID = "790751637625";
    String regid = null;
    private int appVersion;
    public RegisterApp(Context ctx, GoogleCloudMessaging gcm, String id_viaje, String tlf, int aux){
        this.ctx = ctx;
        this.gcm = gcm;
        this.id_viaje=id_viaje;
        this.tlf=tlf;
        this.aux=aux;

    }
    public RegisterApp(Context ctx, GoogleCloudMessaging gcm, String tlf, int aux,String nombre, String pass)
    {
        this.ctx = ctx;
        this.gcm = gcm;
        this.tlf=tlf;
        this.aux=aux;
        this.nombre=nombre;
        this.pass=pass;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(Void... arg0) {
        String msg = "";

        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            regid = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regid;
            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
                if(aux==1) {
                    sendRegistrationIdToBackend2();
                }else if(aux==2)
                {
                    sendRegistrationIdToBackend();
                }
            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.

        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
        return msg;
    }

    private void storeRegistrationId(Context ctx, String regid, String tlfono) {
        final SharedPreferences prefs = ctx.getSharedPreferences(dialogReserva.class.getSimpleName(),
                Context.MODE_PRIVATE);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor2 = prefs.edit();
        editor2.putString("id_shared", regid);
        editor2.putString("tlfono", tlf);
        editor2.putInt("appVersion", appVersion);
        editor2.commit();

    }


    private void sendRegistrationIdToBackend() {
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://agorapps.com/register.php");
            List<NameValuePair> parame = new ArrayList<NameValuePair>();
            parame.add(new BasicNameValuePair("tlf",tlf));
            parame.add(new BasicNameValuePair("pass",pass));
            parame.add(new BasicNameValuePair("regId",regid));
            parame.add(new BasicNameValuePair("nombre",nombre));
            httppost.setEntity(new UrlEncodedFormEntity(parame));
            HttpResponse resp = httpclient.execute(httppost);
            String responseBody =EntityUtils.toString(resp.getEntity());
            try {
                JSONObject jresponse = new JSONObject(responseBody);
                msj = jresponse.getString("error");
            }catch(Exception e){Log.d("ERRRRRRO", "erroro");}
        }catch(Exception e){Log.d("ERRRRRRO", "erroro2");}
    }
    private void sendRegistrationIdToBackend2() {

    try {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://agorapps.com/reservar.php");
        List<NameValuePair> parame = new ArrayList<NameValuePair>();
        parame.add(new BasicNameValuePair("regId", regid));
        parame.add(new BasicNameValuePair("idViaje",id_viaje));
        parame.add(new BasicNameValuePair("tlf",tlf));
        parame.add(new BasicNameValuePair("cola", "no"));
        httppost.setEntity(new UrlEncodedFormEntity(parame));
        HttpResponse resp = httpclient.execute(httppost);
        String responseBody =EntityUtils.toString(resp.getEntity());
        try {
            JSONObject jresponse = new JSONObject(responseBody);
            msj = jresponse.getString("error");
        }catch(Exception e){}
    }catch(Exception e){}

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(!Pattern.compile("0").matcher(msj).find())
        {
            Toast.makeText(ctx, "¡Ya tienes una reserva para este viaje!", Toast.LENGTH_SHORT).show();
        }else if(Pattern.compile("200").matcher(msj).find()) {
            SweetAlertDialog swt = new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Todas las plazas ocupadas")
                    .setContentText("¿Desea entrar en la lista de espera? Será avisado en cuanto haya una plaza")
                    .setCancelText("No")
                    .setConfirmText("Si")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.cancel();
                            Toast.makeText(ctx, "Reserva no realizada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ctx, DrawerArrowSample.class);
                            ctx.startActivity(intent);
                            ((Activity)ctx).finish();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                            Intent intent = new Intent(ctx, DrawerArrowSample.class);
                            ctx.startActivity(intent);
                            ((Activity)ctx).finish();
                            mec.añadir(id_viaje, tlf, regid,ctx);
                        }
                    });
            swt.show();



        }else if(Pattern.compile("202").matcher(msj).find()) {

            prefs = ctx.getSharedPreferences("MisPreferencias",loginClass.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("firstOnce", "1");
            editor.commit();
            Intent intent = new Intent(ctx, DrawerArrowSample.class);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
        }else if(Pattern.compile("203").matcher(msj).find()){
            Toast.makeText(ctx, "¡Ya existe este usuario!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ctx, "¡Reserva realizada con éxito!", Toast.LENGTH_SHORT).show();
        }
    }



}
