package com.silencedaemon.seta.Consultas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.silencedaemon.seta.MainMenus.MainMenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.silencedaemon.seta.R;

public class ConsultarEscalera extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    SeekBar BarNumberPasos;
    TextView TextNpasosInd;
    EditText ConsFechaDesde, ConsFechaHasta;
    ImageButton ButtToHome;
    Button ConDisponible, ConRuta;
    RadioButton ButExpansiva, ButTijera;
    Date FechaDesde;
    Date FechaHasta;
    Spinner NumeroEscalera;

    RequestQueue requestQueue;

    int ConsDiaInit, ConsMesInit, ConsAñoInit;
    int ConsDiaEnd, ConsMesEnd, ConsAñoEnd;
    int TotalService;

    ArrayAdapter<CharSequence> VersionArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_escalera);

        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });

        ButExpansiva = (RadioButton) findViewById(R.id.ButtonExpansiva);
        ButTijera = (RadioButton) findViewById(R.id.ButtonTijera);
        TextNpasosInd = (TextView) findViewById(R.id.NumeroPasosIndicador);
        BarNumberPasos =(SeekBar) findViewById(R.id.BarNpasos);
        ConsFechaDesde = (EditText) findViewById(R.id.consFechaDesde);
        ConsFechaHasta = (EditText) findViewById(R.id.consFechaHasta);
        ConDisponible = (Button) findViewById(R.id.conDisponibilidad);
        ConRuta = (Button) findViewById(R.id.conRuta);
        NumeroEscalera = (Spinner) findViewById(R.id.SpinNumeroEsc);


        ConsFechaDesde.setOnClickListener(this);
        ConsFechaHasta.setOnClickListener(this);
        ButExpansiva.setOnClickListener(this);
        ButTijera.setOnClickListener(this);


        ValidateVersionEscalera();

        BarNumberPasos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextNpasosInd.setText(progress+"");
                ValidateVersionEscalera();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ConDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataValidate(ConDisponible);
            }
        });
        ConRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataValidate(ConRuta);
            }
        });

    }

    public void DataValidate(View v){

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            FechaDesde = simpleDateFormat.parse(ConsFechaDesde.getText() + "");
            FechaHasta = simpleDateFormat.parse(ConsFechaHasta.getText() + "");

        } catch (ParseException e) {

            Toast.makeText(getApplicationContext(),"Error: "+ e.getMessage(),Toast.LENGTH_LONG).show();
        }

        if ((ButExpansiva.isChecked() || ButTijera.isChecked()) && !TextNpasosInd.getText().toString().isEmpty()
                && !ConsFechaHasta.getText().toString().isEmpty() && !ConsFechaDesde.getText().toString().isEmpty()
                && (!FechaHasta.before(FechaDesde))) {

            String tipoEscalera, pasos, numero, fechainit, fechaend;

            if(ButExpansiva.isChecked()) tipoEscalera = "Expansiva";
            else if (ButTijera.isChecked()) tipoEscalera = "Tijera";
            pasos = TextNpasosInd.getText().toString();
            //numero =

            /// ejecutar Consulta de Disponibilidad o Ruta
            /*switch (v.getId()){
                case R.id.conDisponibilidad:
                    EjecutarWebService("https://mariansr.com/DBConsDispEscalera.php?","Disponibilidad");
                    break;
                case R.id.conRuta:
                    Toast.makeText(getApplicationContext(),"Consulta Ruta",Toast.LENGTH_LONG).show();
                    EjecutarWebService("https://mariansr.com/DBConsRutaEscalera.php","Ruta");
                    break;
            }
*/
        } else
        {
            if(!ButExpansiva.isChecked() && !ButTijera.isChecked())
                ButExpansiva.setError("Elija una Escalera");
            if(TextNpasosInd.getText().toString().trim().equalsIgnoreCase(""))
                TextNpasosInd.setError("Elije el número de pasos");
            if(ConsFechaDesde.getText().toString().trim().equalsIgnoreCase(""))
                ConsFechaDesde.setError("Elije la Fecha");
            if(ConsFechaHasta.getText().toString().trim().equalsIgnoreCase(""))
                ConsFechaHasta.setError("Elije la Fecha");
            Toast.makeText(getApplicationContext(), "Llena de manera correcta todos los campos", Toast.LENGTH_LONG).show();

        }

    }

    public void EjecutarWebService(String URL, final String Consulta){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (Consulta.equals("Disponibilidad")) {

                    try {
                        if (response.getString(0).equals("null"))
                            Toast.makeText(getApplicationContext(), "La escalera se encuentra disponible", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "La escalera tiene programados "+response.length()+" servicios", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else if (Consulta.equals("Ruta")) {


                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                }

            }

        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(getApplicationContext(), "Error de Conexión:" + error.getCause().toString(),Toast.LENGTH_LONG).show();
                }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void ValidateVersionEscalera()
    {
        if (ButTijera.isChecked() && (!TextNpasosInd.getText().toString().isEmpty()))
        {
            switch (TextNpasosInd.getText().toString()) {
                case "3":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T3, android.R.layout.simple_spinner_item);
                    break;
                case "4":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T4, android.R.layout.simple_spinner_item);
                    break;
                case "5":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T5, android.R.layout.simple_spinner_item);
                    break;
                case "6":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T6, android.R.layout.simple_spinner_item);
                    break;
                case "7":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T7, android.R.layout.simple_spinner_item);
                    break;
                case "8":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T8, android.R.layout.simple_spinner_item);
                    break;
                case "9":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T9, android.R.layout.simple_spinner_item);
                    break;
                case "10":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T10, android.R.layout.simple_spinner_item);
                    break;
                case "12":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.T12, android.R.layout.simple_spinner_item);
                    break;
            default:
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this,R.array.defaultVersion, android.R.layout.simple_spinner_item);
            }
        } else if (ButExpansiva.isChecked() && (!TextNpasosInd.getText().toString().isEmpty())){

            switch (TextNpasosInd.getText().toString()){
                case "6":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this,R.array.E6, android.R.layout.simple_spinner_item);
                    break;
                case "7":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this,R.array.E7, android.R.layout.simple_spinner_item);
                    break;
                case "10":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this,R.array.E10, android.R.layout.simple_spinner_item);
                    break;
                case "12":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this,R.array.E12, android.R.layout.simple_spinner_item);
                    break;
                case "16":
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this,R.array.E16, android.R.layout.simple_spinner_item);
                    break;
                default:
                    VersionArrayAdapter = ArrayAdapter.createFromResource(this,R.array.defaultVersion, android.R.layout.simple_spinner_item);

            }

        }
        else {
            VersionArrayAdapter = ArrayAdapter.createFromResource(this, R.array.defaultVersion, android.R.layout.simple_spinner_item);
        }

        VersionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NumeroEscalera.setAdapter(VersionArrayAdapter);
        NumeroEscalera.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ButtonExpansiva:
            case R.id.ButtonTijera:
                ValidateVersionEscalera();
                break;
            case R.id.consFechaDesde:
                Calendar calDesde = Calendar.getInstance();

                ConsDiaInit = calDesde.get(Calendar.DAY_OF_MONTH);
                ConsMesInit = calDesde.get(Calendar.MONTH);
                ConsAñoInit = calDesde.get(Calendar.YEAR);

                DatePickerDialog datePickerDialogInit = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ConsFechaDesde.setText(year + "-" + (month+ 1) +  "-" + dayOfMonth);
                    }
                }, ConsAñoInit, ConsMesInit, ConsDiaInit);
                datePickerDialogInit.show();

                break;

            case R.id.consFechaHasta:

                Calendar calHasta = Calendar.getInstance();
                ConsDiaEnd = calHasta.get(Calendar.DAY_OF_MONTH);
                ConsMesEnd = calHasta.get(Calendar.MONTH);
                ConsAñoEnd = calHasta.get(Calendar.YEAR);

                DatePickerDialog datePickerDialogEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ConsFechaHasta.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                },ConsAñoEnd,ConsMesEnd,ConsDiaEnd);
                datePickerDialogEnd.show();

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
