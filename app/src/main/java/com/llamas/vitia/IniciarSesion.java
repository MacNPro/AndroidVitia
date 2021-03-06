package com.llamas.vitia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.llamas.vitia.CustomClasses.MediumTextView;

import java.util.Arrays;
import java.util.HashMap;

import static com.llamas.vitia.Constantes.getBaseRef;
import static com.llamas.vitia.Constantes.getUser;
import static com.llamas.vitia.Constantes.getUserRef;

public class IniciarSesion extends Activity {

    public static String TAG = "INICIAR SESION";

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    CallbackManager mCallbackManager;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicia_sesion);
        mAuth = FirebaseAuth.getInstance();

        // LISTENER DE USUARIO
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    print("onAuthStateChanged:signed_in:" + user.getUid());
                    userDefaults();
                } else {
                    print("onAuthStateChanged:signed_out");
                }
            }
        };

        MediumTextView iniciar = (MediumTextView) findViewById(R.id.iniciar);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuentaConFacebook();
            }
        });

    }

    /**------ FUNCIONES PRIMARIAS ------**/

    // INICIAR SESION CON FACEBOOK
    public void crearCuentaConFacebook(){
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        loginManager.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
    }

    // DEFAULTS DE USUARIO EN CASO DE QUE NO EXISTAN
    public void userDefaults(){

        getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){

                    HashMap<String, Object> defaults = new HashMap<>();
                    defaults.put("/nombre", getUser().getDisplayName());
                    defaults.put("/email", getUser().getEmail());
                    defaults.put("/nivel", 1);
                    defaults.put("/puntos", 0);
                    defaults.put("/numeroDeDuelos", 0);
                    defaults.put("/profileURL", getUser().getPhotoUrl().toString());

                    getUserRef().updateChildren(defaults).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(IniciarSesion.this, Inicio.class);
                            startActivity(i);
                        }
                    });

                } else {

                    Intent i = new Intent(IniciarSesion.this, Inicio.class);
                    startActivity(i);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**------ FUNCIONES SECUNDARIAS ------**/

    // PROCESAR TOKEN DE FACEBOOK A FIREBASE
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(IniciarSesion.this, "Error al Iniciar Sesión",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // PRINT A CONSOLA
    public void print(String s){
        Log.d(TAG, s);
    }

    /**------ FUNCIONES DEFAULT ------**/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
