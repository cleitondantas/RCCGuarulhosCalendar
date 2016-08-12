package br.com.v8developmentstudio.rccguarulhos.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.marcohc.robotocalendar.RobotoCalendarView.RobotoCalendarListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.v8developmentstudio.rccguarulhos.R;
import br.com.v8developmentstudio.rccguarulhos.adapter.MyRecyclerViewAdapter;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.util.AssetsPropertyReader;
import br.com.v8developmentstudio.rccguarulhos.util.Constantes;
import br.com.v8developmentstudio.rccguarulhos.util.FiltroDatas;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements RobotoCalendarListener, NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnItemTouchListener, View.OnClickListener {

    private RobotoCalendarView robotoCalendarView;
    private int currentMonthIndex = 0;
    private Calendar currentCalendar;
    private ArrayAdapter<Evento> eventoArrayAdapter;
    private List<Evento> listEventos;
    private List<String> listsumariodomes = new ArrayList<String>();
    private ActivityServices ac = new ActivityServicesImpl();
    private AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(this);
    private PersistenceDao persistenceDao = null;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetector;
    private FiltroDatas filtroDatas = new FiltroDatas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        persistenceDao = new PersistenceDao(this);

        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Menu menu = navigationView.getMenu();
        SubMenu subMenu = menu.addSubMenu(getString(R.string.ministerios));
        for (Calendario calendario : persistenceDao.recuperaTodasConfiguracoesCalendar()) {
            subMenu.add(1, calendario.getId(), calendario.getId(), calendario.getNomeLabel());
        }
        navigationView.setNavigationItemSelectedListener(this);
        setupDrawerContent(navigationView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewOnGestureListener());

        listEventos = persistenceDao.recuperaTodosEventos();
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(filtroDatas.filtraEventosPorDataAtual(listEventos));
        recyclerView.setAdapter(myRecyclerViewAdapter);

        currentCalendar = Calendar.getInstance(Locale.getDefault());
        robotoCalendarView.setRobotoCalendarListener(this);
        robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());

        updateCalendar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ac.redirect(this, SettingsActivity.class, null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método de Click no Menu do Drawer
     *
     * @param navigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.favoritos) {

            Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            redirectListFavoritos();
        } else {
            Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            redirectListEventos(menuItem.getItemId(), String.valueOf(menuItem.getTitle()));
        }

        // Close the navigation drawers
        drawer.closeDrawers();
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onDateSelected(Date date) {
        robotoCalendarView.markDayAsSelectedDay(date);
        listEventos = persistenceDao.recuperaEventosPorDia(date);
        gerarListaMarcarCalendario();
    }

    @Override
    public void onRightButtonClick() {
        currentMonthIndex++;
        updateCalendar();
    }

    @Override
    public void onLeftButtonClick() {
        currentMonthIndex--;
        updateCalendar();
    }

    private void updateCalendar() {
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        robotoCalendarView.initializeCalendar(currentCalendar);
        robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
        listEventos = persistenceDao.recuperaEventosPorMes(currentCalendar.getTime());
        gerarListaMarcarCalendario();
    }

    private void gerarListaMarcarCalendario() {
        listsumariodomes.clear();
        for (Evento evento : listEventos) {
            robotoCalendarView.markFirstUnderlineWithStyle(RobotoCalendarView.RED_COLOR, evento.getDataHoraInicio());
            listsumariodomes.add(evento.getSumario());
        }
        construtorAdapter();
    }

    public void construtorAdapter() {
        filtroDatas.comparatorData(listEventos);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(listEventos);
        recyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void redirectDescricaoDoEvento(final Evento evento) {
        Bundle dados = new Bundle();
        dados.putInt(Constantes.ID, evento.getId().intValue());
        dados.putInt(Constantes.ACTIVITYHISTOTY, Constantes.MAINACTIVITY);
        ac.redirect(this, DescricaoActivity.class, dados);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void redirectListEventos(int idCalenadrio, String tituloCalendario) {
        Bundle dados = new Bundle();
        dados.putInt(Constantes.ID, idCalenadrio);
        dados.putString(Constantes.CALENDARIO, tituloCalendario);
        ac.redirect(this, ListaEventosActivity.class, dados);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void redirectListFavoritos() {
        ac.redirect(this, ListaEventosFavoritosActivity.class, null);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);

        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onClick(View v) {
        redirectDescricaoDoEvento(listEventos.get(v.getId()));
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
