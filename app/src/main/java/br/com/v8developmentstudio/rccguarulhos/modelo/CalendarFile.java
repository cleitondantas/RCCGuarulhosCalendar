package br.com.v8developmentstudio.rccguarulhos.modelo;

import java.io.File;

/**
 * Created by cleiton.dantas on 22/08/2016.
 */
public class CalendarFile {
    private String url;
    private File fileCalendarICS;

    public CalendarFile(String url,File fileCalendarICS){
        this.fileCalendarICS = fileCalendarICS;
        this.url = url;
    }

    public File getFileCalendarICS() {
        return fileCalendarICS;
    }

    public void setFileCalendarICS(File fileCalendarICS) {
        this.fileCalendarICS = fileCalendarICS;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarFile that = (CalendarFile) o;

        if (getUrl() != null ? !getUrl().equalsIgnoreCase(that.getUrl()) : that.getUrl() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getUrl() != null ? getUrl().hashCode() : 0;
    }
}
