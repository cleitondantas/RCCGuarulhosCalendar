package br.com.v8developmentstudio.rccguarulhos.calendar.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class TaskProcessBackground extends AsyncTask<String, Object, String> {
    private Context context;
    private ProcessServiceTask processServiceTask;

    public TaskProcessBackground(Context context) {
        this.context = context;
        processServiceTask = new ProcessServiceTaskImpl(context);
    }

    @Override
    protected void onPreExecute() {
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
        processServiceTask.posProcess();
    }



}
