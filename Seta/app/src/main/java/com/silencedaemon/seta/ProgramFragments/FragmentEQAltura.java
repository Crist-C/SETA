package com.silencedaemon.seta.ProgramFragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.silencedaemon.seta.Funciones.DataModel;
import com.silencedaemon.seta.Funciones.DataBase;
import com.silencedaemon.seta.Funciones.Fechas;
import com.silencedaemon.seta.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEQAltura#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEQAltura extends Fragment implements AdapterView.OnItemSelectedListener ,View.OnClickListener {


    Button ButtAnexarEAltura;
    CheckBox EQAlHSE, EQAscenso, Cuerda;
    ToggleButton SeñalCono, SeñalCinta;
    ToggleButton Bag1, Bag2, Bag3, Bag4, Bag5, Bag6, Bag7, Bag8, Bag9;
    Spinner HorariosHSE, AlturaMax;
    ArrayAdapter HorarioSpinerAdapter, AlturaSpinerAdapter;

    private int TotalMaletasSolicitadas = 0;
    private static final int LimiteDeMaletas = 4;

    private DataModel dataModel;
    Fechas fechas = new Fechas();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataModel = ViewModelProviders.of(getActivity()).get(DataModel.class);

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e_q_altura, container, false);

        ButtAnexarEAltura = (Button) view.findViewById(R.id.AnexarASolicitud);

        EQAlHSE = (CheckBox) view.findViewById(R.id.AddHSE);
        HorariosHSE = (Spinner) view.findViewById(R.id.HorarioSpinner);
        AlturaMax = (Spinner) view.findViewById(R.id.Alturaspinner);

        Bag1 = (ToggleButton) view.findViewById(R.id.BAGButton1);
        Bag1.setOnClickListener(this);
        Bag2 = (ToggleButton) view.findViewById(R.id.BAGButton2);
        Bag2.setOnClickListener(this);
        Bag3 = (ToggleButton) view.findViewById(R.id.BAGButton3);
        Bag3.setOnClickListener(this);
        Bag4 = (ToggleButton) view.findViewById(R.id.BAGButton4);
        Bag4.setOnClickListener(this);
        Bag5 = (ToggleButton) view.findViewById(R.id.BAGButton5);
        Bag5.setOnClickListener(this);
        Bag6 = (ToggleButton) view.findViewById(R.id.BAGButton6);
        Bag6.setOnClickListener(this);
        Bag7 = (ToggleButton) view.findViewById(R.id.BAGButton7);
        Bag7.setOnClickListener(this);
        Bag8 = (ToggleButton) view.findViewById(R.id.BAGButton8);
        Bag8.setOnClickListener(this);
        Bag9 = (ToggleButton) view.findViewById(R.id.BAGButton9);
        Bag9.setOnClickListener(this);

        EQAscenso = (CheckBox) view.findViewById(R.id.AddEQAscensoDescenso);
        Cuerda = (CheckBox) view.findViewById(R.id.AddCuerda);
        SeñalCono = (ToggleButton) view.findViewById(R.id.ConoButton);
        SeñalCinta = (ToggleButton) view.findViewById(R.id.CintaButton);

        HorarioSpinerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Horarios, android.R.layout.simple_dropdown_item_1line);
        HorarioSpinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        HorariosHSE.setAdapter(HorarioSpinerAdapter);
        HorariosHSE.setEnabled(false);

        AlturaSpinerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.AlturaMax,android.R.layout.simple_dropdown_item_1line);
        AlturaSpinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AlturaMax.setAdapter(AlturaSpinerAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        EQAlHSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EQAlHSE.isChecked()){
                    HorariosHSE.setEnabled(true);
                } else if (!EQAlHSE.isChecked()){
                    HorariosHSE.setEnabled(false);
                }
            }
        });


        ButtAnexarEAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se instancia la actividad contenedora para enviar los datos atraves del Model
                Activity ActivityContenedora = getActivity();

                // 1. Comprobamos que todos los campos esten debidamente diligenciados
                if (DataComprovation()) {

                    // 3. Se crea almacena toda la información de la solicitud en dataModel
                    SaveEQAData();

                    ConsultarDisponibilidad();

                    //Transmite la información atraves de la interfaz hacia la activity
                    //((InterfazActivityFragment)ActivityContenedora).AlturaData(dataModel);

                    // 4. Consultamos y validamos si la escalera escogida se encuentra disponible para ser programada en las
                    //      Fechas que se han escogido. De ser así se ejecuta el Intent.
                    // ConsultarDisponibilidadEscalera();


                }
            }
        });
    }

    private boolean ConsultarDisponibilidad() {
        DataBase dataBase = new DataBase(getContext(), getActivity(),dataModel);

        if (dataModel.HSE) dataBase.PersonalHSE("0",dataModel.HorarioHSE, dataModel.DIEQAltura,dataModel.DEEQAltura);
        if (dataModel.TotalEQAltura != 0)dataBase.EquiposAltura(dataModel.TotalEQAltura, dataModel.Bags,dataModel.DIEQAltura,dataModel.DEEQAltura);
        if (dataModel.EQ_Descenso) dataBase.AscDescenso(true);
        if (dataModel.Cuerda)dataBase.Cuerda(true);
        if (dataModel.Señalizacion.contains("Cono") || dataModel.Señalizacion.contains("Cinta") )dataBase.Señalizacion(dataModel.Señalizacion);


        return true;
    }

    private boolean DataComprovation(){
        // 1. LLamamos a la Funcion de comprobar si las fechas han sido coherentemente elegidas
        boolean FechasCorrectas = fechas.ValidarFechas(getActivity(), R.id.FechaInitSolicitud1, R.id.FechaEndSolicitud2);
        if (!FechasCorrectas || (!EQAlHSE.isChecked() && TotalMaletasSolicitadas == 0 && !EQAscenso.isChecked()
            && !Cuerda.isChecked() && !SeñalCono.isChecked() && !SeñalCinta.isChecked()) ){
            if (!FechasCorrectas) Toast.makeText(getContext(),"Elije las fechas, y de manera coherente",Toast.LENGTH_SHORT).show();
            else Toast.makeText(getContext(),"Elija los equipos o personal que requiere",Toast.LENGTH_SHORT).show();
            return false;
        }else if (FechasCorrectas) return true;
        return false;
    }

    private void SaveEQAData(){

        dataModel.HSE = EQAlHSE.isChecked();
        dataModel.HorarioHSE = EQAlHSE.isChecked()? HorariosHSE.getSelectedItem().toString() : "NA";
        dataModel.TotalEQAltura = TotalMaletasSolicitadas;
        dataModel.EQ_Descenso = EQAscenso.isChecked();
        dataModel.Cuerda = Cuerda.isChecked();

        if (SeñalCinta.isChecked() && SeñalCono.isChecked()) dataModel.Señalizacion = "Cinta/Cono";
        else if (SeñalCinta.isChecked()) dataModel.Señalizacion = "Cinta";
        else if (SeñalCono.isChecked()) dataModel.Señalizacion = "Cono";
        else  dataModel.Señalizacion = "";

        dataModel.AlturaMaxima = AlturaMax.getSelectedItem().toString();
        dataModel.DIEQAltura = fechas.getFechasString(getActivity(),R.id.FechaInitSolicitud1);
        dataModel.DEEQAltura = fechas.getFechasString(getActivity(),R.id.FechaEndSolicitud2);
        dataModel.FechaInicioSolicitud = fechas.getFechasString(getActivity(),R.id.FechaInitSolicitud1);
        dataModel.FechaFinalSolicitud = fechas.getFechasString(getActivity(),R.id.FechaEndSolicitud2);


        ToggleButton Bag;
        int[] BagsIDS = {R.id.BAGButton1, R.id.BAGButton2,R.id.BAGButton3,R.id.BAGButton4,
                R.id.BAGButton5, R.id.BAGButton6, R.id.BAGButton7, R.id.BAGButton8, R.id.BAGButton9};

        for (int nBag = 0, i = 0; nBag < BagsIDS.length; nBag++){
            Bag = (ToggleButton) getView().findViewById(BagsIDS[nBag]);
            if (Bag.isChecked()){
                dataModel.Bags[i++] = (nBag+1);
            }
        }

        dataModel.Solicitud = true;

    }

    @Override
    public void onClick(View view2) {

        switch (view2.getId()){

            case R.id.BAGButton1:
            case R.id.BAGButton2:
            case R.id.BAGButton3:
            case R.id.BAGButton4:
            case R.id.BAGButton5:
            case R.id.BAGButton6:
            case R.id.BAGButton7:
            case R.id.BAGButton8:
            case R.id.BAGButton9:

                ToggleButton Bag;
                Bag = (ToggleButton) getView().findViewById(view2.getId());
                if (Bag.isChecked()){
                    if (TotalMaletasSolicitadas < LimiteDeMaletas){
                        TotalMaletasSolicitadas++;
                        //Toast.makeText(getContext(),""+TotalMaletasSolicitadas,Toast.LENGTH_SHORT).show();
                    } else if (TotalMaletasSolicitadas >= LimiteDeMaletas){
                        Bag.setChecked(false);
                        Toast.makeText(getContext(),"Prestamo máximo de "+TotalMaletasSolicitadas+" equipos de altura por servicio",Toast.LENGTH_SHORT).show();
                    }
                }else if (!Bag.isChecked() && TotalMaletasSolicitadas > 0){
                    TotalMaletasSolicitadas--;
                    //Toast.makeText(getContext(),""+TotalMaletasSolicitadas,Toast.LENGTH_SHORT).show();
                    //if (Bag.isChecked()) Bag.setChecked(false);
                }

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public FragmentEQAltura() {
        // Required empty public constructor
    }

    public static FragmentEQAltura newInstance(String param1, String param2) {
        FragmentEQAltura fragment = new FragmentEQAltura();
        Bundle args = new Bundle();

        return fragment;
    }


}