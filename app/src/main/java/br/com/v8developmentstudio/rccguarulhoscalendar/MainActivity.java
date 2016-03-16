
package br.com.v8developmentstudio.rccguarulhoscalendar;

import android.content.Context;
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
import java.util.Random;

import br.com.v8developmentstudio.rccguarulhoscalendar.bo.ConstrutorIcal;
import br.com.v8developmentstudio.rccguarulhoscalendar.modelo.Evento;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements RobotoCalendarListener {

    private RobotoCalendarView robotoCalendarView;
    private int currentMonthIndex;
    private Calendar currentCalendar;
    private ConstrutorIcal contruorical;
    private  ArrayAdapter<String> stringArrayAdapter;
    private List<Evento> listEventos;
    private  List<String> listsumariodomes = new ArrayList<String>();
    private ListView listView;
    private int mesatual;
    private int anotatual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listEventos = getCalendarEventos();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView  = (ListView) findViewById(R.id.listview);
        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);
        // Gets the calendar from the view
        mesatual = new Date().getMonth();
        anotatual = new Date().getYear();
        currentCalendar = Calendar.getInstance(Locale.getDefault());
    //    gerarListaMarcarCalendario(anotatual, mesatual, null);
        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);
        // Initialize the RobotoCalendarPicker with the current index and date
        currentMonthIndex = 0;


        // Mark current day
        robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onDateSelected(Date date) {
        // Mark calendar day
        robotoCalendarView.markDayAsSelectedDay(date);
  //      gerarListaMarcarCalendario(date.getYear(), date.getMonth(), date.getDay());
    }

    @Override
    public void onRightButtonClick() {
        currentMonthIndex++;
        mesatual++;
        updateCalendar();
//        gerarListaMarcarCalendario(anotatual,mesatual,null);
    }

    @Override
    public void onLeftButtonClick() {
        currentMonthIndex--;
        mesatual--;
        updateCalendar();
        //gerarListaMarcarCalendario();
    }

    private void updateCalendar() {
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        robotoCalendarView.initializeCalendar(currentCalendar);
    }

    private void gerarListaMarcarCalendario(Date date,boolean verificarDia){
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

    private List<Evento> getCalendarEventos(){
        List<Evento> eventoList = null;
        try {
            InputStream is = getAssets().open("agendarcc.ics");
            contruorical = new ConstrutorIcal(is);
            eventoList= contruorical.getEventos();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventoList;
    }

}
