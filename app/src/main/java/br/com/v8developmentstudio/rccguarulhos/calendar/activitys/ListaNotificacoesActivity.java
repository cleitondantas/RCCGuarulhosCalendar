package br.com.v8developmentstudio.rccguarulhos.calendar.activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.adapter.MyRecyclerViewAdapterListNotifications;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;

/**
 * Created by cleiton.dantas on 01/02/2017.
 */

public class ListaNotificacoesActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetector;
    List<Notificacao> notificaoes;
    private PersistenceDao persistenceDao = PersistenceDao.getInstance(this);
    private MyRecyclerViewAdapterListNotifications myRecyclerViewAdapterListNotifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notificaoes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.lista_notificaoes_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.lista_notificaoes_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gestureDetector = new GestureDetectorCompat(this, new ListaNotificacoesActivity.RecyclerViewOnGestureListener());


        //notificaoes = persistenceDao.recuperaTodasNotificaoes(persistenceDao.openDB(this));
        notificaoes = new ArrayList<>();
        Notificacao notif = new Notificacao();
        notif.setAtivo(true);
        notif.setTitulo("NOVO");
        notif.setTexto("DESCRICAO");
        notif.setTituloTicker("TICKET");
        notif.setKey("URL");
        notif.setValue("http://www.rccguarulhos.com.br");
        notificaoes.add(notif);

        myRecyclerViewAdapterListNotifications = new MyRecyclerViewAdapterListNotifications(notificaoes,this);
        recyclerView.setAdapter(myRecyclerViewAdapterListNotifications);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onClick(View view) {

    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            int idx = recyclerView.getChildAdapterPosition(view);
            view.setId(idx);
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            int idx = recyclerView.getChildAdapterPosition(view);
            super.onLongPress(e);
        }
    }
}
