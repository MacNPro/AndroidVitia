package com.llamas.vitia.Model;

/**
 * Created by MacNPro on 11/8/16.
 */

public class Contrincante {

    String id;
    String nombre;
    int nivel;

    public Contrincante() {

    }

    public Contrincante(String id, String nombre, int nivel) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
