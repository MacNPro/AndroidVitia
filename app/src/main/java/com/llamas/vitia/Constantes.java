package com.llamas.vitia;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by MacNPro on 11/16/16.
 */

public class Constantes {

    public static int colores[] = {
            R.color.verde,
            R.color.naranja,
            R.color.azul,
            R.color.morado,
            R.color.cyan,
            R.color.rojo,
            R.color.verdeClaro,
            R.color.azulClaro,
            R.color.rosa,
            R.color.azulMarino,
            R.color.amarillo
    };

    public static int fondosRedondos[] = {
            R.drawable.carta_redonda_verde,
            R.drawable.carta_redonda_naranja,
            R.drawable.carta_redonda_azul,
            R.drawable.carta_redonda_morado,
            R.drawable.carta_redonda_cyan,
            R.drawable.carta_redonda_rojo,
            R.drawable.carta_redonda_verde_claro,
            R.drawable.carta_redonda_azul_claro,
            R.drawable.carta_redonda_rosa,
            R.drawable.carta_redonda_azul_marino,
            R.drawable.carta_redonda_amarillo
    };

    public static int fondosDuelos[] = {
            R.drawable.fondo_duelo_verde,
            R.drawable.fondo_duelo_naranja,
            R.drawable.fondo_duelo_azul,
            R.drawable.fondo_duelo_morado,
            R.drawable.fondo_duelo_cyan,
            R.drawable.fondo_duelo_rojo,
            R.drawable.fondo_duelo_verde_claro,
            R.drawable.fondo_duelo_azul_claro,
            R.drawable.fondo_duelo_rosa,
            R.drawable.fondo_duelo_azul_marino,
            R.drawable.fondo_duelo_amarillo
    };

    // OBJETO FIREBASE DEL USUARIO
    public static FirebaseUser getUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // REFERENCIA DE CONTENIDOS
    public static DatabaseReference getBaseRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    // REFERENCIA DE USUARIO
    public static DatabaseReference getUserRef(){
        return FirebaseDatabase.getInstance().getReference().child("usuarios").child(getUser().getUid());
    }

}
