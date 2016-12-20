package br.com.v8developmentstudio.rccguarulhos.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

/**
 * Created by cleiton.dantas on 19/12/2016.
 */

public class NotificationService {



    public void gerarNotificacao(Context context, Intent intent, CharSequence ticker, CharSequence titulo, CharSequence descricao, int numerodaNotificacao) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, numerodaNotificacao, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(titulo);
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.rcc);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.rcc));

        builder.setContentIntent(pendingIntent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setFullScreenIntent(pendingIntent, true);
        }

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(numerodaNotificacao, n);

        emitirNotificacaoSonora(context);
    }

    private void emitirNotificacaoSonora(Context context){
        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);

            if(toque.isPlaying()){
                toque.play();
            }else{
                toque.stop();
                toque.play();
            }

        }
        catch(Exception e){
            Log.e("ERROR", "EMITIR SOM ()--> " + e);
            e.printStackTrace();
        }
        Log.i("Script", "-> gerarNotificacao");
    }


    public Intent redirectDescricaoDoEvento(final Context context ,final Evento evento,final Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        Bundle dados = new Bundle();
        dados.putInt(Constantes.ID,evento.getId().intValue());
        dados.putInt(Constantes.CALENDARIO, evento.getIdCalendario());
        dados.putSerializable(Constantes.OBJ_EVENTO,evento);
        intent.putExtras(dados);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
