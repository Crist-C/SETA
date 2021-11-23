package com.silencedaemon.seta.MainMenus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.silencedaemon.seta.GestionServicios.ServiceStateMain;
import com.silencedaemon.seta.Servicios.NewSolicitud;
import com.silencedaemon.seta.Rutas.GenerarRuta;
import com.silencedaemon.seta.Antiguos.ProgramEscaleraMain;
import com.silencedaemon.seta.R;


public class MainMenuHSE extends AppCompatActivity {
    Button BotProgEscalera, BotConsDisponibilidad, BotConsInventario, BotQRread, BotActEstado;
    Button BotGenerarRuta;
    private Intent IntentToProgEscalera, IntentToConsDisponibilidad;
    private Intent IntentToGenRuta, IntentToActEstado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_hse);


        BotProgEscalera = (Button) findViewById(R.id.IconProgEscalera);
        BotConsDisponibilidad = (Button) findViewById(R.id.IconConsultas);
        BotConsInventario = (Button) findViewById(R.id.IconConsInventario);
        BotQRread = (Button) findViewById(R.id.IconQR);
        BotActEstado = (Button) findViewById(R.id.IconActEstado);
        BotGenerarRuta = (Button) findViewById(R.id.IconGenerarRuta);




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
                IntentToConsDisponibilidad = new Intent(MainMenuHSE.this, NewSolicitud.class);
                startActivity(IntentToConsDisponibilidad);
            }
        });

        BotProgEscalera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentToProgEscalera = new Intent(MainMenuHSE.this, ProgramEscaleraMain.class);
                startActivity(IntentToProgEscalera);
            }


        });

        BotGenerarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToGenRuta = new Intent(MainMenuHSE.this, GenerarRuta.class);
                startActivity(IntentToGenRuta);
            }
        });

        BotActEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToActEstado = new Intent(MainMenuHSE.this, ServiceStateMain.class);
                startActivity(IntentToActEstado);
            }
        });

    }
}
