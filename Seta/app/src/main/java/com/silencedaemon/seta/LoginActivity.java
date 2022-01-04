package com.silencedaemon.seta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.silencedaemon.seta.MainMenu.vista.MainMenuRV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText UserName, Password;
    Button ButtLogin;
    CheckedTextView Recordarme;
    ProgressBar LoginProgressBar;
    private Intent IntentToMainMenu;
    Boolean LoginInProcces = false;
    private RequestQueue requestQueue;

    final boolean Inicio = true;
    final boolean Finalizado = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences sharedPreferences = getSharedPreferences("UserLoginPreferences", Context.MODE_PRIVATE);
        IntentToMainMenu = new Intent(LoginActivity.this, MainMenuRV.class);

        UserName = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        ButtLogin = (Button) findViewById(R.id.ButtonLogin);
        Recordarme = (CheckedTextView) findViewById(R.id.ReocordarmeCB);
        LoginProgressBar = (ProgressBar) findViewById(R.id.LoginProgressBar);

        IndicadoresDeProceso(Finalizado);

        if(sharedPreferences.getBoolean("RECORDARME",false)){
            UserName.setText(sharedPreferences.getString("CORREO",""));
            Password.setText(sharedPreferences.getString("PASSWORD",""));
        } else {
            UserName.setText("");
            Password.setText("");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        ButtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (LoginInProcces == false){

                    IndicadoresDeProceso(Inicio);

                    if (DataValidation()){ // si el form está correctamente diligenciado

                        //Valida en la base de datos si es un usuario registrado
                        ValidateLogin(UserName.getText().toString(), Password.getText().toString());

                    }else {
                        IndicadoresDeProceso(Finalizado);
                    }

                }else Toast.makeText(getApplicationContext(),"Login en Proceso...",Toast.LENGTH_SHORT).show();


            }
        });

        Recordarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Recordarme.isChecked()){
                    Recordarme.setChecked(false);
                    //PreferenceEditor.putBoolean("RECORDARME", false);

                } else if (!Recordarme.isChecked()){
                    Recordarme.setChecked(true);
                    //PreferenceEditor.putBoolean("RECORDARME", true);
                }

            }
        });




    }

    private void ValidateLogin(final String userName,final String passWord){

        String URL = "https://setaapp.000webhostapp.com/DBLogin.php?"; // Anterior: https://mariansr.com/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //Se transforma el String response en un JSONArray
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject ResultadoLogin;
                    //Luego se extrae el primer objeto del array
                    ResultadoLogin = jsonArray.getJSONObject(0);
                    if (ResultadoLogin.getString("LOGIN_RESPONSE").equals("CorrectAccess")){

                        //Almacenamos en las Preferencias todos los Datos del usuario que se logea
                        SharedPreferences sharedPreferences = getSharedPreferences("UserLoginPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor PreferenceEditor = sharedPreferences.edit();

                        PreferenceEditor.putString("IDENTIFICADOR",ResultadoLogin.getString("IDENTIFICADOR"));
                        PreferenceEditor.putString("NUMERO_ID",ResultadoLogin.getString("NUMERO_ID"));
                        PreferenceEditor.putString("NOMBRE_USUARIO",ResultadoLogin.getString("NOMBRE"));
                        PreferenceEditor.putString("DOCUMENTO",ResultadoLogin.getString("NUMERO_DOCUMENTO"));
                        PreferenceEditor.putString("CELULAR1",ResultadoLogin.getString("CELULAR1_CORP"));
                        PreferenceEditor.putString("CORREO",userName);
                        PreferenceEditor.putString("CARGO",ResultadoLogin.getString("CARGO"));
                        PreferenceEditor.putString("AREA",ResultadoLogin.getString("AREA_DEPARTAMENTO"));

                        if (Recordarme.isChecked()){
                            PreferenceEditor.putBoolean("RECORDARME", true);
                            PreferenceEditor.putString("PASSWORD",passWord);
                        }
                        else PreferenceEditor.putBoolean("RECORDARME", false);

                        //Almacenamos todas las preferencias
                        PreferenceEditor.commit();

                        //Mensaje de Bienvenida
                        int ind = ResultadoLogin.getString("NOMBRE").indexOf(" ");
                        String NombreBienvenida = ResultadoLogin.getString("NOMBRE").substring(0,ind);

                        //Reinicializamos las variables de control de Flujo
                        IndicadoresDeProceso(Finalizado);

                        //Iniciamos la nueva Actividad ManinMenú
                        Toast.makeText(getApplicationContext(),"Bienvenido " + NombreBienvenida,Toast.LENGTH_SHORT).show();
                        startActivity(IntentToMainMenu);

                        }else if (ResultadoLogin.getString("LOGIN_RESPONSE").equals("DeniedAccess")){

                        IndicadoresDeProceso(Finalizado);
                        Toast.makeText(getApplicationContext(),"Usuario o contrasena incorrecta",Toast.LENGTH_SHORT).show();

                        }else if (ResultadoLogin.getString("LOGIN_RESPONSE").equals("Bad_DB_Access")){

                        IndicadoresDeProceso(Finalizado);
                        Toast.makeText(getApplicationContext(),"Bad_DB_Access 0 Rows find",Toast.LENGTH_SHORT).show();
                    }
                    //Se procesa el resltado

                } catch (JSONException e) {
                    e.printStackTrace();
                    IndicadoresDeProceso(Finalizado);
                    Toast.makeText(getApplicationContext(),"Error de Conexión: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error Login Posible mala conexión: " + error.toString(),Toast.LENGTH_LONG).show();
                IndicadoresDeProceso(Finalizado);

            }

    }){
            // Mapa de toda la Información con la que se solicitará el servicio. <-- IMPORTANTE
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parametros = new HashMap<String, String>();
                parametros.put("USERNAME", userName);
                parametros.put("PASSWORD",passWord);

                return parametros;
            }

    };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private boolean DataValidation(){
        boolean DataIsCorrect = false;
        if (UserName.getText().toString().isEmpty() || Password.getText().toString().isEmpty()){
            if (UserName.getText().toString().isEmpty()) UserName.setError("Ingrese su nombre de usuario");
            if (Password.getText().toString().isEmpty()) Password.setError("Ingrese su Contraseña");
            DataIsCorrect = false;
        }else if (!UserName.getText().toString().contains("@co.g4s.com")){
            UserName.setError("Tu usuario no es valido");
            DataIsCorrect = false;
        }else DataIsCorrect = true;

        return DataIsCorrect;
    }

    private void IndicadoresDeProceso(boolean state){
        if (state == Finalizado){

            LoginInProcces = false;
            LoginProgressBar.setVisibility(View.INVISIBLE);
            UserName.setEnabled(true);
            Password.setEnabled(true);
            Recordarme.setEnabled(true);

        }

        if (state == Inicio){
            LoginInProcces = true;
            LoginProgressBar.setVisibility(View.VISIBLE);
            UserName.setEnabled(false);
            Password.setEnabled(false);
            Recordarme.setEnabled(false);
        }

    }

        @Override
    public void onClick(View view) {

    }
}