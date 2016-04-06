package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;


import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.services.Preferences;

public class SettingsActivity extends AppCompatActivity {
    private NumberPicker timeRefresh,dayForAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Preferences preferences = new Preferences(this);
        timeRefresh =(NumberPicker) findViewById(R.id.numberPicker1);
        dayForAlarm =(NumberPicker) findViewById(R.id.numberPicker2);

        timeRefresh.setMinValue(1);
        timeRefresh.setMaxValue(120);
        dayForAlarm.setMinValue(1);
        dayForAlarm.setMaxValue(120);

          timeRefresh.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
              @Override
              public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                  Log.i("DEBUG", "-->  Numero" + newVal);
                  preferences.salvarPrefTimeRepeating((newVal * 60 * 1000));
              }
          });

        dayForAlarm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("DEBUG", "-->  Dia" + newVal);
                preferences.salvarPrefDiaAlarm(newVal);
            }
        });

    }

}
