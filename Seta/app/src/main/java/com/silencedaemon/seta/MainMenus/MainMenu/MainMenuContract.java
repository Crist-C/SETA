package com.silencedaemon.seta.MainMenus.MainMenu;

import android.content.Intent;
import android.view.View;

import com.silencedaemon.seta.Consultas.ConsultarEscalera;
import com.silencedaemon.seta.MainMenus.MainMenu.vista.MainMenuActivity;

public interface MainMenuContract {

    interface view{



    }


    interface presenter{

        public void goToNuevaSolicitud();
        public void goToConsultarDisponibilidad();
        public void goToConsultarInventario();
        public void goToMensajeria();
        public void goToActualizarEstado();
        public void goToGenerarRuta();

    }

}
