package com.examplecodes.Seta.Funciones;

import android.app.Activity;
import android.content.Context;
import android.text.PrecomputedText;
import android.widget.TextView;
import android.widget.Toast;

import com.examplecodes.learnapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fechas {

    public Fechas() {
    }

    private Date FechaInit, FechaEnd, Today = Today();
    private TextView FI, FE, F;

    public String getFechasString(Activity activity, int ID_Fecha){
        F = activity.findViewById(ID_Fecha);
        return F.getText().toString();
    }

    // Retorna True si Fechainit es despues o igual a Today, y FechaEnd de igual o despues
    // de FechaInit
    // Si las fechas no han sido escogidas o incumple lo anterior retorna False
    public boolean ValidarFechas(Activity activity, int ID_FechaInit, int ID_FechaEnd){

        FI = activity.findViewById(ID_FechaInit);
        FE = activity.findViewById(ID_FechaEnd);

        if (FI.getText().toString().isEmpty() || FE.getText().toString().isEmpty()){
            //Toast.makeText(activity.getApplicationContext(),"Elije las fechas de manera Correcta",Toast.LENGTH_SHORT).show();
            return false;
        }

        FechaInit = StringtoFecha(FI.getText().toString());
        FechaEnd = StringtoFecha(FE.getText().toString());
        //Toast.makeText(activity.getApplicationContext(), "Consultando entre: "+FI.getText().toString()+" y "+FE.getText().toString(), Toast.LENGTH_LONG).show();

        return ((!FechaEnd.before(FechaInit)) && (!FechaInit.before(Today)));

    }

    // Convierte una fecha en tipo String a una fecha en formato Date
    public Date StringtoFecha(String FechaString) {
        Date F;
        // 1. Tomamos el valor de las Fechas y les convertimos a un fomato, para luego poderlas comparar
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // se crea el Formato al que se traduciran las fechas para luego compararlas
            F = sdf.parse(FechaString);
            return F;

        } catch (ParseException e) {
            return null;
        }

    }

    // Retorna la fecha de hoy, se utiliza para corroborar que las
    // Fechas escogidas sean coherentes
    public Date Today() {

        // 1. Tomamos el valor de las Fechas y les convertimos a un fomato, para luego poderlas comparar
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // se crea el Formato al que se traduciran las fechas para luego compararlas
            Calendar c = Calendar.getInstance();
            String today = (c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "");
            Today = sdf.parse(today);
            return Today;
        } catch (ParseException e) {

        }
        return null;
    }


}
