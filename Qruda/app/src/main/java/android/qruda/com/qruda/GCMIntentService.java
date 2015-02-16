package android.qruda.com.qruda;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;



public class GCMIntentService extends IntentService{
    private static final int NOTIF_ALERTA_ID = 1;

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // mostrarNotification(extras.getString("mensaje"));
                mostrarNotification(extras.getString("mensaje"),extras.getString("titulo"));
            }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Muestra en la barra de tareas la notificación en android
    private void mostrarNotification(String msg,String titulo)
    {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(titulo)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.RED, 3000, 3000)
                        .setContentText(msg)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
                        //.setSubText(msg);


        Intent notIntent =  new Intent(this, SplashScreen.class);
        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);

        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }

}
