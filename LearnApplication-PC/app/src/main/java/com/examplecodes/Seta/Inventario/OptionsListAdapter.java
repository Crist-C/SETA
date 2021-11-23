package com.examplecodes.Seta.Inventario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.examplecodes.learnapplication.R;

import java.util.List;

public class OptionsListAdapter extends ArrayAdapter<OptionItem> {

    public List<OptionItem> OptionsList;
    public Context context;
    private int resourceLayout;

    public OptionsListAdapter(@NonNull Context context, int resource, List<OptionItem> objects) {
        super(context, resource, objects);

        this.OptionsList = objects;
        this.context = context;
        this.resourceLayout = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null)
            view = LayoutInflater.from(context).inflate(resourceLayout,null);


        OptionItem optionItem = OptionsList.get(position);

        ImageView imagen = (ImageView) view.findViewById(R.id.ItemImagen);
        imagen.setImageResource(optionItem.getImagen());

        TextView textViewOption = (TextView)view.findViewById(R.id.ItemText);
        textViewOption.setText(optionItem.getOpcion());



        return view;
    }
}
