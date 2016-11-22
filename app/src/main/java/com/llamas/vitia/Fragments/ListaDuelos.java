package com.llamas.vitia.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.llamas.vitia.Adapters.ContrincantesAdapter;
import com.llamas.vitia.Adapters.DuelosAdapter;
import com.llamas.vitia.Model.Contrincante;
import com.llamas.vitia.Model.Duelo;
import com.llamas.vitia.R;

import java.util.ArrayList;

public class ListaDuelos extends Fragment {

    public static  String TAG = "FRAGMENT - LISTA DUELOS";

    ArrayList<Duelo> duelos = new ArrayList<>();

    public static ListaDuelos init(ArrayList<Duelo> duelos) {
        Bundle args = new Bundle();
        args.putSerializable("Duelos", duelos);
        ListaDuelos fragment = new ListaDuelos();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.duelos = (ArrayList<Duelo>) getArguments().getSerializable("Duelos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycleview, container, false);

        // CAST RECYCLEVIEW + INIT ATRIBUTOS
        RecyclerView contrincantesrv = (RecyclerView) v.findViewById(R.id.listaContrincantes);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        contrincantesrv.setLayoutManager(layoutManager);
        contrincantesrv.setHasFixedSize(true);
        contrincantesrv.setAdapter(new DuelosAdapter(getActivity(), duelos));

        return v;
    }

}
