package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServicesImpl;

public class WebViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView wv;
    private ActivityServices ac = new ActivityServicesImpl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        wv = (WebView) findViewById(R.id.idwebview);
       final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressweb);

         wv.loadUrl("https://www.google.com.br/");
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(false);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);

                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                wv.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });


    }
    @Override
    public void onBackPressed() {
        if(wv.canGoBack()){
            wv.goBack();
        }else{
            ac.redirect(this, MainActivity.class, null);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }

    }

}
