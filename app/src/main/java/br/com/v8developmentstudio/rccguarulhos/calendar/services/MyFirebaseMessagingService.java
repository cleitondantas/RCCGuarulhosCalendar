package br.com.v8developmentstudio.rccguarulhos.calendar.services;

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
import java.util.Random;

import br.com.v8developmentstudio.rccguarulhos.calendar.AberturaSplashActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.WebViewActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;
import br.com.v8developmentstudio.rccguarulhos.calendar.task.TaskProcessBackground;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.Constantes;

/**
 * Created by cleiton.dantas on 13/12/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private PersistenceDao persistenceDao;
    private NotificationService notificationService;
    private ActivityServices activityServices = new ActivityServicesImpl();
    public final String NOTIFICATION = "NOTIFICACAO";
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

            Notificacao notificacao = new Notificacao();
            notificacao.setAtivo(true);

            if(remoteMessage.getData().get("TICKER")!=null){
                notificacao.setTituloTicker((String)remoteMessage.getData().get("TICKER"));
            }
            if(remoteMessage.getData().get("TITLE")!=null){
                notificacao.setTitulo((String)remoteMessage.getData().get("TITLE"));
            }
            if(remoteMessage.getData().get("DESCRICAO")!=null){
                notificacao.setTexto((String)remoteMessage.getData().get("DESCRICAO"));
            }

            if(remoteMessage.getData().get("UID")!=null){
                notificacao.setKey("UID");
                notificacao.setValue((String)remoteMessage.getData().get("UID"));
            }
            if(remoteMessage.getData().get("URL")!=null){
                notificacao.setKey("URL");
                notificacao.setValue((String)remoteMessage.getData().get("URL"));
            }

            if(remoteMessage.getData().get("FACE")!=null){
                notificacao.setKey("FACE");
                notificacao.setValue((String)remoteMessage.getData().get("FACE"));
            }
            if(remoteMessage.getData().get("YOUTUBE")!=null){
                notificacao.setKey("YOUTUBE");
                notificacao.setValue((String)remoteMessage.getData().get("YOUTUBE"));
            }
            if(remoteMessage.getData().get("UPDATE")!=null){
                if(remoteMessage.getData().get("UPDATE").equals("TRUE")){
                    boolean isOnline = activityServices.isOnline(getApplicationContext());
                    if(isOnline){
                        TaskProcessBackground taskPross = new TaskProcessBackground(getApplicationContext());
                        taskPross.execute();
                        Log.i("Script", "-> Base Atualizado in BackGround");
                    }
                }
            }
            notificacao.setNumericNotification(new Random().nextInt(10000));

            persistenceDao.salvaNotificacao(notificacao,persistenceDao.openDB(getApplicationContext()));

            Intent newIntent = new Intent(getApplicationContext(), AberturaSplashActivity.class);
            Bundle dados = new Bundle();
            dados.putSerializable(Constantes.OBJ_NOTIFICACAO,notificacao);
            newIntent.putExtras(dados);
            notificationService.gerarNotificacao(getApplicationContext(),newIntent,notificacao.getTitulo(),notificacao.getTexto(),0);
            Log.i(NOTIFICATION,"onMessageReceived");
        }
    }
}
