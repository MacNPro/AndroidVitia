package com.llamas.vitia;

import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ImageView;
import android.support.v4.app.FragmentPagerAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.llamas.vitia.Adapters.ContrincantesAdapter;
import com.llamas.vitia.CustomClasses.HeavyTextView;
import com.llamas.vitia.Fragments.ListaContrincantes;
import com.llamas.vitia.Fragments.ListaDuelos;
import com.llamas.vitia.Model.Contrincante;
import com.llamas.vitia.Model.Duelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.llamas.vitia.Constantes.fondosDuelos;
import static com.llamas.vitia.Constantes.getBaseRef;
import static com.llamas.vitia.Constantes.getUser;
import static com.llamas.vitia.Constantes.getUserRef;

public class Inicio extends FragmentActivity {

    public static String TAG = "INICIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // CAST IMAGEVIEW + CARGAR IMAGEN DE FONDO
        Glide.with(this).load(R.drawable.fondo).into((ImageView) findViewById(R.id.fondo));

        // CAST PROFILE IMAGE + LOAD
        Glide.with(this).load(String.valueOf(getUser().getPhotoUrl())).bitmapTransform(new CropCircleTransformation(this)).into((ImageView) findViewById(R.id.profile));

        getUserInfo();

    }

    /**
     * ------ FUNCIONES PRIMARIAS ------
     **/

    //CAST VIEWPAGER + TABLAYOUT
    private void createViewPager(InicioPagerAdapter adapter){

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //OBTENER INFO DE USUARIO
    public void getUserInfo() {

        getUserRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue(String.class);
                int nivel = dataSnapshot.child("nivel").getValue(Integer.class);
                int puntos = dataSnapshot.child("puntos").getValue(Integer.class);
                int numeroDeDuelos = dataSnapshot.child("numeroDeDuelos").getValue(Integer.class);

                HeavyTextView nombrett = (HeavyTextView) findViewById(R.id.nombre);
                HeavyTextView niveltt = (HeavyTextView) findViewById(R.id.nivel);
                HeavyTextView puntostt = (HeavyTextView) findViewById(R.id.puntos);
                HeavyTextView duelostt = (HeavyTextView) findViewById(R.id.numeroDeDuelos);

                nombrett.setText(nombre);
                niveltt.setText("" + nivel);
                puntostt.setText("" + puntos);
                duelostt.setText("" + numeroDeDuelos);

                ArrayList<String> ids = new ArrayList<>();
                ArrayList<Duelo> duelos = new ArrayList<>();
                for (DataSnapshot duelo : dataSnapshot.child("duelos").getChildren()) {

                    Duelo d = duelo.getValue(Duelo.class);

                    String p1 = duelo.child("player1ID").getValue(String.class);
                    String p2 = duelo.child("player2ID").getValue(String.class);

                    if (!p1.equals(getUser().getUid())) {
                        ids.add(p1);
                    } else if (!p2.equals(getUser().getUid())) {
                        ids.add(p2);
                    }

                    print(p1 + "\n" + p2);

                    duelos.add(d);

                }

                getContrincantes(ids, duelos);
                print(ids.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // OBTENER CONTRINCANTES
    public void getContrincantes(final ArrayList<String> ids, final ArrayList<Duelo> duelos) {
        getBaseRef().child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = getUser().getUid();
                ArrayList<Contrincante> contrincantes = new ArrayList<>();
                for (DataSnapshot usuario : dataSnapshot.getChildren()) {

                    if (!usuario.getKey().equals(id) && !ids.contains(usuario.getKey())) {
                        contrincantes.add(new Contrincante(
                                usuario.getKey(),
                                usuario.child("nombre").getValue(String.class),
                                usuario.child("nivel").getValue(Integer.class),
                                new Random().nextInt(fondosDuelos.length)
                        ));
                    }

                }
                InicioPagerAdapter adapter = new InicioPagerAdapter(getSupportFragmentManager(), contrincantes, duelos);
                createViewPager(adapter);
                print("Numero de contrincantes: " + contrincantes.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * ------ FUNCIONES SECUNDARIAS ------
     **/

    // PRINT A CONSOLA
    public void print(String s) {
        Log.d(TAG, s);
    }

    /**
     * ------ VIEWPAGER ADAPTER ------
     **/

    public class InicioPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{"Contrincantes", "Duelos"};
        ArrayList<Contrincante> contrincantes;
        ArrayList<Duelo> duelos;

        InicioPagerAdapter(FragmentManager fm, ArrayList<Contrincante> contrincantes, ArrayList<Duelo> duelos) {
            super(fm);
            this.contrincantes = contrincantes;
            this.duelos = duelos;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ListaContrincantes.init(contrincantes);
                case 1:
                    return ListaDuelos.init(duelos);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
