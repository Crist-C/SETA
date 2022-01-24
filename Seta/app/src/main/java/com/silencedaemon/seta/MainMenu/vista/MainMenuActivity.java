package com.silencedaemon.seta.MainMenu.vista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.silencedaemon.seta.Antiguos.ProgramEscaleraMain;
import com.silencedaemon.seta.GestionServicios.ServiceStateMain;
import com.silencedaemon.seta.Inventario.GestionInventario;
import com.silencedaemon.seta.MainMenu.presenter.*;
import com.silencedaemon.seta.MainMenu.MainMenuContract;
import com.silencedaemon.seta.R;
import com.silencedaemon.seta.Rutas.GenerarRuta;
import com.silencedaemon.seta.Servicios.Mensajeria_Activity;
import com.silencedaemon.seta.Servicios.NewSolicitud;


public class MainMenuActivity extends AppCompatActivity implements MainMenuContract.view {

    private MainMenuContract.presenter presenter;

    private ImageButton BotNewSolicitud, BotConsDisponibilidad, BotConsInventario, BotMensajeria, BotActEstado;
    private ImageButton BotGenerarRuta;


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

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        presenter = new MainMenuPresenter(this, context);
        return super.onCreateView(name, context, attrs);
    }


    @Override
    protected void onResume() {
        super.onResume();



        BotConsDisponibilidad.setOnClickListener(v -> { presenter.goToActivity(ProgramEscaleraMain.class); });

        BotNewSolicitud.setOnClickListener(v -> presenter.goToActivity(NewSolicitud.class));

        BotGenerarRuta.setOnClickListener(v -> presenter.goToActivity(GenerarRuta.class));

        BotActEstado.setOnClickListener(v -> presenter.goToActivity(ServiceStateMain.class));

        BotMensajeria.setOnClickListener(view -> presenter.goToActivity(Mensajeria_Activity.class));

        BotConsInventario.setOnClickListener(view -> presenter.goToActivity(GestionInventario.class));


    }
}
