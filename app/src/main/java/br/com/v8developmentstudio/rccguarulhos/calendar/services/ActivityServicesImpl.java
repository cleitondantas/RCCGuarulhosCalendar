package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.AberturaSplashActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.DescricaoActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.Constantes;

/**
 * Created by cleiton.dantas on 07/04/2016.
 */
public class ActivityServicesImpl implements ActivityServices{
    private Context context;

    public ActivityServicesImpl(){

    }
    public ActivityServicesImpl(Context context){
    this.context = context;
    }


    @Override
    public void redirect(Context context, Class<?> clazz, Bundle dados) {
        Intent intent = new Intent(context,clazz);
        if(dados!=null) {
            intent.putExtras(dados);
        }
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

    }

    @Override
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void redirectWebBrowser(Context context,String url){
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.setData(Uri.parse(url));
        intentSite.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intentSite);
        if(context instanceof AberturaSplashActivity){
            ((AberturaSplashActivity)context).finish();
        }
    }

    public void startFacebook(Context context, String facebookUrl) {
        try {
            Uri uri = null;
            int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if (versionCode >= 3002850) {
                facebookUrl = facebookUrl.toLowerCase().replace("www.", "m.");
                if (!facebookUrl.startsWith("https")) {
                    facebookUrl = "https://" + facebookUrl;
                }
                uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
            } else {
                String pageID = facebookUrl.substring(facebookUrl.lastIndexOf("/"));
                uri = Uri.parse("fb://page" + pageID);
            }
            Log.d("FACE", "startFacebook: uri = " + uri.toString());
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } catch (Throwable e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

    public void startYoutube(Context context, String youtubeUrl){
        String replaceURL = youtubeUrl.replace("https://www.youtube.com/watch?v=","");
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + replaceURL));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeUrl));
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public void hub(Context context,Bundle bundle){
       Notificacao notificacao = (Notificacao)bundle.getSerializable(Constantes.OBJ_NOTIFICACAO);
        hub(context,notificacao);
    }

    public void hub(Context context, Notificacao notificacao){
        PersistenceDao persistenceDao = new PersistenceDao(context);
        notificacao.setAtivo(false);
        persistenceDao.updateInvalidaNotificacao(persistenceDao.openDB(),notificacao.getNumericNotification());
        if(notificacao.getKey().contains("URL")){
            redirectWebBrowser(context,notificacao.getValue());
        }
        if(notificacao.getKey().contains("FACE")){
            startFacebook(context,notificacao.getValue());
        }
        if(notificacao.getKey().contains("YOUTUBE")){
            startYoutube(context,notificacao.getValue());
        }
        if(notificacao.getKey().contains("UID")){

            List<Evento> events = persistenceDao.recuperaEventoPorUID(notificacao.getValue(), persistenceDao.openDB());
            NotificationService notificationService = new NotificationService();
           Intent intent = notificationService.redirectDescricaoDoEvento(context,events.get(0), DescricaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            if(context instanceof AberturaSplashActivity){
                ((AberturaSplashActivity)context).finish();
            }
        }
    }
}
