package br.com.v8developmentstudio.rccguarulhos.calendar.services;

/**
 * Created by cleiton.dantas on 01/04/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;

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

