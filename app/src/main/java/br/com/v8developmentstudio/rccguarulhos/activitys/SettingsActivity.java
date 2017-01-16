package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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

import br.com.v8developmentstudio.rccguarulhos.AberturaSplashActivity;
import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.services.Preferences;
import br.com.v8developmentstudio.rccguarulhos.task.TaskProcess;
import br.com.v8developmentstudio.rccguarulhos.task.TaskProcessBackground;

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
        Date date = new Date(preferences.preferencesTimeAtulizacao());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        dataAtulizcao.setText(dateFormat.format(date));
        fabRefresh = (FloatingActionButton) findViewById(R.id.fb_refresh);
//        dayForAlarm = (NumberPicker) findViewById(R.id.diasparaalarme);


        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaBase(v.getContext());
            }
        });




//        dayForAlarm.setMinValue(1);
//        dayForAlarm.setMaxValue(120);
//        dayForAlarm.setValue(preferences.getPreferencesDiaAlarm());
//        dayForAlarm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Log.i("DEBUG", "-->  Dia" + newVal);
//                preferences.salvarPrefDiaAlarm(newVal);
//            }
//        });
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
