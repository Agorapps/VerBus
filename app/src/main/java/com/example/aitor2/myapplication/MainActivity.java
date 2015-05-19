//package com.example.aitor2.myapplication;
//
///**
// * Created by aitor2 on 01/04/2015.
// */
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import com.google.android.gcm.GCMRegistrar;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import android.os.Bundle;
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//public class MainActivity extends Activity {
//
//    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//    public static final String EXTRA_MESSAGE = "message";
//    public static final String PROPERTY_REG_ID = "registration_idd";
//    private static final String PROPERTY_APP_VERSION = "appVersion";
//    private static final String TAG = "GCMRelated";
//    GoogleCloudMessaging gcm;
//    AtomicInteger msgId = new AtomicInteger();
//    String regid;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        final Button button = (Button) findViewById(R.id.register);
//
//
//            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
//            regid = getRegistrationId(getApplicationContext());
//
//
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                // Check device for Play Services APK.
//
//                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
//                    regid = getRegistrationId(getApplicationContext());
//                    //new RegisterApp(getApplicationContext(), gcm, getAppVersion(getApplicationContext())).execute();
//                    if (regid.isEmpty()) {
//                        //button.setEnabled(false);
//                        new RegisterApp(getApplicationContext(), gcm, getAppVersion(getApplicationContext())).execute();
//                    }else{
//                        Toast.makeText(getApplicationContext(), "Device already Registered", Toast.LENGTH_SHORT).show();
//                    }
//
//            }
//        });
//
//
//    }
//
//    @Override
//     public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    /**
//     * Check the device to make sure it has the Google Play Services APK. If
//     * it doesn't, display a dialog that allows users to download the APK from
//     * the Google Play Store or enable it in the device's system settings.
//     */
//
////    private boolean checkPlayServices() {
////        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
////        if (resultCode != ConnectionResult.SUCCESS) {
////            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
////                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
////                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
////            } else {
////                Log.i(TAG, "This device is not supported.");
////                finish();
////            }
////            return false;
////        }
////        return true;
////    }
//
//    /**
//     * Gets the current registration ID for application on GCM service.
//     * <p>
//     * If result is empty, the app needs to register.
//     *
//     * @return registration ID, or empty string if there is no existing
//     *         registration ID.
//     */
//    private String getRegistrationId(Context context) {
//        final SharedPreferences prefs = getGCMPreferences(context);
//        String registrationId = prefs.getString("id_shared", "");
//        Log.d("TAAAG", registrationId);
//        if (registrationId.isEmpty()) {
//            Log.d("eooooo", "Registration not found.");
//            return "";
//        }
//        // Check if app was updated; if so, it must clear the registration ID
//        // since the existing regID is not guaranteed to work with the new
//        // app version.
//        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//        int currentVersion = getAppVersion(getApplicationContext());
//        if (registeredVersion != currentVersion) {
//            Log.i(TAG, "App version changed.");
//            return "";
//        }
//        return registrationId;
//    }
//
//    /**
//     * @return Application's {@code SharedPreferences}.
//     */
//    private SharedPreferences getGCMPreferences(Context context) {
//        // This sample app persists the registration ID in shared preferences, but
//        // how you store the regID in your app is up to you.
//        return getSharedPreferences(MainActivity.class.getSimpleName(),
//                Context.MODE_PRIVATE);
//    }
//
//    /**
//     * @return Application's version code from the {@code PackageManager}.
//     */
//    private static int getAppVersion(Context context) {
//        try {
//            PackageInfo packageInfo = context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(), 0);
//            return packageInfo.versionCode;
//        } catch (NameNotFoundException e) {
//            // should never happen
//            throw new RuntimeException("Could not get package name: " + e);
//        }
//    }
//
//   /* private void sendRegistrationIdToBackend() {
//        URI url = null;
//        try {
//            url = new URI("http://agorapps.com/register.php?regId=" + regid+"&tlf="+"622674598");
//        } catch (URISyntaxException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpGet request = new HttpGet();
//        request.setURI(url);
//        try {
//            httpclient.execute(request);
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }*/
//}
