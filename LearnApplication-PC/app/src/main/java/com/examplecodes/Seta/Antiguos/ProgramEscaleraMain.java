package com.examplecodes.Seta.Antiguos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.examplecodes.Seta.Servicios.ProgramEscaleraSecond;
import com.examplecodes.learnapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProgramEscaleraMain extends AppCompatActivity implements View.OnClickListener {


    Button BotProgEscalera2;
    SeekBar BarNumeroPasos;
    TextView TextNPasosInd;
    EditText FechaInitSolicitud, FechaEndSolicitud;
    RadioGroup ButTipoEscalera;
    RadioButton ButTijera, ButExpan;

    Date FechaEnd;
    Date FechaInit;
    Date Today;
    RequestQueue requestQueue;
    private Intent IntentToProgEscalera2;

    String NumEscToProgram = "0", TipoEsc;
    private int diaInit, mesInit, añoInit, diaEnd, mesEnd, añoEnd;
    static final String Tag = "DATO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_escalera_main);
        Log.d(Tag,"OnCreate");

        BotProgEscalera2 = (Button) findViewById(R.id.ButtonContSolicitud);
        BarNumeroPasos = (SeekBar) findViewById(R.id.BarNpasos);
        TextNPasosInd = (TextView) findViewById(R.id.NumeroPasosIndicador);
        ButTipoEscalera = (RadioGroup) findViewById(R.id.GroupBtTipoEscalera);
        ButTijera = (RadioButton) findViewById(R.id.ButtonTijera);
        ButExpan = (RadioButton) findViewById(R.id.ButtonExpansiva);


        FechaInitSolicitud = (EditText) findViewById(R.id.FechaInitSolicitud);
        FechaInitSolicitud.setOnClickListener(this);

        FechaEndSolicitud = (EditText) findViewById(R.id.FechaEndSolicitud);
        FechaEndSolicitud.setOnClickListener(this);

        TextNPasosInd.setText((BarNumeroPasos.getProgress() + 3 ) + "");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(Tag,"OnSAVE_INSTANCE");
        outState.putString("TextNPasosSaved", TextNPasosInd.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(Tag,"OnRESTORE_INSTANCE");
        super.onRestoreInstanceState(savedInstanceState);
        TextNPasosInd.setText(savedInstanceState.getString("TextNPasosSaved"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag,"OnRESUME");
        BarNumeroPasos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextNPasosInd.setText((BarNumeroPasos.getProgress() + 3 ) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BotProgEscalera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1. Tomamos el valor de las Fechas y les convertimos a un fomato, para luego poderlas comparar
                ConvertirFechas();

                // 2. Comprobamos que todos los campos esten debidamente diligenciados
                if (DataComprovation()) {

                    // 3. Se crea el Intent para cambio de actividad
                    CreateNewIntent();

                    // 4. Consultamos y validamos si la escalera escogida se encuentra disponible para ser programada en las
                    //      Fechas que se han escogido. De ser así se ejecuta el Intent.
                    String MyURL = "https://mariansr.com/DBConsDispEscalera.php?TipoEscalera=" + TipoEsc + "&Pasos=" +
                            TextNPasosInd.getText().toString() + "&Numero=0&FechaInit=" + FechaInitSolicitud.getText().toString() + "&FechaEnd=" + FechaEndSolicitud.getText().toString();
                    EjecutarWebService(MyURL);

                    //5. Si no se encuentran bien diligenciados, se muestra un Mensaje Informativo
                } else {
                    ShowErrors();
                }


            }
        });

    }

    private boolean DataComprovation(){
        return ((ButTijera.isChecked() || ButExpan.isChecked()) && !(TextNPasosInd.getText().toString().equals("0")) &&
                (!FechaInitSolicitud.getText().toString().isEmpty()) && (!FechaEndSolicitud.getText().toString().isEmpty())
                && (!FechaEnd.before(FechaInit)) && (!FechaInit.before(Today)));
    }

    private void ShowErrors(){
        if (!(FechaInitSolicitud.getText().toString().trim().isEmpty()) && !(FechaEndSolicitud.getText().toString().trim().isEmpty()) &&
                ((FechaEnd.before(FechaInit)) || (FechaInit.before(Today))))
            Toast.makeText(getApplicationContext(), "Verifica que las Fechas sean coherentes", Toast.LENGTH_LONG).show();
        if (TextNPasosInd.getText().toString().trim().equalsIgnoreCase("0"))
            TextNPasosInd.setError("Indique la cantidad de pasos de la escalera que necesita");
        if ((!ButTijera.isChecked() && !ButExpan.isChecked()))
            ButExpan.setError("Elige el tipo de escalera que necesitas");
        if (FechaInitSolicitud.getText().toString().trim().isEmpty())
            FechaInitSolicitud.setError("Elige la fecha");
        if (FechaEndSolicitud.getText().toString().trim().equalsIgnoreCase(""))
            FechaEndSolicitud.setError("Elige la fecha");
        //Toast.makeText(getApplicationContext(), "Verifica que todos los campos esten Correctamente diligenciados", Toast.LENGTH_LONG).show();
    }

    private void ConvertirFechas(){

        // 1. Tomamos el valor de las Fechas y les convertimos a un fomato, para luego poderlas comparar
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // se crea el Formato al que se traduciran las fechas para luego compararlas
            Calendar c = Calendar.getInstance();
            String today = ( c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "" );
            Today = sdf.parse(today);

            FechaInit = sdf.parse(FechaInitSolicitud.getText() + "");
            FechaEnd = sdf.parse(FechaEndSolicitud.getText() + "");

        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(),"Ingresa todos los datos antes de consultar", Toast.LENGTH_LONG).show();
        }

    }

    private void CreateNewIntent() {

        // 3. Se crea el Intent para cambio de actividad
        IntentToProgEscalera2 = new Intent(ProgramEscaleraMain.this, ProgramEscaleraSecond.class);
        if (ButExpan.isChecked()) {
            IntentToProgEscalera2.putExtra("tipo", "Extension");
            TipoEsc = "Extension";
        } else if (ButTijera.isChecked()) {
            IntentToProgEscalera2.putExtra("tipo", "Tijera");
            TipoEsc = "Tijera";
        }
        //IntentToProgEscalera2.putExtra("tipo", "Tijera");
        IntentToProgEscalera2.putExtra("pasos", TextNPasosInd.getText().toString());
        //IntentToProgEscalera2.putExtra("NumEscalera",NEscaleras.getText().toString());
        IntentToProgEscalera2.putExtra("fechaInit", FechaInitSolicitud.getText().toString());
        IntentToProgEscalera2.putExtra("fechaEnd", FechaEndSolicitud.getText().toString());
        IntentToProgEscalera2.putExtra("totalDias", String.valueOf((((int) ((FechaEnd.getTime() - FechaInit.getTime()) / 86400000)) / 365) + 1));

    }

    private void EjecutarWebService(String URL)
    {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                    try {

                        jsonObject = response.getJSONObject(0); // Escogemos el primer Objeto JSON
                        NumEscToProgram = (jsonObject.getString("NumeroEscalera"));
                        // Escogemos el valor con la CLAVE "NumeroEscalera"
                        if (NumEscToProgram.equals("NoExist")){
                            Toast.makeText(getApplicationContext(), "Esta escalera NO EXISTE en el Inventario", Toast.LENGTH_LONG).show();
                        }else if(NumEscToProgram.equals("0")){
                            Toast.makeText(getApplicationContext(), "Esta escalera NO SE ENCUENTRA DISPONIBLE. Intenta con otra o en otras fechas", Toast.LENGTH_LONG).show();
                        }else if (!NumEscToProgram.equals("0")){
                            Toast.makeText(getApplicationContext(), "Escalera disponible: #" + NumEscToProgram, Toast.LENGTH_LONG).show();
                            IntentToProgEscalera2.putExtra("numEscalera", NumEscToProgram);
                            startActivity(IntentToProgEscalera2);
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Jarray Error 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Jarray Error 1:" + error.getMessage(), Toast.LENGTH_LONG ).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.FechaInitSolicitud:
                Calendar cal = Calendar.getInstance();

                diaInit= cal.get(Calendar.DAY_OF_MONTH);
                mesInit= cal.get(Calendar.MONTH);
                añoInit= cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialogInit = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        FechaInitSolicitud.setText(year + "-" + (month+1) + "-" + dayOfMonth);

                    }
                }
                        , añoInit,mesInit, diaInit);
                datePickerDialogInit.show();

                break;

            case R.id.FechaEndSolicitud:
                Calendar cal2 = Calendar.getInstance();
                diaEnd= cal2.get(Calendar.DAY_OF_MONTH);
                mesEnd= cal2.get(Calendar.MONTH);
                añoEnd= cal2.get(Calendar.YEAR);

                DatePickerDialog datePickerDialogEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        FechaEndSolicitud.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }
                        ,añoEnd ,mesEnd, diaEnd);
                datePickerDialogEnd.show();

                break;



            case R.id.ButtonContSolicitud:
                EjecutarWebService("https://mariansr.com/DBConsDispEscalera.php?TipoEscalera=" + TipoEsc +
                        "&Pasos=" + TextNPasosInd.getText().toString() +"&FechaInit=" + FechaInitSolicitud.getText().toString() +
                                "&FechaEnd=" + FechaEndSolicitud.getText().toString());

                break;

        }




    }


}
















