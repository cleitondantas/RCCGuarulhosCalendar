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
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Notificacao;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.ColorDrawables;

/**
 * Created by cleiton.dantas on 28/03/2016.
 */
public class MyRecyclerViewAdapterListNotifications extends RecyclerView.Adapter<MyRecyclerViewAdapterListNotifications.ViewHolder> {
    private List<Notificacao> notificaoes;
    private Activity activity;
    private ColorDrawables dra ;
    public MyRecyclerViewAdapterListNotifications(List<Notificacao> notificaoes, Activity activity) {
        this.activity = activity;
        this.notificaoes = notificaoes;
        dra = new ColorDrawables(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row_notifications, parent, false);
        ViewHolder dataObjectHolder = new ViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titulo.setText(notificaoes.get(position).getTitulo());
        holder.texto.setText(notificaoes.get(position).getTexto());
        int i = 4;
        if(notificaoes.get(position).getAtivo()){
            i = 8;
        }
        Drawable drawer = dra.customView(GradientDrawable.RECTANGLE,15,90,i);
        holder.imagedrawr.setImageDrawable(drawer);
    }

    public void addItem(Notificacao dataObj, int index) {
        notificaoes.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        notificaoes.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return notificaoes.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView texto;
        TextView titulo;
        ImageView imagedrawr;

        public ViewHolder(View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.tv_notification_title);
            texto = (TextView) itemView.findViewById(R.id.tv_notification_text);
            imagedrawr = (ImageView) itemView.findViewById(R.id.colorrowcardview_notification);
        }
    }
}
