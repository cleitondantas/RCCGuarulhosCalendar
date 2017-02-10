package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

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
      //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        context.startActivity(intentSite);
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
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Throwable e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));

        }
    }

    public void startYoutube(Context context, String youtubeUrl){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
        try {
            if(context.getPackageManager().getPackageInfo("com.google.android.youtube", 0)!=null){
                intent.setClassName("com.google.android.youtube", "com.google.android.youtube.WatchActivity");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ERRO AO ABRIR YOUTUBE",e.getMessage());
        }
        context.startActivity(intent);

    }

    public void hub(Context context,Bundle bundle){
       Notificacao notificacao = (Notificacao)bundle.getSerializable(Constantes.OBJ_NOTIFICACAO);
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
            PersistenceDao persistenceDao = new PersistenceDao(context);
            List<Evento> events = persistenceDao.recuperaEventoPorUID(notificacao.getValue(), persistenceDao.openDB());
            NotificationService notificationService = new NotificationService();
            notificationService.redirectDescricaoDoEvento(context,events.get(0), DescricaoActivity.class);
        }



    }
}
