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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Calendario that = (Calendario) o;

        if (!getUrl().equals(that.getUrl())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getUrl().hashCode();
    }
}
