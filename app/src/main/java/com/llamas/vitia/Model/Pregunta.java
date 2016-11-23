package com.llamas.vitia.Model;

/**
 * Created by MacNPro on 11/22/16.
 */

public class Pregunta {

    String id;
    String p;
    String r1;
    String r2;
    int rc;
    int ru;

    public Pregunta() {

    }

    public Pregunta(String id, String p, String r1, String r2, int rc, int ru) {
        this.id = id;
        this.p = p;
        this.r1 = r1;
        this.r2 = r2;
        this.rc = rc;
        this.ru = ru;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPregunta() {
        return p;
    }

    public void setPregunta(String p) {
        this.p = p;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public String getR2() {
        return r2;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public int getRu() {
        return ru;
    }

    public void setRu(int ru) {
        this.ru = ru;
    }
}
