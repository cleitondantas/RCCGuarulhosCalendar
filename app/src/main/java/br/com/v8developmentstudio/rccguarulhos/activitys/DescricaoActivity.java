package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.adapter.ScaleImageView;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.modelo.EventoFavorito;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.services.CalendarEventService;
import br.com.v8developmentstudio.rccguarulhos.task.DownloadImagesTask;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

import android.widget.Toast;

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
    private Toolbar toolbar;
    private ActivityServices ac = new ActivityServicesImpl();
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private CalendarEventService calendarEventService;
    private FloatingActionButton fabMenu, fabShare,fabAddCalendar;
    private TextView textViewSumario,textViewDescricao,textViewDataHoraInicio,textViewDataHoraFim,textViewLocal;
    private ScaleImageView thumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descricao_cards_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int id = getIntent().getIntExtra(Constantes.ID, 1);
        activityHistory = getIntent().getIntExtra(Constantes.ACTIVITYHISTOTY, 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarEventService = new CalendarEventService(this);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT); // transperent color = #00000000

        textViewSumario = (TextView) findViewById(R.id.idsumario);
        textViewDescricao = (TextView) findViewById(R.id.idDescricao);
        textViewDataHoraInicio = (TextView) findViewById(R.id.idDataHoraInicio);
        textViewDataHoraFim = (TextView) findViewById(R.id.idDataHoraFim);
        textViewLocal = (TextView) findViewById(R.id.idLocal);
        thumbnail = (ScaleImageView) findViewById(R.id.thumbnail);

        final Animation animeFloating = AnimationUtils.loadAnimation(this, R.animator.rotate);
        final Animation animeFloating2 = AnimationUtils.loadAnimation(this, R.animator.rotate2);
        fabMenu = (FloatingActionButton) findViewById(R.id.idFabMenu);
        fabShare = (FloatingActionButton) findViewById(R.id.idFabShare);
        fabAddCalendar = (FloatingActionButton) findViewById(R.id.idFabAddCalendar);
        fabShare.hide();
        fabAddCalendar.hide();
        fabMenu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(View.GONE == fabShare.getVisibility()) {
                   fabMenu.startAnimation(animeFloating);
                   fabShare.show();
                   fabAddCalendar.show();
               }else{
                   fabMenu.startAnimation(animeFloating2);
                   fabShare.hide();
                   fabAddCalendar.hide();
               }
           }
       });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = prepereShare(evento);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,evento.getSumario());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent,"RCC Share"));
            }
        });

        fabAddCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventoLocalCalendar();
            }
        });

        evento = getEventoDao(id);
        calendario = persistenceDao.recuperaConfigCalendarPorID(evento.getIdCalendario());
        Display disply = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        disply.getSize(size);

        if (evento.getUri() != null) {
            thumbnail.setTag(evento.getUri());
            Object[] obj = {thumbnail, evento.getUid()};
            new DownloadImagesTask().execute(obj);
            int width = size.x;
            int height = size.y/2;
            thumbnail.setMaxHeight(height);
            thumbnail.setMinimumHeight(height);
            thumbnail.setMaxWidth(width);
            thumbnail.setMinimumWidth(width);
            appBarLayout.setExpanded(true);
        }else{
            int width = size.x/4;
            int height = size.y/4;
            thumbnail.setMaxHeight(height);
            thumbnail.setMinimumHeight(height);
            thumbnail.setMaxWidth(width);
            thumbnail.setMinimumWidth(width);
            appBarLayout.setExpanded(false);
            appBarLayout.setActivated(false);

        }

        textViewSumario.setText(evento.getSumario());
        textViewDescricao.setText(evento.getDescricao());
        textViewDataHoraInicio.setText(dateFormat.format(evento.getDataHoraInicio()));
        textViewDataHoraFim.setText(dateFormat.format(evento.getDataHoraFim()));
        textViewLocal.setText(evento.getLocal());
        eventoFavoritos = persistenceDao.recuperaFavoritoPorUID(evento.getUid());

    }

    private void addEventoLocalCalendar(){
        calendarEventService.addEventoAoCalendarioLocal(evento);
        Toast.makeText(this,"EVENTO ADD NO CALENDARIO LOCAL", Toast.LENGTH_SHORT).show();

    }

    private String prepereShare(Evento evento){
       final SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String msg ="";

        if(evento.getSumario()!=null){
            msg+=evento.getSumario()+"\n \n";
        }
        if(evento.getDescricao()!=null){
            msg+=evento.getDescricao()+"\n";
        }
        if(evento.getLocal()!=null){
            msg+="LOCAL: "+evento.getLocal()+"\n";
        }
        if(evento.getDataHoraInicio()!=null){
            msg+="INICIO: "+dateFormat2.format(evento.getDataHoraInicio())+"\n";
        }
        if(evento.getDataHoraFim()!=null){
            msg+="FIM: "+dateFormat2.format(evento.getDataHoraFim());
        }
        return msg;
    }

    private Evento getEventoDao(int id) {
        return persistenceDao.recuperaEventoPorID(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem someMenuItem = menu.findItem(R.id.action_star);
        if (eventoFavoritos.size() > 0) {
            someMenuItem.setIcon(android.R.drawable.btn_star_big_on);
        } else {
            someMenuItem.setIcon(android.R.drawable.btn_star_big_off);
        }
        return super.onPrepareOptionsMenu(menu);
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
        switch (activityHistory) {
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


    private void pressFavorito(MenuItem item) {
        eventoFavoritos = persistenceDao.recuperaFavoritoPorUID(evento.getUid());
        if (eventoFavoritos != null && eventoFavoritos.size() > 0) {
            persistenceDao.deletaEventoFavoritoPorUID(evento.getUid());
            item.setIcon(android.R.drawable.btn_star_big_off);
            Log.i("DEBUG", "CHECK FALSE");
        } else {
            persistenceDao.salvaEventoFavorito(evento, calendario, false);
            item.setIcon(android.R.drawable.btn_star_big_on);
            Log.i("DEBUG", "CHECK TRUE");
        }

    }

}
