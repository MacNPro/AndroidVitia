package com.llamas.vitia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.llamas.vitia.CustomClasses.HeavyTextView;
import com.llamas.vitia.CustomClasses.MediumRadioButton;
import com.llamas.vitia.CustomClasses.MediumTextView;
import com.llamas.vitia.Model.Duelo;
import com.llamas.vitia.Model.Pregunta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.llamas.vitia.Constantes.getBaseRef;
import static com.llamas.vitia.Constantes.getUser;
import static com.llamas.vitia.Constantes.getUserRef;
import static com.llamas.vitia.R.id.foreignPuntos;
import static com.llamas.vitia.R.id.puntos;

public class DueloActivity extends Activity {

    public static String TAG = "DUELO";

    ArrayList<Pregunta> preguntas = new ArrayList<>();
    ArrayList<Pregunta> historialPreguntas = new ArrayList<>();
    Duelo duelo;

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duelo);

        Intent i = getIntent();
        String type = i.getStringExtra("Type");

        switch (type) {

            case "nuevo":

                final String foreignID = i.getStringExtra("ID");
                getBaseRef().child("usuarios").child(foreignID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("profileURL").exists()) {
                            String foreignProfile = dataSnapshot.child("profileURL").getValue(String.class);
                            String foreignName = dataSnapshot.child("nombre").getValue(String.class);
                            crearNuevoDuelo(foreignID, foreignProfile, foreignName);
                        } else {
                            String foreignName = dataSnapshot.child("nombre").getValue(String.class);
                            crearNuevoDuelo(foreignID, "http://www.nowseethis.org/avatars/default/missing.gif", foreignName);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case "continuar":

                duelo = (Duelo) getIntent().getSerializableExtra("Duelo");
                continuarConDuelo();

                break;

            default:
                break;

        }

        MediumTextView checar = (MediumTextView) findViewById(R.id.checar);
        checar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checarRespuesta();
            }
        });

    }


    /**
     * ------ FUNCIONES PRIMARIAS ------
     **/


    // CREAR NUEVO DUELO CON DEFAULTS
    private void crearNuevoDuelo(String foreignID, String foreignProfile, String foreignName) {

        DatabaseReference baseRef = getBaseRef();
        String key = baseRef.child("duelos").push().getKey();
        String IDClient = getUser().getUid();

        duelo = new Duelo(key, IDClient, foreignID, getUser().getDisplayName(), foreignName, String.valueOf(getUser().getPhotoUrl()), foreignProfile, IDClient, 1, 0, 0);
        Map<String, Object> dueloMap = duelo.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/duelos/" + key, dueloMap);
        childUpdates.put("/usuarios/" + duelo.getPlayer1ID() + "/duelos/" + key, dueloMap);
        childUpdates.put("/usuarios/" + duelo.getPlayer2ID() + "/duelos/" + key, dueloMap);

        baseRef.updateChildren(childUpdates);

        continuarConDuelo();

    }

    // CONTINUAR DUELO Y MANEJO DE INFO
    private void continuarConDuelo() {

        // LLENAR LA INFORMACION DEL DUELO
        HeavyTextView round = (HeavyTextView) findViewById(R.id.round);
        round.setText("Round " + duelo.getRound() + "/3");
        actualizarInformacion();

        // CARGAR IMAGENES DE USUARIOS
        ImageView localProfile = (ImageView) findViewById(R.id.localProfile);
        ImageView foreignProfile = (ImageView) findViewById(R.id.foreignProfile);
        Glide.with(this).load(getLocalURL(duelo)).bitmapTransform(new CropCircleTransformation(this)).into(localProfile);
        Glide.with(this).load(getForeignURL(duelo)).bitmapTransform(new CropCircleTransformation(this)).into(foreignProfile);

        // CARGAR NOMBRES DE USUARIOS
        MediumTextView local = (MediumTextView) findViewById(R.id.local);
        MediumTextView foreign = (MediumTextView) findViewById(R.id.foreign);
        local.setText(getLocalPlayerName(duelo));
        foreign.setText(getForeignPlayerName(duelo));

        // INICIAR LOOP DE 5 PREGUNTAS
        getPreguntas();

    }

    // GUARDAR DUELO
    private void guardarDuelo() {

        duelo.setTurno(getForeignID(duelo));

        if (!eresPlayer1(duelo) && duelo.getRound() == 3) {
            // ACABO EL DUELO

            // ACTUALIZAR LOCAL PUNTOS
            getUserRef().child("puntos").runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {

                    int puntos = mutableData.getValue(Integer.class);
                    puntos = puntos + getLocalPuntos(duelo);

                    // Set value and report transaction success
                    mutableData.setValue(puntos);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    // Transaction completed
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });

            // ACTUALIZAR FOREIGN PUNTOS
            getBaseRef().child("usuarios").child(getForeignID(duelo)).child("puntos").runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {

                    int puntos = mutableData.getValue(Integer.class);
                    puntos = puntos + getLocalPuntos(duelo);

                    // Set value and report transaction success
                    mutableData.setValue(puntos);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    // Transaction completed
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });

            // ACTUALIZAR NUMERO DE DUELOS LOCAL
            getUserRef().child("numeroDeDuelos").runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {

                    int numDeDuelos = mutableData.getValue(Integer.class);
                    numDeDuelos++;

                    // Set value and report transaction success
                    mutableData.setValue(numDeDuelos);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    // Transaction completed
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });

            // ACTUALIZAR NUMERO DE DUELOS FOREIGN
            getBaseRef().child("usuarios").child(getForeignID(duelo)).child("numeroDeDuelos").runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {

                    int numDeDuelos = mutableData.getValue(Integer.class);
                    numDeDuelos++;

                    // Set value and report transaction success
                    mutableData.setValue(numDeDuelos);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    // Transaction completed
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/duelos/" + duelo.getId(), null);
                    childUpdates.put("/usuarios/" + duelo.getPlayer1ID() + "/duelos/" + duelo.getId(), null);
                    childUpdates.put("/usuarios/" + duelo.getPlayer2ID() + "/duelos/" + duelo.getId(), null);

                    getBaseRef().updateChildren(childUpdates);
                }
            });

            finish();

        } else if (!eresPlayer1(duelo)) {
            // SE SUMA UN ROUND
            duelo.setRound(duelo.getRound() + 1);
        } else {
            // SE ACTUALIZA Y GUARDA EL DUELO
            Map<String, Object> dueloMap = duelo.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/duelos/" + duelo.getId(), dueloMap);
            childUpdates.put("/usuarios/" + duelo.getPlayer1ID() + "/duelos/" + duelo.getId(), dueloMap);
            childUpdates.put("/usuarios/" + duelo.getPlayer2ID() + "/duelos/" + duelo.getId(), dueloMap);

            getBaseRef().updateChildren(childUpdates);
        }

    }

    private void getPreguntas() {
        getBaseRef().child("preguntas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                preguntas.clear();
                for (DataSnapshot pregunta : dataSnapshot.getChildren()) {
                    Pregunta p = pregunta.getValue(Pregunta.class);
                    p.setId(pregunta.getKey());
                    preguntas.add(p);
                }

                displayNuevaPregunta();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayNuevaPregunta() {

        MediumTextView preguntatt = (MediumTextView) findViewById(R.id.preguntaView);
        MediumRadioButton r1View = (MediumRadioButton) findViewById(R.id.r1View);
        MediumRadioButton r2View = (MediumRadioButton) findViewById(R.id.r2View);

        if (index < 5) {
            int preguntaRandom = new Random().nextInt(preguntas.size());

            Pregunta p = preguntas.get(preguntaRandom);
            historialPreguntas.add(p);
            preguntatt.setText(p.getPregunta());
            r1View.setText(p.getR1());
            r2View.setText(p.getR2());

        }

    }

    private void checarRespuesta() {
        MediumRadioButton r1View = (MediumRadioButton) findViewById(R.id.r1View);
        MediumRadioButton r2View = (MediumRadioButton) findViewById(R.id.r2View);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        if (r1View.isChecked()) {
            historialPreguntas.get(index).setRu(1);
        } else if (r2View.isChecked()) {
            historialPreguntas.get(index).setRu(2);
        } else {
            Toast.makeText(this, "Debes seleccionar un inciso", Toast.LENGTH_SHORT).show();
        }

        print(historialPreguntas.get(index).getRu() + " <---- Respuesta Usuario");

        Pregunta p = historialPreguntas.get(index);

        if (p.getRu() != 0) {

            if (p.getRc() == p.getRu()) {
                duelo = agregarPuntos(duelo);
                Toast.makeText(this, "Respuesta correcta +150", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Respuesta incorrecta :(", Toast.LENGTH_SHORT).show();
            }

            index++;
            radioGroup.clearCheck();
            if (index < 5) {
                displayNuevaPregunta();
            } else {
                // CAMBIAR TURNO Y GUARDAR
                Toast.makeText(this, "Termino tu turno :)", Toast.LENGTH_SHORT).show();
                guardarDuelo();
            }

        }

        actualizarInformacion();

    }

    /**
     * ------ FUNCIONES SECUNDARIAS ------
     **/

    private boolean eresPlayer1(Duelo d) {

        boolean playerID = false;

        if (d.getPlayer1ID().equals(getUser().getUid())) {
            playerID = true;
        }

        return playerID;

    }

    private void actualizarInformacion() {

        MediumTextView localPuntostt = (MediumTextView) findViewById(R.id.localPuntos);
        MediumTextView foreignPuntostt = (MediumTextView) findViewById(R.id.foreignPuntos);
        int localPuntos = getLocalPuntos(duelo);
        int foreignPuntos = getForeignPuntos(duelo);
        localPuntostt.setText(localPuntos + " pt.");
        foreignPuntostt.setText(foreignPuntos + " pt.");

    }

    private String getForeignID(Duelo d) {

        String playerID = "";
        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (!player1ID.equals(getUser().getUid())) {
            playerID = d.getPlayer1ID();
        } else if (!player2ID.equals(getUser().getUid())) {
            playerID = d.getPlayer2ID();
        }

        return playerID;
    }

    private String getLocalID(Duelo d) {

        String playerID = "";
        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (!player1ID.equals(getUser().getUid())) {
            playerID = d.getPlayer1ID();
        } else if (!player2ID.equals(getUser().getUid())) {
            playerID = d.getPlayer2ID();
        }

        return playerID;
    }

    private String getForeignPlayerName(Duelo d) {

        String playerName = "";
        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (!player1ID.equals(getUser().getUid())) {
            playerName = d.getPlayer1();
        } else if (!player2ID.equals(getUser().getUid())) {
            playerName = d.getPlayer2();
        }

        return playerName;
    }

    private String getLocalPlayerName(Duelo d) {

        String playerName = "";
        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (player1ID.equals(getUser().getUid())) {
            playerName = d.getPlayer1();
        } else if (player2ID.equals(getUser().getUid())) {
            playerName = d.getPlayer2();
        }

        return playerName;
    }

    private String getForeignURL(Duelo d) {

        String playerName = "";
        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (!player1ID.equals(getUser().getUid())) {
            playerName = d.getPlayer1Profile();
        } else if (!player2ID.equals(getUser().getUid())) {
            playerName = d.getPlayer2Profile();
        }

        return playerName;
    }

    private String getLocalURL(Duelo d) {

        String playerName = "";
        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (player1ID.equals(getUser().getUid())) {
            playerName = d.getPlayer1Profile();
        } else if (player2ID.equals(getUser().getUid())) {
            playerName = d.getPlayer2Profile();
        }

        return playerName;
    }

    private int getForeignPuntos(Duelo d) {

        int playerName = 0;
        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (!player1ID.equals(getUser().getUid())) {
            playerName = d.getPlayer1Puntos();
        } else if (!player2ID.equals(getUser().getUid())) {
            playerName = d.getPlayer2Puntos();
        }

        return playerName;
    }

    private int getLocalPuntos(Duelo d) {

        int playerName = 0;
        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (player1ID.equals(getUser().getUid())) {
            playerName = d.getPlayer1Puntos();
        } else if (player2ID.equals(getUser().getUid())) {
            playerName = d.getPlayer2Puntos();
        }

        return playerName;
    }

    private Duelo agregarPuntos(Duelo d) {

        String player1ID = d.getPlayer1ID();
        String player2ID = d.getPlayer2ID();

        if (player1ID.equals(getUser().getUid())) {
            d.setPlayer1Puntos(d.getPlayer1Puntos() + 150);
        } else if (player2ID.equals(getUser().getUid())) {
            d.setPlayer2Puntos(d.getPlayer2Puntos() + 150);
        }

        return d;

    }

    // PRINT A CONSOLA
    public void print(String s) {
        Log.d(TAG, s);
    }

}
