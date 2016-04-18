package br.com.v8developmentstudio.rccguarulhos.services;

/**
 * Created by cleiton.dantas on 01/04/2016.
 */

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
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

public class BroadcastReceiverAux extends BroadcastReceiver {
    private PersistenceDao persistenceDao;
    private Preferences preferences;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @Override
    public void onReceive(Context context, Intent intent) {
        persistenceDao= new PersistenceDao(context);
        preferences = new Preferences(context);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, preferences.getPreferencesDiaAlarm());
        Date dia = cal.getTime();

        Log.i("Script", "-> Alarme");
        List<Evento> eventos = persistenceDao.recuperaEventosPorDia(dia);

        int numIdentificacao=0;
        for (Evento evento: eventos) {
            numIdentificacao++;
           if(persistenceDao.recuperaFavoritoPorUID(evento.getUid()).size()!=0) {
               gerarNotificacao(context, redirectDescricaoDoEvento(context, evento), context.getString(R.string.novoevento), evento.getSumario(), dateFormat.format(evento.getDataHoraInicio()), numIdentificacao);
           }
        }
    }

    public void gerarNotificacao(Context context, Intent intent, CharSequence ticker, CharSequence titulo, CharSequence descricao,int numerodaNotificacao) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, numerodaNotificacao, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(titulo);
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.rcc);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.rcc));
        builder.setContentIntent(pendingIntent);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(numerodaNotificacao, n);

        emitirNotificacaoSonora(context);
    }
    private Intent redirectDescricaoDoEvento(final Context context ,final Evento evento) {
        Intent intent = new Intent(context, DescricaoActivity.class);
        Bundle dados = new Bundle();
        dados.putInt(Constantes.ID,evento.getId().intValue());
        dados.putInt(Constantes.CALENDARIO, evento.getIdCalendario());
        intent.putExtras(dados);
       return intent;
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
}

