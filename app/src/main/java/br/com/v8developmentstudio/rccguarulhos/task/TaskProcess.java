package br.com.v8developmentstudio.rccguarulhos.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Properties;

import br.com.v8developmentstudio.rccguarulhos.bo.ConstrutorIcal;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.util.AssetsPropertyReader;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class TaskProcess extends AsyncTask<String,Object,String> {
    private Context context;
    private ProgressDialog progress;
    private ConstrutorIcal contruorical;
    private PersistenceDao persistenceDao;
    private Properties properties;
    private AssetsPropertyReader assetsPropertyReader;
    private List<Calendario> calendarios;
    public TaskProcess(Context context) {
        this.context = context;
        persistenceDao = new PersistenceDao(context);
        assetsPropertyReader  = new AssetsPropertyReader(context);

    }
    @Override
    protected void onPreExecute(){
        progress = new ProgressDialog(context);
        progress.setMessage("Garregando ...");
        progress.show();
       // persistenceDao.onDrop(persistenceDao.openDB());
        persistenceDao.onCreate(persistenceDao.openDB());// Cria a TB_CONFIG_CALENDAR
        // ESSE PROCESSO SÓ DEVE SER EXECUTADO UMA VEZ ---- OU Em Atualizações
        //Processa o arquivo properties
        if(!persistenceDao.isTBContemRegistro(persistenceDao.openDB(), persistenceDao.TB_CONFIG_CALENDAR)){
            for(Calendario calendarios : assetsPropertyReader.processaCalendariosProperties()){
                //Salva os dados do Properties na TB_CONFIG_CALENDAR
                persistenceDao.salvaConfiguracaoCalendario(calendarios);
                Log.i("DEBUG", "PERSISTINDO TABELAS DE CALENDARIOS");
            }
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            Log.i("DEBUG", "Iniciado");
            InputStream input = null;
            if(isOnline()) {
                publishProgress("Abrindo Connecxao");
                Log.i("DEBUG", "Abrindo Connecxao");
                calendarios = persistenceDao.recuperaTodasConfiguracoesCalendar();
                for(Calendario calendario: calendarios) {
                    URL url = new URL(calendario.getUrl());
                    URLConnection conec = url.openConnection();
                    input = conec.getInputStream();
                    Log.i("DEBUG", "Iniciado Gravacao");
                    persistenceDao.onDropTabelaDeCalandario(persistenceDao.openDB(), calendario);
                    persistenceDao.onCreateTabelaDeCalandario(persistenceDao.openDB(), calendario);
                    List<Evento> eventoList = getCalendarEventos(input);
                    for (Evento evento : eventoList) {
                        persistenceDao.salvaNovoEvento(evento,calendario);
                    }
                }
            }else{
                publishProgress("Sem Connecxao");
                Log.i("DEBUG", "Sem Connecxao");
                //input = context.getAssets().open("agendarcc.ics");
               // persistenceDao.onCreate(persistenceDao.openDB());
            }

            Log.i("DEBUG", "Dados Gravados");
        } catch (IOException e) {
            Log.i("DEBUG","Erro"+e);
            publishProgress("Erro ao Gravar dados"+e);
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Object... params){

    }
    @Override
    protected void onPostExecute(String params){
        progress.setMessage("Base atualizada !");
        progress.dismiss();
    }

    private List<Evento> getCalendarEventos(InputStream is){
        List<Evento> eventoList = null;
        try {
            contruorical = new ConstrutorIcal(is);
            eventoList= contruorical.getEventos();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventoList;
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}