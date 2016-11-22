package com.llamas.vitia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.llamas.vitia.Model.Duelo;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.key;
import static com.llamas.vitia.Constantes.getBaseRef;
import static com.llamas.vitia.Constantes.getUser;
import static com.llamas.vitia.R.id.foreignProfile;
import static com.llamas.vitia.R.id.profile;

public class DueloActivity extends Activity {

    public static String TAG = "DUELO";

    Duelo duelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duelo);

        Intent i = getIntent();
        String type = i.getStringExtra("Type");

        switch (type){

            case "nuevo":

                final String foreignID = i.getStringExtra("ID");
                getBaseRef().child("usuarios").child(foreignID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("profileURL").exists()){
                            String foreignProfile = dataSnapshot.child("profileURL").getValue(String.class);
                            crearNuevoDuelo(foreignID, foreignProfile);
                        } else {
                            crearNuevoDuelo(foreignID, "http://www.nowseethis.org/avatars/default/missing.gif");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case "continuar":

                continuarConDuelo();

                break;

            default:
                break;

        }

    }


    /** ------ FUNCIONES PRIMARIAS ------ **/


    private void crearNuevoDuelo(String foreignID, String foreignProfile){

        DatabaseReference baseRef = getBaseRef();
        String key = baseRef.child("duelos").push().getKey();
        String IDClient = getUser().getUid();

        duelo = new Duelo(key, IDClient, foreignID, String.valueOf(getUser().getPhotoUrl()), foreignProfile, IDClient, 1, 0,0);
        Map<String, Object> dueloMap = duelo.toMap();

        print(duelo.getPlayer1());
        print(duelo.getPlayer2());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/duelos/" + key, dueloMap);
        childUpdates.put("/usuarios/" + duelo.getPlayer1() + "/duelos/" + key, dueloMap);
        childUpdates.put("/usuarios/" + duelo.getPlayer2() + "/duelos/" + key, dueloMap);

        baseRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null){
                    print(databaseError.getMessage());
                }
            }
        });

    }

    private void continuarConDuelo(){

    }

    private void guardarDuelo(){
        Map<String, Object> dueloMap = duelo.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/duelos/" + key, dueloMap);
        childUpdates.put("/usuarios/" + duelo.getPlayer1() + "/duelos/" + key, dueloMap);
        childUpdates.put("/usuarios/" + duelo.getPlayer2() + "/duelos/" + key, dueloMap);

        getBaseRef().updateChildren(childUpdates);
    }

    /** ------ FUNCIONES SECUNDARIAS ------ **/

    // PRINT A CONSOLA
    public void print(String s){
        Log.d(TAG, s);
    }

}
