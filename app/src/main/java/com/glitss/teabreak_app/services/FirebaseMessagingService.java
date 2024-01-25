package com.glitss.teabreak_app.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Ui.SplashScreen;
import com.glitss.teabreak_app.Utils.AppController;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static  String ChannelID = "";
    private NotificationChannel mChannel;
    private static final String TAG = "MyAndroidFCMService";
    Bitmap bitmap;

    @Override
    public void onNewToken(@NonNull String token) {
        Log.e("NEW_TOKEN",token);
        SharedPreferences pref = AppController.getAppContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("FCMToken", token);
        editor.commit();
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        //Log data to Log Cat
        Map data = remoteMessage.getData();
//        Object PageName= ((ArrayMap) data).valueAt(0);
//        Object sch_id= ((ArrayMap) data).valueAt(1);
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "msg: " + remoteMessage);
        Log.e(TAG, "msg: " + data);
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        String TrueOrFlase = remoteMessage.getData().get("title");
        //  String Imageurl=remoteMessage.getNotification().getIcon();
//        String Imageurl=remoteMessage.getData().get("image");
//        bitmap = getBitmapfromUrl(Imageurl);
        String click_action = remoteMessage.getNotification().getClickAction();

        // create notification
        createNotification(remoteMessage.getNotification().getBody(),click_action//PageName.toString()
                ,remoteMessage.getNotification().getTitle(),TrueOrFlase,bitmap,remoteMessage.getData().get("kind"));
        super.onMessageReceived(remoteMessage);
    }

    @SuppressLint("WrongConstant")
    private void createNotification(String messageBody, String clickAction, String Title, String TrueOrFlase, Bitmap Icon, String sch_id) {
        Log.d("TAG", "msg: created notification " );
        Intent intent = new Intent(this, SplashScreen.class);
        PendingIntent resultIntent =null;
//        if (sch_id.equalsIgnoreCase("task")){
//            intent = new Intent(this, SplashScreenActivity.class);
//        }else {
//            intent = new Intent(this, SplashScreenActivity.class);
//        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("MSg", clickAction);
//        intent.putExtra("id", pagename);
        intent.putExtra("pushnotification", "yes");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_IMMUTABLE);
        }else {
            resultIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }



        ChannelID= this.getString(R.string.default_notification_channel_id);
//        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        long[] VIBRATE_PATTERN = {0, 500};
//        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.apsflnotification);
        NotificationCompat.Builder mNotificationBuilder = null;
        mNotificationBuilder = new NotificationCompat.Builder( this,ChannelID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(Title)
                .setContentText(messageBody)
                .setAutoCancel( true )
//                .setSound(sound)
                .setLargeIcon(Icon)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(getResources().getColor(R.color.red), 1000, 1000)
                // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setContentIntent(resultIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AudioAttributes mAudioAttributes=new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mChannel = new NotificationChannel(ChannelID, "Qwykr_Channel_way",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            mChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            mChannel.setSound(sound,mAudioAttributes);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(0, mNotificationBuilder.build());
    }

//    public Bitmap getBitmapfromUrl(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//            return bitmap;
//        } catch (Exception e) {

//            e.printStackTrace();
//            return null;
//
//        }
//    }


}
