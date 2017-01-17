package br.com.v8developmentstudio.rccguarulhos.calendar.modelo;

/**
 * Created by cleiton.dantas on 13/04/2016.
 */
public class EventoFavorito {
    private Integer id;
    private Integer idCalendario;
    private Integer idEvento;
    private String uid;
    private Boolean alarme;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCalendario() {
        return idCalendario;
    }

    public void setIdCalendario(Integer idCalendario) {
        this.idCalendario = idCalendario;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getAlarme() {
        return alarme;
    }

    public void setAlarme(Boolean alarme) {
        this.alarme = alarme;
    }
}
