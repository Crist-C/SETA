package com.examplecodes.Seta.Rutas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.examplecodes.Seta.MainMenus.MainMenuActivity;
import com.examplecodes.learnapplication.R;

public class ListOfService extends AppCompatActivity {

    ImageButton ButtToHome;
    String[] OTs;
    String[] Clientes;
    String[] TipoEscalera;
    String[] Address;
    String[] Date;
    String[] EstadoServicio;
    String[] TipoRuta;

    ListView ListServices;
    Button NuevaConsulta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_service);

        OTs= getIntent().getStringArrayExtra("OTs");
        Clientes = getIntent().getStringArrayExtra("CLIENTES");
        TipoEscalera = getIntent().getStringArrayExtra("TIPOESCALERA");
        Address = getIntent().getStringArrayExtra("ADDRESS");
        Date = getIntent().getStringArrayExtra("DATE");
        EstadoServicio = getIntent().getStringArrayExtra("ESTADO_SERVICIO");
        TipoRuta = getIntent().getStringArrayExtra("TIPO_RUTA");

        ListServices = (ListView) findViewById(R.id.listOfServices);
        ListServices.setAdapter(new ListServiceAdapter(this, OTs, Clientes, TipoEscalera, Address, Date, EstadoServicio, TipoRuta));

        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });





    }



}
