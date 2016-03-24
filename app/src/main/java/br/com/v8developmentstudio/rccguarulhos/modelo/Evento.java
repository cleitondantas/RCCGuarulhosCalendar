package br.com.v8developmentstudio.rccguarulhos.modelo;

import java.util.Date;

/**
 * Created by cleiton.dantas on 16/03/2016.
 */
public class Evento {
    private Integer id;
    private Integer idCalendario;
    private Date dataHoraInicio;
    private Date dataHoraFim;
    private String local;
    private String sumario;
    private String descricao;


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


}
