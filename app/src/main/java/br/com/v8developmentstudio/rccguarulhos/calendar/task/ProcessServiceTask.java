package br.com.v8developmentstudio.rccguarulhos.calendar.task;

/**
 * Created by cleiton.dantas on 04/05/2016.
 */
public interface ProcessServiceTask {

    public void preProcess();

    public void runProcessBackgrund();

    public void posProcess();

}
