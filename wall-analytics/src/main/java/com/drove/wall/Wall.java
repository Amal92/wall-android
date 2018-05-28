package com.drove.wall;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.drove.wall.Utils.Endpoints;
import com.drove.wall.Utils.NoAcraLog;
import com.drove.wall.Utils.SharedPreferencesUtils;
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
        setUpAnalytics(application, apiKey);
        setUpAndInstallCrashLogging(application, apiKey);
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

        Log.d("amal", "log data: " + json.toString());
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
                            String appId = jsonObject.optString("app_user_id");
                            SharedPreferencesUtils.setParam(application, SharedPreferencesUtils.USER_ID, appId);
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

    }

}
