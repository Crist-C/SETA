package com.silencedaemon.seta.MainMenus.MainMenu.vista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.silencedaemon.seta.MainMenus.MainMenu.MainMenuContract;
import com.silencedaemon.seta.MainMenus.MainMenu.presenter.MainMenuPresenter;
import com.silencedaemon.seta.R;


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



        BotConsDisponibilidad.setOnClickListener(v -> { presenter.goToConsultarDisponibilidad(); });

        BotNewSolicitud.setOnClickListener(v -> presenter.goToNuevaSolicitud());

        BotGenerarRuta.setOnClickListener(v -> presenter.goToGenerarRuta());

        BotActEstado.setOnClickListener(v -> presenter.goToActualizarEstado());

        BotMensajeria.setOnClickListener(view -> presenter.goToMensajeria());

        BotConsInventario.setOnClickListener(view -> presenter.goToConsultarInventario());


    }
}
