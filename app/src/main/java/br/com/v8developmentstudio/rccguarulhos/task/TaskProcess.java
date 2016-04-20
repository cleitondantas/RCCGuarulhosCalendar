package br.com.v8developmentstudio.rccguarulhos.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import br.com.v8developmentstudio.rccguarulhos.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.bo.ConstrutorIcal;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.util.AssetsPropertyReader;
import br.com.v8developmentstudio.rccguarulhos.util.FileUtil;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class TaskProcess extends AsyncTask<String, Object, String> {
    private Context context;
    private ProgressDialog progress;
    private ConstrutorIcal construtorIcal;
    private PersistenceDao persistenceDao;
    private AssetsPropertyReader assetsPropertyReader;
    private List<Calendario> calendarios;
    private FileUtil fileUtil = new FileUtil();
    private File inFile;
    private ActivityServices ac = new ActivityServicesImpl();

    public TaskProcess(Context context) {
        this.context = context;
        persistenceDao = new PersistenceDao(context);
        assetsPropertyReader = new AssetsPropertyReader(context);

    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        postMensagem("Garregando...");
        persistenceDao.onDropTabelaEventos(persistenceDao.openDB());
        persistenceDao.onCreate(persistenceDao.openDB());
        for (Calendario calendarios : assetsPropertyReader.processaCalendariosProperties()) {
            //Salva os dados do Properties na TB_CONFIG_CALENDAR
            persistenceDao.salvaConfiguracaoCalendario(calendarios);
            Log.i("DEBUG", "PERSISTINDO TABELAS DE CALENDARIOS");
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            Log.i("DEBUG", "Iniciado");
            InputStream input = null;
                Log.i("DEBUG", "Abrindo Connecxao");
                calendarios = persistenceDao.recuperaTodasConfiguracoesCalendar();
                for (Calendario calendario : calendarios) {
                    URL url = new URL(calendario.getUrl());
                    URLConnection conec = url.openConnection();
                    Log.i("DEBUG", "URL CALENDAR : -->" + calendario.getUrl());
                    input = conec.getInputStream();
                    //Depois de Baixar o aquivo é gravado um file temporario
                    inFile = fileUtil.criaArquivo(input, ".ical");
                    Log.i("DEBUG", "Iniciado Gravacao");
                    final List<Evento> eventoList = getCalendarEventosICAL(fileUtil.recuperaArquivos(inFile.getPath()));
                    for (final Evento evento : eventoList) {
                        persistenceDao.updateEvents(evento, calendario);
                    }
                    inFile.deleteOnExit();
                }
            Log.i("DEBUG", "Dados Gravados");
        } catch (IOException e) {
            Log.e("ERROR", "Erro" + e);
            publishProgress("Erro ao Gravar dados" + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... params) {
        Log.i("DEBUG", "onProgressUpdate" + params);
        super.onProgressUpdate(params);
    }

    @Override
    protected void onPostExecute(String params) {
        progress.setMessage("Base atualizada !");
        progress.dismiss();
        ac.redirect(context, MainActivity.class, null);
    }

    private List<Evento> getCalendarEventosICAL(InputStream is) {
        List<Evento> eventoList = null;
        try {
            construtorIcal = new ConstrutorIcal(is);
            eventoList = construtorIcal.getEventos();
        } catch (IOException e) {
            Log.e("ERROR", "Erro" + e);
            e.printStackTrace();
        }
        return eventoList;
    }
    private void postMensagem(String mensagem){
        progress.setMessage(mensagem);
        progress.show();
    }
}

