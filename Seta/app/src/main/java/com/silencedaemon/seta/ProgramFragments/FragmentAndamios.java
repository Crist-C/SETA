package com.silencedaemon.seta.ProgramFragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAndamios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAndamios extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    View view;
    private DataModel dataModel;
    Button ButtAnexarAndamio;
    SeekBar BarNumeroSecciones;
    TextView TextNSecccionesInd;
    ToggleButton TipoAndamioBut;
    TextView TipoAndamioInd, AlturaInd, PreguntaNSecciones;
    CheckBox ChooseReference;
    Spinner AndamioReference;
    ArrayAdapter<CharSequence> AndamioReferenceAdapter;

    private Fechas fechas = new Fechas();

    static final boolean UnipersonalState = true, CertificadoState = false;
    static final int MinAndamioSeccions = 2;
    String TipoAndamio;
    static final String Tag = "DATO";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataModel = ViewModelProviders.of(requireActivity()).get(DataModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_andamios, container, false);


        PreguntaNSecciones = (TextView) view.findViewById(R.id.PregNumeroSecciones);
        ButtAnexarAndamio = (Button) view.findViewById(R.id.AnexarASolicitud);
        BarNumeroSecciones = (SeekBar) view.findViewById(R.id.BarNSecciones);
        TextNSecccionesInd = (TextView) view.findViewById(R.id.NumeroSeccionesIndicador);
        TipoAndamioBut = (ToggleButton) view.findViewById(R.id.TipoAndamioButton);
        TipoAndamioInd = (TextView) view.findViewById(R.id.TipoAndamioInd);
        AlturaInd = (TextView) view.findViewById(R.id.AlturaAlcanzadaInd);


        TextNSecccionesInd.setText(dataModel.Secciones);
        if (TextNSecccionesInd.getText().toString().isEmpty()){
            AlturaInd.setText("");
        } else AlturaInd.setText( "Altura alcanzada: " + (Integer.parseInt(TextNSecccionesInd.getText().toString())  * 2) + " metros");


        ChooseReference = (CheckBox) view.findViewById(R.id.CheckReference);
        AndamioReference = (Spinner) view.findViewById(R.id.spinnerReferences);

        AndamioReferenceAdapter = ArrayAdapter.createFromResource(getContext(),R.array.AndaUnipersonal,android.R.layout.simple_dropdown_item_1line);
        AndamioReferenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AndamioReference.setAdapter(AndamioReferenceAdapter);
        AndamioReference.setOnItemSelectedListener(this);
        AndamioReference.setEnabled(false);
        ChooseReference.setEnabled(false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ChooseReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ChooseReference.isChecked()){
                    AndamioReference.setEnabled(true);
                } else if (!ChooseReference.isChecked()){
                    AndamioReference.setEnabled(false);
                }
            }
        });

        TipoAndamioBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TipoAndamioBut.isChecked()){
                    TipoAndamioInd.setText("And. Unipersonal");
                    ChooseReference.setEnabled(true);
                    BarNumeroSecciones.setEnabled(false);
                    if (ChooseReference.isChecked()){
                        AndamioReference.setEnabled(true);;
                    } else if (!ChooseReference.isChecked()){
                        AndamioReference.setEnabled(false);
                    }
                }

                if (!TipoAndamioBut.isChecked()){

                    TipoAndamioInd.setText("And. Certificado");
                    ChooseReference.setEnabled(false);
                    BarNumeroSecciones.setEnabled(true);
                    AndamioReference.setEnabled(false);
                }

            }
        });

        BarNumeroSecciones.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dataModel.Secciones = ((BarNumeroSecciones.getProgress() + MinAndamioSeccions) + "");
                TextNSecccionesInd.setText(dataModel.Secciones);
                if (TextNSecccionesInd.getText().toString().isEmpty()){
                    AlturaInd.setText("");
                } else AlturaInd.setText(  "Altura alcanzada: " + (Integer.parseInt(TextNSecccionesInd.getText().toString())  * 2) + " metros");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ButtAnexarAndamio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity ActividadContenedora = getActivity();
                // 1. Tomamos el valor de las Fechas y les convertimos a un fomato, para luego poderlas comparar
                //ConvertirFechas();

                // 2. Comprobamos que todos los campos esten debidamente diligenciados
                if (DataComprovation()) {

                    // 3. Se crea el Intent para cambio de actividad
                    SaveAndamioData();

                    //Transmite la información atraves de la interfaz hacia la activity
                    ((InterfazActivityFragment)ActividadContenedora).AndamioData(dataModel);

                    // 4. Consultamos y validamos si la escalera escogida se encuentra disponible para ser programada en las
                    //      Fechas que se han escogido. De ser así se ejecuta el Intent.
                    //String MyURL = "https://kiotx.000webhostapp.com/DBConsDispEscalera.php?TipoEscalera=" + TipoAndamio + "&Pasos=" +
                    //        TextNSecccionesInd.getText().toString() + "&Numero=0&FechaInit=" + FechaInitSolicitud.getText().toString() + "&FechaEnd=" + FechaEndSolicitud.getText().toString();
                    ConsultarDispAndamio();

                    //5. Si no se encuentran bien diligenciados, se muestra un Mensaje Informativo
                } else {

                }


            }
        });

    }


    private boolean DataComprovation(){

        // 1. LLamamos a la Funcion de comprobar si las fechas han sido coherentemente elegidas
        boolean FechasSonCorrectas = fechas.ValidarFechas(getActivity(), R.id.FechaInitSolicitud1, R.id.FechaEndSolicitud2);

        if (TipoAndamioBut.isChecked() && FechasSonCorrectas){
            return true;
        } else if (TipoAndamioBut.isChecked() && !FechasSonCorrectas) {
            Toast.makeText(getContext(), "Elije las fechas, y de manera coherente", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TipoAndamioBut.isChecked()){

            if (!TextNSecccionesInd.getText().toString().equals("0") && !TextNSecccionesInd.getText().toString().isEmpty() && FechasSonCorrectas){
                return true;
            }else {

                if (TextNSecccionesInd.getText().toString().isEmpty() || TextNSecccionesInd.getText().toString().trim().equals("0")){
                    TextNSecccionesInd.setError("Indique la cantidad de Secciones");
                    Toast.makeText(getContext(),"Escoje la cantidad de secciones",Toast.LENGTH_SHORT).show();
                } else if (!FechasSonCorrectas){
                    Toast.makeText(getContext(),"Elije las fechas, y de manera coherente",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getContext(),"Asegurate de haber escogido todos los datos de forma correcta",Toast.LENGTH_SHORT).show();

                return false;
            }

        }
        return false;
    }

    private void SaveAndamioData() {

        // 3. Se Cargan todos los datos al dataModel

        if (TipoAndamioBut.isChecked()) {
            TipoAndamio = "Unipersonal";

            if (ChooseReference.isChecked()){
                dataModel.AndamReference = true;
                dataModel.NumAndamReference = AndamioReference.getSelectedItem().toString();
                dataModel.Secciones = "0";
            } else if (!ChooseReference.isChecked()){
                dataModel.AndamReference = false;
                dataModel.NumAndamReference = "0";
                dataModel.Secciones = "0";
            }

        } else if (!TipoAndamioBut.isChecked()) {
            TipoAndamio = "Certificado";
            dataModel.AndamReference = false;
            dataModel.NumAndamReference = "0";
            dataModel.Secciones = TextNSecccionesInd.getText().toString();
        }
        dataModel.AndamioType = TipoAndamio;


        dataModel.DIAndamio = fechas.getFechasString(getActivity(),R.id.FechaInitSolicitud1);
        dataModel.DEAndamio = fechas.getFechasString(getActivity(),R.id.FechaEndSolicitud2);
        dataModel.FechaInicioSolicitud = fechas.getFechasString(getActivity(),R.id.FechaInitSolicitud1);
        dataModel.FechaFinalSolicitud = fechas.getFechasString(getActivity(),R.id.FechaEndSolicitud2);



    }

    private void ConsultarDispAndamio(){

        DataBase dataBase = new DataBase(getContext(), getActivity(), dataModel);
        dataModel.Solicitud = dataBase.ConsultarAndamioDisponible(dataModel.AndamioType ,dataModel.Secciones,dataModel.DIAndamio,
                dataModel.DEAndamio,dataModel.NumAndamReference);
    }


    public FragmentAndamios() {
        // Required empty public constructor
    }

    public static FragmentAndamios newInstance() {
        FragmentAndamios fragment = new FragmentAndamios();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}