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
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.task.TaskProcessBackground;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

public class BroadcastReceiverAux extends BroadcastReceiver {
    private PersistenceDao persistenceDao;
    private Preferences preferences;
    private ActivityServices activityServices = new ActivityServicesImpl();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @Override
    public void onReceive(Context context, Intent intent) {
        persistenceDao = PersistenceDao.getInstance(context);
        preferences = new Preferences(context);
        Log.i("Script", "-> Alarme");

        int numIdentificacao=0;
        int[]p = {1,2,4,3,5,6,7,8,9,10,11,12,13,15,17,19,20,22};

        for(int dia :p) {
            for (Evento evento : persistenceDao.recuperaEventosPorDia(getDatePreferences(dia))) {
                numIdentificacao++;
                if (persistenceDao.recuperaFavoritoPorUID(evento.getUid()).size() != 0) {
                    gerarNotificacao(context, redirectDescricaoDoEvento(context, evento), context.getString(R.string.lembrete), evento.getSumario(), dateFormat.format(evento.getDataHoraInicio()), numIdentificacao);
                }
            }
        }
        atualizaBase(context);
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setFullScreenIntent(pendingIntent, true);
        }

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
        dados.putSerializable(Constantes.OBJ_EVENTO,evento);
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


    public void atualizaBase(Context context){
        boolean isOnline = activityServices.isOnline(context);
        if(isOnline){
            TaskProcessBackground taskPross = new TaskProcessBackground(context);
            taskPross.execute();
            Log.i("Script", "-> Base Atualizado in BackGround");
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

