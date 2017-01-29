package br.com.v8developmentstudio.rccguarulhos.calendar.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.marcohc.robotocalendar.RobotoCalendarView.RobotoCalendarListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.adapter.MyRecyclerScroll;
import br.com.v8developmentstudio.rccguarulhos.calendar.adapter.MyRecyclerViewAdapter;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.AssetsPropertyReader;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.ColorDrawables;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.Constantes;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.FiltroDatas;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements RobotoCalendarListener, NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnItemTouchListener, View.OnClickListener {

    private RobotoCalendarView robotoCalendarView;
    private int currentMonthIndex = 0;
    private Calendar currentCalendar;
    private ArrayAdapter<Evento> eventoArrayAdapter;
    private List<Evento> listEventos;
    private List<Evento> listEventosFiltrados;
    private List<String> listsumariodomes = new ArrayList<String>();
    private ActivityServices ac = new ActivityServicesImpl();
    private AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(this);
    private PersistenceDao persistenceDao = PersistenceDao.getInstance(this);
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetector;
    private FiltroDatas filtroDatas = new FiltroDatas();
    private Menu menu;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private ColorDrawables drawablecolor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currentMonthIndex = getIntent().getIntExtra(Constantes.CURRENT_MONTH, 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        persistenceDao = PersistenceDao.getInstance(this);
        drawablecolor = new ColorDrawables(this);
        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        menu = navigationView.getMenu();
        navigationView.setItemIconTintList(null);
        SubMenu subMenu = menu.addSubMenu(getString(R.string.ministerios));

        for (Calendario calendario : persistenceDao.recuperaTodasConfiguracoesCalendar(persistenceDao.openDB(this))) {
            Drawable drawer = drawablecolor.customView(GradientDrawable.OVAL,35,35,calendario.getId());
            subMenu.add(1, calendario.getId(), calendario.getId(), calendario.getNomeLabel()).setIcon(drawer);
        }
        navigationView.setNavigationItemSelectedListener(this);
        setupDrawerContent(navigationView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewOnGestureListener());

        listEventos = persistenceDao.recuperaTodosEventos(persistenceDao.openDB(this));
        listEventosFiltrados = filtroDatas.filtraEventosPorDataAtual(listEventos);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(listEventosFiltrados,this);
        recyclerView.setAdapter(myRecyclerViewAdapter);

        currentCalendar = Calendar.getInstance(Locale.getDefault());
        robotoCalendarView.setRobotoCalendarListener(this);
        robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());

        updateCalendar();


    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        listEventos = persistenceDao.recuperaEventosPorDia(date,persistenceDao.openDB());
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
        listEventos = persistenceDao.recuperaEventosPorMes(currentCalendar.getTime(),persistenceDao.openDB());
        gerarListaMarcarCalendario();
    }

    private void gerarListaMarcarCalendario() {
        listsumariodomes.clear();
        List<Date> datas = new ArrayList<>();
        for (Evento evento : listEventos) {
               for(Date item: evento.getDatasEntreInicioEoFim()){
                robotoCalendarView.markSecondUnderlineWithStyle(RobotoCalendarView.RED_COLOR,item);
            }
            listsumariodomes.add(evento.getSumario());
        }
        construtorAdapter();
    }

    public void construtorAdapter() {
        filtroDatas.comparatorData(listEventos);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(listEventos,this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void redirectDescricaoDoEvento(final Evento evento) {
        Bundle dados = new Bundle();
        dados.putInt(Constantes.ID, evento.getId().intValue());
        dados.putInt(Constantes.ACTIVITYHISTOTY, Constantes.MAINACTIVITY);
        dados.putInt(Constantes.CURRENT_MONTH,currentMonthIndex);
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
