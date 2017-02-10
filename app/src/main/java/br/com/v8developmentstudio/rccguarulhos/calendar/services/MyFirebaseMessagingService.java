package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.WebViewActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;

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

            Notificacao notificacao = new Notificacao();
            notificacao.setTitulo(titulo);
            notificacao.setAtivo(true);
            notificacao.setTexto(descricao);


            if(remoteMessage.getData().get("UID")!=null) {
                String uid = remoteMessage.getData().get("UID");
                notificacao.setKey("UID");
                notificacao.setValue(uid);
                List<Evento> events = persistenceDao.recuperaEventoPorUID(uid, persistenceDao.openDB());
                if (events != null && events.size() > 0) {
                  notificationService.gerarNotificacao(this, notificationService.redirectDescricaoDoEvento(getApplicationContext(), events.get(0), DescricaoActivity.class),title, descricao, 0);
                }
            }
            if(remoteMessage.getData().get("URL")!=null){
                String url = remoteMessage.getData().get("URL");
                notificacao.setKey("URL");
                notificacao.setValue(url);

                notificationService.gerarNotificacao(this,notificationService.redirectURL(getApplicationContext(),url),title,descricao,0);
            }
        }

    }
}
