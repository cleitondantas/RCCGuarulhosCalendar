package br.com.v8developmentstudio.rccguarulhos.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

/**
 * Created by cleiton.dantas on 20/04/2016.
 */
public class ImageDownloadAndSave extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... arg) {
        downloadImagesToSdCard(arg[0],arg[1]);
        return null;
    }

    private void downloadImagesToSdCard(String downloadUrl, String imageName) {
        try {

            URL url = new URL(downloadUrl);
                        /* making a directory in sdcard */
            String sdCard = Environment.getExternalStorageDirectory().toString()+"/"+ Constantes.IMG_PATH_TEMP+"/";
            File myDir = new File(sdCard,imageName+Constantes.JPG);

                        /*  if specified not exist create new */
            if (!myDir.exists()) {
                myDir.mkdir();
                Log.v("", "inside mkdir");
            }

                             /* Open a connection */
            URLConnection ucon = url.openConnection();
            InputStream inputStream = null;
            HttpURLConnection httpConn = (HttpURLConnection) ucon;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }

            FileOutputStream fos = new FileOutputStream(myDir);
            int totalSize = httpConn.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
            }

            fos.close();
            Log.d("test", "Image Saved in sdcard..");
        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}