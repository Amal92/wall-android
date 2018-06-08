package com.drove.wall;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.drove.wall.Utils.AppForegroundStateManager;
import com.drove.wall.Utils.Endpoints;
import com.drove.wall.Utils.NoAcraLog;
import com.drove.wall.Utils.SharedPreferencesUtils;
import com.drove.wall.gcm.GcmRegistration;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by amal on 25/05/18.
 */

public class Wall {

    final static String TAG = "wall";

    public static void with(Application application, String apiKey) {
        // setUpAnalytics(application, apiKey);
        setUpAndInstallCrashLogging(application, apiKey);
        application.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks(application));
        AppForegroundStateManager.getInstance().addListener(new MyActivityStateChangeListener(application, apiKey));
    }

    private static void setUpAnalytics(Application application, String apiKey) {
        JsonObject json = new JsonObject();
        json.addProperty("app_key", apiKey);
        json.addProperty("app_version", getAppVersion(application));
        json.addProperty("os_type", "android");
        json.addProperty("os_version", getAndroidVersion());
        json.addProperty("brand", getBrand());
        json.addProperty("hardware", getHardware());
        json.addProperty("manufacture", getManufacturer());
        json.addProperty("model", getPhoneModel());
        json.addProperty("app_user_id", (String) SharedPreferencesUtils.getParam(application, SharedPreferencesUtils.USER_ID, ""));

        Ion.with(application.getApplicationContext())
                .load(Endpoints.INIT_URL)
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback((e, result) -> {
                    // do stuff with the result or error
                    if (result == null)
                        return;
                    if (result.getHeaders().code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.getResult());
                            Log.d("Amal", result.getResult());
                            String appId = jsonObject.optString("app_user_id");
                            SharedPreferencesUtils.setParam(application, SharedPreferencesUtils.USER_ID, appId);
                            String session_id = jsonObject.optString("session_id");
                            SharedPreferencesUtils.setParam(application, SharedPreferencesUtils.SESSION_ID, session_id);
                            GcmRegistration.getInstance(application).getAndSendGcmRegId();
                            ACRA.getErrorReporter().putCustomData("user_id", (String) SharedPreferencesUtils.getParam(application, SharedPreferencesUtils.USER_ID, ""));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private static String getPhoneModel() {
        return Build.MODEL;
    }

    private static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    private static String getHardware() {
        return Build.HARDWARE;
    }

    private static String getBrand() {
        return Build.BRAND;
    }

    private static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    private static String getAppVersion(Context context) {
        return (String) getBuildConfigValue(context, "VERSION_NAME");
    }


    private static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class getBuildClass(Context context) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");

            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setUpAndInstallCrashLogging(Application application, String apiKey) {

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(application);
        builder.setBuildConfigClass(getBuildClass(application)).setReportFormat(StringFormat.JSON);

        builder.setReportContent(ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE,
                ReportField.BRAND,
                ReportField.PHONE_MODEL,
                ReportField.USER_CRASH_DATE,
                ReportField.AVAILABLE_MEM_SIZE,
                ReportField.TOTAL_MEM_SIZE);
        builder.setReportField(ReportField.CUSTOM_DATA, true);


        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class).setEnabled(true)
                .setHttpMethod(HttpSender.Method.POST).setUri("http://139.59.10.55/api/check");


        ACRA.init(application, builder);
        ACRA.setLog(new NoAcraLog());
        ACRA.getErrorReporter().putCustomData("user_id", (String) SharedPreferencesUtils.getParam(application, SharedPreferencesUtils.USER_ID, ""));
        ACRA.getErrorReporter().putCustomData("app_key", apiKey);

    }

    private static void sendEndSession(Application application, String apiKey) {
        JsonObject json = new JsonObject();
        // json.addProperty("app_key", apiKey);
        json.addProperty("session_id", (String) SharedPreferencesUtils.getParam(application, SharedPreferencesUtils.SESSION_ID, ""));

        Ion.with(application.getApplicationContext())
                .load(Endpoints.SESSION_END_URL)
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback((e, result) -> {
                    // do stuff with the result or error
                    if (result == null)
                        return;
                    if (result.getHeaders().code() == 200) {

                    }
                });
    }

    public static class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        private Context activity;

        public MyActivityLifecycleCallbacks(Context activity) {
            this.activity = activity;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d("amal", "created");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d("amal", "start");
            AppForegroundStateManager.getInstance().onActivityVisible(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d("amal", "resume");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d("amal", "pause");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d("amal", "stop");
            AppForegroundStateManager.getInstance().onActivityNotVisible(activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d("amal", "destroy");
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
        }
    }

    public static class MyActivityStateChangeListener implements AppForegroundStateManager.OnAppForegroundStateChangeListener {

        private Application application;
        private String apiKey;

        public MyActivityStateChangeListener(Application application, String apiKey) {
            this.application = application;
            this.apiKey = apiKey;
        }

        @Override
        public void onAppForegroundStateChange(AppForegroundStateManager.AppForegroundState newState) {
            if (AppForegroundStateManager.AppForegroundState.IN_FOREGROUND == newState) {
                // App just entered the foreground. Do something here!
                Log.d("Amal", "foreground");
                setUpAnalytics(application, apiKey);
            } else {
                // App just entered the background. Do something here!
                Log.d("Amal", "background");
                sendEndSession(application, apiKey);
                AppForegroundStateManager.getInstance().removeListener(this);
            }
        }


    }

}
