package com.silencedaemon.seta.ProgramFragments;

import com.silencedaemon.seta.Funciones.DataModel;

public interface InterfazActivityFragment {


    public void LadderData(DataModel InformacionEscalera);
    public void AndamioData(DataModel InformacionAndamio);
    public void AlturaData(DataModel InfoAltura);
    public void AscDescenso(DataModel AscDescenso);
    public void Cuerda(DataModel Cuerda);
    public void Señalizacion(DataModel Señalización);


    public void EstadoDeSolicitud(String Proceso, int Estado, boolean Resultado);


}
