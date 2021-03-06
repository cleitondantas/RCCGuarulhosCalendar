package br.com.v8developmentstudio.rccguarulhos.calendar.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.EventoFavorito;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;

/**
 * Created by cleiton.dantas on 17/03/2016.
 */
public class PersistenceDao extends SQLiteOpenHelper {
    private static final int version =1;

    public static final String DATABASE_NAME = "DB_CALENDARIOS";

    public static final String ID = "ID";
    public static final String ID_EVENTO = "ID_EVENTO";
    public static final String DATAHORAINICIO = "DATAHORAINICIO";
    public static final String DATAHORAFIM = "DATAHORAFIM";
    public static final String DATAHORAMODIFICADO = "DATAHORAMODIFICADO";
    public static final String LOCAL = "LOCAL";
    public static final String SUMARIO = "SUMARIO";
    public static final String DESCRICAO = "DESCRICAO";

    public static final String TB_CONFIG_CALENDAR = "TB_CONFIG_CALENDAR";
    public static final String TB_FAVORITOS = "TB_FAVORITOS";
    public static final String TB_EVENTOS = "TB_EVENTOS";
    public static final String TB_NOTIFICACAO = "TB_NOTIFICACAO";

    public static final String ID_CALENDARIO = "ID_CALENDARIO";
    public static final String NOME_CALENDARIO = "NOME_CALENDARIO";
    public static final String NOME_LABEL = "NOME_LABEL";
    public static final String URL = "URL";
    public static final String URI = "URI";//LINK
    public static final String URLIMAGEM = "URLIMAGEM";//Imagens
    public static final String UID = "UID";
    public static final String ALARME = "ALARME";

    public static final String NOTIFICATION_TITLE ="TITLE";
    public static final String NOTIFICATION_NUMERIC ="NUMERIC";
    public static final String NOTIFICATION_DESCRICAO ="DESCRICAO";
    public static final String NOTIFICATION_KEY ="KEY";
    public static final String NOTIFICATION_VALUE ="VALUE";
    public static final String NOTIFICATION_ISATIVO ="ISATIVO";


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Cursor cursor;
    private static Context context;
    private static PersistenceDao persistenceDao;

    public PersistenceDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public PersistenceDao(Context context){
        super(context, DATABASE_NAME, null, version);
        this.context =context;
    }

    public static PersistenceDao getInstance(Context context){
        if(persistenceDao == null) {
            persistenceDao = new PersistenceDao(context);
            return persistenceDao;
        }else{
            return persistenceDao;
        }
    }

    public void updateEvents(Evento evento,Calendario calendario,SQLiteDatabase bancoDados){
        salvaNovoEvento(evento, calendario,bancoDados);
    }

