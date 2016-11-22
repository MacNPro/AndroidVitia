package com.llamas.vitia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llamas.vitia.CustomClasses.BoldTextView;
import com.llamas.vitia.DueloActivity;
import com.llamas.vitia.Model.Contrincante;
import com.llamas.vitia.Model.Duelo;
import com.llamas.vitia.R;

import java.util.List;

import static com.llamas.vitia.Constantes.fondosDuelos;
import static com.llamas.vitia.Constantes.fondosRedondos;

public class DuelosAdapter extends RecyclerView.Adapter<DuelosAdapter.ViewHolder> {

    private List<Duelo> duelos;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre, nivel;
        RelativeLayout fondoDuelo;
        LinearLayout view;
        ViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            fondoDuelo = (RelativeLayout) v.findViewById(R.id.fondoDuelo);
            nombre = (BoldTextView) v.findViewById(R.id.nombre);
            nivel = (BoldTextView) v.findViewById(R.id.nivel);
            view = (LinearLayout) v.findViewById(R.id.view);
        }

    }

    public DuelosAdapter(Context context, List<Duelo> duelos) {
        this.duelos = duelos;
        this.context = context;
    }

    @Override
    public DuelosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_contrincante, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Duelo d = duelos.get(position);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return duelos.size();
    }
}

