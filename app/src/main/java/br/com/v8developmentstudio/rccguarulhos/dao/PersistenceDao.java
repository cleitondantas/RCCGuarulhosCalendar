package br.com.v8developmentstudio.rccguarulhos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class PersistenceDao extends SQLiteOpenHelper {
    private static final int version =1;

    public static final String DATABASE_NAME = "DB_CALENDARIOS";
    public static final String TB_CAL_DIOCESANO = "TB_CAL_DIOCESANO";
    public static final String ID = "ID";
    public static final String DATAHORAINICIO = "DATAHORAINICIO";
    public static final String DATAHORAFIM = "DATAHORAFIM";
    public static final String LOCAL = "LOCAL";
    public static final String SUMARIO = "SUMARIO";
    public static final String DESCRICAO = "DESCRICAO";

    public static final String TB_CALENDARIOS = "TB_CALENDARIOS";
    public static final String ID_CALENDARIO = "ID_CALENDARIO";
    public static final String NOME_CALENDARIO = "NOME_CALENDARIO";
    public static final String NOME_LABEL = "NOME_LABEL";
    public static final String URL = "URL";
    public static final String ALARME = "ALARME";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    private Cursor cursor;
    private Context context;
    public static SQLiteDatabase bancoDados = null;

    public PersistenceDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public PersistenceDao(Context context){
        super(context, DATABASE_NAME, null, version);
        this.context =context;
    }

    public void salvaNovoEvento(Evento evento,Calendario calendario){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CALENDARIO, calendario.getId());
        contentValues.put(DATAHORAINICIO, dateFormat.format(evento.getDataHoraInicio()));
        contentValues.put(DATAHORAFIM, dateFormat.format(evento.getDataHoraFim()));
        contentValues.put(LOCAL,evento.getLocal());
        contentValues.put(SUMARIO, evento.getSumario());
        contentValues.put(DESCRICAO, evento.getDescricao());
        getWritableDatabase().insert(calendario.getNomeCalendario(), null, contentValues);
    }

    public List<Evento> recuperaTodosEventos() {
        List<Evento> eventoList = new ArrayList<Evento>();
        List<Calendario> calendarios = recuperaTodosCalendarios();
        for(Calendario calendario : calendarios) {
            cursor = getWritableDatabase().query(calendario.getNomeCalendario(), new String[]{ID, ID_CALENDARIO, DATAHORAINICIO, DATAHORAFIM, LOCAL, SUMARIO, DESCRICAO}, null, null, null, null, null);

            Evento evento;
            try {
                while (cursor.moveToNext()) {
                    evento = new Evento();
                    evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    evento.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                    evento.setDataHoraInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAINICIO))));
                    evento.setDataHoraFim(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAFIM))));
                    evento.setLocal(cursor.getString(cursor.getColumnIndex(LOCAL)));
                    evento.setSumario(cursor.getString(cursor.getColumnIndex(SUMARIO)));
                    evento.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));

                    eventoList.add(evento);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return eventoList;
    }

    /**
     * Métoddo que recupera lista de eventos por mes
     * @param date
     * @return
     */
    public List<Evento> recuperaEventosPorMes(Date date){
        return recuperaEventosPor(date,false);
    }

    /**
     * Métoddo que recupera lista de eventos por dia
     * @param date
     * @return
     */
    public List<Evento> recuperaEventosPorDia(Date date){
        return recuperaEventosPor(date,true);
    }

    /**
     * recuperaEventosPor DIA OU MES
     * Para recuperar por dia envie como true e Para recuperar por Mes envie como false
     * @param date
     * @param diaTrue_MesFalse
     * @return
     */
    private List<Evento> recuperaEventosPor(Date date,boolean diaTrue_MesFalse){
        openDB();
        List<Evento> eventoList = new ArrayList<Evento>();
        Calendar cal = new GregorianCalendar();
        DecimalFormat df = new DecimalFormat("00");
        cal.setTimeInMillis(date.getTime());
        String mesFormat = df.format((cal.get(Calendar.MONTH) + 1));
        String anoFormt =""+(cal.get(Calendar.YEAR));

        String diaMin = "";
        String diaMax = "";
        if(diaTrue_MesFalse) {
            diaMin = df.format(cal.get(Calendar.DAY_OF_MONTH));
            diaMax = df.format(cal.get(Calendar.DAY_OF_MONTH));
        }else{
            diaMin = df.format(cal.getMinimum(Calendar.DAY_OF_MONTH));
            diaMax = df.format(cal.getMaximum(Calendar.DAY_OF_MONTH));
        }
        String dataInicio = anoFormt+"-"+mesFormat+"-"+ diaMin+" 00:00:00" ;
        String dataFim = anoFormt+"-"+mesFormat+"-"+diaMax+" 23:59:59" ;
        String[] args = {dataInicio, dataFim};
        String[] coluns = new String[]{ID, DATAHORAINICIO, DATAHORAFIM, LOCAL, SUMARIO, DESCRICAO};

        List<Calendario> calendarios = recuperaTodosCalendarios();
        for(Calendario calendario : calendarios) {
            try {
                cursor = bancoDados.rawQuery("SELECT * FROM '" + calendario.getNomeCalendario() + "' WHERE " + DATAHORAINICIO + " BETWEEN '" + dataInicio + "' AND '" + dataFim + "'", null);
                Evento evento;
                while (cursor.moveToNext()) {
                    evento = new Evento();
                    evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    evento.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                    evento.setDataHoraInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAINICIO))));
                    evento.setDataHoraFim(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAFIM))));
                    evento.setLocal(cursor.getString(cursor.getColumnIndex(LOCAL)));
                    evento.setSumario(cursor.getString(cursor.getColumnIndex(SUMARIO)));
                    evento.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
                    eventoList.add(evento);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return eventoList;
    }


    public Evento recuperaEventoPorID(final int id,final String calendario){
        openDB();
        Evento evento=null;
        try {
            cursor = bancoDados.rawQuery("SELECT * FROM '"+calendario+"' WHERE ID ="+id,null);
            while (cursor.moveToNext()) {
                evento = new Evento();
                evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                evento.setDataHoraInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAINICIO))));
                evento.setDataHoraFim(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAFIM))));
                evento.setLocal(cursor.getString(cursor.getColumnIndex(LOCAL)));
                evento.setSumario(cursor.getString(cursor.getColumnIndex(SUMARIO)));
                evento.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return evento;
    }


    public void salvaCalendario(Calendario calendario){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME_CALENDARIO,calendario.getNomeCalendario() );
        contentValues.put(NOME_LABEL, calendario.getNomeLabel());
        contentValues.put(URL,calendario.getUrl());
        getWritableDatabase().insert(TB_CALENDARIOS, null, contentValues);
    }

    public List<Calendario> recuperaTodosCalendarios() {
        cursor = getWritableDatabase().query(TB_CALENDARIOS, new String[]{ID, NOME_CALENDARIO, NOME_LABEL, URL,ALARME}, null, null, null, null, null);
        List<Calendario> calendarios = new ArrayList<Calendario>();
        Calendario calendario;
            while (cursor.moveToNext()) {
                calendario = new Calendario();
                calendario.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                calendario.setNomeCalendario(cursor.getString(cursor.getColumnIndex(NOME_CALENDARIO)));
                calendario.setNomeLabel(cursor.getString(cursor.getColumnIndex(NOME_LABEL)));
                calendario.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                calendario.setAlarme(cursor.getInt(cursor.getColumnIndex(ALARME))>0);

                calendarios.add(calendario);
            }
        return calendarios;
    }

    public Calendario recuperaCalendarioPorID(int id) {
        cursor = bancoDados.rawQuery("SELECT * FROM TB_CALENDARIOS WHERE ID =" + id, null);
        Calendario calendario=null;
        while (cursor.moveToNext()) {
            calendario = new Calendario();
            calendario.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            calendario.setNomeCalendario(cursor.getString(cursor.getColumnIndex(NOME_CALENDARIO)));
            calendario.setNomeLabel(cursor.getString(cursor.getColumnIndex(NOME_LABEL)));
            calendario.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
            calendario.setAlarme(cursor.getInt(cursor.getColumnIndex(ALARME)) > 0);

        }
        return calendario;
    }


    public SQLiteDatabase openDB(Context context){
        try{
            bancoDados = context.openOrCreateDatabase(PersistenceDao.DATABASE_NAME, Context.MODE_WORLD_READABLE, null);
        }catch (Exception e){
        }
        return bancoDados;
    }
    public SQLiteDatabase openDB(){
        try{
            bancoDados = context.openOrCreateDatabase(PersistenceDao.DATABASE_NAME, Context.MODE_WORLD_READABLE, null);
        }catch (Exception e){
        }
        return bancoDados;
    }

    public void onCreateTabelaCalandario(SQLiteDatabase db,Calendario calendario) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + calendario.getNomeCalendario() + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_CALENDARIO+" INTEGER NOT NULL," + DATAHORAINICIO + " DATETIME NOT NULL, " + DATAHORAFIM + " DATETIME, " + LOCAL + " VARCHAR (200),"+ SUMARIO + " VARCHAR (200) NOT NULL, "+ DESCRICAO +" TEXT );");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
           db.execSQL("CREATE TABLE IF NOT EXISTS "+TB_CALENDARIOS+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+NOME_CALENDARIO+" VARCHAR NOT NULL,"+NOME_LABEL+" VARCHAR NOT NULL,"+URL+" VARCHAR NOT NULL,"+ALARME+" BOOLEAN );");
    }

    public void onDropTabelaCalandario(SQLiteDatabase db,Calendario calendario) {
        db.execSQL("DROP TABLE IF EXISTS "+calendario.getNomeCalendario());
    }

    public void onDrop(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+TB_CALENDARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isTBContemRegistro(SQLiteDatabase db,String nomeTabela){
       int i = db.rawQuery("SELECT * FROM " + nomeTabela,null).getCount();
        return i>0;
    }
}
