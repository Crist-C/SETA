package com.examplecodes.Seta.Funciones;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.examplecodes.Seta.ProgramFragments.InterfazActivityFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataBase {


    Context context;
    Activity ActivityContenedora;
    DataModel dataModel;
    private RequestQueue requestQueue;
    private String EscaleraDisponible, ReferenciaAndDisponible, SeccionesDisponibles, HSEDisponible, EPCC;

    int SinConsultar = 1;
    int EnConsulta = 2;
    int ConsultaFinalizada = 3;
    boolean LeadderIsAvaliable, AndamioIsAvaliable, HSEIsAvaliable, M1Avaliable, M2Avaliable, M3Avaliable, M4Avaliable;
    boolean [] EPCCIsAvaliable = {false,false,false,false};
    boolean returnEPCCAvaliable = true;
    String [] EPCCDisponible = {"","","",""};

    private String URLDispEscalera = "https://mariansr.com/DBConsDispEscalera.php?",
                    URLDispAndamio = "https://mariansr.com/DBConsDispAndamio.php?",
                    URLDispHSE = "https://mariansr.com/DBConsDispHSE.php?",
                    URLDispEqAltura = "https://mariansr.com/DBConsDispEPCC.php?";

    private String NofActvty = "ProgramEscaleraSecond";

    public DataBase(Context context, Activity ActivityContenedora, DataModel dataModel) {
        this.context = context;
        this.ActivityContenedora = ActivityContenedora;
        this.dataModel = dataModel;
    }


    //TipoEscalera=" + TipoEsc + "&Pasos=" + TextNPasosInd.getText().toString() + "&Numero=0&FechaInit=" + FechaInitSolicitud.getText().toString() + "&FechaEnd=" + FechaEndSolicitud.getText().toString();

    public boolean ConsultarEscaleraDisponible(final String TipoEscalera, final String PasosEscalera, String FI, String FE, String Referencia)
    {

        // Informamos a la activity contenedora que hemos empezado una solicitud
        ((InterfazActivityFragment)ActivityContenedora).EstadoDeSolicitud("ESCALERA",EnConsulta,false);

        URLDispEscalera = "https://mariansr.com/DBConsDispEscalera.php?";
        URLDispEscalera += ("TipoEscalera="+TipoEscalera+"&Pasos="+PasosEscalera+"&Referencia="+Referencia+"&FechaInit='"+FI+"'&FechaEnd='"+FE+"'");
        //Toast.makeText(context, URLDispEscalera, Toast.LENGTH_LONG).show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLDispEscalera, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;

                try {

                    jsonObject = response.getJSONObject(0); // Escogemos el primer Objeto JSON
                    EscaleraDisponible = (jsonObject.getString("NumeroEscalera"));
                    // Escogemos el valor con la CLAVE "NumeroEscalera"

                    if (EscaleraDisponible.equals("NoExist")){
                        SetResults("ESCALERA", false);
                        Toast.makeText(context, "Esta escalera NO EXISTE en el Inventario", Toast.LENGTH_SHORT).show();
                    }else if(EscaleraDisponible.equals("0")){
                        SetResults("ESCALERA", false);
                        Toast.makeText(context, "Esta escalera NO se encuentra disponible. Intenta en otras fechas", Toast.LENGTH_LONG).show();
                    }else if (!EscaleraDisponible.equals("0")){
                        dataModel.NumLadderReference = EscaleraDisponible;
                        SetResults("ESCALERA", true);
                        if (!ActivityContenedora.getLocalClassName().equals(NofActvty))Toast.makeText(context, "Escalera disponible: " + TipoEscalera + " " + PasosEscalera + " Pasos No."+ EscaleraDisponible, Toast.LENGTH_SHORT).show();
                        //else Toast.makeText(context, "Escalera Confirmada", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    SetResults("ESCALERA", false);
                    Toast.makeText(context, "Jarray Error 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SetResults("ESCALERA", false);
                Toast.makeText(context, "Jarray Error 1:" + error.getMessage(), Toast.LENGTH_LONG ).show();
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

        return LeadderIsAvaliable;
    }

    public void SetResults(String EquipoSolicitado, boolean ProgNOProg){

        dataModel.Solicitud = ProgNOProg;

        switch (EquipoSolicitado){
            case "ESCALERA": LeadderIsAvaliable = ProgNOProg;
                             dataModel.LadderAvaliable = ProgNOProg;
                ((InterfazActivityFragment)ActivityContenedora).LadderData(dataModel);
                break;
            case "ANDAMIO": AndamioIsAvaliable = ProgNOProg;
                            dataModel.AndamAvaliable = ProgNOProg;
                ((InterfazActivityFragment)ActivityContenedora).AndamioData(dataModel);
                break;
            case "HSE": HSEIsAvaliable = ProgNOProg;
                        dataModel.HSEAvaliable = ProgNOProg;
                ((InterfazActivityFragment)ActivityContenedora).AlturaData(dataModel);
                break;
            case "EPCC": returnEPCCAvaliable = ProgNOProg;
                         dataModel.EPCCAvaliable = ProgNOProg;
                ((InterfazActivityFragment)ActivityContenedora).AlturaData(dataModel);
                break;
            case "ASCDESCENSO":
                ((InterfazActivityFragment)ActivityContenedora).AscDescenso(dataModel);
                break;
            case "CUERDA":
                ((InterfazActivityFragment)ActivityContenedora).Cuerda(dataModel);
                break;
            case "SEÑALIZACION":
                ((InterfazActivityFragment)ActivityContenedora).Señalizacion(dataModel);
                break;

        }

        ((InterfazActivityFragment)ActivityContenedora).EstadoDeSolicitud(EquipoSolicitado,ConsultaFinalizada, ProgNOProg);

    }

    public boolean ConsultarAndamioDisponible(final String TipoAndamio, final String Secciones, String FI, String FE, String Referencia)
    {
        // Informamos a la activity contenedora que hemos empezado una solicitud
        ((InterfazActivityFragment)ActivityContenedora).EstadoDeSolicitud("ANDAMIO",EnConsulta,false);

        URLDispAndamio = "https://mariansr.com/DBConsDispAndamio.php?";
        URLDispAndamio += ("TipoAndamio="+TipoAndamio+"&Secciones="+Secciones+"&Referencia="+Referencia+"&FechaInit='"+FI+"'&FechaEnd='"+FE+"'");
        //Toast.makeText(context, URLDispAndamio, Toast.LENGTH_LONG).show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLDispAndamio, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;

                try {

                    jsonObject = response.getJSONObject(0); // Escogemos el primer Objeto JSON
                    SeccionesDisponibles = (jsonObject.getString("ReferenciaAndamio"));
                    ReferenciaAndDisponible = SeccionesDisponibles;
                    // Escogemos el valor con la CLAVE "NumeroEscalera"

                    if (SeccionesDisponibles.equals("NoExist")){

                         SetResults("ANDAMIO",false);
                        Toast.makeText(context, "El andamio NO está disponible en el Inventario", Toast.LENGTH_SHORT).show();

                    }else if((TipoAndamio.equals("Certificado") && (Integer.parseInt(SeccionesDisponibles) < Integer.parseInt(Secciones)) ) || (
                            (TipoAndamio.equals("Unipersonal") && ReferenciaAndDisponible.equals("0")) )){

                        SetResults("ANDAMIO",false);

                            if (TipoAndamio.equals("Certificado")){
                                Toast.makeText(context, "La cantidad de secciones que requiere NO se encuentran disponibles.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "La cantidad de secciones disponibles en las fechas son "+ SeccionesDisponibles, Toast.LENGTH_LONG).show();
                            }else Toast.makeText(context, "El andamio " +TipoAndamio+" NO se encuentran disponible. Intenta en otras fechas", Toast.LENGTH_SHORT).show();


                    }else if (Integer.parseInt(SeccionesDisponibles) >= Integer.parseInt(Secciones) || !ReferenciaAndDisponible.equals("0")){

                        if (TipoAndamio.equals("Unipersonal")) dataModel.NumAndamReference = ReferenciaAndDisponible;

                        SetResults("ANDAMIO",true);

                        if (TipoAndamio.equals("Certificado") && !ActivityContenedora.getLocalClassName().equals(NofActvty)){
                            Toast.makeText(context, "Se anexó a la solicitud: " + Secciones + " Secciones de Andamio "+ TipoAndamio, Toast.LENGTH_SHORT).show();
                        }else if (TipoAndamio.equals("Unipersonal") && !ActivityContenedora.getLocalClassName().equals(NofActvty)) Toast.makeText(context, "Se anexó a la solicitud: Andamio "+ TipoAndamio + " No "+ReferenciaAndDisponible, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    SetResults("ANDAMIO",false);
                    Toast.makeText(context, "Jarray Error 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SetResults("ANDAMIO",false);
                Toast.makeText(context, "Jarray Error 1:" + error.getMessage(), Toast.LENGTH_LONG ).show();
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

        return AndamioIsAvaliable;
    }

    public boolean PersonalHSE(final String NombreHSE, final String Horario, String FI, String FE)
    {
        // Informamos a la activity contenedora que hemos empezado una solicitud
        ((InterfazActivityFragment)ActivityContenedora).EstadoDeSolicitud("HSE",EnConsulta,false);


        URLDispHSE = "https://mariansr.com/DBConsDispHSE.php?";
        URLDispHSE += ("Horario="+Horario+"&Nombre="+NombreHSE+"&FechaInit='"+FI+"'&FechaEnd='"+FE+"'");
        //Toast.makeText(context, URLDispHSE, Toast.LENGTH_LONG).show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLDispHSE, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;

                try {

                    jsonObject = response.getJSONObject(0); // Escogemos el primer Objeto JSON
                    HSEDisponible = (jsonObject.getString("HSE"));

                    // Escogemos el valor con la CLAVE "NumeroEscalera"

                    if (HSEDisponible.equals("DESHABILITADO")){
                        Toast.makeText(context, "El personal HSE NO habilitado", Toast.LENGTH_SHORT).show();
                        SetResults("HSE", false);

                    }else if(HSEDisponible.equals("NO_DISPONIBLE")){
                        Toast.makeText(context, "El personal HSE NO se encuentra disponible.", Toast.LENGTH_SHORT).show();
                        SetResults("HSE", false);
                    }else {
                        dataModel.CoordinadorHSE = HSEDisponible;
                        SetResults("HSE", true);
                        if (!ActivityContenedora.getLocalClassName().equals(NofActvty)) Toast.makeText(context, "Se agendó al Coordinador: " + HSEDisponible, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Jarray HSE 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    SetResults("HSE", false);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Jarray HSE 2:" + error.getMessage(), Toast.LENGTH_LONG ).show();
                SetResults("HSE", false);
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

        return HSEIsAvaliable;
    }

    public boolean EquiposAltura(final int NMaletas, final int[] Bags, String FI, String FE)
    {
        // Informamos a la activity contenedora que hemos empezado una solicitud
        ((InterfazActivityFragment)ActivityContenedora).EstadoDeSolicitud("EPCC",EnConsulta,false);

        URLDispEqAltura = "https://mariansr.com/DBConsDispEPCC.php?";
        URLDispEqAltura += ("NMaletas="+NMaletas+"&M1="+Bags[0]+"&M2="+Bags[1]+"&M3="+Bags[2]+"&M4="+Bags[3]+"&FechaInit='"+FI+"'&FechaEnd='"+FE+"'");
        //Toast.makeText(context, URLDispEqAltura, Toast.LENGTH_LONG).show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLDispEqAltura, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;
                String Desabilitados = null, Disponibles = null,  NoDisponible = null;

                try {

                    jsonObject = response.getJSONObject(0); // Escogemos el primer Objeto JSON
                    for (int i = 0; i < NMaletas; i++){
                        // Escogemos el valor con la CLAVE "EPCCx"
                        int e = i+1;
                        EPCCDisponible[i] = (jsonObject.getString("EPCC"+(i+1) ));
                        //Toast.makeText(context, "EPCC"+ (i+1) + " "+EPCCDisponible[i], Toast.LENGTH_SHORT).show();


                        if (EPCCDisponible[i].equals("DESHAB")){
                            if (Desabilitados == null) Desabilitados = (""+Bags[i]); else Desabilitados += (" ,"+Bags[i]);
                            EPCCIsAvaliable[i]=false;
                            returnEPCCAvaliable &= EPCCIsAvaliable[i];
                        } else if (EPCCDisponible[i].equals("DIS")){
                            if (Disponibles == null) Disponibles = (""+Bags[i]); else Disponibles += (" ,"+Bags[i]);
                            EPCCIsAvaliable[i]=true;
                            returnEPCCAvaliable &= EPCCIsAvaliable[i];
                        } else {
                            if (NoDisponible == null) NoDisponible = (""+Bags[i]); else NoDisponible += (" ,"+Bags[i]);
                            EPCCIsAvaliable[i]=false;
                            returnEPCCAvaliable &= EPCCIsAvaliable[i];
                        }

                    }

                    SetResults("EPCC",returnEPCCAvaliable);

                    if (!ActivityContenedora.getLocalClassName().equals(NofActvty)){

                        if (Disponibles != null && Desabilitados == null && NoDisponible == null){

                            Toast.makeText(context, "Todos los EPCC se añadieron con éxito", Toast.LENGTH_SHORT).show();

                        } else if (Disponibles != null && (Desabilitados != null || NoDisponible != null)){
                            String mensaje = "";

                            if (Desabilitados != null && NoDisponible != null) mensaje = NoDisponible+" "+Desabilitados;
                            else if (Desabilitados != null)mensaje = Desabilitados;
                            else if (NoDisponible != null)mensaje = NoDisponible;

                            Toast.makeText(context, "Los EPCC: "+ mensaje +" No se encuentran disponibles", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Los EPCC NO se añadieron de manera éxitosa, intente con otros.", Toast.LENGTH_SHORT).show();

                        } else if (Disponibles == null) Toast.makeText(context, "Ningún EPCC se encuentra disponible, intente con otros.", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {

                    Toast.makeText(context, "Jarray EPCC 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    SetResults("EPCC", false);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Jarray EPCC 2:" + error.getMessage(), Toast.LENGTH_LONG ).show();
                SetResults("EPCC", false);
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

        return returnEPCCAvaliable;
    }

    public boolean AscDescenso(boolean AscDescenso){

        // Informamos a la activity contenedora que hemos empezado una solicitud
        ((InterfazActivityFragment)ActivityContenedora).EstadoDeSolicitud("ASCDESCENSO",EnConsulta,false);
        dataModel.EQ_Descenso = AscDescenso;
        SetResults("ASCDESCENSO",AscDescenso);

        if (!ActivityContenedora.getLocalClassName().equals(NofActvty))Toast.makeText(context,"Equipo de Ascenso/Descenso Solicitado",Toast.LENGTH_SHORT).show();
        //else Toast.makeText(context,"Equipo de Ascenso/Descenso Solicitado",Toast.LENGTH_SHORT).show();

        return true;
    }

    public boolean Cuerda(boolean Cuerda){

        // Informamos a la activity contenedora que hemos empezado una solicitud
        ((InterfazActivityFragment)ActivityContenedora).EstadoDeSolicitud("CUERDA",EnConsulta,false);
        dataModel.Cuerda = Cuerda;
        SetResults("CUERDA",Cuerda);

        if (!ActivityContenedora.getLocalClassName().equals(NofActvty)) Toast.makeText(context,"Cuerda Solicitada",Toast.LENGTH_SHORT).show();
        //else Toast.makeText(context,"Cuerda Solicitada",Toast.LENGTH_SHORT).show();

        return true;
    }

    public boolean Señalizacion(String señalizacion){

        // Informamos a la activity contenedora que hemos empezado una solicitud
        ((InterfazActivityFragment)ActivityContenedora).EstadoDeSolicitud("SEÑALIZACION",EnConsulta,false);
        dataModel.Señalizacion = señalizacion;
        SetResults("SEÑALIZACION", true);

        if (!ActivityContenedora.getLocalClassName().equals(NofActvty)) Toast.makeText(context,"Señalización Solicitada "+dataModel.Señalizacion,Toast.LENGTH_SHORT).show();
        //else Toast.makeText(context,"Señalización Solicitada",Toast.LENGTH_SHORT).show();

        return true;
    }

}
