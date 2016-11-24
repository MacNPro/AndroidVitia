package com.llamas.vitia.Model;


import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MacNPro on 11/21/16.
 */

public class Duelo implements Serializable{

    private String id;
    private String player1;
    private String player2;
    private String player1ID;
    private String player2ID;
    private String player1Profile;
    private String player2Profile;
    private String turno;
    private int round;
    private int player1Puntos;
    private int player2Puntos;

    public Duelo() {

    }

    public Duelo(String id, String player1ID, String player2ID, String player1, String player2, String player1Profile, String player2Profile, String turno, int round, int player1Puntos, int player2Puntos) {
        this.id = id;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Profile = player1Profile;
        this.player2Profile = player2Profile;
        this.turno = turno;
        this.round = round;
        this.player1Puntos = player1Puntos;
        this.player2Puntos = player2Puntos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer1ID() {
        return player1ID;
    }

    public void setPlayer1ID(String player1ID) {
        this.player1ID = player1ID;
    }

    public String getPlayer2ID() {
        return player2ID;
    }

    public void setPlayer2ID(String player2ID) {
        this.player2ID = player2ID;
    }

    public String getPlayer1Profile() {
        return player1Profile;
    }

    public void setPlayer1Profile(String player1Profile) {
        this.player1Profile = player1Profile;
    }

    public String getPlayer2Profile() {
        return player2Profile;
    }

    public void setPlayer2Profile(String player2Profile) {
        this.player2Profile = player2Profile;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPlayer1Puntos() {
        return player1Puntos;
    }

    public void setPlayer1Puntos(int player1Puntos) {
        this.player1Puntos = player1Puntos;
    }

    public int getPlayer2Puntos() {
        return player2Puntos;
    }

    public void setPlayer2Puntos(int player2Puntos) {
        this.player2Puntos = player2Puntos;
    }

    @Exclude
    @Override
    public String toString(){
        return  "ID: " + id + "\n" +
                "Player 1 ID: " + player1ID + "\n" +
                "Player 2 ID: " + player2ID + "\n" +
                "Player 1: " + player1 + "\n" +
                "Player 2: " + player2 + "\n" +
                "Round: " + round + "\n" +
                "Turno: " + turno + "\n"
                ;
    }

    @Exclude
    public Map<String, Object> toMap(){

        HashMap<String, Object> dueloMap = new HashMap<>();
        dueloMap.put("id", id);
        dueloMap.put("player1ID", player1ID);
        dueloMap.put("player2ID", player2ID);
        dueloMap.put("player1", player1);
        dueloMap.put("player2", player2);
        dueloMap.put("player1Profile", player1Profile);
        dueloMap.put("player2Profile", player2Profile);
        dueloMap.put("round", round);
        dueloMap.put("turno", turno);
        dueloMap.put("player1Puntos", player1Puntos);
        dueloMap.put("player2Puntos", player2Puntos);

        return dueloMap;
    }
}
