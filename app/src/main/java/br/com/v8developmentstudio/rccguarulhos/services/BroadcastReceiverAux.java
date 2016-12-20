package br.com.v8developmentstudio.rccguarulhos.services;

/**
 * Created by cleiton.dantas on 01/04/2016.
 */

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.task.TaskProcessBackground;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

public class BroadcastReceiverAux extends BroadcastReceiver {
    private PersistenceDao persistenceDao;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private NotificationService notificationService  = new NotificationService();

    @Override
    public void onReceive(Context context, Intent intent) {
        persistenceDao = PersistenceDao.getInstance(context);
        Log.i("Script", "-> Alarme onReceive");
        int numIdentificacao=0;
        int[]p = {1,2,4};
        for(int dia :p) {
            for (Evento evento : persistenceDao.recuperaEventosPorDia(getDatePreferences(dia),persistenceDao.openDB(context))) {
                numIdentificacao++;
                if (persistenceDao.recuperaFavoritoPorUID(evento.getUid(),persistenceDao.openDB()).size() != 0) {
                    notificationService.gerarNotificacao(context, notificationService.redirectDescricaoDoEvento(context, evento,DescricaoActivity.class), context.getString(R.string.lembrete), evento.getSumario(), dateFormat.format(evento.getDataHoraInicio()), numIdentificacao);
                }
            }
        }
    }


    @NonNull
    private Date getDatePreferences(int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //cal.add(Calendar.DAY_OF_MONTH, preferences.getPreferencesDiaAlarm());
        cal.add(Calendar.DAY_OF_MONTH,dias);
        return cal.getTime();
    }
}

