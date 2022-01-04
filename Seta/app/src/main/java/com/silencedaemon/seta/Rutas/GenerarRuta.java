package com.silencedaemon.seta.Rutas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.silencedaemon.seta.MainMenus.MainMenu.vista.MainMenuActivity;
import com.silencedaemon.seta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenerarRuta extends AppCompatActivity implements View.OnClickListener {

    // Intento para cambio de ACTIVITY
    Intent IntentToListService;

    // VARIABLES DE AUXILIARES DE CONTROL
    Date DateFechaConsult;
    Date Today;
    int TotalDatos = 0;
    boolean EndConsulta, ConsultaEnProceso;
    RequestQueue requestQueue;

    // Creacion de Vistas
    EditText FechaConsultaRuta, AuxFechaConsultaRuta;
    Button BotConsultarRuta, BotVisualizarRuta;
    ImageButton ButtToHome;

    // Variablea que almacenas la Fechas del Dialogo
    private int diaR, mesR, añoR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_rute);



        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });

        // Instanciacion de Objetos
        BotConsultarRuta = (Button) findViewById(R.id.ButtConsultarRuta);
        BotVisualizarRuta = (Button) findViewById(R.id.ButtVisualizarRuta);
        FechaConsultaRuta = (EditText) findViewById(R.id.fechaConsRuta);
        AuxFechaConsultaRuta = (EditText) findViewById(R.id.AuxfechaConsRuta);
        AuxFechaConsultaRuta.setText("");
        FechaConsultaRuta.setText("");
        FechaConsultaRuta.setOnClickListener(this);
        IntentToListService = new Intent(GenerarRuta.this, ListOfService.class);



    }

    @Override
    protected void onResume() {
        super.onResume();

        BotConsultarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Validamos que se hay elegido una fecha
                if (FechaConsultaRuta.getText().toString().trim().equalsIgnoreCase("")) {
                    FechaConsultaRuta.setError("Elije una fecha");
                    Toast.makeText(getApplicationContext(),"Primero escoge una Fecha",Toast.LENGTH_SHORT).show();
                }// Si no se encuentra una consulta en proceso o si ya se finalizó la consulta de la última fecha ingresada
                else if (EndConsulta == false && ConsultaEnProceso == false){
                    ConsultaEnProceso = true;
                    ConsultarSolicitud("https://mariansr.com/DBConsultaRuta.php?FECHARUTA="+FechaConsultaRuta.getText().toString());
                }else if (EndConsulta == false && ConsultaEnProceso == true){
                    Toast.makeText(getApplicationContext(), "Consulta en proceso, espera un momento", Toast.LENGTH_SHORT).show();

                }
                else if (EndConsulta == true && ConsultaEnProceso == false)
                    Toast.makeText(getApplicationContext(),"Ya realizó la consulta, presione visualizar para verla.",Toast.LENGTH_SHORT).show();

            }
        });

        BotVisualizarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validamos que se hay elegido una fecha
                if (FechaConsultaRuta.getText().toString().trim().equalsIgnoreCase("")) {
                    FechaConsultaRuta.setError("Elije una fecha");
                    Toast.makeText(getApplicationContext(),"Primero escoge una Fecha",Toast.LENGTH_SHORT).show();
                    // SI NO SE HA CONSULTADO LA RUTA Y TAMPOCO ESTA EN PROCESO: SE MUESTRA EL MENSAJE SOLICITANDOLO
                }else if (EndConsulta == false && ConsultaEnProceso == false) {
                    Toast.makeText(getApplicationContext(), "Presiona consultar antes de Visualizar", Toast.LENGTH_SHORT).show();
                    // Si la consulta esta en proceso:
                }else if (ConsultaEnProceso == true){
                    Toast.makeText(getApplicationContext(), "Consulta en proceso, espera un momento", Toast.LENGTH_SHORT).show();
                    // Si ya finalizo la consulta
                }else if (EndConsulta == true && ConsultaEnProceso == false){ // Si tenemos una Fecha y ya se ACTUALIZÓ LA CONSULTA (CONSULTÓ NUEVAMENTE)
                    EndConsulta = false;
                    Toast.makeText(getApplicationContext(), "Ruta del día " + FechaConsultaRuta.getText().toString(), Toast.LENGTH_SHORT).show();
                    startActivity(IntentToListService); // Iniciamos la siguiente activity
                }
            }
        });

    }

    private void ConvertirFechas(){

        // 1. Tomamos el valor de las Fechas y les convertimos a un fomato, para luego poderlas comparar
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // se crea el Formato al que se traduciran las fechas para luego compararlas
            Calendar c = Calendar.getInstance();
            String today = ( c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "" );
            Today = sdf.parse(today);

            DateFechaConsult = sdf.parse(FechaConsultaRuta.getText() + "");

        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(),"Error: Conversión de Fecha", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {

        EndConsulta = false;
        switch (v.getId()){
            case R.id.fechaConsRuta:
                Calendar cal= Calendar.getInstance();

                diaR = cal.get(Calendar.DAY_OF_MONTH);
                mesR = cal.get(Calendar.MONTH);
                añoR = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthString, dayString;
                        if (month <= 8) monthString = "0" + (month+1);
                        else monthString = (month+1) + "";
                        if (dayOfMonth <= 9) dayString = "0" + dayOfMonth;
                        else dayString = dayOfMonth + "";

                        AuxFechaConsultaRuta.setText(year+"-"+monthString+"-"+dayString);
                        FechaConsultaRuta.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                        EndConsulta = false;

                    }
                }, añoR,mesR,diaR);
                datePickerDialog.show();


                break;
        }

    }


    private void ConsultarSolicitud(String URL)
    {

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                TotalDatos = response.length();

                String[] OTs = new String[TotalDatos];
                String[] Clientes = new String[TotalDatos];
                String[] Address = new String[TotalDatos];
                String[] TipoEscalera = new String[TotalDatos];
                String[] Date = new String[TotalDatos];
                String[] EstadoServicio = new String[TotalDatos];
                String[] TipoRuta = new String[TotalDatos];

                for (int i = 0; i < TotalDatos; i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //El objeto JSon devuelve un valor en la CLave "CantidadRuta" = "0", solo si no existe ningún servicio
                        if (!jsonObject.isNull("CantidadRuta")) {
                            Toast.makeText(getApplicationContext(),"No hay ruta para este día",Toast.LENGTH_LONG).show();
                            EndConsulta = false;
                            ConsultaEnProceso = false;
                            break;
                        }else { // Si el servicio si existe nos retorna todos los valores

                            OTs[i] = jsonObject.getString("OTM");
                            Clientes[i] = jsonObject.getString("CLIENTE");
                            Address[i] = jsonObject.getString("ADDRESS");
                            TipoEscalera[i] = jsonObject.getString("TIPOESCALERA") + " - " + jsonObject.getString("PASOS") + " Pasos";
                            Date[i] = jsonObject.getString("FECHAEND") ;//+ " - " + jsonObject.getString("HORA");
                            EstadoServicio[i] = jsonObject.getString("ESTADO_SERVICIO");

                            if (AuxFechaConsultaRuta.getText().toString().equals(jsonObject.getString("FECHAINIT")))
                                TipoRuta[i] = "ENTREGAR";
                            else if (AuxFechaConsultaRuta.getText().toString().equals(jsonObject.getString("FECHAEND")) || EstadoServicio[i].equals("FINALIZADO"))
                                TipoRuta[i] = "RECOGER";
                            else TipoRuta[i] = "INDEFINIDO";
                            //Toast.makeText(getApplicationContext(),AuxFechaConsultaRuta.getText().toString()+" - "+jsonObject.getString("FECHAINIT"),Toast.LENGTH_LONG).show();


                            IntentToListService.putExtra("OTs", OTs);
                            IntentToListService.putExtra("CLIENTES", Clientes);
                            IntentToListService.putExtra("ADDRESS", Address);
                            IntentToListService.putExtra("TIPOESCALERA", TipoEscalera);
                            IntentToListService.putExtra("DATE", Date);
                            IntentToListService.putExtra("ESTADO_SERVICIO", EstadoServicio);
                            IntentToListService.putExtra("TIPO_RUTA", TipoRuta);
                            if (i == (TotalDatos - 1)) {
                                EndConsulta = true;
                                ConsultaEnProceso = false;
                                Toast.makeText(getApplicationContext(), TotalDatos + " servicios encontrados ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), "Error GenRuta JsonObject: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        EndConsulta = false;
                        ConsultaEnProceso = false;
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EndConsulta = false;
                ConsultaEnProceso = false;
                //Toast.makeText(getApplicationContext(), "NO EXISTEN servicios programados en esa Fecha", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Posible error de Conexión: " + error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }



}
