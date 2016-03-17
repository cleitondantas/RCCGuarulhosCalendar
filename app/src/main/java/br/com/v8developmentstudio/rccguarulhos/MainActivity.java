
package br.com.v8developmentstudio.rccguarulhos;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.marcohc.robotocalendar.RobotoCalendarView.RobotoCalendarListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.v8developmentstudio.rccguarulhos.bo.ConstrutorIcal;
import br.com.v8developmentstudio.rccguarulhos.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.task.TaskProcess;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;
    private int currentMonthIndex;
    private Calendar currentCalendar;
    private  ArrayAdapter<String> stringArrayAdapter;
    private List<Evento> listEventos;
    private  List<String> listsumariodomes = new ArrayList<String>();
    private ListView listView;

    private PersistenceDao persistenceDao=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        persistenceDao = new PersistenceDao(this);
        listEventos = persistenceDao.recuperaTodosEventos();
        listView  = (ListView) findViewById(R.id.listview);
        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);

        currentCalendar = Calendar.getInstance(Locale.getDefault());
    //    gerarListaMarcarCalendario(anotatual, mesatual, null);
        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);
        // Initialize the RobotoCalendarPicker with the current index and date
        currentMonthIndex = 0;

        // Mark current day
        robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());

        gerarListaMarcarCalendario();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onDateSelected(Date date) {
        // Mark calendar day
        robotoCalendarView.markDayAsSelectedDay(date);
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
    }

    private void gerarListaMarcarCalendario(){
        listsumariodomes.clear();
        for(Evento evento:listEventos) {
                robotoCalendarView.markFirstUnderlineWithStyle(RobotoCalendarView.RED_COLOR, evento.getDataHoraInicio());
            listsumariodomes.add(evento.getSumario());
        }
        construtorAdapter();
    }

    private void construtorAdapter(){
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listsumariodomes);
        listView.setAdapter(stringArrayAdapter);
    }



}
