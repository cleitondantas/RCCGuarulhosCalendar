package br.com.v8developmentstudio.rccguarulhos.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.modelo.Notificacao;

/**
 * Created by cleiton.dantas on 01/04/2016.
 */
public class ServicoNotificacao {

    private void criarNotificacao(Context context,Notificacao notificacao) {
        NotificationManager notif = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setTicker(notificacao.getTituloTicker());
        mBuilder.setContentTitle(notificacao.getTitulo());
        mBuilder.setContentText(notificacao.getTexto());
        mBuilder.setSmallIcon(notificacao.getDrawableSmallIcon());
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),notificacao.getDrawableLargeIcon()));
        Notification notification = mBuilder.build();
        notification.vibrate = new long[]{150,350,600,600};
        notif.notify(notificacao.getDrawableSmallIcon(),notification);
    }
}
