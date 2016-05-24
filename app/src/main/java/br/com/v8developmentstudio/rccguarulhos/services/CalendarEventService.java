package br.com.v8developmentstudio.rccguarulhos.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.TimeZone;

import br.com.v8developmentstudio.rccguarulhos.modelo.Evento;

/**
 * Created by cleiton.dantas on 24/05/2016.
 */
public class CalendarEventService {
    private Context context;
    public CalendarEventService(Context context){
    this.context = context;
    }


    public void addEventoAoCalendarioLocal(Evento evento){
        long calId = getCalendarId();

        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calStart.setTime(evento.getDataHoraInicio());
        calEnd.setTime(evento.getDataHoraFim());
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, calStart.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, calEnd.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, evento.getSumario());
        values.put(CalendarContract.Events.EVENT_LOCATION,evento.getLocal()==null?"":evento.getLocal());
        values.put(CalendarContract.Events.DESCRIPTION, evento.getDescricao()==null?"":evento.getDescricao());
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE,  TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.ORGANIZER, "some.mail@some.address.com");
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        Uri uri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        long eventId = new Long(uri.getLastPathSegment());
    }

    private long getCalendarId() {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        String accountName = "";
        String accountType = "";
        for (Account account : accounts) {
            accountName = account.name;
            accountType = account.type;
            break;
        }
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection = CalendarContract.Calendars.ACCOUNT_NAME + " =? AND " + CalendarContract.Calendars.ACCOUNT_TYPE + " =?";
        String[] selArgs = new String[]{accountName,accountType};

        Cursor cursor =  context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection, selection, selArgs, null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }


}
