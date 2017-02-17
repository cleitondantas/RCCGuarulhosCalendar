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

        String titulo =  remoteMessage.getNotification().getTitle();// TITULO DA MENSAGEM (Título)(ANDROID E IOS) (no console console.firebase.google.com)
        String texto = remoteMessage.getNotification().getBody(); // CORPO DA MENSAGEM (Texto da mensagem) (no console console.firebase.google.com)
            Log.d(TAG, "Titulo :" +titulo);
            Log.d(TAG, "Texto :" +texto);
            Notificacao notificacao = new Notificacao();
            notificacao.setTitulo(titulo);
            notificacao.setTexto(texto);

        if(!remoteMessage.getData().isEmpty()) {

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
            notificacao.setAtivo(true);
            persistenceDao.salvaNotificacao(notificacao,persistenceDao.openDB(getApplicationContext()));

            Intent newIntent = new Intent(getApplicationContext(), AberturaSplashActivity.class);
            Bundle dados = new Bundle();
            dados.putSerializable(Constantes.OBJ_NOTIFICACAO,notificacao);
            newIntent.putExtras(dados);
            notificationService.gerarNotificacao(getApplicationContext(),newIntent,notificacao.getTitulo(),notificacao.getTexto(),0);

        }
    }
}
