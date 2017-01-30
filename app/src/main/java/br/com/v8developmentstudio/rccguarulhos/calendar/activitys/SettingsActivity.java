package br.com.v8developmentstudio.rccguarulhos.calendar.activitys;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.v8developmentstudio.rccguarulhos.calendar.BuildConfig;
import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.Preferences;
import br.com.v8developmentstudio.rccguarulhos.calendar.task.TaskProcessBackground;

public class SettingsActivity extends AppCompatActivity {
    private NumberPicker dayForAlarm;
    private ActivityServices activityServices = new ActivityServicesImpl();
    private FloatingActionButton fabRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        final Preferences preferences = new Preferences(this);
        TextView dataAtulizcao = (TextView) findViewById(R.id.atualizacaoView);
        TextView versao =(TextView) findViewById(R.id.idVersionApp);

        Date date = new Date(preferences.preferencesTimeAtulizacao());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        dataAtulizcao.setText(dateFormat.format(date));
        String version = BuildConfig.VERSION_NAME;
        versao.setText("Versão: "+version);

        fabRefresh = (FloatingActionButton) findViewById(R.id.fb_refresh);
//        dayForAlarm = (NumberPicker) findViewById(R.id.diasparaalarme);


        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaBase(v.getContext());
            }
        });


    }

    public void atualizaBase(Context context){
        boolean isOnline = activityServices.isOnline(context);
        if(isOnline){
            final Animation animeFloating = AnimationUtils.loadAnimation(this,R.anim.rotate2);
            fabRefresh.setAnimation(animeFloating);
            fabRefresh.show();
            TaskProcessBackground taskPross = new TaskProcessBackground(context);
            taskPross.execute();
            Toast toast = Toast.makeText(this, "INICIANDO ATUALIZAÇÃO...", Toast.LENGTH_LONG);
            toast.show();
            Log.i("Script", "-> Base Atualizado in BackGround");
        }
    }
    @Override
    public void onBackPressed() {
        activityServices.redirect(this, MainActivity.class, null);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
