package br.com.v8developmentstudio.rccguarulhos.calendar.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.ColorDrawables;

/**
 * Created by cleiton.dantas on 28/03/2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<Evento> eventoList;
    private Activity activity;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private ColorDrawables dra ;

    public MyRecyclerViewAdapter(List<Evento> eventos, Activity activity) {
        this.activity = activity;
        eventoList = eventos;
        dra = new ColorDrawables(activity);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);
        ViewHolder dataObjectHolder = new ViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label.setText(eventoList.get(position).getSumario());
        holder.dateTime.setText(dateFormat.format(eventoList.get(position).getDataHoraInicio()));

        Drawable drawer = dra.customView(GradientDrawable.RECTANGLE,15,90,eventoList.get(position).getIdCalendario());
        holder.imagedrawr.setImageDrawable(drawer);
    }

    public void addItem(Evento dataObj, int index) {
        eventoList.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        eventoList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView dateTime;
        ImageView imagedrawr;

        public ViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.tvtitulo);
            dateTime = (TextView) itemView.findViewById(R.id.tvdatainicio);
            imagedrawr = (ImageView) itemView.findViewById(R.id.colorrowcardview);
        }
    }
}
