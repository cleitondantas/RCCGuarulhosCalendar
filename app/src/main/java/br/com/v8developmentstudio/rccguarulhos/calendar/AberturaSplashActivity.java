package br.com.v8developmentstudio.rccguarulhos.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.Date;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.WebViewActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.Preferences;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ServicoNotificacao;
import br.com.v8developmentstudio.rccguarulhos.calendar.task.TaskProcess;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.Constantes;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.FiltroDatas;

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
    private  Context context;

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abertura_splashscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        servicoNotificacao.createAlarmNotification(this);
        servicoNotificacao.atualizacao(this);
        this.context = getApplicationContext();

        boolean controlaFluxo = true;
        final Bundle bundle = getIntent().getExtras();

        if(bundle!=null && bundle.getSerializable(Constantes.OBJ_NOTIFICACAO)!=null){
            controlaFluxo = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityServices.hub(context,bundle);
                }
            }, TIMESLEAP);
        }
            if(controlaFluxo) {
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
