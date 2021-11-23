package com.silencedaemon.seta.ProgramFragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.silencedaemon.seta.Funciones.DataBase;
import com.silencedaemon.seta.Funciones.DataModel;
import com.silencedaemon.seta.Funciones.Fechas;
import com.silencedaemon.seta.R;


public class FragmentEscaleras extends Fragment implements AdapterView.OnItemSelectedListener {

    /// Instancia de Elementos que componen el View de la Activity
    View view;
    CheckBox ChooseReference;
    Spinner stairReference;
    Button ButtAnexarEscalera;
    SeekBar BarNumeroPasos;
    TextView TextNPasosInd;
    ToggleButton TipoEscalera;
    TextView IndTipoEscalera;

    /// Array para el despliegue del Spiner de las Referencias de las escaleras
    ArrayAdapter<CharSequence> spinnerAdapter;

    /// Request para el paso de la información a la Base de Datos
    Activity activity = getActivity();
    Fechas fechas = new Fechas();
    DataModel dataModel;
    String TipoEsc;

    // Base de Datos
    DataBase dataBase;

    /// Constantes del proceso
    static final String Tag = "DATO";
    static final int MinLadderSteps = 3;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag,"OnCreate");
        dataModel = ViewModelProviders.of(requireActivity()).get(DataModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(Tag,"OnCreateView");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_escaleras, container, false);

        BarNumeroPasos = (SeekBar) view.findViewById(R.id.BarNpasos);
        TextNPasosInd = (TextView) view.findViewById(R.id.NumeroPasosIndicador);
        ChooseReference = (CheckBox) view.findViewById(R.id.CheckReference);
        stairReference = (Spinner) view.findViewById(R.id.spinnerReferences);
        TipoEscalera = (ToggleButton) view.findViewById(R.id.TipoEscaleraButton);
        ButtAnexarEscalera = (Button) view.findViewById(R.id.AnexarASolicitud);
        IndTipoEscalera = (TextView) view.findViewById(R.id.IndTipoEscalera);

        TextNPasosInd.setText(dataModel.LadderSteps);
        stairReference.setEnabled(false);

        if (savedInstanceState != null) TextNPasosInd.setText(savedInstanceState.getString("TextNPasosSaved"));



        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag,"OnResume");

        //Se instancia la DataBase, con la actividad contenedora y el dataModel que ya contiene toda la información.
        dataBase = new DataBase(getContext(),getActivity(),dataModel);

        //////////////  Función de los Elementos de la Activity //////////////////

        ChooseReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ChooseReference.isChecked()){
                    stairReference.setEnabled(true);
                    SpinnerAdapter(TextNPasosInd.getText().toString());
                } else if (!ChooseReference.isChecked()){
                    stairReference.setEnabled(false);
                }
            }
        });

        TipoEscalera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stairReference.isEnabled()) SpinnerAdapter(TextNPasosInd.getText().toString());
                if (TipoEscalera.isChecked()) IndTipoEscalera.setText("Expansiva");
                if (!TipoEscalera.isChecked())IndTipoEscalera.setText("Tijera");
            }
        });

        BarNumeroPasos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dataModel.LadderSteps = ((BarNumeroPasos.getProgress() + MinLadderSteps) + "");
                TextNPasosInd.setText(dataModel.LadderSteps);
                if(stairReference.isEnabled()) SpinnerAdapter(TextNPasosInd.getText().toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        ButtAnexarEscalera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // 1. Comprobamos que todos los campos esten debidamente diligenciados
                if (DataComprovation()) {

                    // 3. Se crea almacena toda la información de la solicitud en dataModel
                    SaveLadderData();

                    // 4. Consultamos y validamos si la escalera escogida se encuentra disponible para ser programada en las
                    //      Fechas que se han escogido. De ser así se envia la Información a la Activity Principal (NewSolicitud).
                    ConsultarDisponibilidadEscalera();

                }


            }
        });




    }

    private boolean DataComprovation(){
        // 1. LLamamos a la Funcion de comprobar si las fechas han sido coherentemente elegidas
        boolean FechasCorrectas = fechas.ValidarFechas(getActivity(), R.id.FechaInitSolicitud1, R.id.FechaEndSolicitud2);
        if (!(TextNPasosInd.getText().toString().equals("0")) && !TextNPasosInd.getText().toString().isEmpty() && FechasCorrectas){
            return true;
        }else {
            //5. Si no se encuentran bien diligenciados, se muestra un Mensaje Informativo
            if (TextNPasosInd.getText().toString().isEmpty() || TextNPasosInd.getText().toString().trim().equalsIgnoreCase("0")){
                TextNPasosInd.setError("Indique la cantidad de pasos de la escalera que necesita");
                Toast.makeText(getContext(),"Escoje los datos necesarios",Toast.LENGTH_SHORT).show();
            }else if (!FechasCorrectas){
                Toast.makeText(getContext(),"Elije las fechas, y de manera coherente",Toast.LENGTH_SHORT).show();
            }else Toast.makeText(getContext(),"Asegurate de haber escogido todos los datos de forma correcta",Toast.LENGTH_SHORT).show();

            return false;
        }

    }

    private void SaveLadderData() {

        // 3. Se Cargan todos los datos al dataModel

        // 3. La referencia de la escalera en caso de haberla escogido
        if (ChooseReference.isChecked()){
            dataModel.LadderReference = true;
            dataModel.NumLadderReference = stairReference.getSelectedItem().toString();

        } else if (!ChooseReference.isChecked()){
            dataModel.LadderReference = false;
            dataModel.NumLadderReference = "0";
        }

        if (dataModel.NumLadderReference.contains("P")){
            TipoEsc = "Plataforma";
            dataModel.NumLadderReference = dataModel.NumLadderReference.substring(1);

        } else {
            // 1. Tipo de Escalera
            if (TipoEscalera.isChecked()) {
                TipoEsc = "Extension";
            } else {
                TipoEsc = "Tijera";
            }
        }
        dataModel.LadderType = TipoEsc;

        // 2.Pasos de la escalera
        dataModel.LadderSteps = ((BarNumeroPasos.getProgress() + MinLadderSteps) + "");
        TextNPasosInd.setText((BarNumeroPasos.getProgress() + MinLadderSteps) + "");


        dataModel.DILadder = fechas.getFechasString(getActivity(),R.id.FechaInitSolicitud1);
        dataModel.DELadder = fechas.getFechasString(getActivity(),R.id.FechaEndSolicitud2);
        dataModel.FechaInicioSolicitud = fechas.getFechasString(getActivity(),R.id.FechaInitSolicitud1);
        dataModel.FechaFinalSolicitud = fechas.getFechasString(getActivity(),R.id.FechaEndSolicitud2);


    }

    private void ConsultarDisponibilidadEscalera() {

        dataBase.ConsultarEscaleraDisponible(dataModel.LadderType,dataModel.LadderSteps,dataModel.DILadder,
                    dataModel.DELadder,dataModel.NumLadderReference);

    }


    private void SpinnerAdapter(String Pasos)
    {
        if (!TipoEscalera.isChecked()){
            switch (Pasos) {
                case "3":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T3, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "4":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T4, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "5":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T5, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "6":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T6, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "7":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T7, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "8":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T8, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "9":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T9, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "10":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T10, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "12":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.T12, android.R.layout.simple_dropdown_item_1line);
                    break;
                default:
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.defaultVersion,android.R.layout.simple_dropdown_item_1line);
            }
        } else if (TipoEscalera.isChecked()){
            switch (Pasos) {
                case "6":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.E6, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "7":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.E7, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "10":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.E10, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "12":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.E12, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "14":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.E14, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "16":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.E16, android.R.layout.simple_dropdown_item_1line);
                    break;
                case "17":
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.E17, android.R.layout.simple_dropdown_item_1line);
                    break;
                default:
                    spinnerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.defaultVersion,android.R.layout.simple_dropdown_item_1line);
            }

        }else spinnerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.defaultVersion,android.R.layout.simple_dropdown_item_1line);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stairReference.setAdapter(spinnerAdapter);
        stairReference.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        dataModel.NumLadderReference = String.valueOf(spinnerAdapter.getItemId(i));
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public FragmentEscaleras() {
        // Required empty public constructor
    }

}