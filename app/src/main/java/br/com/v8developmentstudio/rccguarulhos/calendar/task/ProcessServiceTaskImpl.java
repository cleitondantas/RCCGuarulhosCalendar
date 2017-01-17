package br.com.v8developmentstudio.rccguarulhos.calendar.task;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.bo.ConstrutorIcal;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.CalendariosDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.CalendarFile;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.Preferences;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.AssetsPropertyReader;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.FileUtil;

/**
 * Created by cleiton.dantas on 04/05/2016.
 */
public class ProcessServiceTaskImpl implements ProcessServiceTask {

    private PersistenceDao persistenceDao;
    private CalendariosDao calendariosDao;
    private List<Calendario> calendarioList;
    private List<Calendario> calendariosAssets;
    private AssetsPropertyReader assetsPropertyReader;
    private Preferences preferences;
    private File inFile;
    private List<CalendarFile> calendarFiles;
    private FileUtil fileUtil = new FileUtil();
    private Context context;

    public ProcessServiceTaskImpl(Context context) {
        this.context = context;
        persistenceDao = PersistenceDao.getInstance(context);
        calendariosDao = new CalendariosDao(context);
        assetsPropertyReader = new AssetsPropertyReader(context);
        preferences = new Preferences(context);
        calendarFiles = new ArrayList<>();
    }

    @Override
    public void preProcess() {
        calendariosAssets = assetsPropertyReader.processaCalendariosProperties();
    }
    @Override
    public void runProcessBackgrund() {
            try {
                InputStream input = null;
                Log.i("DEBUG", "Abrindo Connecxao");
                for (Calendario item : calendariosAssets) {
                    URL url = new URL(item.getUrl());
                    URLConnection conec = url.openConnection();
                    input = conec.getInputStream();
                    inFile = fileUtil.criaArquivo(input, System.currentTimeMillis() + ".ical");
                    calendarFiles.add(new CalendarFile(item.getUrl(), inFile));
                }
            } catch (IOException e) {
                Log.e("ERROR", "Erro" + e);
                e.printStackTrace();
            }
            if (calendariosAssets.size() == calendarFiles.size()) {
                persistenceDao.onDropTabelaEventos(persistenceDao.openDB());
                persistenceDao.onCreate(persistenceDao.openDB());
            }
            calendarioList = persistenceDao.recuperaTodasConfiguracoesCalendar(persistenceDao.openDB(context));

        //Salva os dados do Properties na TB_CONFIG_CALENDAR
        for (Calendario calendarios : calendariosDao.verificaListaCalendarios(calendarioList, calendariosAssets)) {
            persistenceDao.salvaConfiguracaoCalendario(calendarios, persistenceDao.openDB());
            Log.i("DEBUG", "PERSISTINDO TABELAS DE CALENDARIOS");
        }
        try {
            Log.i("DEBUG", "Iniciado");
            for (Calendario calendario : persistenceDao.recuperaTodasConfiguracoesCalendar(persistenceDao.openDB(context))) {
                for (CalendarFile filecar: calendarFiles) {
                    if(calendario.getUrl().equalsIgnoreCase(filecar.getUrl())) {
                        Log.i("DEBUG", "Iniciado Gravacao");
                        final List<Evento> eventoList = new ConstrutorIcal(new FileInputStream(filecar.getFileCalendarICS())).getEventos();
                        for (final Evento evento : eventoList) {
                            persistenceDao.updateEvents(evento, calendario, persistenceDao.openDB(context));
                        }
                        filecar.getFileCalendarICS().deleteOnExit();
                    }
                }
            }
        } catch (IOException e) {
            Log.e("ERRO", "runProcessBackgrund", e);
            e.printStackTrace();
        }
    }

    @Override
    public void posProcess() {
        preferences.salvarPrefTimeAtulizacao(System.currentTimeMillis());
        Toast toast = Toast.makeText(this.context, "REALIZADO ATUALIZAÇÃO!", Toast.LENGTH_LONG);
        toast.show();
    }
}
