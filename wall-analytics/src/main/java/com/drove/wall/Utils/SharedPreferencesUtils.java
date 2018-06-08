package com.drove.wall.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtils {

    private static final String FILE_NAME = "SHAREDPREF_DROVE_ANALYTICS_KEY1739";


    public static final String EMAIL_ID = "com.drove.wall.SESSION_TOKEN";
    public static final String USER_ID = "com.drove.wall.USER_ID";
    public static final String SESSION_ID = "com.drove.wall.SESSION_ID";
    public static final String GCM_REG_ID = "com.drove.wall.GCM_REG_ID";

    /**
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key,
                                  Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static String getMemberId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString("memberId", null);
    }

    public static boolean hasBind(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }

    public static void setIsShortCut(Context context, Boolean isLogin) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isShortCut", isLogin);
        editor.commit();
    }

    public static Boolean isShortCut(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean("isShortCut", false);
    }

    public static String getFileName(){
        return FILE_NAME;
    }

}
