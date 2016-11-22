package com.llamas.vitia.Model;

import java.io.Serializable;

/**
 * Created by MacNPro on 11/8/16.
 */

public class Contrincante implements Serializable{

    String id;
    String nombre;
    int nivel;
    int color;

    public Contrincante() {

    }

    public Contrincante(String id, String nombre, int nivel, int color) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
