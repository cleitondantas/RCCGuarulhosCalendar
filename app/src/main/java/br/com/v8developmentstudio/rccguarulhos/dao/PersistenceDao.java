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

    private static final List<String> sqls = Arrays.asList("CREATE TABLE IF NOT EXISTS " + TB_CAL_DIOCESANO + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATAHORAINICIO + " DATETIME NOT NULL, " + DATAHORAFIM + " DATETIME, " + LOCAL + " VARCHAR (200),"+ SUMARIO + " VARCHAR (200) NOT NULL, "+ DESCRICAO +" TEXT );");
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

    public void salvaNovoEvento(Evento evento){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATAHORAINICIO, dateFormat.format(evento.getDataHoraInicio()));
        contentValues.put(DATAHORAFIM, dateFormat.format(evento.getDataHoraFim()));
        contentValues.put(LOCAL,evento.getLocal());
        contentValues.put(SUMARIO, evento.getSumario());
        contentValues.put(DESCRICAO, evento.getDescricao());
        getWritableDatabase().insert(TB_CAL_DIOCESANO, null, contentValues);
    }

    public List<Evento> recuperaTodosEventos() {
        cursor = getWritableDatabase().query(TB_CAL_DIOCESANO, new String[]{ID, DATAHORAINICIO, DATAHORAFIM, LOCAL, SUMARIO, DESCRICAO}, null, null, null, null, null);
        List<Evento> eventoList = new ArrayList<Evento>();
        Evento evento;
        try {
            while (cursor.moveToNext()) {
                evento = new Evento();
                evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
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
        String mesFormat = df.format((cal.get(Calendar.MONTH)+1));
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
        try {

         String[] args = {dataInicio,dataFim};
         String[] coluns = new String[]{ID, DATAHORAINICIO, DATAHORAFIM, LOCAL, SUMARIO, DESCRICAO};
        cursor = bancoDados.rawQuery("SELECT * FROM '"+TB_CAL_DIOCESANO+"' WHERE "+DATAHORAINICIO+" BETWEEN '"+dataInicio+"' AND '"+dataFim+"'",null);

        Evento evento;
            while (cursor.moveToNext()) {
                evento = new Evento();
                evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
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
        return eventoList;
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
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+TB_CAL_DIOCESANO);
       for(String sql:sqls) {
           db.execSQL(sql);
       }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
