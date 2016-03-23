package br.com.v8developmentstudio.rccguarulhos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;

/**
 * Created by cleiton.dantas on 18/03/2016.
 */
public class DescricaoActivity extends Activity{

    private PersistenceDao persistenceDao = new PersistenceDao(this);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descricao_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int id  = getIntent().getIntExtra(Constantes.ID,1);
        String tbcalendario =PersistenceDao.TB_CAL_DIOCESANO; //savedInstanceState.getString(Constantes.CALENDARIO);

        TextView textViewSumario = (TextView)findViewById(R.id.idsumario);
        TextView textViewDescricao = (TextView)findViewById(R.id.idDescricao);
        TextView textViewDataHoraInicio = (TextView)findViewById(R.id.idDataHoraInicio);
        TextView textViewDataHoraFim = (TextView)findViewById(R.id.idDataHoraFim);
        TextView textViewLocal = (TextView)findViewById(R.id.idLocal);

        Evento evento = getEventoDao(id,tbcalendario);
        textViewSumario.setText(evento.getSumario());
        textViewDescricao.setText(evento.getDescricao());
        textViewDataHoraInicio.setText(dateFormat.format(evento.getDataHoraInicio()));
        textViewDataHoraFim.setText(dateFormat.format(evento.getDataHoraFim()));
        textViewLocal.setText(evento.getLocal());
    }
    private Evento getEventoDao(int id,String tbcalendario){
        return  persistenceDao.recuperaEventoPorID(id,tbcalendario);
    }

}
