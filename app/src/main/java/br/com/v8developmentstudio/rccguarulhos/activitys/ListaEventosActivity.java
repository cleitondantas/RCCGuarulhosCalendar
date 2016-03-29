package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.adapter.MyRecyclerViewAdapter;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;

/**
 * Created by cleiton.dantas on 28/03/2016.
 */
public class ListaEventosActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, View.OnClickListener{
    private List<Evento> listEventos;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private GestureDetectorCompat gestureDetector;
    private PersistenceDao persistenceDao = new PersistenceDao(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_eventos_activity);
        int idCalendario  = getIntent().getIntExtra(Constantes.ID,1);
        String titulo = getIntent().getStringExtra(Constantes.CALENDARIO);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setSubtitle(titulo);

        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gestureDetector = new GestureDetectorCompat(this,new RecyclerViewOnGestureListener());

        listEventos =   persistenceDao.recuperaTodosEventosPorCalendario(idCalendario);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(listEventos);
        recyclerView.setAdapter(myRecyclerViewAdapter);

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
    public void onClick(View v) {
        redirectDescricaoDoEvento(listEventos.get(v.getId()));

    }
    private void redirectDescricaoDoEvento(final Evento evento) {
        Intent intent = new Intent(this, DescricaoActivity.class);
        Bundle dados = new Bundle();
        dados.putInt(Constantes.ID,evento.getId().intValue());
        dados.putInt(Constantes.CALENDARIO,evento.getIdCalendario());
        intent.putExtras(dados);
        this.startActivity(intent);
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
