package com.silencedaemon.seta.MainMenus;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.silencedaemon.seta.Consultas.ConsultarEscalera;
import com.silencedaemon.seta.GestionServicios.ServiceStateMain;
import com.silencedaemon.seta.Inventario.GestionInventario;
import com.silencedaemon.seta.Rutas.GenerarRuta;
import com.silencedaemon.seta.Servicios.Mensajeria_Activity;
import com.silencedaemon.seta.Servicios.NewSolicitud;
import com.silencedaemon.seta.R;


public class MainMenuActivity extends AppCompatActivity {
    ImageButton BotNewSolicitud, BotConsDisponibilidad, BotConsInventario, BotMensajeria, BotActEstado;
    ImageButton BotGenerarRuta;
    private Intent IntentToNext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);


        BotNewSolicitud = (ImageButton) findViewById(R.id.IconProgEscalera);
        BotConsDisponibilidad = (ImageButton) findViewById(R.id.IconConsultas);
        BotConsInventario = (ImageButton) findViewById(R.id.IconConsInventario);
        BotMensajeria = (ImageButton) findViewById(R.id.IconMensajeria);
        BotActEstado = (ImageButton) findViewById(R.id.IconActEstado);
        BotGenerarRuta = (ImageButton) findViewById(R.id.IconGenerarRuta);




    }

    @Override
    protected void onResume() {
        super.onResume();

        BotConsDisponibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*IntentToConsDisponibilidad = new Intent(MainMenuActivity.this, ConsultarEscalera.class);
                IntentToConsDisponibilidad.putExtra("origen","main");
                startActivity(IntentToConsDisponibilidad);*/
                IntentToNext = new Intent(MainMenuActivity.this, ConsultarEscalera.class);
                startActivity(IntentToNext);
            }
        });

        BotNewSolicitud.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentToNext = new Intent(MainMenuActivity.this, NewSolicitud.class);
                startActivity(IntentToNext);
            }


        });

        BotGenerarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToNext = new Intent(MainMenuActivity.this, GenerarRuta.class);
                startActivity(IntentToNext);
            }
        });

        BotActEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToNext = new Intent(MainMenuActivity.this, ServiceStateMain.class);
                startActivity(IntentToNext);
            }
        });

        BotMensajeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentToNext = new Intent(MainMenuActivity.this, Mensajeria_Activity.class);
                startActivity(IntentToNext);
            }
        });

        BotConsInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentToNext = new Intent(MainMenuActivity.this, GestionInventario.class);
                startActivity(IntentToNext);
            }
        });


    }
}