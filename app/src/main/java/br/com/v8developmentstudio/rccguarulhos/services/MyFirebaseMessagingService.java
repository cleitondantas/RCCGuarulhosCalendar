package br.com.v8developmentstudio.rccguarulhos.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.activitys.WebViewActivity;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

/**
 * Created by cleiton.dantas on 13/12/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private PersistenceDao persistenceDao;
    private NotificationService notificationService;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        persistenceDao = new PersistenceDao(getApplicationContext());
        notificationService = new NotificationService();
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String titulo = remoteMessage.getFrom();
        String texto ="";
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            texto = remoteMessage.getNotification().getBody();
        }

        if(!remoteMessage.getData().isEmpty()) {

            String ticker = remoteMessage.getData().get("TICKER");
            String title = remoteMessage.getData().get("TITLE");
            String descricao = remoteMessage.getData().get("DESCRICAO");

            if(remoteMessage.getData().get("UID")!=null) {
                String uid = remoteMessage.getData().get("UID");
                List<Evento> events = persistenceDao.recuperaEventoPorUID(uid, persistenceDao.openDB());
                if (events != null && events.size() > 0) {
                    notificationService.gerarNotificacao(getApplicationContext(), notificationService.redirectDescricaoDoEvento(getApplicationContext(), events.get(0), DescricaoActivity.class), ticker, title, descricao, 0);
                }
            }
            if(remoteMessage.getData().get("URL")!=null){
                String url = remoteMessage.getData().get("URL");
                notificationService.gerarNotificacao(getApplicationContext(),notificationService.redirectURL(getApplicationContext(),url, WebViewActivity.class),ticker,title,descricao,0);
            }

        }

    }
//
//    private void sendNotification(String title,String messageBody,Context context) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,  PendingIntent.FLAG_ONE_SHOT);
//        Log.d(TAG,messageBody);
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.rcc)
//                .setTicker(title)
//                .setContentTitle(title)
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.rcc));
//
//        notificationBuilder.setContentIntent(pendingIntent);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setFullScreenIntent(pendingIntent, true);
//        }
//
//        NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());
//    }

}
