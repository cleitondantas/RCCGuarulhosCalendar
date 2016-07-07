package br.com.v8developmentstudio.rccguarulhos.services;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.activitys.ListaEventosActivity;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

/**
 * Created by cleiton.dantas on 07/04/2016.
 */
public class ActivityServicesImpl implements ActivityServices{


    @Override
    public void redirect(Context context, Class<?> clazz, Bundle dados) {
        Intent intent = new Intent(context,clazz);
        if(dados!=null) {
            intent.putExtras(dados);
        }
      //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

    }

    @Override
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
