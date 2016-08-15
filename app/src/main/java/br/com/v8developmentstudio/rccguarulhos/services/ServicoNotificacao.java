package br.com.v8developmentstudio.rccguarulhos.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.provider.SyncStateContract;
import android.util.Log;

import java.util.Calendar;

import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

/**
 * Created by cleiton.dantas on 01/04/2016.
 */
public class ServicoNotificacao {
    private Preferences preferences;

    private boolean verificaExistencia(Context context){
        preferences = new Preferences(context);
        return (PendingIntent.getBroadcast(context, 0, new Intent(Constantes.CALENDARIO_RCC_DISPARADO), PendingIntent.FLAG_NO_CREATE) == null);
    }



    public void createAlarmNotification(Context context) {
        if (verificaExistencia(context)) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.SECOND,5);

            Log.i("Script", "Novo alarme");
            Intent intent = new Intent(Constantes.CALENDARIO_RCC_DISPARADO);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarme = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
            //AlarmManager.INTERVAL_HALF_DAY
            //3000000
        } else {
            Log.i("Script", "Alarme já ativo");
        }
    }
}
