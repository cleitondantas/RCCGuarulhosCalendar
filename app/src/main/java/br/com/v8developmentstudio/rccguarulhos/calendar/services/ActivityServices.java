package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.content.Context;
import android.os.Bundle;

import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;

/**
 * Created by cleiton.dantas on 07/04/2016.
 */
public interface ActivityServices {
    public void redirect(Context context,Class<?>  clazz, Bundle dados);
    public boolean isOnline(Context context);
    public void redirectWebBrowser(Context context,String url);
    public void startFacebook(Context context, String facebookUrl);
    public void startYoutube(Context context,String youtubeUrl);
    public void hub(Context context,Bundle bundle);
    public void hub(Context context, Notificacao notificacao);
}


