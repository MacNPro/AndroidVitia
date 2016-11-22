package com.llamas.vitia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static com.llamas.vitia.Constantes.getBaseRef;

public class Splash extends Activity {

    static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            siguienteActivity(Inicio.class);
        } else {
            siguienteActivity(IniciarSesion.class);
        }

        getBaseRef().keepSynced(true);

    }

    public void siguienteActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
        finish();
    }

}
