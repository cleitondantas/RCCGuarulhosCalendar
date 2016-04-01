package br.com.v8developmentstudio.rccguarulhos;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Calendar;

import br.com.v8developmentstudio.rccguarulhos.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.task.TaskProcess;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class AberturaSplashActivity  extends Activity {

    private PersistenceDao persistenceDao = new PersistenceDao(this);
    public static Integer TIMESLEAP;
    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abertura_splashscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        persistenceDao.onCreate(persistenceDao.openDB());



        boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("CALENDARIO_RCC_DISPARADO"), PendingIntent.FLAG_NO_CREATE) == null);
        alarmNotification(alarmeAtivo);
        if(isOnline()){
            TIMESLEAP=6000;
            TaskProcess taskPross = new TaskProcess(this);
            taskPross.execute();
        }else{
            TIMESLEAP=2000;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent minhaintent = new Intent(AberturaSplashActivity.this,MainActivity.class);
                AberturaSplashActivity.this.startActivity(minhaintent);
                AberturaSplashActivity.this.finish();
            }
        },TIMESLEAP);

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void alarmNotification(boolean alarmeAtivo) {
        if (alarmeAtivo) {
            Log.i("Script", "Novo alarme");

            Intent intent = new Intent("CALENDARIO_RCC_DISPARADO");
            PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.SECOND, 3);

            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 5000, p);
        } else {
            Log.i("Script", "Alarme já ativo");
        }
    }
}
