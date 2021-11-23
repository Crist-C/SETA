package com.examplecodes.Seta.GestionServicios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.examplecodes.Seta.MainMenus.MainMenuActivity;
import com.examplecodes.learnapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceStateSecond extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ImageButton ButtToHome;
    TextView EstadoActual;
    TextView TittleTextEscalera, TittleTextAndamio;
    EditText AccessCode;
    Button ButChangeState;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String NextEstate, State, OTM;
    String KeyConductor1 = "1919", KeyConductor2 = "2919", KeyTecnico, KeyProg1 = "1828", KeyProg2 = "2828", KeyMaster = "060995";

    RequestQueue requestQueue;

    //**************  NUEVAS DEFINICIONES  ***************************//

    // Número total de cards views
    final int nCardsViews = 9;

    // Arrays de Objetos en JAVA
    CheckedTextView[] TCheckButtons = new CheckedTextView[nCardsViews];
    Button[] TButtons = new Button[nCardsViews];
    LinearLayout[] LLayouts = new LinearLayout[nCardsViews];
    CardView[] CardsViews = new CardView[nCardsViews];

    // IDs de Layouts que contienen las especificaciones de cada Equipo
    int [] LLIDs = {R.id.LayoutCardEscalera,  R.id.LayoutCardAndUnipersonal, R.id.LayoutCardAndamio,
                    R.id.LayoutCardEPCC1, R.id.LayoutCardEPCC2, R.id.LayoutCardEPCC3, R.id.LayoutCardEPCC4,
                    R.id.LayoutCardExtras, R.id.LayoutCardHSE};

    // IDs de CardView de cada Equipo
    int [] CVIDs = {R.id.CardCheckEscalera, R.id.CardCheckAndUnipersonal, R.id.CardCheckAndamio, R.id.CardCheckEPCC1, R.id.CardCheckEPCC2,
                    R.id.CardCheckEPCC3, R.id.CardCheckEPCC4, R.id.CardCheckExtras, R.id.CardCheckHSE};

    // IDs de Buttons en el titulo del CarView y que visualiza u oculta los Layuts
    int [] TBIDs = {R.id.EscaleraCheckBtn, R.id.AndUnipersonalCheckBtn, R.id.AndamioCheckBtn, R.id.EPCC1CheckBtn, R.id.EPCC2CheckBtn,
                    R.id.EPCC3CheckBtn, R.id.EPCC4CheckBtn, R.id.ExtrasCheckBtn, R.id.HSECheckBtn};

    // IDs de Check Button ubicados en el titulo de cada CARDVIEW
    int [] TCBIDs = {R.id.EscaleraCB, R.id.AndUnipersonalCB, R.id.AndamioCB, R.id.EPCC1CB, R.id.EPCC2CB,
            R.id.EPCC3CB, R.id.EPCC4CB, R.id.ExtrasCB, R.id.HSECB};


    //**************  ORDEN EN QUE SE RECIBEN LOS EQUIPOS QUE SE SOLICITAN  ***************************//
    //  0-ECALERA
    //  1-AND.UNIPERSONAL
    //  2-AND.CERTIFICADO
    //  3-EPCC1
    //  4-EPCC2
    //  5-EPCC3
    //  6-EPCC4
    //  7-EXTRAS
    //  8-HSE
    //


    //**************  NUEVAS DEFINICIONES  ***************************//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_state_second);

        TittleTextEscalera = (TextView) findViewById(R.id.TittleTextEscalera);
        TittleTextAndamio = (TextView) findViewById(R.id.TittleTextAndamio);

        //**************  NUEVAS DEFINICIONES  ***************************//

        for (int i = 0; i < nCardsViews; i++){
            TCheckButtons[i] = (CheckedTextView) findViewById(TCBIDs[i]);
            TCheckButtons[i].setOnClickListener(this);

            TButtons[i] = (Button) findViewById(TBIDs[i]);
            TButtons[i].setOnClickListener(this);

            LLayouts[i] = (LinearLayout) findViewById(LLIDs[i]);
            CardsViews[i] = (CardView) findViewById(CVIDs[i]);

            if (getIntent().hasExtra(""+i)){
                CardsViews[i].setVisibility(View.VISIBLE);
                if (i==0)TittleTextEscalera.setText(getIntent().getStringExtra("0"));
                if (i==2)TittleTextAndamio.setText(getIntent().getStringExtra("2"));
                if (i==3) TButtons[i].setText(getIntent().getStringExtra("3").substring(0,5));
                if (i==4) TButtons[i].setText(getIntent().getStringExtra("3").substring(8,13));
                if (i==5) TButtons[i].setText(getIntent().getStringExtra("3").substring(16,21));
                if (i==6) TButtons[i].setText(getIntent().getStringExtra("3").substring(24,29));

            }


        }

        //**************  NUEVAS DEFINICIONES  ***************************//

        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });

        EstadoActual = (TextView) findViewById(R.id.ViewServiceState);
        AccessCode = (EditText) findViewById(R.id.ViewAccesCode);
        ButChangeState = (Button) findViewById(R.id.ButtChangeState);
        spinner = findViewById(R.id.SpinerState);                             // Se instancia el Spinner ()

        EstadoActual.setText(getIntent().getStringExtra("EstadoActual"));              // Se recibe el estado actual desde la Activity anterior
        State = EstadoActual.getText().toString();
        OTM =  getIntent().getStringExtra("OTM");
        SpinnerAdapter(State);




    }


    @Override
    protected void onResume() {
        super.onResume();

        SpinnerAdapter(State);

        // Cuando se presione el Boton Cambiar estado, se evalua ¿Cual es el seguiente estado de la OTM?
        // Luego se evalua que PERFIL es quien intenta realizar el cambio. si esta autorizado se ejecutará el cambio
        // NEXT STATE               AGENTE PERMITIDO
        //  TRANSITO A SERVICIO - CONDUCTOR, MASTER
        //  EJECUCIÓN           - CONDUCTOR, MASTER
        //  FINALIZADO          - TÉCNICO, PROGRAMADOR
        //  CANCELADO           - TÉCNICO, PROGRAMADOR
        //  RECOGIDO            - CONDUCTOR, MASTER
        //
        ButChangeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AccessCode.getText().toString().isEmpty() || (!AccessCode.getText().toString().equals(KeyConductor1) && !AccessCode.getText().toString().equals(KeyConductor2) && !AccessCode.getText().toString().equals(KeyMaster) &&
                        !AccessCode.getText().toString().equals(KeyProg1) && !AccessCode.getText().toString().equals(KeyProg2) && !AccessCode.getText().toString().equals(KeyTecnico))){
                    Toast.makeText(getApplicationContext(),"No tiene permisos para realizar el cambio", Toast.LENGTH_SHORT).show();}
                else {
                    switch (NextEstate) {

                        case "TRANSITO_SERVICIO":
                        case "EJECUCION":
                        case "RECOGIDO":

                            if (AccessCode.getText().toString().equals(KeyConductor1) || AccessCode.getText().toString().equals(KeyConductor2) || AccessCode.getText().toString().equals(KeyMaster)) {
                                //ejecutar servicio Cambiar estado
                                ChangeServiceState(OTM, NextEstate);
                                Toast.makeText(getApplicationContext(), "Cambio realizado a " + NextEstate, Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(getApplicationContext(), "No tiene permisos para realizar el cambio", Toast.LENGTH_SHORT).show();
                            break;

                        case "FINALIZADO":
                        case "CANCELADO":

                            if (AccessCode.getText().toString().equals(KeyProg1) || AccessCode.getText().toString().equals(KeyProg2) || AccessCode.getText().toString().equals(KeyTecnico) || AccessCode.getText().toString().equals(KeyMaster)){
                                //ejecutar servicio Cambiar estado
                                ChangeServiceState(OTM, NextEstate);
                                Toast.makeText(getApplicationContext(), "Cambio autorizado a " + NextEstate, Toast.LENGTH_LONG).show();
                            }else Toast.makeText(getApplicationContext(),"No tiene permisos para realizar el cambio", Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), "Estado Desconocido", Toast.LENGTH_SHORT).show();

                            break;
                    }
                }

            }
        });

    }

    private void SpinnerAdapter(String State)
    {
        switch (State){ // Según el estado actual del servicio se despliega una lista

            case "PROGRAMADO":
                adapter = ArrayAdapter.createFromResource(this, R.array.PROGRAMATED_STATES, android.R.layout.simple_spinner_item);
                break;
            case "TRANSITO_SERVICIO":
                adapter = ArrayAdapter.createFromResource(this, R.array.TRANSITO_STATES, android.R.layout.simple_spinner_item);
                break;
            case "EJECUCION":
                adapter = ArrayAdapter.createFromResource(this, R.array.EJECUCION_STATES, android.R.layout.simple_spinner_item);
                break;
            case "FINALIZADO":
                adapter = ArrayAdapter.createFromResource(this, R.array.FINALIZADO_STATES, android.R.layout.simple_spinner_item);
                break;
            case "CANCELADO":
                adapter = ArrayAdapter.createFromResource(this, R.array.CANCELADO_STATES, android.R.layout.simple_spinner_item);
                spinner.setEnabled(false);
                break;
            case "RECOGIDO":
                adapter = ArrayAdapter.createFromResource(this, R.array.RECOGIDO_STATES, android.R.layout.simple_spinner_item);
                spinner.setEnabled(false);
                break;
            default: Toast.makeText(getApplicationContext(),"Estado Desconocida¿o", Toast.LENGTH_LONG).show();
                adapter = ArrayAdapter.createFromResource(this, R.array.CANCELADO_STATES, android.R.layout.simple_spinner_item);
                spinner.setEnabled(false);
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }

    // Crea un servicio en la Base de Datos
    private void ChangeServiceState(final String OTM, final String NewEstate){

        String URL = "https://mariansr.com/DBChangeServiceState.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    //Se transforma el String response en un JSONArray
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject Estado;
                    //Luego se extrae el primer objeto del array
                    Estado = jsonArray.getJSONObject(0);

                    if (!Estado.isNull("ESTADO_SERVICIO")){

                        EstadoActual.setText(Estado.getString("ESTADO_SERVICIO"));
                        SpinnerAdapter(Estado.getString("ESTADO_SERVICIO"));

                    }else Toast.makeText(getApplicationContext(),Estado.getString("ESTADO"),Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error CambEstad: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
                //Change Service State Success = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error ProgEscSecond - EjecProgram BadConexión: " + error.toString(),Toast.LENGTH_LONG).show();
                //ProgServiceSuccess = false;
            }
        }){

            // Mapa de toda la Información con la que se solicitará el servicio. <-- IMPORTANTE
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parametros = new HashMap<String, String>();
                parametros.put("NewEstate", NewEstate);
                parametros.put("OTM",OTM);

                return parametros;
            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        NextEstate = parent.getItemAtPosition(position).toString();

        switch (NextEstate) {
            case "Transito a Servicio": NextEstate = "TRANSITO_SERVICIO"; break;
            case "Ejecución":           NextEstate = "EJECUCION"; break;
            case "Recogido":            NextEstate = "RECOGIDO"; break;
            case "Finalizado":          NextEstate = "FINALIZADO"; break;
            case "Cancelado":           NextEstate = "CANCELADO"; break;
            default:
                //Toast.makeText(getApplicationContext(), "Estado Desconocido", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    int[] nClick = new int[nCardsViews];

    @Override
    public void onClick(View view) {


        for (int i = 0; i < nCardsViews; i++){

            if (TBIDs[i] == view.getId()) {

                if (nClick[i] == 0){
                    LLayouts[i].setVisibility(View.VISIBLE);
                    nClick[i] = 1;
                }else if (nClick[i] != 0){
                    LLayouts[i].setVisibility(View.GONE);
                    nClick[i] = 0;
                }

                break;
            }


            if (TCBIDs[i] == view.getId()) {

                if (TCheckButtons[i].isChecked()){
                    TCheckButtons[i].setChecked(false);
                }else if (!TCheckButtons[i].isChecked()){
                    TCheckButtons[i].setChecked(true);
                }

                break;
            }


        }




    }



}
