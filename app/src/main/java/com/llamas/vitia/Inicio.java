package com.llamas.vitia;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.llamas.vitia.Adapters.ContrincantesAdapter;
import com.llamas.vitia.Model.Contrincante;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Inicio extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // CAST IMAGEVIEW + CARGAR IMAGEN DE FONDO
        Glide.with(this).load(R.drawable.fondo).into((ImageView) findViewById(R.id.fondo));

        // CREAR LIST LLENA DE OBJETOS VACIOS
        List<Contrincante> contrincantes = new ArrayList<>();

        contrincantes.add(new Contrincante("","",0));

        // CAST RECYCLEVIEW + INIT ATRIBUTOS
        RecyclerView contrincantesrv = (RecyclerView) findViewById(R.id.listaContrincantes);
        GridLayoutManager layoutManager = new GridLayoutManager(Inicio.this, 1);
        contrincantesrv.setLayoutManager(layoutManager);
        contrincantesrv.setHasFixedSize(true);
        contrincantesrv.setAdapter(new ContrincantesAdapter(Inicio.this, contrincantes));

    }
}
