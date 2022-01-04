package com.silencedaemon.seta.MainMenus.MainMenu.presenter;

import android.content.Context;
import android.content.Intent;

import com.silencedaemon.seta.Consultas.ConsultarEscalera;
import com.silencedaemon.seta.GestionServicios.ServiceStateMain;
import com.silencedaemon.seta.Inventario.GestionInventario;
import com.silencedaemon.seta.MainMenus.MainMenu.MainMenuContract;
import com.silencedaemon.seta.Rutas.GenerarRuta;
import com.silencedaemon.seta.Servicios.Mensajeria_Activity;
import com.silencedaemon.seta.Servicios.NewSolicitud;

public class MainMenuPresenter implements MainMenuContract.presenter {

    private MainMenuContract.view vista;
    private Context context;
    private Intent IntentToNext;


    public MainMenuPresenter(MainMenuContract.view vista, Context context) {
        this.vista = vista;
        this.context = context;
    }

    @Override
    public void goToNuevaSolicitud() {
        IntentToNext = new Intent(context, NewSolicitud.class);
        context.startActivity(IntentToNext);
    }

    @Override
    public void goToConsultarDisponibilidad() {
        IntentToNext = new Intent(context, ConsultarEscalera.class);
        //IntentToConsDisponibilidad.putExtra("origen","main");
        context.startActivity(IntentToNext);
    }

    @Override
    public void goToConsultarInventario() {
        IntentToNext = new Intent(context, GestionInventario.class);
        context.startActivity(IntentToNext);
    }

    @Override
    public void goToMensajeria() {
        IntentToNext = new Intent(context, Mensajeria_Activity.class);
        context.startActivity(IntentToNext);
    }

    @Override
    public void goToActualizarEstado() {
        IntentToNext = new Intent(context, ServiceStateMain.class);
        context.startActivity(IntentToNext);
    }

    @Override
    public void goToGenerarRuta() {
        IntentToNext = new Intent(context, GenerarRuta.class);
        context.startActivity(IntentToNext);
    }
}
