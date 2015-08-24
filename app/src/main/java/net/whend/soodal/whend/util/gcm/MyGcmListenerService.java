/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.whend.soodal.whend.util.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.view.A2_UserProfileActivity;
import net.whend.soodal.whend.view.A3_SpecificScheduleActivity;
import net.whend.soodal.whend.view.MainActivity;

public class MyGcmListenerService extends GcmListenerService {
    private static String message_static;
    public static int unreadNotificationCount;
    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("actor_name") +" "+ data.getString("verb");


        //String target_type = data.getString("target_type") == "null"? "follow": data.getString("target_type");

        //int target_id = data.getString("target_id") == "null"? data.getInt("actor_id"): data.getInt("target_id");

        //Log.d("TYPE",target_type);
        //Log.d(TAG, "From: " + from);
        //Log.d(TAG, "Message: " + message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        final AppPrefs appPrefs = new AppPrefs(this);


        //푸시알리 설정이 true, 그리고 나랑 이름이 다를 때만 받음
        //if(appPrefs.getPush_setting() && !data.getString("actor_name").equals(appPrefs.getUsername().toString()))
        if(appPrefs.getPush_setting())
          sendNotification(message);

        //sendNotification(message, target_type, target_id);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = null;


            intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fromGCM", true);

        int unique_id = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, unique_id /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);





        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_03)
                .setContentTitle("whenD")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        Bitmap large_icon = getLargeIcon();
        if(large_icon!=null){
            notificationBuilder.setLargeIcon(large_icon);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        message_static = "WhenD : " + message;
        Thread thread = new Thread(new Runnable() {
            public void run() {

                handler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }


    private void sendNotification(String message, String target_type, int target_id) {
        Intent intent = null;

        if(target_type.equals("schedule")) {
            intent = new Intent(this, A3_SpecificScheduleActivity.class);
            intent.putExtra("id", target_id);
        }else if(target_type.equals("follow")){
            intent = new Intent(this, A2_UserProfileActivity.class);
            intent.putExtra("id", target_id);
        }else{
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_03)
                .setContentTitle("whenD")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        Bitmap large_icon = getLargeIcon();
        if(large_icon!=null){
            notificationBuilder.setLargeIcon(large_icon);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification*/ , notificationBuilder.build());

        message_static = "WhenD : " + message;
        Thread thread = new Thread(new Runnable() {
            public void run() {

                handler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            AppPrefs appPrefs = new AppPrefs(getApplicationContext());

                //Toast toast = Toast.makeText(context, message_static, duration);
               // toast.show();


            int count =  appPrefs.getUnreadNotificationCount();
            count++;
            appPrefs.setUnreadNotificationCount(count);

            message_static = null;
        }


    };



    public Bitmap getLargeIcon(){
        BitmapDrawable bmpdrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_03);
        Bitmap bitmap = bmpdrawable.getBitmap();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float target_px = 36f * getResources().getDisplayMetrics().density;
        float multiplier=metrics.density/3f;

        bitmap = bitmap.createScaledBitmap(bitmap, (int)target_px, (int)target_px, false);

        return bitmap;
    }
}


