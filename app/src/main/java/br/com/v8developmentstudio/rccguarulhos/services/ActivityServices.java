package br.com.v8developmentstudio.rccguarulhos.services;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by cleiton.dantas on 07/04/2016.
 */
public interface ActivityServices {
    public void redirect(Context context,Class<?>  clazz, Bundle dados);
    public boolean isOnline(Context context);
}


