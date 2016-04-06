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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Preferences preferences = new Preferences(this);
        NumberPicker numberPicker =(NumberPicker) findViewById(R.id.numberPicker1);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(120);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("DEBUG","-->  Numero" +newVal);
                preferences.salvarPrefTimeRepeating((newVal*60*1000));
            }
        });
    }

}