    public void salvaNovoEvento(Evento evento,Calendario calendario,SQLiteDatabase bancoDados){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CALENDARIO, calendario.getId());
        contentValues.put(UID, evento.getUid());
        contentValues.put(DATAHORAINICIO, dateFormat.format(evento.getDataHoraInicio()));
        contentValues.put(DATAHORAFIM, dateFormat.format(evento.getDataHoraFim()));
        contentValues.put(DATAHORAMODIFICADO, dateFormat.format(evento.getDataHoraModifcado()));
        contentValues.put(LOCAL,evento.getLocal());
        contentValues.put(SUMARIO, evento.getSumario());
        contentValues.put(DESCRICAO, evento.getDescricao());
        contentValues.put(URI, evento.getUri());
        contentValues.put(URLIMAGEM,evento.getUrlImg());
        bancoDados.insert(TB_EVENTOS, null, contentValues);
        if(bancoDados.isOpen()){
            bancoDados.close();
        }
    }

    public void salvaEventoFavorito(Evento evento,Calendario calendario,Boolean isAlarme,SQLiteDatabase bancoDados){
        try {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CALENDARIO, calendario.getId());
        contentValues.put(ID_EVENTO, evento.getId());
        contentValues.put(UID, evento.getUid());
        contentValues.put(ALARME,isAlarme);
        bancoDados.insert(TB_FAVORITOS, null, contentValues);
        } finally {
            if (bancoDados.isOpen()) {
                bancoDados.close();
            }
        }
    }

    public void deletaEventoFavoritoPorUID(String UID,SQLiteDatabase bancoDados){
        openDB();
        try {
        String whereClause ="UID=?";
        String[] whereArgs={UID};
        bancoDados.delete(TB_FAVORITOS, whereClause, whereArgs);
        } finally {
            if (bancoDados.isOpen()) {
                bancoDados.close();
            }
        }
    }

    public List<EventoFavorito> recuperaFavoritoPorUID(String uid,SQLiteDatabase bancoDados){
        ArrayList<EventoFavorito> eventoFavoritos = new ArrayList<EventoFavorito>();
        String whereClause ="UID = ?";
        String[] whereArgs={uid};
        cursor = bancoDados.query(TB_FAVORITOS, new String[]{ID, ID_CALENDARIO, ID_EVENTO, UID, ALARME}, whereClause, whereArgs, null, null, null);
        EventoFavorito eventoFavorito;
        try {
            while (cursor.moveToNext()) {
                eventoFavorito = new EventoFavorito();
                eventoFavorito.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                eventoFavorito.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                eventoFavorito.setUid(cursor.getString(cursor.getColumnIndex(UID)));
                eventoFavorito.setAlarme(cursor.getInt(cursor.getColumnIndex(ALARME)) > 0);
                eventoFavoritos.add(eventoFavorito);
            }
        } catch (Exception e) {
            Log.e("ERROR", "recuperaListEventosFavoritos()-->"+ e);
            e.printStackTrace();
        } finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
    }
        return eventoFavoritos;
    }
    public List<EventoFavorito> recuperaTodosFavoritos(SQLiteDatabase bancoDados){
        List<EventoFavorito> eventoFavoritos = new ArrayList<EventoFavorito>();
        openDB();
        cursor = bancoDados.query(TB_FAVORITOS, new String[]{ID, ID_CALENDARIO, ID_EVENTO, UID, ALARME}, null, null, null, null, null);
        EventoFavorito eventoFavorito;
            try {
                while (cursor.moveToNext()) {
                    eventoFavorito = new EventoFavorito();
                    eventoFavorito.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    eventoFavorito.setIdEvento(cursor.getInt(cursor.getColumnIndex(ID_EVENTO)));
                    eventoFavorito.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                    eventoFavorito.setUid(cursor.getString(cursor.getColumnIndex(UID)));
                    Boolean alarme =false;
                    if(cursor.getInt(cursor.getColumnIndex(ALARME))>0){
                     alarme  =    true;
                    }
                    eventoFavorito.setAlarme(alarme);
                    eventoFavoritos.add(eventoFavorito);
                }
            } catch (Exception e) {
                Log.e("ERROR", "recuperaListEventosFavoritos()-->"+ e);
                e.printStackTrace();
            }finally {
                if (bancoDados.isOpen()) {
                    bancoDados.close();
                }
            }
        return eventoFavoritos;
    }

    public List<Evento> recuperaTodosEventosFavoritos(SQLiteDatabase bancoDados){
        List<EventoFavorito> eventoFavoritos = recuperaTodosFavoritos(bancoDados);
        List<Evento> eventoList = new ArrayList<Evento>();
        try {
        for(EventoFavorito favoritos : eventoFavoritos){
            List<Evento>  eventoList1 =  recuperaEventoPorUID(favoritos.getUid(),persistenceDao.openDB(persistenceDao.context));
            eventoList.addAll(eventoList1);
        }
        }finally {
            if (bancoDados.isOpen()) {
                bancoDados.close();
            }
        }
        return eventoList;
    }


    public List<Evento> recuperaTodosEventos(SQLiteDatabase bancoDados) {
        List<Evento> eventoList = new ArrayList<Evento>();
            Log.i("BANCO_DE_DADOS","recuperaTodosEventos()");
            openDB();
            Log.i("BANCO_DE_DADOS"," Passou -> openDB() ");
            Log.i("BANCO_DE_DADOS",""+bancoDados.isOpen());
        try {
            cursor = bancoDados.query(TB_EVENTOS, new String[]{ID, UID, ID_CALENDARIO, DATAHORAINICIO, DATAHORAFIM, DATAHORAMODIFICADO, LOCAL, SUMARIO, DESCRICAO, URI,URLIMAGEM}, null, null, null, null, null);
            Evento evento;

                while (cursor.moveToNext()) {
                    evento = new Evento();
                    evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    evento.setUid(cursor.getString(cursor.getColumnIndex(UID)));
                    evento.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                    evento.setDataHoraInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAINICIO))));
                    evento.setDataHoraFim(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAFIM))));
                    evento.setDataHoraModifcado(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAMODIFICADO))));
                    evento.setLocal(cursor.getString(cursor.getColumnIndex(LOCAL)));
                    evento.setSumario(cursor.getString(cursor.getColumnIndex(SUMARIO)));
                    evento.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
                    evento.setUri(cursor.getString(cursor.getColumnIndex(URI)));
                    evento.setUrlImg(cursor.getString(cursor.getColumnIndex(URLIMAGEM)));
                    eventoList.add(evento);
                }
            } catch (ParseException e) {
                Log.e("ERROR", "recuperaTodosEventos()--> "+ e);
                e.printStackTrace();
        }finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
        }

        return eventoList;
    }

    public List<Evento> recuperaTodosEventosPorCalendario(final int idCalendario,SQLiteDatabase bancoDados) {
        List<Evento> eventoList = new ArrayList<Evento>();
        String whereClause ="ID_CALENDARIO = ?";
        String[] whereArgs={""+idCalendario};
        try {
                cursor = bancoDados.query(TB_EVENTOS, new String[]{ID,UID, ID_CALENDARIO, DATAHORAINICIO, DATAHORAFIM,DATAHORAMODIFICADO, LOCAL, SUMARIO, DESCRICAO,URI,URLIMAGEM}, whereClause, whereArgs, null, null, null);
                Evento evento;

                    while (cursor.moveToNext()) {
                        evento = new Evento();
                        evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        evento.setUid(cursor.getString(cursor.getColumnIndex(UID)));
                        evento.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                        evento.setDataHoraInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAINICIO))));
                        evento.setDataHoraFim(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAFIM))));
                        evento.setDataHoraModifcado(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAMODIFICADO))));
                        evento.setLocal(cursor.getString(cursor.getColumnIndex(LOCAL)));
                        evento.setSumario(cursor.getString(cursor.getColumnIndex(SUMARIO)));
                        evento.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
                        evento.setUri(cursor.getString(cursor.getColumnIndex(URI)));
                        evento.setUrlImg(cursor.getString(cursor.getColumnIndex(URLIMAGEM)));
                        eventoList.add(evento);
                    }
                } catch (ParseException e) {
                    Log.e("ERROR", "recuperaTodosEventosPorCalendario()-->"+ e);
                    e.printStackTrace();
                }finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
        }

        return eventoList;
    }


    /**
     * Métoddo que recupera lista de eventos por mes
     * @param date
     * @return
     */
    public List<Evento> recuperaEventosPorMes(Date date,SQLiteDatabase bancoDados){
        return recuperaEventosPor(date,false,bancoDados);
    }

    /**
     * Métoddo que recupera lista de eventos por dia
     * @param date
     * @return
     */
    public List<Evento> recuperaEventosPorDia(Date date,SQLiteDatabase bancoDados){
        return recuperaEventosPor(date,true,bancoDados);
    }

    /**
     * recuperaEventosPor DIA OU MES
     * Para recuperar por dia envie como true e Para recuperar por Mes envie como false
     * @param date
     * @param diaTrue_MesFalse
     * @return
     */
    private List<Evento> recuperaEventosPor(Date date,boolean diaTrue_MesFalse,SQLiteDatabase bancoDados){
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
            try {
                cursor = bancoDados.rawQuery("SELECT * FROM '" + TB_EVENTOS + "' WHERE (" + DATAHORAINICIO + " BETWEEN '" + dataInicio + "' AND '" + dataFim + "') OR (" + DATAHORAFIM +" BETWEEN '" + dataInicio +"' AND '" + dataFim +"') ", null);
                Evento evento;
                while (cursor.moveToNext()) {
                    evento = new Evento();
                    evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    evento.setUid(cursor.getString(cursor.getColumnIndex(UID)));
                    evento.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                    evento.setDataHoraInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAINICIO))));
                    evento.setDataHoraFim(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAFIM))));
                    evento.setDataHoraModifcado(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAMODIFICADO))));
                    evento.setLocal(cursor.getString(cursor.getColumnIndex(LOCAL)));
                    evento.setSumario(cursor.getString(cursor.getColumnIndex(SUMARIO)));
                    evento.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
                    evento.setUri(cursor.getString(cursor.getColumnIndex(URI)));
                    evento.setUrlImg(cursor.getString(cursor.getColumnIndex(URLIMAGEM)));
                    eventoList.add(evento);
                }
            } catch (ParseException e) {
                Log.e("ERROR", "recuperaEventosPor()--> " + e);
                e.printStackTrace();
            }finally {
                if(bancoDados.isOpen()){
                    bancoDados.close();
                }
            }

        return eventoList;
    }


    public Evento recuperaEventoPorID(final int id,SQLiteDatabase bancoDados){
        Evento evento=null;
        try {
            cursor = bancoDados.rawQuery("SELECT * FROM '" + TB_EVENTOS + "' WHERE ID =" + id, null);
            Log.i("INFO",cursor.toString());
            while (cursor.moveToNext()) {
                evento = new Evento();
                evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                evento.setUid(cursor.getString(cursor.getColumnIndex(UID)));
                evento.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                evento.setDataHoraInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAINICIO))));
                evento.setDataHoraFim(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAFIM))));
                evento.setDataHoraModifcado(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAMODIFICADO))));
                evento.setLocal(cursor.getString(cursor.getColumnIndex(LOCAL)));
                evento.setSumario(cursor.getString(cursor.getColumnIndex(SUMARIO)));
                evento.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
                evento.setUri(cursor.getString(cursor.getColumnIndex(URI)));
                evento.setUrlImg(cursor.getString(cursor.getColumnIndex(URLIMAGEM)));
            }
        } catch (ParseException e) {
            Log.e("ERROR", "recuperaEventoPorID()--> "+ e);
        }finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
        }
        return evento;
    }

    public List<Evento> recuperaEventoPorUID(final String uid,SQLiteDatabase bancoDados){
        Evento evento=null;
        List<Evento> eventoList = new ArrayList<Evento>();
        try {
            String sqlcoluns[] ={ID,ID_CALENDARIO,UID,DATAHORAINICIO,DATAHORAFIM,DATAHORAMODIFICADO,LOCAL,SUMARIO,DESCRICAO,URI,ALARME,URLIMAGEM};
            String[] query = {"'"+uid +"'"};
            cursor = bancoDados.rawQuery("SELECT * FROM " + TB_EVENTOS + " WHERE UID ='" + uid + "'", null);
           //cursor = bancoDados.query(TB_EVENTOS, sqlcoluns, "UID=? ", new String[]{uid}, null, null, null);

            while (cursor.moveToNext()) {
                evento = new Evento();
                evento.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                evento.setUid(cursor.getString(cursor.getColumnIndex(UID)));
                evento.setIdCalendario(cursor.getInt(cursor.getColumnIndex(ID_CALENDARIO)));
                evento.setDataHoraInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAINICIO))));
                evento.setDataHoraFim(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAFIM))));
                evento.setDataHoraModifcado(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DATAHORAMODIFICADO))));
                evento.setLocal(cursor.getString(cursor.getColumnIndex(LOCAL)));
                evento.setSumario(cursor.getString(cursor.getColumnIndex(SUMARIO)));
                evento.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
                evento.setUri(cursor.getString(cursor.getColumnIndex(URI)));
                evento.setUrlImg(cursor.getString(cursor.getColumnIndex(URLIMAGEM)));
                eventoList.add(evento);
            }
        } catch (ParseException e) {
            Log.e("ERROR", "recuperaEventoPorID()--> "+ e);
            e.printStackTrace();
        }finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
        }
        return eventoList;
    }

    public void deletaEventoPorID(final int id,final String calendario,SQLiteDatabase bancoDados){
        try {
            String whereClause = "ID=?";
            String[] whereArgs = {"" + id};
            bancoDados.delete(calendario, whereClause, whereArgs);
        } finally {
                if(bancoDados.isOpen()){
                    bancoDados.close();
                }
            }
    }

    /**
     * Salva os registros do properties calendaris
     * @param calendario
     */
    public void salvaConfiguracaoCalendario(Calendario calendario,SQLiteDatabase bancoDados){
        try {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME_CALENDARIO,calendario.getNomeCalendario() );
        contentValues.put(NOME_LABEL, calendario.getNomeLabel());
        contentValues.put(URL,calendario.getUrl());
        bancoDados.insert(TB_CONFIG_CALENDAR, null, contentValues);
        } finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
        }
    }

    public List<Calendario> recuperaTodasConfiguracoesCalendar(SQLiteDatabase bancoDados) {
        Calendario calendario;
        List<Calendario> calendarios = new ArrayList<Calendario>();
        try {
        cursor = bancoDados.query(TB_CONFIG_CALENDAR, new String[]{ID, NOME_CALENDARIO, NOME_LABEL, URL, ALARME}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                calendario = new Calendario();
                calendario.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                calendario.setNomeCalendario(cursor.getString(cursor.getColumnIndex(NOME_CALENDARIO)));
                calendario.setNomeLabel(cursor.getString(cursor.getColumnIndex(NOME_LABEL)));
                calendario.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                calendario.setAlarme(cursor.getInt(cursor.getColumnIndex(ALARME)) > 0);
                calendarios.add(calendario);
            }
        } finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
        }
        return calendarios;
    }

    public List<Notificacao> recuperaTodasNotificaoes(SQLiteDatabase bancoDados) {
        Notificacao notificacao;
        List<Notificacao> notificacaos = new ArrayList<Notificacao>();
        try {
            cursor = bancoDados.query(TB_NOTIFICACAO, new String[]{ID, NOTIFICATION_TITLE,NOTIFICATION_DESCRICAO, NOTIFICATION_KEY,NOTIFICATION_VALUE,NOTIFICATION_NUMERIC,NOTIFICATION_ISATIVO}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                notificacao = new Notificacao();
                notificacao.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                notificacao.setTitulo(cursor.getString(cursor.getColumnIndex(NOTIFICATION_TITLE)));
                notificacao.setTexto(cursor.getString(cursor.getColumnIndex(NOTIFICATION_DESCRICAO)));
                notificacao.setKey(cursor.getString(cursor.getColumnIndex(NOTIFICATION_KEY)));
                notificacao.setValue(cursor.getString(cursor.getColumnIndex(NOTIFICATION_VALUE)));
                notificacao.setNumericNotification(cursor.getInt(cursor.getColumnIndex(NOTIFICATION_NUMERIC)));
                notificacao.setAtivo(cursor.getInt(cursor.getColumnIndex(NOTIFICATION_ISATIVO))>0);
                notificacaos.add(notificacao);
            }
        } finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
        }
        return notificacaos;
    }

    public  Integer contNotificacoesAtivas(SQLiteDatabase bancoDados){
        cursor = bancoDados.query(TB_NOTIFICACAO, new String[]{ID, NOTIFICATION_TITLE,NOTIFICATION_DESCRICAO, NOTIFICATION_KEY,NOTIFICATION_VALUE,NOTIFICATION_NUMERIC,NOTIFICATION_ISATIVO}, null, null, null, null, null);
        Integer i = 0;
        while (cursor.moveToNext()) {
            if(cursor.getInt(cursor.getColumnIndex(NOTIFICATION_ISATIVO))>0) {
                i++;
            }
        }
        if(bancoDados.isOpen()){
            bancoDados.close();
        }
        return i;
    }



    public void updateInvalidaNotificacao(SQLiteDatabase bancoDados,Integer numericNotification){
        //bancoDados.execSQL("UPDATE "+TB_NOTIFICACAO +" SET "+NOTIFICATION_ISATIVO+" = "+false+" WHERE  = "+numericNotification);

        ContentValues newValues = new ContentValues();
        newValues.put(NOTIFICATION_ISATIVO,false);

        bancoDados.update(TB_NOTIFICACAO, newValues,NOTIFICATION_NUMERIC+"="+numericNotification, null);

        if(bancoDados.isOpen()){
            bancoDados.close();
        }
    }



    public void salvaNotificacao(Notificacao notificacao,SQLiteDatabase bancoDados){
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put(NOTIFICATION_NUMERIC,notificacao.getNumericNotification() );
            contentValues.put(NOTIFICATION_TITLE,notificacao.getTitulo() );
            contentValues.put(NOTIFICATION_DESCRICAO,notificacao.getTexto());
            contentValues.put(NOTIFICATION_KEY,notificacao.getKey());
            contentValues.put(NOTIFICATION_VALUE,notificacao.getValue());
            contentValues.put(NOTIFICATION_ISATIVO,notificacao.getAtivo());
            bancoDados.insert(TB_NOTIFICACAO, null, contentValues);
        } finally {
            if(bancoDados.isOpen()){
                bancoDados.close();
            }
        }

    }

    public Calendario recuperaConfigCalendarPorID(int id,SQLiteDatabase bancoDados) {
        Calendario calendario = null;
        try {
            cursor = bancoDados.rawQuery("SELECT * FROM " + TB_CONFIG_CALENDAR + " WHERE ID =" + id, null);
            while (cursor.moveToNext()) {
                calendario = new Calendario();
                calendario.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                calendario.setNomeCalendario(cursor.getString(cursor.getColumnIndex(NOME_CALENDARIO)));
                calendario.setNomeLabel(cursor.getString(cursor.getColumnIndex(NOME_LABEL)));
                calendario.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                calendario.setAlarme(cursor.getInt(cursor.getColumnIndex(ALARME)) > 0);
            }
        } finally {
                if(bancoDados.isOpen()){
                    bancoDados.close();
                }
            }
        return calendario;
    }


    public SQLiteDatabase openDB(Context context){
        SQLiteDatabase bancoDados=null;
        try{
            bancoDados = context.openOrCreateDatabase(PersistenceDao.DATABASE_NAME, Context.MODE_WORLD_READABLE, null);
        }catch (Exception e){
            Log.e("ERROR", "openDB(Context)--> "+ e);
        }
        return bancoDados;
    }
    public SQLiteDatabase openDB(){
        SQLiteDatabase bancoDados=null;
        try{
            bancoDados = context.openOrCreateDatabase(PersistenceDao.DATABASE_NAME, Context.MODE_WORLD_READABLE, null);
        }catch (Exception e){
            Log.e("ERROR", "openDB()--> "+ e);
        }
        return bancoDados;
    }


    @Override
    public void onCreate(SQLiteDatabase bancoDados) {
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS "+ TB_CONFIG_CALENDAR +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+NOME_CALENDARIO+" VARCHAR NOT NULL UNIQUE,"+NOME_LABEL+" VARCHAR NOT NULL,"+URL+" VARCHAR NOT NULL,"+ALARME+" BOOLEAN );");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS "+ TB_FAVORITOS +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+ID_CALENDARIO+" INTEGER NOT NULL,"+ID_EVENTO+" INTEGER NOT NULL,"+UID+" VARCHAR NOT NULL,"+ALARME+" BOOLEAN );");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS " + TB_EVENTOS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+UID+" VARCHAR NOT NULL, " +ID_CALENDARIO+" INTEGER NOT NULL," + DATAHORAINICIO + " DATETIME NOT NULL, " + DATAHORAFIM + " DATETIME, " + DATAHORAMODIFICADO + " DATETIME, " + LOCAL + " VARCHAR (200),"+ SUMARIO + " VARCHAR (200) NOT NULL, "+ DESCRICAO +" TEXT,"+ URI +" VARCHAR (300),"+URLIMAGEM+" VARCHAR (300));");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NOTIFICACAO + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ NOTIFICATION_NUMERIC + " INTEGER, "+ NOTIFICATION_TITLE+ " VARCHAR (200),"+NOTIFICATION_DESCRICAO+" TEXT,"+NOTIFICATION_KEY+" VARCHAR (50)," +NOTIFICATION_VALUE+" TEXT,"+NOTIFICATION_ISATIVO+" BOOLEAN);");
        if(bancoDados.isOpen()){
            bancoDados.close();
        }
    }

    public void onDrop(SQLiteDatabase bancoDados){
        bancoDados.execSQL("DROP TABLE IF EXISTS "+ TB_CONFIG_CALENDAR);
        if(bancoDados.isOpen()){
            bancoDados.close();
        }
    }
    public void onDropTabelaEventos(SQLiteDatabase bancoDados) {
        bancoDados.execSQL("DROP TABLE IF EXISTS "+TB_EVENTOS);
        if(bancoDados.isOpen()){
            bancoDados.close();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NOTIFICACAO + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ NOTIFICATION_NUMERIC + " INTEGER, "+ NOTIFICATION_TITLE+ " VARCHAR (200),"+NOTIFICATION_DESCRICAO+" TEXT,"+NOTIFICATION_KEY+" VARCHAR (50)," +NOTIFICATION_VALUE+" TEXT,"+NOTIFICATION_ISATIVO+" BOOLEAN);");
        if(db.isOpen()){
            db.close();
        }
    }



    public boolean isTBContemRegistro(SQLiteDatabase bancoDados,String nomeTabela){
        int i=0;
        try {
            i = bancoDados.rawQuery("SELECT * FROM " + nomeTabela, null).getCount();
        } finally {
                if(bancoDados.isOpen()){
                    bancoDados.close();
                }
            }
        return i>0;
    }
}
