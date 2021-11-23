package com.examplecodes.Seta.Servicios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.examplecodes.Seta.MainMenus.MainMenuActivity;
import com.examplecodes.learnapplication.R;

import java.util.HashMap;
import java.util.Map;

public class Mensajeria_Activity extends AppCompatActivity {

    ImageButton ButtToHome;
    Button SolicitarMensajeria;
    ToggleButton ButMoto, ButCarro;
    Spinner ZonaMoto, FuncionMoto, CamionCamioneta, FuncionCarro;
    EditText ObservMoto, ObservCarro;

    ArrayAdapter ArrAdapZonaMoto, ArrAdapFuncionMoto, ArrAdapCamionCamioneta, ArrAdapFuncionCarro;

    RequestQueue requestQueueToProgMens;
    String URLProgramMensajeria = "https://mariansr.com/DBProgramarMensajeria.php?";
    String Mensajero, Funcion, Zona, Vehiculo, Observaciones, FechaSolMens;

    Intent IntentToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajeria);


        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });


        SolicitarMensajeria = (Button) findViewById(R.id.SolictarMensajeria);
        ButMoto = (ToggleButton) findViewById(R.id.MotoTB);
        ButCarro = (ToggleButton) findViewById(R.id.CamionButton);
        ZonaMoto = (Spinner) findViewById(R.id.SZonaMoto);
        FuncionMoto = (Spinner) findViewById(R.id.SFuncionMoto);
        CamionCamioneta = (Spinner) findViewById(R.id.SCamionCamioneta);
        FuncionCarro = (Spinner) findViewById(R.id.FuncionCamionSpinner);
        ObservMoto = (EditText) findViewById(R.id.ETObservMoto);
        ObservCarro = (EditText) findViewById(R.id.ETObservCarro);

        ArrAdapZonaMoto = ArrayAdapter.createFromResource(getApplicationContext(),R.array.ZonaMoto,android.R.layout.simple_dropdown_item_1line);
        ArrAdapZonaMoto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ZonaMoto.setAdapter(ArrAdapZonaMoto);

        ArrAdapFuncionMoto = ArrayAdapter.createFromResource(getApplicationContext(),R.array.FuncionesMensajeroMoto,android.R.layout.simple_dropdown_item_1line);
        ArrAdapFuncionMoto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FuncionMoto.setAdapter(ArrAdapFuncionMoto);

        ArrAdapCamionCamioneta = ArrayAdapter.createFromResource(getApplicationContext(),R.array.TipoVehiculo,android.R.layout.simple_dropdown_item_1line);
        ArrAdapCamionCamioneta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CamionCamioneta.setAdapter(ArrAdapCamionCamioneta);

        ArrAdapFuncionCarro = ArrayAdapter.createFromResource(getApplicationContext(),R.array.FuncionCarro,android.R.layout.simple_dropdown_item_1line);
        ArrAdapFuncionCarro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FuncionCarro.setAdapter(ArrAdapFuncionCarro);

        CamionCamioneta.setEnabled(false);
        FuncionCarro.setEnabled(false);
        ObservCarro.setEnabled(false);



    }

    @Override
    protected void onResume() {
        super.onResume();

        SolicitarMensajeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ButMoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ButMoto.isChecked()){
                    ZonaMoto.setEnabled(false);
                    FuncionMoto.setEnabled(false);
                    ObservMoto.setEnabled(false);


                } else if (ButMoto.isChecked()){

                    ZonaMoto.setEnabled(true);
                    FuncionMoto.setEnabled(true);
                    ObservMoto.setEnabled(true);

                    if (ButCarro.isChecked()){
                        ButCarro.setChecked(false);
                        CamionCamioneta.setEnabled(false);
                        FuncionCarro.setEnabled(false);
                        ObservCarro.setEnabled(false);
                    }

                }


            }
        });


        ButCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ButCarro.isChecked()){

                    CamionCamioneta.setEnabled(false);
                    FuncionCarro.setEnabled(false);
                    ObservCarro.setEnabled(false);

                } else if (ButCarro.isChecked()){

                    CamionCamioneta.setEnabled(true);
                    FuncionCarro.setEnabled(true);
                    ObservCarro.setEnabled(true);

                    if (ButMoto.isChecked()){
                        ButMoto.setChecked(false);
                        ZonaMoto.setEnabled(false);
                        FuncionMoto.setEnabled(false);
                        ObservMoto.setEnabled(false);
                    }

                }


            }
        });


    }


    private void IndicadoresDeProceso(boolean state, String from) {


                if (state == Finalizado) {
                    ProgServiceSuccess = false;
                    ProgramInProcess = false;
                    ProgramProgressBar.setVisibility(View.INVISIBLE);
                    ConfSolicitud.setEnabled(true);
                    NombreRecibe.setEnabled(true);
                    TelRecibe.setEnabled(true);
                    AreaRecive.setEnabled(true);
                    EmailRecibe.setEnabled(true);

                    OTM.setEnabled(true);
                    ClienteName.setEnabled(true);
                    Nit.setEnabled(true);
                    ClienteAddress.setEnabled(true);
                    HoraEntrega.setEnabled(true);

                }

                if (state == EnProceso) {
                    ProgServiceSuccess = false;
                    ProgramInProcess = true;
                    ProgramProgressBar.setVisibility(View.VISIBLE);
                    ConfSolicitud.setEnabled(false);
                    NombreRecibe.setEnabled(false);
                    TelRecibe.setEnabled(false);
                    AreaRecive.setEnabled(false);
                    EmailRecibe.setEnabled(false);

                    OTM.setEnabled(false);
                    ClienteName.setEnabled(false);
                    Nit.setEnabled(false);
                    ClienteAddress.setEnabled(false);
                    HoraEntrega.setEnabled(false);

                }

    }

    private void ProgramarServicio(){

        String URL ="https://mariansr.com/DBProgramService.php?";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //int i = response.indexOf("_");
                //String ProgramResponse = response.substring(0,i);
                String ProgramResponse = response;
                if (!ProgramResponse.equals("NOTSUCCESS")){
                    //IndicadoresDeProceso(Finalizado,"ProgService");
                    //ProgServiceSuccess = true;
                    Toast.makeText(getApplicationContext(), "Solicitud " + ProgramResponse + " creada con éxito : )", Toast.LENGTH_LONG).show();
                    IntentToHome = new Intent(Mensajeria_Activity.this,MainMenuActivity.class);
                    startActivity(IntentToHome);
                }else if (ProgramResponse.equals("NOTSUCCESS")){
                    IndicadoresDeProceso(Finalizado,"ProgService");
                    Toast.makeText(getApplicationContext(), "Fallo la Solicitud. Intente nuevamente " +response, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error ProgEscSecond - EjecProgram BadConexión: " + error.toString(),Toast.LENGTH_LONG).show();
                IndicadoresDeProceso(Finalizado, "ProgService");
            }
        }){


            // Mapa de toda la Información con la que se solicitará el servicio. <-- IMPORTANTE
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parametros = new HashMap<String, String>();
                //parametros.put("ESTADO_SERVICIO", "PROGRAMADO");
                parametros.put("MENSAJERO",dataModel.LadderType);
                parametros.put("FUNCION",dataModel.LadderSteps);
                parametros.put("ZONA", dataModel.NumLadderReference);
                parametros.put("VEHICULO", dataModel.AndamioType);
                parametros.put("OBSERVACIONES", dataModel.NumAndamReference);
                parametros.put("FECHA", dataModel.Secciones);

                return parametros;
            }
        };
        requestQueueToProgMens= Volley.newRequestQueue(this);
        requestQueueToProgMens.add(stringRequest);


    }




}