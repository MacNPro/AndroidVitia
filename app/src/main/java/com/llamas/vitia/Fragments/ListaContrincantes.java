package com.llamas.vitia.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.llamas.vitia.Adapters.ContrincantesAdapter;
import com.llamas.vitia.Inicio;
import com.llamas.vitia.Model.Contrincante;
import com.llamas.vitia.R;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class ListaContrincantes extends Fragment {

    public static  String TAG = "FRAGMENT - LISTA CONTRINCANTES";

    ArrayList<Contrincante> contrincantes = new ArrayList<>();

    public static ListaContrincantes init(ArrayList<Contrincante> contrincantes) {
        Bundle args = new Bundle();
        args.putSerializable("Contrincantes", contrincantes);
        ListaContrincantes fragment = new ListaContrincantes();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contrincantes = (ArrayList<Contrincante>) getArguments().getSerializable("Contrincantes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycleview, container, false);

        // CAST RECYCLEVIEW + INIT ATRIBUTOS
        RecyclerView contrincantesrv = (RecyclerView) v.findViewById(R.id.listaContrincantes);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        contrincantesrv.setLayoutManager(layoutManager);
        contrincantesrv.setHasFixedSize(true);
        contrincantesrv.setAdapter(new ContrincantesAdapter(getActivity(), contrincantes));

        return v;
    }

}
