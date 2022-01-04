package com.silencedaemon.seta.Inventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.silencedaemon.seta.MainMenu.vista.MainMenuActivity;
import com.silencedaemon.seta.R;
import com.google.android.material.textfield.TextInputEditText;

public class InvEscalera extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //**********            VIEW OBJECTS            ***************//
    ImageButton ButtToHome;
    ToggleButton TipoEscalera;
    TextView TittleCardView;
    Switch AptaParaUso;
    TextInputEditText UbicacionActual, EstadoEstructura, UltimoServicio, UltimaInspeccion;
    TextInputEditText ProximaInspeccion, CapacidadMaximaCarga, Observaciones;
    private Button Consultar, Editar, Aplicar;
    CardView CardViewData;

    private Spinner RefEscleraSpinner, PasosSpinner;
    private ArrayAdapter<CharSequence> SpinnerAdapter;

    //**********            VIEW OBJECTS            ***************//

    //**********            VARIABLES            ***************//
    private String TipoEscaleraToConsult = "Tijera";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inv_escalera);

        //*****************     INSTANCIAS DE LOS OBJETOS           ************************//
        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });

        TipoEscalera = (ToggleButton)findViewById(R.id.ButtonTipoEscalera);

        RefEscleraSpinner = (Spinner)findViewById(R.id.SpinnerRefEscalera);
        //RefEscleraSpinner.setOnItemSelectedListener(this);

        PasosSpinner = (Spinner)findViewById(R.id.SpinnerPasos);
        PasosSpinner.setOnItemSelectedListener(this);

        TittleCardView = (TextView) findViewById(R.id.textTittleCardView);
        AptaParaUso = (Switch) findViewById(R.id.switchAptoParaUso);
        UbicacionActual = (TextInputEditText) findViewById(R.id.TextUbicacionActual);
        EstadoEstructura = (TextInputEditText) findViewById(R.id.TextEstadoEstructura);
        UltimoServicio = (TextInputEditText) findViewById(R.id.TextUltimoServicio);
        UltimaInspeccion = (TextInputEditText) findViewById(R.id.TextUltimaInspeccion);
        ProximaInspeccion = (TextInputEditText) findViewById(R.id.TextUltimaInspeccion);
        CapacidadMaximaCarga = (TextInputEditText) findViewById(R.id.TextCapacidadMax);
        Observaciones = (TextInputEditText) findViewById(R.id.TextObservaciones);

        Consultar = (Button) findViewById(R.id.ButtConsultarGesInvEscalera);
        Editar =  (Button) findViewById(R.id.buttonEditar);
        Aplicar =  (Button) findViewById(R.id.buttonAplicar);

        CardViewData = (CardView) findViewById(R.id.CardViewData);
        //*****************     INSTANCIAS DE LOS OBJETOS           ************************//

        if (TipoEscalera.isChecked())
            SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.ListaEscaleraExpansivaPasos, R.layout.spinner_layout);
        else if (!TipoEscalera.isChecked())
            SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.ListaEscaleraTijeraPasos, R.layout.spinner_layout);
        else
            SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.defaultVersion, R.layout.spinner_layout);

        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PasosSpinner.setAdapter(SpinnerAdapter);
        SpinnerAdapter(PasosSpinner.getSelectedItem().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();

        TipoEscalera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TipoEscalera.isChecked()){

                    TipoEscaleraToConsult = "Expansiva";
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.ListaEscaleraExpansivaPasos, R.layout.spinner_layout);
                }
                else if (!TipoEscalera.isChecked()){

                    TipoEscaleraToConsult = "Expansiva";
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.ListaEscaleraTijeraPasos, R.layout.spinner_layout);
                }
                else SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.defaultVersion, R.layout.spinner_layout);

                SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                PasosSpinner.setAdapter(SpinnerAdapter);

                SpinnerAdapter(PasosSpinner.getSelectedItem().toString());
            }
        });

        Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Consultando...",Toast.LENGTH_SHORT).show();
                CardViewData.setVisibility(View.VISIBLE);
                TittleCardView.setText(TipoEscaleraToConsult +" "+PasosSpinner.getSelectedItem()+" Pasos No. "+RefEscleraSpinner.getSelectedItem());
            }
        });

        Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Editar...",Toast.LENGTH_SHORT).show();
                Habilitar_Deshabilitar(true);
            }
        });


        Aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Aplicando...",Toast.LENGTH_SHORT).show();
                Habilitar_Deshabilitar(false);
            }
        });


    }

    private void Habilitar_Deshabilitar(boolean Si_Habilitar){

        AptaParaUso.setEnabled(Si_Habilitar);
        UbicacionActual.setEnabled(Si_Habilitar);
        EstadoEstructura.setEnabled(Si_Habilitar);
        UltimoServicio.setEnabled(Si_Habilitar);
        UltimaInspeccion.setEnabled(Si_Habilitar);
        ProximaInspeccion.setEnabled(Si_Habilitar);
        CapacidadMaximaCarga.setEnabled(Si_Habilitar);
        Observaciones.setEnabled(Si_Habilitar);

    }

    private void Editar(){

    }

    private void AplicarCambios(){

    }

    private void SpinnerAdapter(String Pasos)
    {
        if (!TipoEscalera.isChecked()){
            switch (Pasos) {
                case "3":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T3, R.layout.spinner_layout);
                    break;
                case "4":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T4, R.layout.spinner_layout);
                    break;
                case "5":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T5, R.layout.spinner_layout);
                    break;
                case "6":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T6, R.layout.spinner_layout);
                    break;
                case "7":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T7, R.layout.spinner_layout);
                    break;
                case "8":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T8, R.layout.spinner_layout);
                    break;
                case "9":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T9, R.layout.spinner_layout);
                    break;
                case "10":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T10, R.layout.spinner_layout);
                    break;
                case "12":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T12, R.layout.spinner_layout);
                    break;
                default:
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.defaultVersion,R.layout.spinner_layout);
            }
        }
        else if (TipoEscalera.isChecked()){
            switch (Pasos) {
                case "6":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.E6, R.layout.spinner_layout);
                    break;
                case "7":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.E7, R.layout.spinner_layout);
                    break;
                case "10":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.E10, R.layout.spinner_layout);
                    break;
                case "12":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.E12, R.layout.spinner_layout);
                    break;
                case "14":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.E14, R.layout.spinner_layout);
                    break;
                case "16":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.E16, R.layout.spinner_layout);
                    break;
                case "17":
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.E17, R.layout.spinner_layout);
                    break;
                default:
                    SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.defaultVersion,R.layout.spinner_layout);
            }

        }
        else SpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.defaultVersion,R.layout.spinner_layout);

        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RefEscleraSpinner.setAdapter(SpinnerAdapter);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        SpinnerAdapter(PasosSpinner.getSelectedItem().toString());

        //if (view.getId() == R.id.SpinnerRefEscalera);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}