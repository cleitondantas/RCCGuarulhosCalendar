package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by cleiton.dantas on 06/04/2016.
 */
public class BootCompletedReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Script", "-> BOOT COMPLETO");
        if (intent != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                ServicoNotificacao servicoNotificacao = new ServicoNotificacao();
                servicoNotificacao.createAlarmNotification(context);
                servicoNotificacao.atualizacao(context);
            }
        }
    }
}
