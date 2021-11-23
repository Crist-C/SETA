package com.examplecodes.Seta.Inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.examplecodes.Seta.MainMenus.MainMenuActivity;
import com.examplecodes.learnapplication.R;

import java.util.ArrayList;
import java.util.List;

public class GestionInventario extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ImageButton ButtToHome;
    private ListView InventariOptionsListView;
    List<OptionItem> mOptionList = new ArrayList<OptionItem>();
    OptionsListAdapter optionsListAdapter;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_inventario);

        ButtToHome = (ImageButton) findViewById(R.id.ImageToHomeGI);
        ButtToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHome = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentToHome);
            }
        });


        InventariOptionsListView = findViewById(R.id.InventariOptionsList);
        InventariOptionsListView.setOnItemClickListener(this);


        mOptionList.add(new OptionItem("Escaleras",R.drawable.ic_baseline_arrow_right_24));
        mOptionList.add(new OptionItem("Andamio Certificado",R.drawable.ic_baseline_arrow_right_24));
        mOptionList.add(new OptionItem("Andamio Unipersonal",R.drawable.ic_baseline_arrow_right_24));
        mOptionList.add(new OptionItem("Personal HSE",R.drawable.ic_baseline_arrow_right_24));
        mOptionList.add(new OptionItem("EPCC",R.drawable.ic_baseline_arrow_right_24));
        optionsListAdapter = new OptionsListAdapter(this,R.layout.inventari_option_item,mOptionList);
        InventariOptionsListView.setAdapter(optionsListAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int OptionClicked, long l) {

        switch (OptionClicked){

            case 0:       intent = new Intent(this,InvEscalera.class);
                            startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;

        }
        Toast.makeText(getApplicationContext(),"Elemento Click Numero "+OptionClicked,Toast.LENGTH_SHORT).show();
    }
}