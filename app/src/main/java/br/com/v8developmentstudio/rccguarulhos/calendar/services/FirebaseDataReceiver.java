package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;
import br.com.v8developmentstudio.rccguarulhos.calendar.task.TaskProcessBackground;

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
        Log.d(TAG, "RECEBIDO PELO FirebaseDataReceiver");
        notificationService = new NotificationService();
        persistenceDao = new PersistenceDao(context);
        Notificacao notificacao = new Notificacao();

        if(intent.getExtras().get("TICKER")!=null){
            notificacao.setTituloTicker((String)intent.getExtras().get("TICKER"));
        }
        if(intent.getExtras().get("TITLE")!=null){
            notificacao.setTitulo((String)intent.getExtras().get("TITLE"));
        }
        if(intent.getExtras().get("DESCRICAO")!=null){
            notificacao.setTexto((String)intent.getExtras().get("DESCRICAO"));
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

        persistenceDao.salvaNotificacao(notificacao,persistenceDao.openDB(context));
        notificationService.gerarNotificacao(context,intent,notificacao.getTitulo(),notificacao.getTexto(),0);

    }
}
