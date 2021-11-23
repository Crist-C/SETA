package com.examplecodes.Seta.Funciones;

import androidx.lifecycle.ViewModel;

public class DataModel extends ViewModel {

    // General
    public String FechaInicioSolicitud = "", FechaFinalSolicitud = "";

    //Solicitud de escalera
    public String LadderType = "", LadderSteps = "", DILadder, DELadder;
    public String NumLadderReference = "", LadderTotalDays  = "0";
    public boolean LadderReference = false, LadderAvaliable = false, HSE = false, Solicitud = false;

    public String AndamioType = "", NumAndamReference = "", Secciones = "";
    public String DIAndamio, DEAndamio, AndamTotalDays = "0";
    public boolean AndamReference = false, AndamAvaliable = false;


    public String HorarioHSE = "", CoordinadorHSE = "", AlturaMaxima = "";
    public Boolean HSEAvaliable = false;
    public String DIEQAltura, DEEQAltura, EPCCTotalDias = "0";

    static final int LimiteEQAltura = 4;
    public int[] Bags = {0,0,0,0};
    public int TotalEQAltura = 0;
    public Boolean EPCCAvaliable = false;

    public boolean EQ_Descenso = false, Cuerda = false;
    public String Señalizacion = "";



}
