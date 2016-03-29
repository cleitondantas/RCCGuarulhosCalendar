package br.com.v8developmentstudio.rccguarulhos.bo;

import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;

/**
 * Created by cleiton.dantas on 18/03/2016.
 */
public class Armazenador {

    // Declaração dos campos
    private TextView sumario = null;
    private TextView dataHoraInicio = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    // Construtor que instancia os campos
    public Armazenador(View linha, int position) {
        sumario = (TextView) linha.findViewById(R.id.sumario);
        dataHoraInicio = (TextView) linha.findViewById(R.id.datahora);
    }

    // Método para "jogar" os textos nos respectivos campos
    public void popularFormulario(Evento evento) {
        sumario.setText(evento.getSumario());
        dataHoraInicio.setText( dateFormat.format(evento.getDataHoraInicio()) );
    }
}


