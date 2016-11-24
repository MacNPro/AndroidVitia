package com.llamas.vitia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.llamas.vitia.CustomClasses.BoldTextView;
import com.llamas.vitia.DueloActivity;
import com.llamas.vitia.Model.Duelo;
import com.llamas.vitia.R;

import java.util.List;
import java.util.Random;

import static com.llamas.vitia.Constantes.fondosDuelos;
import static com.llamas.vitia.Constantes.fondosRedondos;
import static com.llamas.vitia.Constantes.getBaseRef;
import static com.llamas.vitia.Constantes.getUser;

public class DuelosAdapter extends RecyclerView.Adapter<DuelosAdapter.ViewHolder> {

    private List<Duelo> duelos;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre, round;
        RelativeLayout fondoDuelo;
        LinearLayout view;
        ViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            fondoDuelo = (RelativeLayout) v.findViewById(R.id.fondoDuelo);
            nombre = (BoldTextView) v.findViewById(R.id.nombre);
            round = (BoldTextView) v.findViewById(R.id.round);
            view = (LinearLayout) v.findViewById(R.id.view);
        }

    }

    public DuelosAdapter(Context context, List<Duelo> duelos) {
        this.duelos = duelos;
        this.context = context;
    }

    @Override
    public DuelosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_duelo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        int rand = new Random().nextInt(fondosRedondos.length);
        holder.imagen.setBackgroundResource(fondosRedondos[rand]);
        holder.fondoDuelo.setBackgroundResource(fondosDuelos[rand]);

        final Duelo d = duelos.get(position);
        print(d.toString());

        if (esTuTurno(d)){
            holder.round.setText("Round " + d.getRound() + " - Es tu turno");
        } else {
            holder.round.setText("Round " + d.getRound() + " - No es tu turno");
        }

        final int pNumber = getPlayerNumber(d);
        final String foreignID;

        if (pNumber == 1){
            foreignID = d.getPlayer2ID();
        } else {
            foreignID = d.getPlayer1ID();
        }

        getBaseRef().child("usuarios").child(foreignID).child("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.nombre.setText("Duelo con " + dataSnapshot.getValue(String.class).split(" ")[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d.getTurno().equals(getUser().getUid())){
                    Intent i = new Intent(context, DueloActivity.class);
                    i.putExtra("Type", "continuar");
                    i.putExtra("Duelo", d);
                    context.startActivity(i);
                } else {
                    Toast.makeText(context, "Espera a que sea tu turno", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return duelos.size();
    }

    private int getPlayerNumber(Duelo d){

        int playerNumber = 0;
        String player1 = d.getPlayer1ID();
        String player2 = d.getPlayer2ID();

        if (player1.equals(getUser().getUid())){
            playerNumber = 1;
        } else if (player2.equals(getUser().getUid())){
            playerNumber = 2;
        }

        return playerNumber;
    }

    private boolean esTuTurno(Duelo d){

        boolean tuTurno = false;

        if (d.getTurno().equals(getUser().getUid())){
            tuTurno = true;
        }

        return tuTurno;

    }

    // PRINT A CONSOLA
    public void print(String s) {
        String TAG = "DUELOS ADAPTER";
        Log.d(TAG, s);
    }

}

