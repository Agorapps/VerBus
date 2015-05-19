package com.example.aitor2.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by aitor2 on 15/05/2015.
 */
public class loginClass extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        SharedPreferences prefs = getSharedPreferences("MisPreferencias",loginClass.MODE_PRIVATE);
        Log.d("SHARED",prefs.getString("firstOnce","h"));
        if(prefs.getString("firstOnce","h")=="h")
        {
            Intent intent = new Intent(loginClass.this, registerClass.class);
            startActivity(intent);
            finish();
        }
    }
}
