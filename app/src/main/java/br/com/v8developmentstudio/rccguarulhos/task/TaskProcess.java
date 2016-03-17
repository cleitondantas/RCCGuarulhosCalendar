package br.com.v8developmentstudio.rccguarulhos.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.bo.ConstrutorIcal;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class TaskProcess extends AsyncTask<String,Object,String> {
    private Context context;
    private ProgressDialog progress;
    private ConstrutorIcal contruorical;
    private PersistenceDao persistenceDao;
    public TaskProcess(Context context) {
        this.context = context;
        persistenceDao = new PersistenceDao(context);
        persistenceDao.onCreate(persistenceDao.openDB());
    }
    @Override
    protected void onPreExecute(){
        progress = new ProgressDialog(context);
        progress.setMessage("Garregando ...");
        progress.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            Log.i("DEBUG", "Iniciado");
            URL url = new URL("http://www.grupoeternaalianca.com/arquivos/basedb.sql");
            publishProgress("Abrindo Connecxao");
           // URLConnection conec = url.openConnection();
           // InputStream input  = conec.getInputStream();
            Log.i("DEBUG", "Iniciado Gravacao");
           List<Evento> eventoList = getCalendarEventos();
            for(Evento evento :eventoList){
                persistenceDao.salvaNovoEvento(evento);
            }
            Log.i("DEBUG","Dados Gravados");
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

    private List<Evento> getCalendarEventos(){
        List<Evento> eventoList = null;
        try {
            InputStream is = context.getAssets().open("agendarcc.ics");
            contruorical = new ConstrutorIcal(is);
            eventoList= contruorical.getEventos();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventoList;
    }

}