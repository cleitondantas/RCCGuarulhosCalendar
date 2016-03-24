package br.com.v8developmentstudio.rccguarulhos.modelo;

/**
 * Created by cleiton.dantas on 24/03/2016.
 */
public class Calendario {
        private Integer id;
        private String nomeCalendario;
        private String nomeLabel;
        private String url;
        private Boolean alarme;


    public String getNomeLabel() {
        return nomeLabel;
    }

    public void setNomeLabel(String nomeLabel) {
        this.nomeLabel = nomeLabel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCalendario() {
        return nomeCalendario;
    }

    public void setNomeCalendario(String nomeCalendario) {
        this.nomeCalendario = nomeCalendario;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getAlarme() {
        return alarme;
    }

    public void setAlarme(Boolean alarme) {
        this.alarme = alarme;
    }
}
