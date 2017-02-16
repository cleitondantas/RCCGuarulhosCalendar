package br.com.v8developmentstudio.rccguarulhos.calendar.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cleiton.dantas on 13/12/2016.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseID";
    private Preferences preferences;
    @Override
    public void onTokenRefresh() {
        preferences = new Preferences(this);
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token:"+ refreshedToken);
        preferences.salvarTokenFirebase(refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
      //  sendRegistrationToServer(refreshedToken);
    }

    //Método que pode ser usado para registrar o token para criar um serviço de mensagem individual
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        //Enviar para servidor
        Log.e("TOKENID:", token); //NOT PRINT

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;

        String param = "dtoken="+token+"&dtype=Android";
        try {
            url = new URL("http://myurl.com/action.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(param);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            isr.close();
            reader.close();

        } catch (IOException e) {
            // Error
        }


    }
}
