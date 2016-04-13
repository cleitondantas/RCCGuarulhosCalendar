package br.com.v8developmentstudio.rccguarulhos.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.ProviderException;

/**
 * Created by cleiton.dantas on 01/04/2016.
 */
public class FileUtil {




    /**
     * Metodo que cria um arquivo fisico apartir de um array de Bytes
     * @param is
     * @param sufix
     * @return
     */
    public File criaArquivo(InputStream is,String sufix){
        FileOutputStream fs = null;
        File file = null;
        try {
            file = File.createTempFile("temp",sufix);
            Log.i("DEBUG", "Iniciado" + file.getAbsolutePath());
            fs = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                fs.write(bytes, 0, read);
            }
            Log.i("DEBUG", "Arquivos Processados com sucesso");
        } catch (IOException e1) {
            Log.i("DEBUG", "Erro ao processar arquivo:" + e1);

        } catch (Exception e) {
            Log.i("DEBUG", "Erro ao processar arquivo:" + e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                    fs.flush();
                    fs.close();
                } catch (IOException e) {
                    Log.i("DEBUG", "Erro ao fechar arquivo!");
                }
            }
        }
        return file;
    }



    /**
     * Convert path de arquivo em InputStream
     * @param path
     * @return
     * @throws ProviderException
     */
    public InputStream recuperaArquivos(String path) throws ProviderException {
        File file = new File(path);
        InputStream stream = null;
        try {
            stream = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "Erro ao fechar arquivo!"+e);
            throw new ProviderException(e);
        }
        return stream;
    }


}
