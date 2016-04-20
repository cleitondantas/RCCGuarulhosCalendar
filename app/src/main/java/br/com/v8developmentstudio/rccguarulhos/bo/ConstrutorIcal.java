package br.com.v8developmentstudio.rccguarulhos.bo;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Attachment;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.util.FiltroDatas;

/**
 * Created by cleiton.dantas on 15/03/2016.
 */
public class ConstrutorIcal {
    private List<ICalendar> icals;
    private FiltroDatas filtroDatas = new FiltroDatas();
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

            String descriccao = event.getDescription().getValue();
            if(descriccao.contains("<img>") && descriccao.contains("</img>")){
                String uri =  descriccao.substring(descriccao.indexOf("<img>") + 5, descriccao.indexOf("</img>", descriccao.indexOf("<img>")));
                if(!uri.isEmpty()){
                    evento.setUri(uri);
                    descriccao = descriccao.replace("<img>"+uri+"</img>","");
                }
            }

            evento.setDescricao(descriccao);
            evento.setUid(event.getUid().getValue());
            evento.setDataHoraCriado(event.getCreated().getValue());
            evento.setDataHoraInicio(new Date(event.getDateStart().getValue().getTime()));
            evento.setDataHoraFim(new Date(event.getDateEnd().getValue().getTime()));
            evento.setDataHoraModifcado(new Date(event.getLastModified().getValue().getTime()));
            evento.setSumario(event.getSummary().getValue());
            evento.setLocal(event.getLocation().getValue());

            /* Temporariamente Inutilizado Devido a forma do google montar a uri
            for(Attachment attachment :event.getAttachments()) {
                evento.setUri(attachment.getUri());
            }
            */
            eventoList.add(evento);
        }
    }
    filtroDatas.comparatorData(eventoList);

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

