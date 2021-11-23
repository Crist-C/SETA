package com.examplecodes.Seta.Servicios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.examplecodes.Seta.Funciones.DataModel;
import com.examplecodes.Seta.MainMenus.MainMenuActivity;
import com.examplecodes.Seta.ProgramFragments.InterfazActivityFragment;
import com.examplecodes.Seta.ProgramFragments.PagerControler;
import com.examplecodes.learnapplication.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Date;

import static com.examplecodes.learnapplication.R.*;

public class NewSolicitud extends AppCompatActivity implements InterfazActivityFragment, View.OnClickListener {

    ViewPager TabsViewPager;
    ImageButton ButtToHome;
    TabLayout TabsEquipos;
    TabItem TabEscalera, TabAndamio, TabEqAltura;
    Button ContinuarSolicitud;
    EditText FechaInitSolicitud, FechaEndSolicitud;

    Date FechaEnd;
    Date FechaInit;
    Date Today;
    private int diaInit, mesInit, añoInit, diaEnd, mesEnd, añoEnd;

    DataModel dataLadderModel, dataAndamioModel, dataEQAlturaModel;
    PagerControler pagerAdapter;
    private Intent IntentToProgEscalera2;

    boolean EscaleraSolicitada = false, AndamioSolicitado = false, EPCCSolicitado = false, HSESolicitado = false,
            CuerdaSolicitada = false, AscDescensoSolicitado = false, SeñalizacionSolicitada = false;

