package br.com.v8developmentstudio.rccguarulhos.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;

/**
 * Created by cleiton.dantas on 31/03/2016.
 */
public class FiltroDatas {

    public List<Evento> filtraEventosPorDataAtual(List<Evento> eventos){
        List<Evento> eventoList = new ArrayList<Evento>();
        Date hoje = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(hoje);
        c.add(Calendar.DATE, -1);
        hoje = c.getTime();
        for(Evento evento :eventos){
            if(evento.getDataHoraInicio().after(hoje)  ){
                eventoList.add(evento);
            }
        }
        return eventoList;
    }

}
