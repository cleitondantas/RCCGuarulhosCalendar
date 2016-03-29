package br.com.v8developmentstudio.rccguarulhos.util;

/**
 * Created by cleiton.dantas on 24/03/2016.
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;

public class AssetsPropertyReader {
    private Context context;
    private Properties properties;

    public AssetsPropertyReader(Context context) {
        this.context = context;
        /**
         * Constructs a new Properties object.
         */
        properties = new Properties();
    }
    public Properties getProperties(String FileName) {

        try {
            /**
             * getAssets() Return an AssetManager instance for your
             * application's package. AssetManager Provides access to an
             * application's raw asset files;
             */
            AssetManager assetManager = context.getAssets();
            /**
             * Open an asset using ACCESS_STREAMING mode. This
             */
            InputStream inputStream = assetManager.open(FileName);
            /**
             * Loads properties from the specified InputStream,
             */
            properties.load(inputStream);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("AssetsPropertyReader",e.toString());
        }
        return properties;

    }


    public List<Calendario> processaCalendariosProperties(){
        List<Calendario> calendarios = new ArrayList<Calendario>();
        Calendario calendario;
        properties =  getProperties("config.properties");
        int i =0;
        for(Object obj: properties.keySet()) {
            calendario  = new Calendario();
            calendario.setNomeCalendario(properties.getProperty("TB.CALENDARIO.NOME_TB." + i)) ;
            calendario.setNomeLabel(properties.getProperty("TB.CALENDARIO.NOME_LABEL." + i));
            calendario.setUrl(properties.getProperty("TB.CALENDARIO.URL."+i));
            i++;
            calendarios.add(calendario);
        }

        return calendarios;
    }
}