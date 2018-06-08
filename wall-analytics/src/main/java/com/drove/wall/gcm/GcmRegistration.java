package com.drove.wall.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.drove.wall.R;
import com.drove.wall.Utils.Endpoints;
import com.drove.wall.Utils.SharedPreferencesUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.io.IOException;


/**
 * Created by Arjun on 9/12/15.
 */
public class GcmRegistration {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static GoogleCloudMessaging gcm;
    private static String regId;
    private static Context context;

    public GcmRegistration(Context context) {
        this.context = context;
    }

    public static GcmRegistration getInstance(Context context) {
        return new GcmRegistration(context);
    }

    private static void storeRegistrationId() {
        JsonObject json = new JsonObject();
        json.addProperty("device_token", regId);
        json.addProperty("app_user_id", (String) SharedPreferencesUtils.getParam(context,SharedPreferencesUtils.USER_ID,""));

        Ion.with(context)
                .load(Endpoints.REG_ID_URL)
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback((e, result) -> {
                    // do stuff with the result or error
                    if (result == null)
                        return;
                    if (result.getHeaders().code() == 200) {
                        Log.d("Amal",result.getResult());
                        SharedPreferencesUtils.setParam(context, SharedPreferencesUtils.GCM_REG_ID, regId);
                    }
                });
    }

    public void getAndSendGcmRegId() {
        if (checkGooglePlayServices()) {
           // regId = getRegistrationId();
         //   Log.d("Amal", "GCM ID " + regId);
            /*if (regId.isEmpty()) {

            }*/
            registerInBackground();
        } else {
            Log.i("Play services", "No valid Google Play Services APK found.");
        }
    }

    private boolean checkGooglePlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (AppCompatActivity) context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("Play services error", "This device is not supported.");
                ((AppCompatActivity) context).finish();
            }
            return false;
        }
        return true;
    }

    private static String getRegistrationId() {
        //        if (registrationId.isEmpty()) {
//            Log.i("GCM", "Registration not found in shared prefs.");
//            return "";
//        }
        return (String) SharedPreferencesUtils
                .getParam(context, SharedPreferencesUtils.GCM_REG_ID, "");
    }

    private void registerInBackground() {
       /* new AsyncTask() {
            @Override
            protected String doInBackground(Object[] params) {
                String msg;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    InstanceID instanceID = InstanceID.getInstance(context);
                    regId = instanceID.getToken(context.getString(R.string.gcm_sender_id),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    storeRegistrationId();
                    msg = "Device registered, registration ID=" + regId;
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        }.execute(null, null, null);*/
        new BackgroundOperation().execute(null, null, null);
    }

    private static class BackgroundOperation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                InstanceID instanceID = InstanceID.getInstance(context);
                regId = instanceID.getToken(context.getString(R.string.gcm_sender_id),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.d("Amal", "RegID: " + regId);
                if (!getRegistrationId().equals(regId)){
                    storeRegistrationId();
                }


            } catch (IOException ex) {

            }
            return null;
        }
    }
}
