package com.silencedaemon.seta.MainMenu.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.silencedaemon.seta.Consultas.ConsultarEscalera;
import com.silencedaemon.seta.GestionServicios.ServiceStateMain;
import com.silencedaemon.seta.Inventario.GestionInventario;
import com.silencedaemon.seta.R;
import com.silencedaemon.seta.Rutas.GenerarRuta;
import com.silencedaemon.seta.Servicios.Mensajeria_Activity;
import com.silencedaemon.seta.Servicios.NewSolicitud;

import java.util.ArrayList;

public class MainMenuRV extends AppCompatActivity {

    private RecyclerView recyclerViewItems;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<ContenedorItemsMenu> contenedorItemsMenuArrayList;
    private AdaptadorMainMenu adaptadorMainMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_rv);

        recyclerViewItems = findViewById(R.id.rvItemsMainMenu);
        gridLayoutManager = new GridLayoutManager(this,2);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerViewItems.setLayoutManager(gridLayoutManager);

        contenedorItemsMenuArrayList = new ArrayList<ContenedorItemsMenu>();
        contenedorItemsMenuArrayList.add( new ContenedorItemsMenu(R.drawable.calendario5_150x120,"Nueva Solicitud", NewSolicitud.class));
        contenedorItemsMenuArrayList.add( new ContenedorItemsMenu(R.drawable.moto_mensajeria3,"Mensajeria", Mensajeria_Activity.class));
        contenedorItemsMenuArrayList.add( new ContenedorItemsMenu(R.drawable.actualizarestado7,"Gestión de Servicios", ServiceStateMain.class));
        contenedorItemsMenuArrayList.add( new ContenedorItemsMenu(R.drawable.generarruta2,"Rutas", GenerarRuta.class));
        contenedorItemsMenuArrayList.add( new ContenedorItemsMenu(R.drawable.conocerdisponibilidad5,"Consultas", ConsultarEscalera.class));
        contenedorItemsMenuArrayList.add( new ContenedorItemsMenu(R.drawable.inventarioescaleras4,"Inventario", GestionInventario.class));


        adaptadorMainMenu = new AdaptadorMainMenu(this,contenedorItemsMenuArrayList);
        recyclerViewItems.setAdapter(adaptadorMainMenu);
    }
}