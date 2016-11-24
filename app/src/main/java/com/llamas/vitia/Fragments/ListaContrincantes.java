package com.llamas.vitia.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.llamas.vitia.Adapters.ContrincantesAdapter;
import com.llamas.vitia.Inicio;
import com.llamas.vitia.Model.Contrincante;
import com.llamas.vitia.Model.Duelo;
import com.llamas.vitia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.llamas.vitia.Constantes.fondosDuelos;
import static com.llamas.vitia.Constantes.getBaseRef;
import static com.llamas.vitia.Constantes.getUser;
import static com.llamas.vitia.Constantes.getUserRef;
import static com.llamas.vitia.R.id.local;

public class ListaContrincantes extends Fragment {

    public static  String TAG = "FRAGMENT CONTRINCANTES";

    private RecyclerView contrincantesrv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycleview, container, false);

        // CAST RECYCLEVIEW + INIT ATRIBUTOS
        contrincantesrv = (RecyclerView) v.findViewById(R.id.listaContrincantes);
        getBlockedIDS();

        return v;
    }

    /**
     * ------ FUNCIONES PRIMARIAS ------
     **/

    // OBTENER CONTRINCANTES
    public void getBlockedIDS() {
        getUserRef().child("duelos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> blockedIDS = new ArrayList<>();
                String localID = getUser().getUid();
                blockedIDS.add(localID);
                for (DataSnapshot usuario : dataSnapshot.getChildren()) {
                    Duelo d = usuario.getValue(Duelo.class);
                    String p1 = d.getPlayer1ID();
                    String p2 = d.getPlayer2ID();
                    if (!p1.equals(localID)){
                        blockedIDS.add(p1);
                    } else if (!p2.equals(localID)){
                        blockedIDS.add(p2);
                    }
                }
                print(blockedIDS.toString());
                getContrincantes(blockedIDS);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // OBTENER CONTRINCANTES
    public void getContrincantes(final ArrayList<String> ids) {
        getBaseRef().child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = getUser().getUid();
                ArrayList<Contrincante> contrincantes = new ArrayList<>();
                for (DataSnapshot usuario : dataSnapshot.getChildren()) {

                    if (!usuario.getKey().equals(id) && !ids.contains(usuario.getKey())) {
                        contrincantes.add(new Contrincante(
                                usuario.getKey(),
                                usuario.child("nombre").getValue(String.class),
                                usuario.child("nivel").getValue(Integer.class),
                                new Random().nextInt(fondosDuelos.length)
                        ));
                    }
                }
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                contrincantesrv.setLayoutManager(layoutManager);
                contrincantesrv.setHasFixedSize(true);
                contrincantesrv.setAdapter(new ContrincantesAdapter(getActivity(), contrincantes));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * ------ FUNCIONES SECUNDARIAS ------
     **/

    // PRINT A CONSOLA
    public void print(String s) {
        Log.d(TAG, s);
    }

}
