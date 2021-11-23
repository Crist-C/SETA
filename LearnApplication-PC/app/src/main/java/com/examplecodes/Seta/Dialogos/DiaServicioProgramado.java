package com.examplecodes.Seta.Dialogos;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examplecodes.learnapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaServicioProgramado#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaServicioProgramado extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiaServicioProgramado() {
        // Required empty public constructor
    }

    public static DiaServicioProgramado newInstance(String param1, String param2) {
        DiaServicioProgramado fragment = new DiaServicioProgramado();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dia_servicio_programado, container, false);
        getDialog().setTitle("Resultado de la Solicitud");
        return view;
    }
}