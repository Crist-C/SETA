package com.silencedaemon.seta.GestionServicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.silencedaemon.seta.MainMenus.MainMenu.vista.MainMenuActivity;
import com.silencedaemon.seta.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceStateMain extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView OTMList;
    ImageButton ButtToHome;
    Button BotConsultarOTM, BotServIncorrecto, BotServCorrecto;
    TextView ViewProgramo, ViewCliente, ViewRecibe, ViewEquiposSolicitados, ViewFechaDesde, ViewFechaHasta, Tittle;
    EditText OTMtoConsult;
    private Intent IntToServiceState2;
    RequestQueue requestQueue;
    String OTMServiceState = "";
    Boolean ConsultaFinalizada = false, ConsultaEnProceso = false;
    int OTMSeleccionada = 0;
    int TotalEPCCprogramados = 0;
    TextInputLayout textInputLayoutOTMService;

    List<String> ListOfOTM = new ArrayList<>();
    ArrayAdapter OTMsAdapter;
    int TotalDatos = 1;

    String mURL = "https://setaapp.000webhostapp.com/DBConsulta.php?OTService="; // Anterior: https://mariansr.com/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_state_main);

        ArrayAdapter<CharSequence> Nombres = ArrayAdapter.createFromResource(this,R.array.NamesList,android.R.layout.simple_dropdown_item_1line);

        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });

        OTMList = (ListView) findViewById(R.id.ListOTM);
        BotConsultarOTM = (Button) findViewById(R.id.ButtConsultarOTM);
        BotServIncorrecto = (Button) findViewById(R.id.ButtonServIncorrecto);
        BotServCorrecto = (Button) findViewById(R.id.ButtonServCorrecto);
        OTMtoConsult = (EditText) findViewById(R.id.OTMtoConsult);
        ViewProgramo = (TextView) findViewById(R.id.ViewProgramo);
        ViewCliente = (TextView) findViewById(R.id.ViewCliente);
        ViewRecibe = (TextView) findViewById(R.id.ViewRecibe);
        ViewEquiposSolicitados = (TextView) findViewById(R.id.ViewEquiposSolicitados);
        ViewFechaDesde = (TextView) findViewById(R.id.ViewFechaDesde);
        ViewFechaHasta = (TextView) findViewById(R.id.ViewFechaHasta);
        Tittle = (TextView) findViewById(R.id.TittleOTMSelected);
        textInputLayoutOTMService = (TextInputLayout) findViewById(R.id.textInputLayout22);
        ConsultaFinalizada = false;

        OTMsAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.otm_list_item,ListOfOTM);
        OTMList.setAdapter(OTMsAdapter);
        OTMList.setOnItemClickListener(this);

        ConsultaFinalizada = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntToServiceState2 = new Intent(getApplicationContext(), ServiceStateSecond.class);

        BotConsultarOTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!OTMtoConsult.getText().toString().isEmpty() && ListOfOTM.isEmpty() && !ConsultaEnProceso){
                    ConsultaEnProceso = true;
                    OTMtoConsult.setEnabled(false);
                    ConsultarServicio(OTMtoConsult.getText().toString());
                }else if (!ListOfOTM.isEmpty()) Toast.makeText(getApplicationContext(),"Presione Nueva Consulta",Toast.LENGTH_SHORT).show();
                else if(OTMtoConsult.getText().toString().isEmpty()) Toast.makeText(getApplicationContext(),"Ingrese una Orden de Trabajo",Toast.LENGTH_SHORT).show();
                else if (ConsultaEnProceso) Toast.makeText(getApplicationContext(),"Consulta en proceso...",Toast.LENGTH_SHORT).show();


            }
        });

        BotServCorrecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConsultaFinalizada == true) {
                    if (!Escalera[OTMSeleccionada].isEmpty())
                        IntToServiceState2.putExtra("0", Escalera[OTMSeleccionada]);
                    if (Andamio[OTMSeleccionada].contains("Unipersonal"))
                        IntToServiceState2.putExtra("1", Andamio[OTMSeleccionada]);
                    if (Andamio[OTMSeleccionada].contains("Andamio") && !Andamio[OTMSeleccionada].contains("Unipersonal"))
                        IntToServiceState2.putExtra("2", Andamio[OTMSeleccionada]);
                    if (TotalEPCCprogramados > 0)                   // EPCC1
                        IntToServiceState2.putExtra("3", EPCCs[OTMSeleccionada]);
                    if (TotalEPCCprogramados > 1)                   // EPCC2
                        IntToServiceState2.putExtra("4", EPCCs[OTMSeleccionada]);
                    if (TotalEPCCprogramados > 2)                   // EPCC3
                        IntToServiceState2.putExtra("5", EPCCs[OTMSeleccionada]);
                    if (TotalEPCCprogramados > 3)                   // EPCC4
                        IntToServiceState2.putExtra("6", EPCCs[OTMSeleccionada]);

                    if (!Extras[OTMSeleccionada].isEmpty())
                        IntToServiceState2.putExtra("7", Extras[OTMSeleccionada]);
                    if (!HSE[OTMSeleccionada].isEmpty())
                        IntToServiceState2.putExtra("8", HSE[OTMSeleccionada]);



                    IntToServiceState2.putExtra("EstadoActual", OTMServiceState);
                    IntToServiceState2.putExtra("OTM", OTMtoConsult.getText().toString());
                    ConsultaFinalizada = false;
                    startActivity(IntToServiceState2);
                }
                else Toast.makeText(getApplicationContext(),"Primero consulte un Servicio", Toast.LENGTH_LONG).show();
            }
        });

        BotServIncorrecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsultaFinalizada = false;

                ListOfOTM.clear();
                OTMsAdapter.notifyDataSetChanged();
                OTMtoConsult.setEnabled(true);
                OTMtoConsult.setText("");
                ViewProgramo.setText("");
                ViewCliente.setText("");
                ViewEquiposSolicitados.setText("");
                ViewFechaDesde.setText("");
                ViewFechaHasta.setText("");
                //ViewClienteAddress.setText("");
                ViewRecibe.setText("");
                OTMtoConsult.setCursorVisible(true);
                Toast.makeText(getApplicationContext(), "Ingresa la OTM nuevamente", Toast.LENGTH_LONG ).show();

            }
        });

    }

    String[] Programo;
    String[] Recibe;
    String[] Andamio;
    String[] EPCCs;
    String[] HSE;
    String[] Extras;
    String[] IDsService;
    String[] Escalera;
    String[] DateInit;
    String[] DateEnd;
    String[] OTs;
    String[] Clientes;
    String[] Address;

    String[] EstadoServicio;
    String[] TipoRuta;

    private void ConsultarServicio(String mOTM)
    {
        final String URL= (mURL+mOTM+"&&From=SS");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            JSONObject Service_Data = null;

            TotalDatos = response.length();
            Programo = new String[TotalDatos];
            Recibe = new String[TotalDatos];
            Andamio = new String[TotalDatos];
            EPCCs = new String[TotalDatos];
            HSE = new String[TotalDatos];
            Extras = new String[TotalDatos];
            IDsService = new String[TotalDatos];
            Escalera = new String[TotalDatos];
            DateInit = new String[TotalDatos];
            DateEnd = new String[TotalDatos];
            OTs = new String[TotalDatos];
            Clientes = new String[TotalDatos];
            Address = new String[TotalDatos];

            EstadoServicio = new String[TotalDatos];
            TipoRuta = new String[TotalDatos];

            Toast.makeText(getApplicationContext(),"Total Datos: "+response.length(),Toast.LENGTH_SHORT).show();
            for (int i = 0; i < response.length(); i++) {
                try {

                    Service_Data = response.getJSONObject(i);
                    //El objeto JSon devuelve un valor en la CLave "OTM" = "NoExist", solo si no existe ningún servicio

                    if (!Service_Data.isNull("OTM_NE")) {
                        Toast.makeText(getApplicationContext(),"Este servicio NO EXISTE",Toast.LENGTH_LONG).show();
                        ConsultaFinalizada = false;
                        OTMtoConsult.setEnabled(true);
                        break;

                    }else { // Si el servicio si existe nos retorna todos los valores

                        IDsService[i] = Service_Data.getString("ID_SERVICIO");
                        OTs[i] = Service_Data.getString("OTM");
                        Programo[i] = Service_Data.getString("NOMBREPROGRAMA");
                        Clientes[i] = Service_Data.getString("CLIENTE");
                        Recibe[i] = Service_Data.getString("NOMBRERECIBE");

                        // Equipos

                        if (Service_Data.has("TIPOESCALERA") && !Service_Data.getString("TIPOESCALERA").isEmpty()) {
                            Escalera[i] = (Service_Data.getString("TIPOESCALERA") + " " + Service_Data.getString("PASOS") + " Pasos N." + Service_Data.getString("NUM_ESCALERA"));
                        }
                        else Escalera[i] = "";
                        //Toast.makeText(getApplicationContext(), "Escalera " + Escalera[i], Toast.LENGTH_SHORT).show();

                        if (Service_Data.has("ANDAMIO") && !Service_Data.getString("ANDAMIO").isEmpty()){
                            if (Service_Data.getString("ANDAMIO").equals("Certificado")){
                                Andamio[i] = ("Andamio " + Service_Data.getString("ANDAMIO_SEC") + " secciones");
                            }else if (Service_Data.getString("ANDAMIO").equals("Unipersonal")){
                                Andamio[i] = ("And."+Service_Data.getString("ANDAMIO") + " No. "+ Service_Data.getString("ANDAMIO_REF"));
                            }
                        }else Andamio[i] = "";
                        //Toast.makeText(getApplicationContext(), "Andamio " + Andamio[i], Toast.LENGTH_SHORT).show();

                        if (Service_Data.has("PERSONA_HSE") && !Service_Data.getString("PERSONA_HSE").equals("0"))
                            HSE[i] = Service_Data.getString("PERSONA_HSE");
                        else HSE[i] = "";
                        //Toast.makeText(getApplicationContext(), "HSE " + HSE[i], Toast.LENGTH_SHORT).show();

                        if (Service_Data.has("EQU_DESCENSO") && Service_Data.getString("EQU_DESCENSO").equals("1"))
                            Extras[i] = "Equ.Descenso";
                        else if (Service_Data.has("EQU_DESCENSO") && Service_Data.getString("EQU_DESCENSO").equals("0"))
                            Extras[i] = "";

                        if (Service_Data.has("CUERDA") && Service_Data.getString("CUERDA").equals("1")) {
                            if (!Extras[i].isEmpty()) Extras[i] += " - ";
                            Extras[i] += "Cuerda";
                        }

                        if (Service_Data.has("SENALIZACION") && !Service_Data.getString("SENALIZACION").equals("")){
                            if(!Extras[i].isEmpty()) Extras[i]+= " - ";
                            Extras[i] += (Service_Data.getString("SENALIZACION"));
                        }
                        if (Extras[i] == null || Extras[i].isEmpty()) Extras[i] = "";
                        //Toast.makeText(getApplicationContext(), "Extras " + Extras[i], Toast.LENGTH_SHORT).show();

                        String ResumenEPCC = "";
                        TotalEPCCprogramados = 0;
                        for (int e = 1; e <= 4; e++) {
                            if (Service_Data.has("EPCC"+e) && !Service_Data.getString("EPCC"+e).equals("0")){
                                TotalEPCCprogramados++;
                                if (e == 4) ResumenEPCC += ("EPCC" + Service_Data.getString("EPCC"+e));
                                else ResumenEPCC += ("EPCC" + Service_Data.getString("EPCC"+e) + " - ");
                            }

                        }
                        EPCCs[i] = ResumenEPCC;
                        //Toast.makeText(getApplicationContext(), "EPCCs " + EPCCs[i], Toast.LENGTH_SHORT).show();


                        DateInit[i] = Service_Data.getString("FECHAINIT");
                        DateEnd[i] = Service_Data.getString("FECHAEND");

                        EstadoServicio[i] = Service_Data.getString("ESTADO_SERVICIO");
                        TipoRuta[i] = Service_Data.getString("ESTADO_SERVICIO");

                        ListOfOTM.add(Service_Data.getString("OTM"));
                        OTMsAdapter.notifyDataSetChanged();




                        if (i == 0){

                            OTMSeleccionada = 0;

                            ViewProgramo.setText(Programo[0]);
                            ViewRecibe.setText(Recibe[0]);
                            ViewCliente.setText(Clientes[0]);
                            //ViewClienteAddress.setText(jsonObject.getString("ADDRESS"));
                            if(!Escalera[0].isEmpty()) ViewEquiposSolicitados.setText(Escalera[0]);

                            if(!Andamio[0].isEmpty()){
                                if (ViewEquiposSolicitados.getText().toString().isEmpty()) ViewEquiposSolicitados.setText(Andamio[0]);
                                else  ViewEquiposSolicitados.setText(ViewEquiposSolicitados.getText()+", "+Andamio[0]);
                            }

                            if(!EPCCs[0].isEmpty()){
                                if (ViewEquiposSolicitados.getText().toString().isEmpty()) ViewEquiposSolicitados.setText(EPCCs[0]);
                                else  ViewEquiposSolicitados.setText(ViewEquiposSolicitados.getText()+", "+EPCCs[0]);
                            }

                            if(!Extras[0].isEmpty()){
                                if (ViewEquiposSolicitados.getText().toString().isEmpty()) ViewEquiposSolicitados.setText(Extras[0]);
                                else  ViewEquiposSolicitados.setText(ViewEquiposSolicitados.getText()+", "+Extras[0]);
                            }

                            if(!HSE[0].isEmpty()){
                                if (ViewEquiposSolicitados.getText().toString().isEmpty()) ViewEquiposSolicitados.setText(HSE[0]);
                                else  ViewEquiposSolicitados.setText(ViewEquiposSolicitados.getText()+", "+HSE[0]);
                            }

                            ViewFechaDesde.setText(DateInit[0]);
                            ViewFechaHasta.setText(DateEnd[0]);
                            OTMServiceState = EstadoServicio[0];

                            textInputLayoutOTMService.setVisibility(View.VISIBLE);
                            ConsultaFinalizada = true;
                            ConsultaEnProceso = false;
                        }


                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    ConsultaFinalizada = false;
                    ConsultaEnProceso = false;
                }
            }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), "Error de Conexión:" + error.toString(),Toast.LENGTH_LONG).show();
                ConsultaFinalizada = false;
                ConsultaEnProceso = false;
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ViewProgramo",ViewProgramo.getText().toString());
        outState.putString("ViewCliente", ViewCliente.getText().toString());
        //outState.putString("ViewClienteAddress", ViewClienteAddress.getText().toString());
        outState.putString("ViewRecibe",ViewRecibe.getText().toString());
        outState.putString("ViewEquiposSolicitados",ViewEquiposSolicitados.getText().toString());
        outState.putString("ViewFechaDesde",ViewFechaDesde.getText().toString());
        outState.putString("ViewFechaHasta",ViewFechaHasta.getText().toString());
        //outState.putString("OTMServiceState", OTMServiceState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ViewProgramo.setText(savedInstanceState.getString("ViewProgramo"));
        ViewCliente.setText(savedInstanceState.getString("ViewCliente"));
        //ViewClienteAddress.setText(savedInstanceState.getString("ViewClienteAddress"));
        //ViewClienteAddress.setText(savedInstanceState.getString("ViewClienteAddress"));
        ViewRecibe.setText(savedInstanceState.getString("ViewRecibe"));
        ViewEquiposSolicitados.setText(savedInstanceState.getString("ViewEquiposSolicitados"));
        ViewFechaDesde.setText(savedInstanceState.getString("ViewFechaDesde"));
        ViewFechaHasta.setText(savedInstanceState.getString("ViewFechaHasta"));
        //OTMServiceState = savedInstanceState.getString("OTMServiceState");
        ConsultaFinalizada = false;
    }

    View viewPrev;
    int PrevItem;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getApplicationContext(),"Posicion "+i,Toast.LENGTH_SHORT).show();

        OTMSeleccionada = i;

        if (viewPrev == null){
            viewPrev = view;
            PrevItem = i;
        } else if(PrevItem != i){
            viewPrev.setBackgroundColor(getColor(R.color.WhiteBackGroud));
            viewPrev = view;
            PrevItem = i;
        }
        view.setBackgroundColor(getColor(R.color.colorAccent));


        ViewProgramo.setText(Programo[i]);
        ViewRecibe.setText(Recibe[i]);
        ViewCliente.setText(Clientes[i]);
        //ViewClienteAddress.setText(jsonObject.getString("ADDRESS"));

        ViewEquiposSolicitados.setText("");
        if(!Escalera[i].isEmpty()) ViewEquiposSolicitados.setText(Escalera[i]);

        if(!Andamio[i].isEmpty()){
            if (ViewEquiposSolicitados.getText().toString().isEmpty()) ViewEquiposSolicitados.setText(Andamio[i]);
            else  ViewEquiposSolicitados.setText(ViewEquiposSolicitados.getText()+", "+Andamio[i]);
        }

        if(!EPCCs[i].isEmpty()){
            if (ViewEquiposSolicitados.getText().toString().isEmpty()) ViewEquiposSolicitados.setText(EPCCs[i]);
            else  ViewEquiposSolicitados.setText(ViewEquiposSolicitados.getText()+", "+EPCCs[i]);
        }

        if(!Extras[i].isEmpty()){
            if (ViewEquiposSolicitados.getText().toString().isEmpty()) ViewEquiposSolicitados.setText(Extras[i]);
            else  ViewEquiposSolicitados.setText(ViewEquiposSolicitados.getText()+", "+Extras[i]);
        }

        if(!HSE[i].isEmpty()){
            if (ViewEquiposSolicitados.getText().toString().isEmpty()) ViewEquiposSolicitados.setText(HSE[i]);
            else  ViewEquiposSolicitados.setText(ViewEquiposSolicitados.getText()+", "+HSE[i]);
        }

        ViewFechaDesde.setText(DateInit[i]);
        ViewFechaHasta.setText(DateEnd[i]);
        OTMServiceState = EstadoServicio[i];


    }
}


