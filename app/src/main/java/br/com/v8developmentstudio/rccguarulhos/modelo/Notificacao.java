package br.com.v8developmentstudio.rccguarulhos.modelo;

/**
 * Created by cleiton.dantas on 01/04/2016.
 */
public class Notificacao {


    private String tituloTicker;
    private String titulo;
    private String texto;
    private int drawableSmallIcon;
    private int drawableLargeIcon;

    public String getTituloTicker() {
        return tituloTicker;
    }

    public void setTituloTicker(String tituloTicker) {
        this.tituloTicker = tituloTicker;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getDrawableSmallIcon() {
        return drawableSmallIcon;
    }

    public void setDrawableSmallIcon(int drawableSmallIcon) {
        this.drawableSmallIcon = drawableSmallIcon;
    }

    public int getDrawableLargeIcon() {
        return drawableLargeIcon;
    }

    public void setDrawableLargeIcon(int drawableLargeIcon) {
        this.drawableLargeIcon = drawableLargeIcon;
    }
}
