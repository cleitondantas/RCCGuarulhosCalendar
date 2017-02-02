package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by cleiton.dantas on 02/02/2017.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {
    private final String TAG = "FirebaseDataReceiver";
    private NotificationService notificationService;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "RECEBIDO PELO FirebaseDataReceiver");
        notificationService = new NotificationService();
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
            }
        }
        notificationService.gerarNotificacao(context,intent,"TICKET FIRE","TITULO FIRE","DESCRICAO FIRE",0);

    }
}
