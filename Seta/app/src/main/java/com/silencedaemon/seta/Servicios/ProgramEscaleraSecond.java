package com.silencedaemon.seta.Servicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.silencedaemon.seta.Funciones.DataBase;
import com.silencedaemon.seta.Funciones.DataModel;
import com.silencedaemon.seta.MainMenu.vista.MainMenuActivity;
import com.silencedaemon.seta.ProgramFragments.InterfazActivityFragment;
import com.silencedaemon.seta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProgramEscaleraSecond extends AppCompatActivity implements InterfazActivityFragment,AdapterView.OnItemSelectedListener, View.OnClickListener {

    ///------------------- vistas de la activity --------------------------------- ///
    CheckBox CB_Escalera, CB_Andamio, CB_HSE, CB_EPCC, CB_AscDescenso, CB_Cuerda, CB_Señalizacion, CB_Fechas;
    String NombreProg, TelProg, EmailProg, AreaProg;
    AutoCompleteTextView NombreRecibe;
    EditText TelRecibe, EmailRecibe, AreaRecive;
    EditText HoraEntrega, Observaciones;
    EditText OTM, ClienteName, Nit, ClienteAddress;
    Button ConfSolicitud, SearchInfo;
    ImageButton ButtToHome;
    ProgressBar ProgramProgressBar;
    ///------------------- vistas de la activity --------------------------------- ///
///------------------- Variables fundamentales --------------------------------- ///
    RequestQueue requestQueue, requestQueueToProg;

    int hora, minuto;
    static final String Tag = "DATO";

    ///------------------- Variables fundamentales --------------------------------- ///
///------------------- Variables de Control y supervición de flujo del proceso  --------------------------------- ///
    boolean ProgServiceSuccess = false, ProgramInProcess = false;
    boolean ConsultaInProccess = false;
    private int TotalServiciosSolicitados = 0;
    boolean ConsultasFinalizadas = true, ResultadoConsultas = true;
    int ConteodeSerivicios = 0;
    private final boolean EnProceso = true;
    private final boolean Finalizado = false;
    String[] ServiciosSolicitados = {"", "", "", "", "", "", "", ""};
    int[] EstadoProcesoDeServicios = {0, 0, 0, 0, 0, 0, 0, 0};
    Boolean[] ResultadoServicios = {false, false, false, false, false, false, false, false};
///------------------- Variables de Control y supervición de flujo del proceso  --------------------------------- ///


    ///------------------- Variables de la Base de Datos  --------------------------------- ///
    DataBase dataBase;
    DataModel dataModel = new DataModel();
    Intent IntentToHome;
    boolean SolicitudInProcess = false;

    ///------------------- Variables de la Base de Datos  --------------------------------- ///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_escalera_second);
        Log.d(Tag, "OnCreate");


        // Instanciamos los Objetos de la Interfaz

        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });

        CB_Escalera = (CheckBox) findViewById(R.id.CheckEscalera);
        CB_Andamio = (CheckBox) findViewById(R.id.CheckAndamio);
        CB_HSE = (CheckBox) findViewById(R.id.CheckHSE);
        CB_EPCC = (CheckBox) findViewById(R.id.CheckEPCC);
        CB_AscDescenso = (CheckBox) findViewById(R.id.CheckAscDescenso);
        CB_Cuerda = (CheckBox) findViewById(R.id.CheckCuerda);
        CB_Señalizacion = (CheckBox) findViewById(R.id.CheckSeñalizacion);
        CB_Fechas = (CheckBox) findViewById(R.id.CheckFechas);
        NombreRecibe = (AutoCompleteTextView) findViewById(R.id.NombreQuienRecibe);
        TelRecibe = (EditText) findViewById(R.id.TelQuienRecibe);
        EmailRecibe = (EditText) findViewById(R.id.EmailQuienRecibe);
        AreaRecive = (EditText) findViewById(R.id.DepartamentoRecibe);

        OTM = (EditText) findViewById(R.id.OT_MPS);
        ClienteName = (EditText) findViewById(R.id.NameCliente);
        Nit = (EditText) findViewById(R.id.NitCliente);
        ClienteAddress = (EditText) findViewById(R.id.Direccion);
        HoraEntrega = (EditText) findViewById(R.id.HoraEntrega);
        HoraEntrega.setOnClickListener(this);

        Observaciones = (EditText) findViewById(R.id.Observaciones);
        ProgramProgressBar = (ProgressBar) findViewById(R.id.ProgramProgressBar);
        SearchInfo = (Button) findViewById(R.id.SearchPersonBoutton);
        ConfSolicitud = (Button) findViewById(R.id.ConfSolicitud);

        // Instanciamos las Listas Desplegables
        // Arrays de Autollenado
        ArrayAdapter<CharSequence> Nombres = ArrayAdapter.createFromResource(this, R.array.NamesList, android.R.layout.simple_dropdown_item_1line);
        NombreRecibe.setAdapter(Nombres);


        // Se instancia el dataBase para ejecutar la confirmación de disponibilidad por cada equipo
        dataBase = new DataBase(getApplicationContext(), this, dataModel);

        LoadPreference();

        ResumenSolicitud();

    }

    private void ResumenSolicitud() {

        // Esta variable indica la posición en el array en donde se almacena el indicador de cada solicitud
        int posicionServicio = 0;

        // Si dentro de los servicios solicitaron Escaleras
        if (getIntent().hasExtra("ServicioEscalera")) {

            // Se carga la información al dataModel
            dataModel.LadderType = getIntent().getStringExtra("TipoEscalera"); // Tipo de escalera
            dataModel.LadderReference = getIntent().getBooleanExtra("IsRefEscalera", false); // Si se escogió una referencia
            dataModel.NumLadderReference = getIntent().getStringExtra("NumReferenceEscalera"); // Numero de la referencia
            dataModel.LadderSteps = getIntent().getStringExtra("PasosEscalera"); // Numero de pasos de la escalera que se escogió
            dataModel.DILadder = getIntent().getStringExtra("FIEscalera"); // Fecha de inicio de la solicitud
            dataModel.DELadder = getIntent().getStringExtra("FEEscalera"); // Fecha de Finalización de la solicitud
            dataModel.LadderTotalDays = getIntent().getStringExtra("TotalDiasEscalera"); // Total de dias solicitados
            // Se almacena el servicio solicitado en el Array de Servicios, para control y supervición
            ServiciosSolicitados[posicionServicio++] = "ESCALERA";

            // Se muestra en el ChecBox de servicios
            CB_Escalera.setText("Escalera " + getIntent().getStringExtra("TipoEscalera") + " " + getIntent().getStringExtra("PasosEscalera") +
                    " Pasos N. " + getIntent().getStringExtra("NumReferenceEscalera"));
            CB_Escalera.setVisibility(View.VISIBLE);

        }

        if (getIntent().hasExtra("ServicioAndamio")) {

            // Se carga la información al dataModel
            dataModel.AndamioType = getIntent().getStringExtra("TipoAndamio"); // Tipo de escalera
            dataModel.AndamReference = getIntent().getBooleanExtra("IsRefAndamio", false); // Si se escogió una referencia
            dataModel.NumAndamReference = getIntent().getStringExtra("NumReferenceAndamio"); // Numero de la referencia
            dataModel.Secciones = getIntent().getStringExtra("Secciones"); // Numero de pasos de la escalera que se escogió
            dataModel.DIAndamio = getIntent().getStringExtra("FIAndamio"); // Fecha de inicio de la solicitud
            dataModel.DEAndamio = getIntent().getStringExtra("FEAndamio"); // Fecha de Finalización de la solicitud
            dataModel.AndamTotalDays = getIntent().getStringExtra("TotalDiasAndamio"); // Total de dias solicitados
            // Se almacena el servicio solicitado en el Array de Servicios, para control y supervición
            ServiciosSolicitados[posicionServicio++] = "ANDAMIO";


            if (getIntent().getStringExtra("TipoAndamio").equals("Certificado")) {
                CB_Andamio.setText("Andamio " + getIntent().getStringExtra("TipoAndamio") + " " + getIntent().getStringExtra("Secciones") +
                        " Secciones ");
            } else if (getIntent().getStringExtra("TipoAndamio").equals("Unipersonal")) {
                CB_Andamio.setText("Andamio " + getIntent().getStringExtra("TipoAndamio") + " N." + getIntent().getStringExtra("NumReferenceAndamio"));
            }
            CB_Andamio.setVisibility(View.VISIBLE);
        }

        if (getIntent().hasExtra("HSE")) {

            // Se carga la información al dataModel
            dataModel.HSE = getIntent().getBooleanExtra("HSE",false);
            dataModel.CoordinadorHSE = getIntent().getStringExtra("CoordinadorHSE"); // Tipo de escalera
            dataModel.HorarioHSE = getIntent().getStringExtra("HorarioHSE"); // Si se escogió una referencia
            dataModel.AlturaMaxima = getIntent().getStringExtra("AlturaMaxima");
            dataModel.DIEQAltura = getIntent().getStringExtra("FIEPCC"); // Fecha de inicio de la solicitud
            dataModel.DEEQAltura = getIntent().getStringExtra("FEEPCC"); // Fecha de Finalización de la solicitud
            // Se almacena el servicio solicitado en el Array de Servicios, para control y supervición
            ServiciosSolicitados[posicionServicio++] = "HSE";

            // Se muestra en el CheckBox la información del HSE
            if (!getIntent().getStringExtra("CoordinadorHSE").isEmpty()) {
                CB_HSE.setText("HSE " + getIntent().getStringExtra("CoordinadorHSE") + ", Horario " + getIntent().getStringExtra("HorarioHSE"));
                CB_HSE.setVisibility(View.VISIBLE);

            }
        }

        int[] Bags = getIntent().getIntArrayExtra("BAGS");
        if (getIntent().hasExtra("ServicioEPCC") && getIntent().getIntExtra("TotalEPCC", 0) > 0) {

            dataModel.TotalEQAltura = getIntent().getIntExtra("TotalEPCC", 0);
            dataModel.Bags = getIntent().getIntArrayExtra("BAGS");

            ServiciosSolicitados[posicionServicio++] = "EPCC";
            String ResumenEPCC = "";
            for (int i = 0; i < getIntent().getIntExtra("TotalEPCC", 0); i++) {

                if (i == (getIntent().getIntExtra("TotalEPCC", 0) - 1))
                    ResumenEPCC += ("EPCC" + Bags[i]);
                else ResumenEPCC += ("EPCC" + Bags[i] + " - ");
            }

            CB_EPCC.setText(ResumenEPCC);
            CB_EPCC.setVisibility(View.VISIBLE);
        }

        if (getIntent().hasExtra("EQDescenso") && getIntent().getBooleanExtra("Extras", false)) {

            // Se carga la información al dataModel
            dataModel.EQ_Descenso = getIntent().getBooleanExtra("EQDescenso", false);
            dataModel.AlturaMaxima = getIntent().getStringExtra("AlturaMaxima");

            // Se almacena el servicio solicitado en el Array de Servicios, para control y supervición
            // y Se valida la información del tipo de andamio solcitado y se muestra en el CheckBox
            if (getIntent().getBooleanExtra("EQDescenso", false)) {
                ServiciosSolicitados[posicionServicio++] = "ASCDESCENSO";

                CB_AscDescenso.setText("Equipo Descenso/Ascenso");
                CB_AscDescenso.setVisibility(View.VISIBLE);
            }


        }

        if (getIntent().hasExtra("Cuerda") && getIntent().getBooleanExtra("Extras", false)) {

            // Se carga la información al dataModel
            dataModel.Cuerda = getIntent().getBooleanExtra("Cuerda", false);
            dataModel.AlturaMaxima = getIntent().getStringExtra("AlturaMaxima");

            // Se almacena el servicio solicitado en el Array de Servicios, para control y supervición
            // y Se valida la información del tipo de andamio solcitado y se muestra en el CheckBox
            if (getIntent().getBooleanExtra("Cuerda", false)) {
                ServiciosSolicitados[posicionServicio++] = "CUERDA";

                CB_Cuerda.setText("Cuerda");
                CB_Cuerda.setVisibility(View.VISIBLE);
            }


        }

        if (getIntent().hasExtra("Señalizacion") && !getIntent().getStringExtra("Señalizacion").isEmpty()) {
            // Se carga la información al dataModel
            dataModel.Señalizacion = getIntent().getStringExtra("Señalizacion");
            dataModel.AlturaMaxima = getIntent().getStringExtra("AlturaMaxima");

            ServiciosSolicitados[posicionServicio++] = "SEÑALIZACION";
            CB_Señalizacion.setText(dataModel.Señalizacion);
            CB_Señalizacion.setVisibility(View.VISIBLE);

        }

        dataModel.FechaInicioSolicitud = getIntent().getStringExtra("FIGeneral");
        dataModel.FechaFinalSolicitud = getIntent().getStringExtra("FEGeneral");
        CB_Fechas.setText(getIntent().getStringExtra("FIGeneral") + " - " + getIntent().getStringExtra("FEGeneral"));
        CB_Fechas.setVisibility(View.VISIBLE);


        TotalServiciosSolicitados = posicionServicio;
        //Toast.makeText(getApplicationContext(),"Total Servicios " + TotalServiciosSolicitados, Toast.LENGTH_SHORT).show();

    }


    private void IndicadoresDeProceso(boolean state, String from) {

        switch (from) {
            case "ProgService":
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
                break;

            case "ConsInfo":
                if (state == Finalizado) {
                    ConsultaInProccess = false;
                    NombreRecibe.setEnabled(true);
                    TelRecibe.setEnabled(false);
                    AreaRecive.setEnabled(false);
                    EmailRecibe.setEnabled(false);
                }

                if (state == EnProceso) {
                    ConsultaInProccess = true;
                    NombreRecibe.setEnabled(false);
                    TelRecibe.setEnabled(false);
                    AreaRecive.setEnabled(false);
                    EmailRecibe.setEnabled(false);
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag, "OnRESUME");


        ConfSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DiaServicioProgramado dialogo = new DiaServicioProgramado();
                //dialogo.show(getSupportFragmentManager(),"Confirmación");
                //Snackbar.make(v, "Aviso Snackbar!", Snackbar.LENGTH_LONG).show();
                // Si no se ha programado el servicio de manera Exitosa
                if (ProgramInProcess == false && ProgServiceSuccess == false) {

                    IndicadoresDeProceso(EnProceso, "ProgService");

                    // validamos que toda la información Esté diligenciada
                    if (DataValidation()) {

                        //Se cofirma y programa servicio
                        ConfirmarDiponibilidad();

                    } else {
                        IndicadoresDeProceso(Finalizado, "ProgService");
                    }

                } else {
                    if (ProgServiceSuccess == true)
                        Toast.makeText(getApplicationContext(), "Su solicitud ya fué procesada", Toast.LENGTH_LONG).show();
                    if (ProgramInProcess == true)
                        Toast.makeText(getApplicationContext(), "Solicitud en proceso...", Toast.LENGTH_LONG).show();
                }


            }
        });

        SearchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IndicadoresDeProceso(EnProceso, "ConsInfo");
                ConsultaPersonal("https://mariansr.com/DBConsultaInfoPersonal.php?");
            }
        });


    }


    private void ConfirmarDiponibilidad() {

        String EquipoEnConsulta = "";
        int i = 0;


        for (i = 0; i < ServiciosSolicitados.length && !ServiciosSolicitados[i].equals(""); i++) {

            EquipoEnConsulta = ServiciosSolicitados[i];

            switch (EquipoEnConsulta) {

                case "ESCALERA":
                    dataBase.ConsultarEscaleraDisponible(dataModel.LadderType, dataModel.LadderSteps, dataModel.FechaInicioSolicitud,
                            dataModel.FechaFinalSolicitud, dataModel.NumLadderReference);
                    break;

                case "ANDAMIO":
                    dataBase.ConsultarAndamioDisponible(dataModel.AndamioType, dataModel.Secciones, dataModel.FechaInicioSolicitud, dataModel.FechaFinalSolicitud,
                            dataModel.NumAndamReference);
                    break;

                case "HSE":
                    dataBase.PersonalHSE(dataModel.CoordinadorHSE, dataModel.HorarioHSE, dataModel.FechaInicioSolicitud, dataModel.FechaFinalSolicitud);
                    break;

                case "EPCC":
                    dataBase.EquiposAltura(dataModel.TotalEQAltura, dataModel.Bags, dataModel.FechaInicioSolicitud, dataModel.FechaFinalSolicitud);

                    break;

                case "ASCDESCENSO":
                    dataBase.AscDescenso(true);
                    break;

                case "CUERDA":
                    dataBase.Cuerda(true);
                    break;

                case "SEÑALIZACION":
                    dataBase.Señalizacion(dataModel.Señalizacion);
                    break;

                default:
            }


        }
        TotalServiciosSolicitados = i;
        IndicadoresDeProceso(Finalizado, "ProgService");

    }

    // Esta variable la incrementamos en cada ingreso que se haga a esta función.
    // Los resultados de confirmación se evaluan cuando este contador llegue a TotalServiciosSolicitados/2
    // Ya que por cada servicio solicitado se debe ingresar 2 veces. En donde la segunda vez se carga el resultado final
    int ConsultaN = 0;
    @Override
    public void EstadoDeSolicitud(String Proceso, int Estado, boolean Resultado) {

        // Usamos el apuntador i, para identificar la posición del servicio en el vector, y así
        // cargar el estado y resultado del proceso en la misma posición
        int i = 0;
        ConsultaN++;


        // Encontramos la posición del servicio
        for (i = 0; i < TotalServiciosSolicitados; i++){
            if (ServiciosSolicitados[i].equals(Proceso))break;
        }

        // Almacenamos el estado de la consulta (SinConsultar = 1, EnConsulta = 2, ConsultaFinalizada = 3)
        // y el resultado de cada consulta.
        EstadoProcesoDeServicios[i] = Estado;
        ResultadoServicios[i] = Resultado;

        //Toast.makeText(getApplicationContext(),"CN: "+ConsultaN+" R: "+ResultadoConsultas+" TS:"+TotalServiciosSolicitados+" "+Proceso+" Estado: "+Estado+" Resultado "+Resultado,Toast.LENGTH_LONG).show();


        // Si el contador ConsultaN llega al doble de los TotalServiciosSolicitados, ya que se ingresa 2 veces a esta función por servicio solicitado.
        // Cuando llega este valor se determina que ya todas las consultas fueron finalizadas.
        if (((ConsultaN/2) == (TotalServiciosSolicitados)) ){

            // Reiniciamos las variables de control.
            ConsultaN = 0;
            ResultadoConsultas = true;


            // Operamos de manera lógica para determinar si todos los resultados despues de finalizar son pósitivos, es decir que todos los equipos están Disponibles
            for (int e = 0; e < TotalServiciosSolicitados; e++){
                ResultadoConsultas &= ResultadoServicios[e];
            }


            if (ResultadoConsultas) ProgramarServicio();
            else Toast.makeText(getApplicationContext(),"No se programó el servicio. Verifique que todos los equipos esten disponibles",Toast.LENGTH_SHORT).show();

        }

        if ((ConsultaN/2) >= TotalServiciosSolicitados)
        {
            ConsultaN = 0;
            ResultadoConsultas = true;
        }

    }

    // Crea un servicio en la Base de Datos
    private void ProgramarServicio(){

        String URL ="https://mariansr.com/DBProgramService.php?";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //int i = response.indexOf("_");
                //String ProgramResponse = response.substring(0,i);
                String ProgramResponse = response;
                if (!ProgramResponse.equals("NOTSUCCESS")){
                    IndicadoresDeProceso(Finalizado,"ProgService");
                    ProgServiceSuccess = true;
                    Toast.makeText(getApplicationContext(), "Solicitud " + ProgramResponse + " creada con éxito : )", Toast.LENGTH_LONG).show();
                    IntentToHome = new Intent(ProgramEscaleraSecond.this,MainMenuActivity.class);
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
                parametros.put("TIPOESCALERA",dataModel.LadderType);
                parametros.put("PASOS",dataModel.LadderSteps);
                parametros.put("NUM_ESCALERA", dataModel.NumLadderReference);
                parametros.put("ANDAMIO", dataModel.AndamioType);
                parametros.put("ANDAMIO_REF", dataModel.NumAndamReference);
                parametros.put("ANDAMIO_SEC", dataModel.Secciones);
                parametros.put("HSE",String.valueOf(dataModel.HSE));
                parametros.put("COORDINADOR_HSE", dataModel.CoordinadorHSE);
                parametros.put("HSE_HORARIO", dataModel.HorarioHSE);
                parametros.put("EPCC1", String.valueOf(dataModel.Bags[0]));
                parametros.put("EPCC2", String.valueOf(dataModel.Bags[1]));
                parametros.put("EPCC3", String.valueOf(dataModel.Bags[2]));
                parametros.put("EPCC4", String.valueOf(dataModel.Bags[3]));
                parametros.put("ASCDESCENSO", String.valueOf(dataModel.EQ_Descenso));
                parametros.put("CUERDA", String.valueOf(dataModel.Cuerda));
                parametros.put("SEÑALIZACION", dataModel.Señalizacion);
                parametros.put("FECHAINIT",dataModel.FechaInicioSolicitud);
                parametros.put("FECHAEND",dataModel.FechaFinalSolicitud);
                parametros.put("PARCIALDIAS",dataModel.LadderTotalDays);

                parametros.put("NOMBREPROGRAMA",NombreProg);
                parametros.put("TEL_PROGRAMA",TelProg);
                parametros.put("CORREOPROGRAMA",EmailProg);
                parametros.put("DEPARTAMENTOPROGRAMA",AreaProg);

                parametros.put("NOMBRERECIBE",NombreRecibe.getText().toString());
                parametros.put("TEL_RECIBE",TelRecibe.getText().toString());
                parametros.put("CORREORECIBE",EmailRecibe.getText().toString());
                parametros.put("DEPARTAMENTORECIBE",AreaRecive.getText().toString());

                parametros.put("OTM",OTM.getText().toString());
                parametros.put("CLIENTE",ClienteName.getText().toString());
                parametros.put("NIT",Nit.getText().toString());
                parametros.put("ADDRESS",ClienteAddress.getText().toString());

                parametros.put("HORA",HoraEntrega.getText().toString());
                parametros.put("OBSERVACIONES",Observaciones.getText().toString());

                return parametros;
            }
        };
        requestQueueToProg= Volley.newRequestQueue(this);
        requestQueueToProg.add(stringRequest);


    }

    private boolean DataValidation(){
        boolean DataIsCorrect = false;

        if (!NombreProg.equals("NOT_INFO") && !TelProg.equals("NOT_INFO") && !EmailProg.equals("NOT_INFO") && !AreaProg.equals("NOT_INFO")
                && !NombreRecibe.getText().toString().isEmpty() && !TelRecibe.getText().toString().isEmpty() && !EmailRecibe.getText().toString().isEmpty()
                && !OTM.getText().toString().isEmpty() && !ClienteName.getText().toString().isEmpty() && !Nit.getText().toString().isEmpty() && !ClienteAddress.getText().toString().isEmpty() && !HoraEntrega.getText().toString().isEmpty()
                && !Observaciones.getText().toString().isEmpty()) {

            DataIsCorrect = true;

        } else {
            DataIsCorrect = false;
            // Se muestran los mensajes de error en caso de no haber diligenciado algún campo
            if (NombreProg.equals("NOT_INFO") || TelProg.equals("NOT_INFO") || EmailProg.equals("NOT_INFO") || AreaProg.equals("NOT_INFO"))
                Toast.makeText(getApplicationContext(),"Su información es incompleta, Ingrese nuevamente",Toast.LENGTH_LONG).show();
            if (NombreRecibe.getText().toString().trim().equalsIgnoreCase(""))
                NombreRecibe.setError("Nombre de quien recibirá la escalera");
            if (TelRecibe.getText().toString().trim().equalsIgnoreCase(""))
                TelRecibe.setError("Teléfono de quien recibirá la escalera");
            if (EmailRecibe.getText().toString().trim().equalsIgnoreCase(""))
                EmailRecibe.setError("Email de quien recibirá la escalera");
            if(OTM.getText().toString().trim().equalsIgnoreCase(""))
                OTM.setError("Ingrese la OTM/OTP/INS del servicio");
            if (ClienteName.getText().toString().trim().equalsIgnoreCase(""))
                ClienteName.setError("Nombre del Cliente, proyecto o unidad");
            if (Nit.getText().toString().trim().equalsIgnoreCase(""))
                Nit.setError("Número de identificacion del Cliente");
            if (ClienteAddress.getText().toString().trim().equalsIgnoreCase(""))
                ClienteAddress.setError("Dirección de entrega");
            if (HoraEntrega.getText().toString().trim().equalsIgnoreCase(""))
                HoraEntrega.setError("Hora en que se entregará la escalera ne el púnto");
            if (Observaciones.getText().toString().trim().equalsIgnoreCase(""))
                Observaciones.setError("Si no tienes, escribe 'ninguna'");

            Toast.makeText(getApplicationContext(), "Asegurese de haber ingresado toda la información para continuar", Toast.LENGTH_LONG).show();
        }

        return DataIsCorrect;
    }

    // Ejecuta una consulta en la Base de Datos
    private void ConsultaPersonal(String URL){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //Se transforma el String response en un JSONArray
                    JSONArray Respuesta = new JSONArray(response);
                    JSONObject RecibeInfo;
                    //Luego se extrae el primer objeto del array
                    RecibeInfo = Respuesta.getJSONObject(0);

                    Toast.makeText(getApplicationContext(), "Info Loaded Successfull", Toast.LENGTH_SHORT).show();

                    if (RecibeInfo.getString("INFO_RESPONSE").equals("SUCCESS")){

                        TelRecibe.setEnabled(true);
                        AreaRecive.setEnabled(true);
                        EmailRecibe.setEnabled(true);

                        TelRecibe.setText(RecibeInfo.getString("CELULAR1"));
                        EmailRecibe.setText(RecibeInfo.getString("CORREO"));
                        AreaRecive.setText(RecibeInfo.getString("AREA"));

                        IndicadoresDeProceso(Finalizado,"ConsInfo");

                    }else if (RecibeInfo.getString("INFO_RESPONSE").equals("NOTSUCCESS")){
                        IndicadoresDeProceso(Finalizado,"ConsInfo");
                        Toast.makeText(getApplicationContext(), "Fallo la Solicitud. Intente nuevamente " +response, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    IndicadoresDeProceso(Finalizado,"ConsInfo");
                    Toast.makeText(getApplicationContext(), "Bad Conexion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                parametros.put("NAME",NombreRecibe.getText().toString());

                return parametros;
            }
        };
        requestQueueToProg= Volley.newRequestQueue(this);
        requestQueueToProg.add(stringRequest);




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.HoraEntrega:

                Calendar cal = Calendar.getInstance();
                hora = cal.get(Calendar.HOUR_OF_DAY);
                minuto = cal.get(Calendar.MINUTE);

                final String[] minutos = new String[1];
                final String[] horaDia = new String[1];
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if (minute < 10) minutos[0] = ("0" + minute);
                        else minutos[0] = "" + minute;

                        if (hourOfDay < 10) horaDia[0] = ("0" + hourOfDay);
                        else horaDia[0] = "" + hourOfDay;

                        HoraEntrega.setText(horaDia[0] + ":" + minutos[0] );
                    }
                },hora,minuto,true);
                timePickerDialog.show();
                break;
        }

    }

    @Override
    public void LadderData(DataModel InformacionEscalera) {
        //Toast.makeText(getApplicationContext(),"escalera confirmada",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void AndamioData(DataModel InformacionAndamio) {
        //Toast.makeText(getApplicationContext(),"Andamio confirmado",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void AlturaData(DataModel InfoAltura) {
        //Toast.makeText(getApplicationContext(),"EPPC - HSE confirmada",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void AscDescenso(DataModel AscDescenso) {
        //Toast.makeText(getApplicationContext(),"Equipo de Ascenso confirmado",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Cuerda(DataModel Cuerda) {
        //Toast.makeText(getApplicationContext(),"Cuerda confirmada",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Señalizacion(DataModel Señalización) {
        //Toast.makeText(getApplicationContext(),"Señalización confirmada",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("ProgServiceSuccess", ProgServiceSuccess);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LoadPreference();
        ProgServiceSuccess = savedInstanceState.getBoolean("ProgServiceSuccess");
    }

    //Cargamos Los valores almacenados en las preferencias
    private void LoadPreference() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserLoginPreferences", Context.MODE_PRIVATE);
        NombreProg = sharedPreferences.getString("NOMBRE_USUARIO","NOT_INFO");
        TelProg = sharedPreferences.getString("CELULAR1","NOT_INFO");
        EmailProg = sharedPreferences.getString("CORREO","NOT_INFO");
        AreaProg = sharedPreferences.getString("AREA","NOT_INFO");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
