package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.modelo.EventoFavorito;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.task.DownloadImagesTask;
import br.com.v8developmentstudio.rccguarulhos.task.ImageDownloadAndSave;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

/**
 * Created by cleiton.dantas on 18/03/2016.
 */
public class DescricaoActivity extends AppCompatActivity {

    private PersistenceDao persistenceDao = new PersistenceDao(this);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
    private Evento evento;
    private Calendario calendario;
    private List<EventoFavorito> eventoFavoritos;
    private int activityHistory;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private  Toolbar toolbar;
    private ActivityServices ac = new ActivityServicesImpl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descricao_cards_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int id  = getIntent().getIntExtra(Constantes.ID,1);
        activityHistory =  getIntent().getIntExtra(Constantes.ACTIVITYHISTOTY,0);


        TextView textViewSumario = (TextView)findViewById(R.id.idsumario);
        TextView textViewDescricao = (TextView)findViewById(R.id.idDescricao);
        TextView textViewDataHoraInicio = (TextView)findViewById(R.id.idDataHoraInicio);
        TextView textViewDataHoraFim = (TextView)findViewById(R.id.idDataHoraFim);
        TextView textViewLocal = (TextView)findViewById(R.id.idLocal);
        ImageView thumbnail = (ImageView)findViewById(R.id.thumbnail);
        thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);

        evento = getEventoDao(id);
        calendario = persistenceDao.recuperaConfigCalendarPorID(evento.getIdCalendario());

        if(evento.getUri()!=null){
            thumbnail.setTag(evento.getUri());
            Object[] obj = {thumbnail,evento.getUid()};
            new DownloadImagesTask().execute(obj);
        }
        textViewSumario.setText(evento.getSumario());
        textViewDescricao.setText(evento.getDescricao());
        textViewDataHoraInicio.setText(dateFormat.format(evento.getDataHoraInicio()));
        textViewDataHoraFim.setText(dateFormat.format(evento.getDataHoraFim()));
        textViewLocal.setText(evento.getLocal());
        eventoFavoritos  =  persistenceDao.recuperaFavoritoPorUID(evento.getUid());

    }


    private Evento getEventoDao(int id){
        return  persistenceDao.recuperaEventoPorID(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem someMenuItem = menu.findItem(R.id.action_star);
        if(eventoFavoritos.size()>0){
            someMenuItem.setIcon(android.R.drawable.btn_star_big_on);
        }else{
            someMenuItem.setIcon(android.R.drawable.btn_star_big_off);
        }
    return  super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_star:
                pressFavorito(item);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        switch (activityHistory){
            case 0:
                ac.redirect(this, MainActivity.class, null);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case 1:
                ac.redirect(this, ListaEventosFavoritosActivity.class, null);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            break;
            case 2:
                Bundle dados = new Bundle();
                dados.putInt(Constantes.ID, calendario.getId());
                dados.putString(Constantes.CALENDARIO, calendario.getNomeLabel());
                ac.redirect(this, ListaEventosActivity.class, dados);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            break;

        }

    }
    private void pressFavorito(MenuItem item){
        eventoFavoritos  =  persistenceDao.recuperaFavoritoPorUID(evento.getUid());
        if(eventoFavoritos!=null && eventoFavoritos.size()>0){
            persistenceDao.deletaEventoFavoritoPorUID(evento.getUid());
            item.setIcon(android.R.drawable.btn_star_big_off);
            Log.i("DEBUG","CHECK FALSE");
        }else{
            persistenceDao.salvaEventoFavorito(evento, calendario, false);
            item.setIcon(android.R.drawable.btn_star_big_on);
            Log.i("DEBUG", "CHECK TRUE");
        }

    }

}
