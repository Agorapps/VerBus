package com.example.aitor2.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by aitor2 on 12/05/2015.
 */
public class meterEnCola {

    String id_viaje;
    String id_registro;
    String tlf;
    Context context;
    public void añadir(String id_viaje, String tlf, String id_registro, Context context) {
       this.id_registro=id_registro;
       this.id_viaje=id_viaje;
       this.tlf=tlf;
       this.context=context;
       new cancelarReservaCola().execute();
    }





    /////////////////////ASYNC TASKS
    class cancelarReservaCola extends AsyncTask<Void, Void, Void> {
        String msj;
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://agorapps.com/reservar.php");
                List<NameValuePair> parame = new ArrayList<NameValuePair>();
                parame.add(new BasicNameValuePair("regId", id_registro));
                parame.add(new BasicNameValuePair("idViaje", id_viaje));
                parame.add(new BasicNameValuePair("tlf", tlf));
                parame.add(new BasicNameValuePair("cola", "si"));
                httppost.setEntity(new UrlEncodedFormEntity(parame));
                HttpResponse resp = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(resp.getEntity());
                try {
                    JSONObject jresponse = new JSONObject(responseBody);
                    msj = jresponse.getString("error");
                    Log.d("XEEEMAA", msj);
                } catch (Exception e) {
                    Log.d("PENE", "horror2");
                }
            } catch (Exception e) {
                Log.d("PENE", "horror");
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if(Pattern.compile("201").matcher(msj).find())
            {
                Toast.makeText(context, "¡Ya estás en la lista de espera!", Toast.LENGTH_SHORT).show();
            }else if(Pattern.compile("si").matcher(msj).find()){
                Toast.makeText(context, "¡Añadido a la lista de espera!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
