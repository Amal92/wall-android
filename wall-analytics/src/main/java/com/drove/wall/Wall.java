package com.drove.wall;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by amal on 25/05/18.
 */

public class Wall {
    public static void with(Activity activity){
        Log.d("Amal", "App version: " + getBuildConfigValue(activity,"versionName"));
        Log.d("Amal", "Android version: " + Build.VERSION.RELEASE);
        Log.d("Amal", "Brand: " + Build.BRAND);
        Log.d("Amal", "Hardware: " + Build.HARDWARE);
        Log.d("Amal", "Manufacture: " + Build.MANUFACTURER);
        Log.d("Amal", "Model: " + Build.MODEL);
    }

    public static Object getBuildConfigValue(Context context, String fieldName) {
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
}
