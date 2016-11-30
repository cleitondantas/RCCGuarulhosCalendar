package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.services.Preferences;

public class SettingsActivity extends AppCompatActivity {
    private NumberPicker dayForAlarm;
    private ActivityServices activityServices = new ActivityServicesImpl();

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
//        dayForAlarm = (NumberPicker) findViewById(R.id.diasparaalarme);
//
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
