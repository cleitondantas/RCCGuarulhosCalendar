package br.com.v8developmentstudio.rccguarulhos.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cleiton.dantas on 20/04/2016.
 */
public class DownloadImagesTask extends AsyncTask<Object, Void, Bitmap> {
    ImageView imageView = null;
    String pathTemp = System.getProperty("java.io.tmpdir");
    String nomeFile ="";
    @Override
    protected Bitmap doInBackground(Object... imageViews) {
        this.imageView = (ImageView)imageViews[0];
        nomeFile = (String) imageViews[1]+".jpg";
        return download_Image((String)imageView.getTag());
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
    Bitmap bmp =null;
    private Bitmap download_Image(String url) {
        try{
            File fileImag = new File(pathTemp+"/"+nomeFile);
            InputStream is=null;
            if(fileImag.exists()){
                is = new FileInputStream(fileImag);
            }else{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                is = con.getInputStream();
            }
            bmp = BitmapFactory.decodeStream(is);
            if(!fileImag.exists()){
                criaArquivo(is,nomeFile);
            }

            if (null != bmp)
                return bmp;

        }catch(Exception e){}
        return bmp;
    }

    public File criaArquivo(InputStream is,String nomeFile){
        FileOutputStream fs = null;
        File file = null;
        Bitmap bitmap= null;
        try {
            file = new File(pathTemp,nomeFile);
            if(!file.exists()){
                file.createNewFile();
            }
            fs = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fs);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                fs.write(bytes, 0, read);
            }
        } catch (Exception e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                    fs.flush();
                    fs.close();
                } catch (IOException e) {

                }
            }
        }
        return file;
    }
}
