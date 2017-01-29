package br.com.v8developmentstudio.rccguarulhos.calendar.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cleiton.dantas on 16/03/2016.
 */
public class Evento implements Serializable{

    private Integer id;
    private String uid;
    private Integer idCalendario;
    private Date dataHoraInicio;
    private Date dataHoraFim;
    private Date dataHoraModifcado;
    private Date dataHoraCriado;
    private String local;
    private String sumario;
    private String descricao;
    private String uri;
    private String urlImg;
    private Boolean alarme;
    private List<Date> datasEntreInicioEoFim;

    public Integer getIdCalendario() {
        return idCalendario;
    }
    public void setIdCalendario(Integer idCalendario) {
        this.idCalendario = idCalendario;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public void setDataHoraFim(Date dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSumario() {
        return sumario;
    }

    public void setSumario(String sumario) {
        this.sumario = sumario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDataHoraModifcado() {
        return dataHoraModifcado;
    }

    public void setDataHoraModifcado(Date dataHoraModifcado) {this.dataHoraModifcado = dataHoraModifcado;}

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getDataHoraCriado() {
        return dataHoraCriado;
    }

    public void setDataHoraCriado(Date dataHoraCriado) {
        this.dataHoraCriado = dataHoraCriado;
    }

    public Boolean getAlarme() {return alarme;}

    public void setAlarme(Boolean alarme) {this.alarme = alarme;}

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public List<Date> getDatasEntreInicioEoFim() {
        return retornaIntervalosDeData(getDataHoraInicio(),getDataHoraFim());
    }

    private List<Date> retornaIntervalosDeData(Date datainicio, Date dataFim){
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
