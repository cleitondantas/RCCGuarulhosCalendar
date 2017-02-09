package br.com.v8developmentstudio.rccguarulhos.calendar.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;

/**
 * Created by cleiton.dantas on 09/02/2017.
 */

public class ActivityHub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descricao_cards_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



    }

    public void startFacebook(String facebookUrl) {
        try {
            Uri uri = null;
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if (versionCode >= 3002850) {
                facebookUrl = facebookUrl.toLowerCase().replace("www.", "m.");
                if (!facebookUrl.startsWith("https")) {
                    facebookUrl = "https://" + facebookUrl;
                }
                uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
            } else {
                String pageID = facebookUrl.substring(facebookUrl.lastIndexOf("/"));
                uri = Uri.parse("fb://page" + pageID);
            }
            Log.d("FACE", "startFacebook: uri = " + uri.toString());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Throwable e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }


}
