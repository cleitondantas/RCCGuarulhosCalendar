package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Random;

import br.com.v8developmentstudio.rccguarulhos.calendar.AberturaSplashActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.SettingsActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;
import br.com.v8developmentstudio.rccguarulhos.calendar.task.TaskProcessBackground;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.Constantes;

/**
 * Created by cleiton.dantas on 02/02/2017.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {
    private final String TAG = "FirebaseDataReceiver";
    private NotificationService notificationService;
    private PersistenceDao persistenceDao;
    private ActivityServices activityServices = new ActivityServicesImpl();
    @Override
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context,intent);

        for (String key : intent.getExtras().keySet()) {
            System.out.println(key + ":" + intent.getExtras().get(key));
            Log.i(TAG, "-> KEY: "+ key+ " DATA:" +intent.getExtras().get(key));
        }
        Log.d(TAG, "RECEBIDO PELO FirebaseDataReceiver");
        notificationService = new NotificationService();
        persistenceDao = new PersistenceDao(context);
        Notificacao notificacao = new Notificacao();

        if(intent.getExtras().get("gcm.notification.title")!=null){
            notificacao.setTitulo((String)intent.getExtras().get("gcm.notification.title"));
        }
        if(intent.getExtras().get("gcm.notification.body")!=null){
            notificacao.setTexto((String)intent.getExtras().get("gcm.notification.body"));
        }

        if(intent.getExtras().get("UID")!=null){
            notificacao.setKey("UID");
            notificacao.setValue((String)intent.getExtras().get("UID"));
        }
        if(intent.getExtras().get("URL")!=null){
            notificacao.setKey("URL");
            notificacao.setValue((String)intent.getExtras().get("URL"));
        }

        if(intent.getExtras().get("FACE")!=null){
            notificacao.setKey("FACE");
            notificacao.setValue((String)intent.getExtras().get("FACE"));
        }
        if(intent.getExtras().get("YOUTUBE")!=null){
            notificacao.setKey("YOUTUBE");
            notificacao.setValue((String)intent.getExtras().get("YOUTUBE"));
        }
        if(intent.getExtras().get("UPDATE")!=null){
            if(intent.getExtras().get("UPDATE").equals("TRUE")){
                boolean isOnline = activityServices.isOnline(context);
                if(isOnline){
                    TaskProcessBackground taskPross = new TaskProcessBackground(context);
                    taskPross.execute();
                    Log.i("Script", "-> Base Atualizado in BackGround");
                }
            }
        }

        notificacao.setNumericNotification(new Random().nextInt(10000));
        notificacao.setAtivo(true);
        persistenceDao.salvaNotificacao(notificacao,persistenceDao.openDB(context));
        Intent newIntent = new Intent(context, AberturaSplashActivity.class);
        Bundle dados = new Bundle();
        dados.putSerializable(Constantes.OBJ_NOTIFICACAO,notificacao);
        newIntent.putExtras(dados);
        notificationService.gerarNotificacao(context,newIntent,notificacao.getTitulo(),notificacao.getTexto(),0);


        completeWakefulIntent(intent);
    }


}
