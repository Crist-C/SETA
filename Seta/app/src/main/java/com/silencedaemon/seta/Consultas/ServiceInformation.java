package com.silencedaemon.seta.Consultas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.silencedaemon.seta.GestionServicios.ServiceStateSecond;
import com.silencedaemon.seta.MainMenus.MainMenuActivity;
import com.silencedaemon.seta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceInformation extends AppCompatActivity {

    ImageButton ButtToHome;
    Button ButtConsular;
    EditText OTService;
    TextView ViewProgramName, ViewProgramTel, ViewProgramEmail, ViewProgramDate;
    TextView ViewClienteName, ViewClienteNit, ViewClienteAddress;
    TextView ViewHora, ViewEscalera;
    TextView ViewRecibeName, ViewRecibeTel, ViewRecibeEmail;
    TextView ViewDateInit, ViewDateEnd;
    RequestQueue requestQueue;
    String respuesta, OTM, EstadoActual;
    Intent IntentToStateChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_information);


        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });


        ButtConsular = (Button) findViewById(R.id.ButtConsultar);
        OTService = (EditText) findViewById(R.id.OTService);
        ViewProgramName = (TextView) findViewById(R.id.ViewProgramName);
        ViewProgramTel = (TextView) findViewById(R.id.ViewProgramoTel);
        ViewProgramEmail = (TextView) findViewById(R.id.ViewProgramoEmail);
        ViewProgramDate = (TextView) findViewById(R.id.ViewProgramoDate);
        ViewRecibeName = (TextView) findViewById(R.id.ViewRecibeName);
        ViewRecibeTel = (TextView) findViewById(R.id.ViewRecibeTel);
        ViewRecibeEmail = (TextView) findViewById(R.id.ViewRecibeEmail);
        ViewHora = (TextView) findViewById(R.id.ViewClienteHora);
        ViewEscalera = (TextView) findViewById(R.id.ViewEscalera);
        ViewDateInit = (TextView)  findViewById(R.id.ViewFechaDesde);
        ViewDateEnd =  (TextView)  findViewById(R.id.ViewFechaHasta);
        ViewClienteName = (TextView) findViewById(R.id.ViewClienteName);
        ViewClienteNit = (TextView) findViewById(R.id.ViewClienteNit);
        ViewClienteAddress = (TextView) findViewById(R.id.ViewClienteAddr);

        //Depende de que actividad nos haya llamado vamos a poder consultar o solamente
        //Visualizar la información
        respuesta = getIntent().getStringExtra("origen"); // OBTENEMOS EL VALOR DE LA KEY ORIGEN

        // Si solamente se requiere visualizar la información
        if (respuesta.equals("viewInformation")){
            OTService.setText(getIntent().getStringExtra("OTMService"));
            ConsultarSolicitud("https://mariansr.com/DBConsulta.php?OTService="+OTService.getText().toString()+"&From=SI");
            ButtConsular.setText("GESTIONAR");
            OTService.setEnabled(false);
        } else { // ó Consultar un servicio en especial
            OTService.setEnabled(true);
            ButtConsular.setText("CONSULTAR");
        }


        ButtConsular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ButtConsular.getText().toString().equals("CONSULTAR"))
                    ConsultarSolicitud("https://mariansr.com/DBConsulta.php?OTService="+OTService.getText().toString()+"&From=SS");
                else if (ButtConsular.getText().toString().equals("GESTIONAR")){
                    IntentToStateChange = new Intent(ServiceInformation.this, ServiceStateSecond.class);
                    IntentToStateChange.putExtra("EstadoActual", EstadoActual);
                    IntentToStateChange.putExtra("OTM", OTM);
                    startActivity(IntentToStateChange);

                }

            }
        });


    }


    private void ConsultarSolicitud(String URL)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ViewEscalera.setText(jsonObject.getString("TIPOESCALERA") + " " + jsonObject.getString("PASOS") + " pasos");
                        ViewDateInit.setText(jsonObject.getString("FECHAINIT"));
                        ViewDateEnd.setText(jsonObject.getString("FECHAEND"));
                        ViewProgramName.setText(jsonObject.getString("NOMBREPROGRAMA"));
                        ViewProgramTel.setText(jsonObject.getString("TEL_PROGRAMA"));
                        ViewProgramEmail.setText(jsonObject.getString("CORREOPROGRAMA"));
                        ViewProgramDate.setText(jsonObject.getString("DATE"));
                        ViewRecibeName.setText(jsonObject.getString("NOMBRERECIBE") );
                        ViewRecibeTel.setText(jsonObject.getString("TEL_RECIBE"));
                        ViewRecibeEmail.setText(jsonObject.getString("CORREORECIBE"));
                        ViewClienteName.setText(jsonObject.getString("CLIENTE"));
                        ViewClienteNit.setText(jsonObject.getString("NIT"));
                        ViewClienteAddress.setText(jsonObject.getString("ADDRESS"));
                        ViewHora.setText(jsonObject.getString("HORA"));
                        OTM = jsonObject.getString("OTM");
                        EstadoActual = jsonObject.getString("ESTADO_SERVICIO");
                    }  catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(getApplicationContext(), "Error de Conexión:" + error.toString(),Toast.LENGTH_LONG).show();
                }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }

}
