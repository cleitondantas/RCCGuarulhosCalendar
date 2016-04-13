package br.com.v8developmentstudio.rccguarulhos.bo;


import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.parameter.ICalParameters;
import biweekly.property.Attachment;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;

/**
 * Created by cleiton.dantas on 15/03/2016.
 */
public class ConstrutorIcal {
    private List<ICalendar> icals;

    public ConstrutorIcal(InputStream in) throws IOException {
        icals = Biweekly.parse(converter(in)).all();
    }

public List<Evento> getEventos(){
    List<Evento> eventoList = new ArrayList<Evento>();

    for (ICalendar  ical : icals){
        List<VEvent> events =  ical.getEvents();
        Evento evento;
        for (VEvent event:events){
            evento = new Evento();
            for(Attachment attachment :event.getAttachments()) {
                evento.setUri(attachment.getUri());
            }
            evento.setUid(event.getUid().getValue());
            evento.setDataHoraInicio(new Date(event.getDateStart().getValue().getTime()));
            evento.setDataHoraFim(new Date(event.getDateEnd().getValue().getTime()));
            evento.setDataHoraModifcado(new Date(event.getLastModified().getValue().getTime()));
            evento.setSumario(event.getSummary().getValue());
            evento.setDescricao(event.getDescription().getValue());
            evento.setLocal(event.getLocation().getValue());
            eventoList.add(evento);
        }
    }
    Comparator<Evento> cmp = new Comparator<Evento>() {
        public int compare(Evento o1, Evento o2) {
            return o1.getDataHoraInicio().compareTo(o2.getDataHoraInicio());
        }
    };
    Collections.sort(eventoList, cmp);

    return eventoList;
}


    /**
     * Método que converte InputStream para File / Criando um arquivo temporario e passando o caminho
      * @param inputStream
     * @return
     */
public File converter(InputStream inputStream){
    OutputStream outputStream = null;
    File file =null;
    try {
        file= File.createTempFile("file",".ical");
            // write the inputStream to a FileOutputStream
            outputStream = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    return file;
    }

}

