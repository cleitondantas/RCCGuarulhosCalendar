package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.modelo.EventoFavorito;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

/**
 * Created by cleiton.dantas on 18/03/2016.
 */
public class DescricaoActivity extends AppCompatActivity {

    private PersistenceDao persistenceDao = new PersistenceDao(this);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
    private Evento evento;
    private Calendario calendario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descricao_cards_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        int id  = getIntent().getIntExtra(Constantes.ID,1);
        int idCalendario = getIntent().getIntExtra(Constantes.CALENDARIO,1); //savedInstanceState.getString(Constantes.CALENDARIO);

        TextView textViewSumario = (TextView)findViewById(R.id.idsumario);
        TextView textViewDescricao = (TextView)findViewById(R.id.idDescricao);
        TextView textViewDataHoraInicio = (TextView)findViewById(R.id.idDataHoraInicio);
        TextView textViewDataHoraFim = (TextView)findViewById(R.id.idDataHoraFim);
        TextView textViewLocal = (TextView)findViewById(R.id.idLocal);

        evento = getEventoDao(id,idCalendario);
        textViewSumario.setText(evento.getSumario());
        textViewDescricao.setText(evento.getDescricao());
        textViewDataHoraInicio.setText(dateFormat.format(evento.getDataHoraInicio()));
        textViewDataHoraFim.setText(dateFormat.format(evento.getDataHoraFim()));
        textViewLocal.setText(evento.getLocal());
    }

    private Evento getEventoDao(int id,int idCalendario){
        calendario = persistenceDao.recuperaConfigCalendarPorID(idCalendario);
        return  persistenceDao.recuperaEventoPorID(id,calendario.getNomeCalendario());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_star:
                pressFavorito(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pressFavorito(MenuItem item){
        List<EventoFavorito> eventoFavoritos =  persistenceDao.recuperaFavoritoPorUID(evento.getUid());
        if(eventoFavoritos!=null && eventoFavoritos.size()>0){
            persistenceDao.deletaEventoFavoritoPorUID(evento.getUid());
            item.setChecked(false);
            Log.i("DEBUG","CHECK FALSE");
        }else{
            persistenceDao.salvaEventoFavorito(evento,calendario,false);
            item.setChecked(true);
            Log.i("DEBUG", "CHECK TRUE");
        }

    }
}
