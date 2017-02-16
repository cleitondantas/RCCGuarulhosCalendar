package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import br.com.v8developmentstudio.rccguarulhos.calendar.util.Constantes;

/**
 * Created by cleiton.dantas on 06/04/2016.
 */
public class Preferences {
    private Context context;
    public Preferences(Context context){
        this.context = context;
    }

    public void salvarTokenFirebase(String tokenFirebase){
        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constantes.TOKENFIREBASE,tokenFirebase);
        editor.commit();
        Log.i("DEBUG", "PERSISTINDO tokenFirebase");
    }
    public String getPreferencesTokenFirebase(){
        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
        return settings.getString(Constantes.TOKENFIREBASE,"");
    }


    public void salvarPrefTimeAtulizacao(long timemilles){
        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("TimeAtulizacao",timemilles);
        editor.commit();
        Log.i("DEBUG", "PERSISTINDO TimeAtulizacao");
    }
    public long preferencesTimeAtulizacao(){
        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
        return settings.getLong("TimeAtulizacao",120000);
    }


    public void salvarPrefTimeRepeating(long timemilles){
        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("PrefTimeRepeating",timemilles);
        editor.commit();
        Log.i("DEBUG", "PERSISTINDO salvarPrefTimeRepeating");
    }

    public long preferencesTimeRepeating(){
        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
       return settings.getLong("PrefTimeRepeating",120000);
    }


    public void salvarPrefDiaAlarm(int dia){
        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("PrefDiaAlarm",dia);
        editor.commit();
        Log.i("DEBUG", "PERSISTINDO salvarPrefDiaAlarm");
    }

    public int getPreferencesDiaAlarm(){
        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
        return settings.getInt("PrefDiaAlarm",1);
    }

}
