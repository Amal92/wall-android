package com.drove.wall;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;


import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.data.StringFormat;

import java.lang.reflect.Field;

/**
 * Created by amal on 25/05/18.
 */

public class Wall {

    final static String TAG = "wall/class";

    public static void with(Application application) {
        Log.d(TAG, "App version: " + getBuildConfigValue(application.getApplicationContext(), "VERSION_NAME"));
        Log.d(TAG, "Android version: " + Build.VERSION.RELEASE);
        Log.d(TAG, "Brand: " + Build.BRAND);
        Log.d(TAG, "Hardware: " + Build.HARDWARE);
        Log.d(TAG, "Manufacture: " + Build.MANUFACTURER);
        Log.d(TAG, "Model: " + Build.MODEL);
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

    private void setUpAndInstallCrashLogging(Application application) {

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


        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class).setEnabled(true);


        ACRA.init(application, builder);
    }

}
