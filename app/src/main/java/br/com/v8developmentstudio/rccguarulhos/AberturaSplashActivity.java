package br.com.v8developmentstudio.rccguarulhos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.task.TaskProcess;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class AberturaSplashActivity  extends Activity {

    private PersistenceDao persistenceDao = new PersistenceDao(this);
    public static SQLiteDatabase bancoDados = null;
    private Integer TIMESLEAP;
    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abertura_splashscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        persistenceDao.onCreate(persistenceDao.openDB());

        if(true){
            TIMESLEAP=4000;
            TaskProcess taskPross = new TaskProcess(this);
            taskPross.execute();
        }else{
            TIMESLEAP=4000;
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

}
