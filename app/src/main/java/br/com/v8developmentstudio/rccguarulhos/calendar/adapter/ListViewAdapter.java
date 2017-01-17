package br.com.v8developmentstudio.rccguarulhos.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.bo.Armazenador;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;

/**
 * Created by cleiton.dantas on 18/03/2016.
 */
public class ListViewAdapter extends ArrayAdapter {
    private List<Evento> eventos;
    private Context context;

    public ListViewAdapter(Context context, List<Evento> eventos) {
        super(context, android.R.layout.simple_list_item_1, eventos);
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View linha = convertView;
        Armazenador armazenador =null;
        final Evento itemLinha = eventos.get(position);

        if (linha == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            linha = inflater.inflate(R.layout.item_listview, null);
            armazenador = new Armazenador(linha, position);
            linha.setTag(armazenador);
        } else {
            armazenador = (Armazenador) linha.getTag();
        }
        armazenador.popularFormulario(itemLinha);
        return linha;
    }
}