    int SinConsultar = 1, EnConsulta = 2, ConsultaFinalizada = 3;
    int SolicitudInProcess = ConsultaFinalizada;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.new_solicitud);

        dataLadderModel = ViewModelProviders.of(this).get(DataModel.class);
        dataAndamioModel = ViewModelProviders.of(this).get(DataModel.class);
        dataEQAlturaModel = ViewModelProviders.of(this).get(DataModel.class);

        ButtToHome = (ImageButton) findViewById(R.id.ImageToHome);

        TabsEquipos = (TabLayout) findViewById(id.EquiposTabLayout);
        TabsViewPager = (ViewPager) findViewById(id.TabsViewPager);
        TabEscalera = (TabItem) findViewById(id.TabEscalera);
        TabAndamio = (TabItem) findViewById(id.TabAndamio);
        TabEqAltura = (TabItem) findViewById(id.TabEQAltura);
        ContinuarSolicitud = (Button) findViewById(id.ButtContinuarSolicitud);

        FechaInitSolicitud = (EditText) findViewById(id.FechaInitSolicitud1);
        FechaInitSolicitud.setOnClickListener(this);

        FechaEndSolicitud = (EditText) findViewById(id.FechaEndSolicitud2);
        FechaEndSolicitud.setOnClickListener(this);


        pagerAdapter = new PagerControler(getSupportFragmentManager(), TabsEquipos.getTabCount());
        TabsViewPager.setAdapter(pagerAdapter);


        TabsEquipos.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabsViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    pagerAdapter.notifyDataSetChanged();
                }
                if (tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();
                }
                if (tab.getPosition() == 2) {
                    pagerAdapter.notifyDataSetChanged();
                }
                if (tab.getPosition() == 3) {
                    pagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TabsEquipos));


        IntentToProgEscalera2 = new Intent(getApplicationContext(), ProgramEscaleraSecond.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });


        ContinuarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if ((EscaleraSolicitada || AndamioSolicitado || EPCCSolicitado || HSESolicitado || AscDescensoSolicitado || CuerdaSolicitada || SeñalizacionSolicitada)
                        && (SolicitudInProcess == ConsultaFinalizada)){

                    if (ValidaciónFechas()){

                       startActivity(IntentToProgEscalera2);

                    }

                }else{

                    if (SolicitudInProcess == EnConsulta) Toast.makeText(getApplicationContext(),"Solicitud en Proceso", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(),"Aún no has solicitado un servicio", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private boolean ValidaciónFechas(){
/*
        if (dataAndamioModel.DIAndamio == null)Toast.makeText(getApplicationContext(),"fecha andamio is NULL "+ dataAndamioModel.DIAndamio, Toast.LENGTH_SHORT).show();
        else Toast.makeText(getApplicationContext(),"fecha andamio: "+ dataAndamioModel.DIAndamio, Toast.LENGTH_SHORT).show();
*/

        if ( dataLadderModel.DILadder != null && !dataLadderModel.DILadder.equals(FechaInitSolicitud.getText().toString()) ){
            Toast.makeText(getApplicationContext(),"Vuelva a solicitar la escalera ya que las Fechas fueron mofificadas", Toast.LENGTH_SHORT).show();
            return false;
        }else if ( dataAndamioModel.DIAndamio != null && !dataAndamioModel.DIAndamio.equals(FechaInitSolicitud.getText().toString()) ){
            Toast.makeText(getApplicationContext(),"Vuelva a solicitar el andamio ya que las Fechas fueron mofificadas", Toast.LENGTH_SHORT).show();
            return false;
        } else if ( dataEQAlturaModel.DIEQAltura != null && !dataEQAlturaModel.DIEQAltura.equals(FechaInitSolicitud.getText().toString()) ){
            Toast.makeText(getApplicationContext(),"Vuelva a solicitar HSE ya que las Fechas fueron mofificadas", Toast.LENGTH_SHORT).show();
            return false;
        } else if ( dataLadderModel.DELadder != null && !dataLadderModel.DELadder.equals(FechaEndSolicitud.getText().toString()) ){
            Toast.makeText(getApplicationContext(),"Vuelva a solicitar la escalera ya que las Fechas fueron mofificadas", Toast.LENGTH_SHORT).show();
            return false;
        } else if ( dataAndamioModel.DEAndamio != null && !dataAndamioModel.DEAndamio.equals(FechaEndSolicitud.getText().toString()) ){
            Toast.makeText(getApplicationContext(),"Vuelva a solicitar el andamio ya que las Fechas fueron mofificadas", Toast.LENGTH_SHORT).show();
            return false;
        } else if ( dataEQAlturaModel.DEEQAltura != null && !dataEQAlturaModel.DEEQAltura.equals(FechaEndSolicitud.getText().toString()) ){
            Toast.makeText(getApplicationContext(),"Vuelva a solicitar HSE ya que las Fechas fueron mofificadas", Toast.LENGTH_SHORT).show();
            return false;
        }

        return (true);
    }

    @Override
    public void LadderData(DataModel InformacionEscalera) {
        dataLadderModel = InformacionEscalera;
        EscaleraSolicitada = dataLadderModel.Solicitud;

        //Toast.makeText(getApplicationContext(),"Solicitud Escalera: "+EscaleraSolicitada,Toast.LENGTH_SHORT).show();

        if (EscaleraSolicitada && InformacionEscalera.LadderAvaliable){
            IntentToProgEscalera2.putExtra("ServicioEscalera",EscaleraSolicitada); // Si se realizo la solicitud de una escalera
            // 3. Se crea el Intent para cambio de actividad
            IntentToProgEscalera2.putExtra("TipoEscalera", dataLadderModel.LadderType); // Tipo de escalera
            IntentToProgEscalera2.putExtra("IsRefEscalera", dataLadderModel.LadderReference); // Si se escogió una referencia
            IntentToProgEscalera2.putExtra("NumReferenceEscalera", dataLadderModel.NumLadderReference); // Numero de la referencia
            IntentToProgEscalera2.putExtra("PasosEscalera", dataLadderModel.LadderSteps); // Numero de pasos de la escalera que se escogió
            IntentToProgEscalera2.putExtra("FIEscalera", dataLadderModel.DILadder); // Fecha de inicio de la solicitud
            IntentToProgEscalera2.putExtra("FEEscalera", dataLadderModel.DELadder); // Fecha de Finalización de la solicitud
            IntentToProgEscalera2.putExtra("FIGeneral", FechaInitSolicitud.getText().toString()); // Fecha de inicio de la solicitud
            IntentToProgEscalera2.putExtra("FEGeneral", FechaEndSolicitud.getText().toString()); // Fecha de Finalización de la solicitud
            IntentToProgEscalera2.putExtra("TotalDiasEscalera", dataLadderModel.LadderTotalDays); // Total de dias solicitados

        }


    }

    @Override
    public void AndamioData(DataModel InformacionAndamio) {
        dataAndamioModel = InformacionAndamio;
        AndamioSolicitado = dataAndamioModel.Solicitud;

        //Toast.makeText(getApplicationContext(),"Andamio Refe: "+dataAndamioModel.NumAndamReference,Toast.LENGTH_SHORT).show();

        if (AndamioSolicitado && InformacionAndamio.AndamAvaliable){
            IntentToProgEscalera2.putExtra("ServicioAndamio",AndamioSolicitado); // Si se realizo la solicitud de una escalera
            // 3. Se crea el Intent para cambio de actividad
            IntentToProgEscalera2.putExtra("TipoAndamio", dataAndamioModel.AndamioType); // Tipo de escalera
            IntentToProgEscalera2.putExtra("IsRefAndamio", dataAndamioModel.AndamReference); // Si se escogió una referencia
            IntentToProgEscalera2.putExtra("NumReferenceAndamio", dataAndamioModel.NumAndamReference); // Numero de la referencia
            IntentToProgEscalera2.putExtra("Secciones", dataAndamioModel.Secciones); // Numero de pasos de la escalera que se escogió
            IntentToProgEscalera2.putExtra("FIAndamio", dataAndamioModel.DIAndamio); // Fecha de inicio de la solicitud
            IntentToProgEscalera2.putExtra("FEAndamio", dataAndamioModel.DEAndamio); // Fecha de Finalización de la solicitud
            IntentToProgEscalera2.putExtra("FIGeneral", FechaInitSolicitud.getText().toString()); // Fecha de inicio de la solicitud
            IntentToProgEscalera2.putExtra("FEGeneral", FechaEndSolicitud.getText().toString()); // Fecha de Finalización de la solicitud
            IntentToProgEscalera2.putExtra("TotalDiasAndamio", dataAndamioModel.AndamTotalDays); // Total de dias solicitados

        }

    }

    @Override
    public void AlturaData(DataModel InfoAltura) {
        dataEQAlturaModel = InfoAltura;
        EPCCSolicitado = dataEQAlturaModel.Solicitud;


        if (EPCCSolicitado){

            // 3. Si se ha solicitado HSE o EPCC se Crea el EXTRA con el Nombre, de lo contario nó, y se dejan los valores por defecto
            if (InfoAltura.HSEAvaliable){

                IntentToProgEscalera2.putExtra("HSE",dataEQAlturaModel.HSE); // Si se realizo la solicitud de una escalera
                IntentToProgEscalera2.putExtra("CoordinadorHSE",dataEQAlturaModel.CoordinadorHSE); // Si se realizo la solicitud de una escalera
                IntentToProgEscalera2.putExtra("HorarioHSE",dataEQAlturaModel.HorarioHSE); // Si se realizo la solicitud de una escalera
            }
            if (InfoAltura.EPCCAvaliable){
                IntentToProgEscalera2.putExtra("TotalDiasEPCC", dataEQAlturaModel.EPCCTotalDias); // Total de dias solicitados
                IntentToProgEscalera2.putExtra("TotalEPCC", dataEQAlturaModel.TotalEQAltura); // Total de dias solicitados
                IntentToProgEscalera2.putExtra("BAGS", dataEQAlturaModel.Bags); // Total de dias solicitados
            }

            IntentToProgEscalera2.putExtra("ServicioEPCC",EPCCSolicitado); // Si se realizo la solicitud de una escalera
            IntentToProgEscalera2.putExtra("AlturaMaxima", dataEQAlturaModel.AlturaMaxima); // Numero de pasos de la escalera que se escogió
            IntentToProgEscalera2.putExtra("FIEPCC", dataEQAlturaModel.DIEQAltura); // Fecha de inicio de la solicitud
            IntentToProgEscalera2.putExtra("FEEPCC", dataEQAlturaModel.DEEQAltura); // Fecha de Finalización de la solicitud
            IntentToProgEscalera2.putExtra("FIGeneral", FechaInitSolicitud.getText().toString()); // Fecha de inicio de la solicitud
            IntentToProgEscalera2.putExtra("FEGeneral", FechaEndSolicitud.getText().toString()); // Fecha de Finalización de la solicitud



        }


    }

    @Override
    public void AscDescenso(DataModel AscDescenso) {
        if (AscDescenso.EQ_Descenso) IntentToProgEscalera2.putExtra("Extras", true);
        IntentToProgEscalera2.putExtra("EQDescenso", AscDescenso.EQ_Descenso); // Tipo de escalera
        IntentToProgEscalera2.putExtra("AlturaMaxima", AscDescenso.AlturaMaxima); // Numero de pasos de la escalera que se escogió
        IntentToProgEscalera2.putExtra("FIGeneral", FechaInitSolicitud.getText().toString()); // Fecha de inicio de la solicitud
        IntentToProgEscalera2.putExtra("FEGeneral", FechaEndSolicitud.getText().toString()); // Fecha de Finalización de la solicitud
        AscDescensoSolicitado = AscDescenso.EQ_Descenso;
    }

    @Override
    public void Cuerda(DataModel Cuerda) {
        if (Cuerda.Cuerda) IntentToProgEscalera2.putExtra("Extras",true);
        IntentToProgEscalera2.putExtra("Cuerda", Cuerda.Cuerda); // Si se escogió una referencia
        IntentToProgEscalera2.putExtra("AlturaMaxima", Cuerda.AlturaMaxima); // Numero de pasos de la escalera que se escogió
        IntentToProgEscalera2.putExtra("FIGeneral", FechaInitSolicitud.getText().toString()); // Fecha de inicio de la solicitud
        IntentToProgEscalera2.putExtra("FEGeneral", FechaEndSolicitud.getText().toString()); // Fecha de Finalización de la solicitud
        CuerdaSolicitada = Cuerda.Cuerda;
    }

    @Override
    public void Señalizacion(DataModel Señalización) {
        if (!dataEQAlturaModel.Señalizacion.equals("")) IntentToProgEscalera2.putExtra("ServicioSeñalizacion",true); // Si se realizo la solicitud de una escalera
        IntentToProgEscalera2.putExtra("Señalizacion", Señalización.Señalizacion); // Numero de la referencia
        IntentToProgEscalera2.putExtra("FIGeneral", FechaInitSolicitud.getText().toString()); // Fecha de inicio de la solicitud
        IntentToProgEscalera2.putExtra("FEGeneral", FechaEndSolicitud.getText().toString()); // Fecha de Finalización de la solicitud
        SeñalizacionSolicitada = !Señalización.Señalizacion.equals("");
    }

    @Override
    public void EstadoDeSolicitud(String Proceso, int Estado, boolean Resultado) {
        SolicitudInProcess = Estado;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.FechaInitSolicitud1:
                Calendar cal = Calendar.getInstance();

                diaInit = cal.get(Calendar.DAY_OF_MONTH);
                mesInit = cal.get(Calendar.MONTH);
                añoInit = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialogInit = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        FechaInitSolicitud.setText(year + "-" + (month + 1) + "-" + dayOfMonth);

                    }
                }
                        , añoInit, mesInit, diaInit);
                datePickerDialogInit.show();
                break;

            case R.id.FechaEndSolicitud2:
                Calendar cal2 = Calendar.getInstance();
                diaEnd = cal2.get(Calendar.DAY_OF_MONTH);
                mesEnd = cal2.get(Calendar.MONTH);
                añoEnd = cal2.get(Calendar.YEAR);

                DatePickerDialog datePickerDialogEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        FechaEndSolicitud.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }
                        , añoEnd, mesEnd, diaEnd);
                datePickerDialogEnd.show();
                break;


        }

    }


}
