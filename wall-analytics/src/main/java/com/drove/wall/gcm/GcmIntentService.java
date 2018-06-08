package com.drove.wall.gcm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;


public class GcmIntentService extends GcmListenerService {

    private static final String TAG = "PlayServices";
    private static final int NOTIFICATION_ID = 1;
    private static PendingIntent pendingIntent;
    private static AlarmManager alarmManager;
    private static Intent intent;
    private NotificationManager mNotificationManager;
    private int requestCode = 987654;
    private Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private String NOTIFICATION_TYPE_ACTIVITY = "medicationActivity";
    private String NOTIFICATION_TYPE_WIFI_CONFIG = "wifiConfigAck";
    private String NOTIFICATION_TYPE_FSBL_CONFIG = "fsblConnection";
    private String NOTIFICATION_TYPE_DEVICE_CONNECTION = "device";
    private String NOTIFICATION_TYPE_REMINDER = "reminder";
    private String NOTIFICATION_TYPE_MISSED = "missed";
    private String NOTIFICATION_TYPE_REFILL = "refill";
    private String NOTIFICATION_TYPE_CONSUMED = "consumed";
    private String KEY_TYPE = "notificationType";
    private String KEY_DATA = "data";
    private String KEY_MESSAGE = "message";
    private String KEY_TITLE = "title";
    private String INTENT_KEY_USER_ID = "userId";
    private String INTENT_KEY_USERNAME = "userName";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        if (data != null) {
            Log.d(TAG, data.toString());
            //  JsonObject object = new Gson().fromJson(data.getString(KEY_DATA), JsonObject.class);
            String type = data.getString(KEY_TYPE);

            if (type.equals(NOTIFICATION_TYPE_DEVICE_CONNECTION)) {
                showDeviceConfigNotification(data.getString(KEY_MESSAGE), data.getString(KEY_TITLE));
            } else if (type.equals(NOTIFICATION_TYPE_REMINDER)) {
                showReminderNotification(data.getString(KEY_MESSAGE), data.getString(KEY_TITLE));
            } else if (type.equals(NOTIFICATION_TYPE_MISSED)) {
                showNewActivityNotification(data);
            } else if (type.equals(NOTIFICATION_TYPE_REFILL)) {

            } else if (type.equals(NOTIFICATION_TYPE_CONSUMED)) {
                showNewActivityNotification(data);
            }
        } else {
            Log.i(TAG, "Data null");
        }
    }

    private void showDeviceConfigNotification(String message, String title) {
       /* mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(alarmSound);
        Intent notificationIntent = new Intent(this, DevicesActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(ActivitiesActivity.class);
        taskStackBuilder.addNextIntent(notificationIntent);
        PendingIntent contentIntent = taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());*/
    }

    private void showNewActivityNotification(Bundle data) {
        //  Activity activity = new Gson().fromJson(data.getString(KEY_DATA), Activity.class);
        //  Log.i("GCMNotification ", data.getString(KEY_DATA));
        //   Set<String> usersSet = SharedPreferencesManager.getStringSetPreference(SharedPreferencesManager.USERS_WITH_UNSEEN_ACTIVITIES);
        //  usersSet.add(activity.actor.actorId);
        //   Log.i("MyDebug ", "Onreceive size " + usersSet.size());
        //   SharedPreferencesManager.setStringSetPreference(SharedPreferencesManager.USERS_WITH_UNSEEN_ACTIVITIES, usersSet);
       /* mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(data.getString(KEY_TITLE))
                .setContentText(data.getString(KEY_MESSAGE))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.getString(KEY_MESSAGE)))
                .setSound(alarmSound);
        Intent notificationIntent = new Intent(this, ActivitiesActivity.class);
        //   notificationIntent.putExtra(INTENT_KEY_USER_ID, activity.actor.actorId);
        //   notificationIntent.putExtra(INTENT_KEY_USERNAME, activity.actor.actorName);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(ActivitiesActivity.class);
        taskStackBuilder.addNextIntent(notificationIntent);
        PendingIntent contentIntent = taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());*/
    }

    private void showReminderNotification(String message, String title) {
       /* mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(alarmSound);
        Intent notificationIntent = new Intent(this, LauncherActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(ActivitiesActivity.class);
        taskStackBuilder.addNextIntent(notificationIntent);
        PendingIntent contentIntent = taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        SharedPreferencesManager.setStringPreferenceData(SharedPreferencesManager.REMINDER, message);
        startAlert();
        sendAlarmBroadcast();*/
    }

    private void startAlert() {
       /* intent = new Intent(this, alarmReceiver.class);
        intent.putExtra("status", 1);
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (1 * 1000), pendingIntent);*/
    }

    public void stopAlert() {
        /*if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            intent.putExtra("status", 0);
            stopService(intent);
        }*/

    }

    private void sendAlarmBroadcast() {
       /* Intent intent = new Intent("alarmReceiver");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);*/
    }

}