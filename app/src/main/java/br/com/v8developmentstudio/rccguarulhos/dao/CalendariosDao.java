package br.com.v8developmentstudio.rccguarulhos.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.modelo.Calendario;

/**
 * Created by cleiton.dantas on 04/05/2016.
 */
public class CalendariosDao extends PersistenceDao{


    public CalendariosDao(Context context) {
        super(context);
    }

    public List<Calendario> verificaListaCalendarios(List<Calendario> calendarioListDB, List<Calendario> calendariosAssets){
        int i = 0;
        if(calendarioListDB.size()!=0){
            for(Calendario cal:calendariosAssets){
                for(Calendario cal2:calendarioListDB){
                    if(cal.getUrl().equalsIgnoreCase(cal2.getUrl())){
                        i++;
                    }
                }
            }
        }
        if(calendarioListDB.size()!=0 && calendarioListDB.size()==i){
            return new ArrayList<Calendario>();//Vazio
        }
        if(calendarioListDB.size()!=0 && calendarioListDB.size()!=i){
            this.onDrop(this.openDB());
        }
        return calendariosAssets;
    }
}
