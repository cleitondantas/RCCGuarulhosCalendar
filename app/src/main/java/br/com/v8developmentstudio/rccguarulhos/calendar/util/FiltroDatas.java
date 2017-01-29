package br.com.v8developmentstudio.rccguarulhos.calendar.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;

/**
 * Created by cleiton.dantas on 31/03/2016.
 */
public class FiltroDatas {

    /**
     * Fiutro de datas (DE DATA ATUAL COM -1 ) PRA FRENTE
     * @param eventos
     * @return
     */
    public List<Evento> filtraEventosPorDataAtual(List<Evento> eventos){
        List<Evento> eventoList = new ArrayList<Evento>();
        Date hoje = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(hoje);
        c.add(Calendar.DATE, -1);
        hoje = c.getTime();
        for(Evento evento :eventos){
            if(evento.getDataHoraFim().after(hoje)){
                eventoList.add(evento);
            }
        }
        comparatorData(eventoList);
        return eventoList;
    }

    /**
     * Verificador de Datas iguais
     * @param primera
     * @param segunda
     * @return
     */
    public boolean verificaSeDatasSaoIguais(Date primera, Date segunda){
        boolean data;
        if (primera.before(segunda)){
            return false;
        } else if (primera.after(segunda)) {
            return false;
        }else{
            return true;
        }
    }

    /**
     * Reordena a lista de eventos
     * @param eventoList
     */
    public void comparatorData(List<Evento> eventoList){
        Comparator<Evento> cmp = new Comparator<Evento>() {
            public int compare(Evento o1, Evento o2) {
                return o1.getDataHoraInicio().compareTo(o2.getDataHoraInicio());
            }
        };
        Collections.sort(eventoList, cmp);
    }

    /**
     * Fiutro de datas (DE DATA ATUAL COM +1 ) PRA FRENTE
     * @return
     */
    public boolean verificaDataUltimaAtualizacao(Date datarecuperada){
        Date hoje = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(hoje);
        c.add(Calendar.DATE, -1);
      //  c.add(Calendar.MINUTE, -20);
        hoje = c.getTime();
            if(datarecuperada.before(hoje)){
                return true;
            }
        return false;
    }

    public List<Date> retornaIntervalosDeData(Date datainicio, Date dataFim){
        List<Date> list = new ArrayList<>();
        Calendar inicio  = Calendar.getInstance();
        inicio.setTime(datainicio);
        Calendar fim =  Calendar.getInstance();
        fim.setTime(dataFim);
        for (Calendar c = (Calendar) inicio.clone(); c.compareTo (fim) <= 0; c.add (Calendar.DATE, +1)) {
             list.add(c.getTime());
        }
        return list;
    }

}
