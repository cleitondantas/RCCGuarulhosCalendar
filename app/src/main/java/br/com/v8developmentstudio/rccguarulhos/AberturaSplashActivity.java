package br.com.v8developmentstudio.rccguarulhos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.services.Preferences;
import br.com.v8developmentstudio.rccguarulhos.services.ServicoNotificacao;
import br.com.v8developmentstudio.rccguarulhos.task.TaskProcess;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;
import br.com.v8developmentstudio.rccguarulhos.util.FiltroDatas;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class AberturaSplashActivity  extends Activity {

    private PersistenceDao persistenceDao =  PersistenceDao.getInstance(this);
    private ServicoNotificacao servicoNotificacao = new ServicoNotificacao();
    private ActivityServices activityServices = new ActivityServicesImpl();
    public static Integer TIMESLEAP = 2000;
    private FiltroDatas filtroDatas = new FiltroDatas();
    private Preferences preferences = new Preferences(this);

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abertura_splashscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        servicoNotificacao.createAlarmNotification(this);
        servicoNotificacao.atualizacao(this);

        final Bundle bundle = getIntent().getExtras();
        if(bundle!=null && bundle.get("UID")!=null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent minhaintent = new Intent(getApplicationContext(), DescricaoActivity.class);
                    minhaintent.putExtras(bundle);
                    AberturaSplashActivity.this.startActivity(minhaintent);
                    AberturaSplashActivity.this.finish();
                }
            }, TIMESLEAP);
        }else{
            Date date = new Date(preferences.preferencesTimeAtulizacao());
            boolean isOnline = activityServices.isOnline(this);
            if (isOnline && filtroDatas.verificaDataUltimaAtualizacao(date)) {
                TaskProcess taskPross = new TaskProcess(this);
                taskPross.execute();
            } else {
                if (!isOnline) {
                    Toast toast = Toast.makeText(this, "SEM CONEXÂO!", Toast.LENGTH_LONG);
                    toast.show();

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent minhaintent = new Intent(getApplicationContext(), MainActivity.class);
                        AberturaSplashActivity.this.startActivity(minhaintent);
                        AberturaSplashActivity.this.finish();
                    }
                }, TIMESLEAP);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
