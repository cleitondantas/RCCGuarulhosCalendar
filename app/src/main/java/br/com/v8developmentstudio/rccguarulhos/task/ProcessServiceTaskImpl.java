package br.com.v8developmentstudio.rccguarulhos.task;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.bo.ConstrutorIcal;
import br.com.v8developmentstudio.rccguarulhos.dao.CalendariosDao;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.services.Preferences;
import br.com.v8developmentstudio.rccguarulhos.util.AssetsPropertyReader;
import br.com.v8developmentstudio.rccguarulhos.util.FileUtil;

/**
 * Created by cleiton.dantas on 04/05/2016.
 */
public class ProcessServiceTaskImpl implements ProcessServiceTask{

    private PersistenceDao persistenceDao;
    private CalendariosDao calendariosDao;
    private List<Calendario> calendarioList;
    private List<Calendario> calendariosAssets;
    private AssetsPropertyReader assetsPropertyReader;
    private Preferences preferences;
    private File inFile;
    private FileUtil fileUtil = new FileUtil();

    public ProcessServiceTaskImpl(Context context) {
        persistenceDao = new PersistenceDao(context);
        calendariosDao = new CalendariosDao(context);
        assetsPropertyReader = new AssetsPropertyReader(context);
        preferences = new Preferences(context);
    }

    @Override
    public void preProcess() {
        persistenceDao.onDropTabelaEventos(persistenceDao.openDB());
        persistenceDao.onCreate(persistenceDao.openDB());
        calendarioList = persistenceDao.recuperaTodasConfiguracoesCalendar();
        calendariosAssets = assetsPropertyReader.processaCalendariosProperties();
    }

    @Override
    public void runProcessBackgrund() {
        //Salva os dados do Properties na TB_CONFIG_CALENDAR
        for (Calendario calendarios : calendariosDao.verificaListaCalendarios(calendarioList,calendariosAssets) ) {
            persistenceDao.salvaConfiguracaoCalendario(calendarios);
            Log.i("DEBUG", "PERSISTINDO TABELAS DE CALENDARIOS");
        }

        try {
            Log.i("DEBUG", "Iniciado");
            InputStream input = null;
            Log.i("DEBUG", "Abrindo Connecxao");
            for (Calendario calendario : persistenceDao.recuperaTodasConfiguracoesCalendar()) {
                URL url = new URL(calendario.getUrl());
                URLConnection conec = url.openConnection();
                Log.i("DEBUG", "URL CALENDAR : -->" + calendario.getUrl());
                input = conec.getInputStream();
                //Depois de Baixar o aquivo é gravado um file temporario
                inFile = fileUtil.criaArquivo(input,System.currentTimeMillis()+".ical");
                Log.i("DEBUG", "Iniciado Gravacao");
                final List<Evento> eventoList = new ConstrutorIcal(fileUtil.recuperaArquivos(inFile.getPath())).getEventos();
                for (final Evento evento : eventoList) {
                    persistenceDao.updateEvents(evento, calendario);
                }
                inFile.deleteOnExit();
            }
            Log.i("DEBUG", "Dados Gravados");
        } catch (IOException e) {
            Log.e("ERROR", "Erro" + e);
            e.printStackTrace();
        }
    }

    @Override
    public void posProcess() {
        preferences.salvarPrefTimeAtulizacao(System.currentTimeMillis());
    }
}
