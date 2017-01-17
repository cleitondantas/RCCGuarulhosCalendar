package br.com.v8developmentstudio.rccguarulhos.calendar.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import br.com.v8developmentstudio.rccguarulhos.calendar.activitys.MainActivity;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServicesImpl;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class TaskProcess extends AsyncTask<String, Object, String> {
    private Context context;
    private ProgressDialog progress;
    private ProcessServiceTask processServiceTask;
    private ActivityServices ac = new ActivityServicesImpl();

    public TaskProcess(Context context) {
        this.context = context;
        processServiceTask = new ProcessServiceTaskImpl(context);
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        postMensagem("Carregando...");
        processServiceTask.preProcess();
    }

    @Override
    protected String doInBackground(String... urls) {
        processServiceTask.runProcessBackgrund();
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... params) {
        Log.i("DEBUG", "onProgressUpdate" + params);
        super.onProgressUpdate(params);
    }

    @Override
    protected void onPostExecute(String params) {
        progress.setMessage("Base atualizada !");
        progress.dismiss();
        processServiceTask.posProcess();
        ac.redirect(context, MainActivity.class, null);
    }


    private void postMensagem(String mensagem){
        progress.setMessage(mensagem);
        progress.show();
    }


}
