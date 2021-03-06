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
import com.llamas.vitia.R;

import java.util.List;

import static com.llamas.vitia.Constantes.fondosDuelos;
import static com.llamas.vitia.Constantes.fondosRedondos;

public class ContrincantesAdapter extends RecyclerView.Adapter<ContrincantesAdapter.ViewHolder> {

    private List<Contrincante> contrincantes;
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

    public ContrincantesAdapter(Context context, List<Contrincante> contrincantes) {
        this.contrincantes = contrincantes;
        this.context = context;
    }

    @Override
    public ContrincantesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_contrincante, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Contrincante c = contrincantes.get(position);

        holder.imagen.setBackgroundResource(fondosRedondos[c.getColor()]);
        holder.fondoDuelo.setBackgroundResource(fondosDuelos[c.getColor()]);

        holder.nombre.setText(c.getNombre());
        holder.nivel.setText("Nivel " + c.getNivel());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DueloActivity.class);
                i.putExtra("Type", "nuevo");
                i.putExtra("ID", c.getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contrincantes.size();
    }
}

