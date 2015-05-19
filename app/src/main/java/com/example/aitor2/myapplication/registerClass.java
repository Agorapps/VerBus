package com.example.aitor2.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by aitor2 on 15/05/2015.
 */
public class registerClass extends Activity {
    SharedPreferences prefs;
    EditText et_tlfR, et_pass,et_nombre;
    String st_tlfR, st_pass, st_nombre;
    GoogleCloudMessaging gcm;
    String regid;
    String SENDER_ID = "790751637625";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        et_tlfR=(EditText)findViewById(R.id.et_tlfR);
        et_pass=(EditText)findViewById(R.id.et_pass);
        et_nombre=(EditText)findViewById(R.id.et_nombre);
        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        prefs = getSharedPreferences("MisPreferencias",loginClass.MODE_PRIVATE);
    }
    public void registrar(View view)
    {
        st_tlfR=et_tlfR.getText().toString();
        st_pass=et_pass.getText().toString();
        st_nombre=et_nombre.getText().toString();
        try {
            regid = gcm.register(SENDER_ID);
        }catch (Exception e){}
        new RegisterApp(registerClass.this, gcm, st_tlfR, 2, st_nombre, st_pass).execute();
    }
}
