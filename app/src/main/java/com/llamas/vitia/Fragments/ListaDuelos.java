package com.llamas.vitia.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.llamas.vitia.Adapters.DuelosAdapter;
import com.llamas.vitia.Model.Duelo;
import com.llamas.vitia.R;

import java.util.ArrayList;

import static com.llamas.vitia.Constantes.getUserRef;


public class ListaDuelos extends Fragment {

    private RecyclerView contrincantesrv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycleview, container, false);

        // CAST RECYCLEVIEW + INIT ATRIBUTOS
        contrincantesrv = (RecyclerView) v.findViewById(R.id.listaContrincantes);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        contrincantesrv.setLayoutManager(layoutManager);
        contrincantesrv.setHasFixedSize(true);

        getDuelos();

        return v;
    }

    // OBTENER CONTRINCANTES
    public void getDuelos() {
        getUserRef().child("duelos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Duelo> duelos = new ArrayList<>();
                duelos.clear();
                for (DataSnapshot duelo : dataSnapshot.getChildren()) {
                    Duelo d = duelo.getValue(Duelo.class);
                    duelos.add(d);
                }

                contrincantesrv.setAdapter(new DuelosAdapter(getActivity(), duelos));
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
        String TAG = "FRAGMENT - LISTA DUELOS";
        Log.d(TAG, s);
    }

}
