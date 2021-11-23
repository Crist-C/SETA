package com.silencedaemon.seta.Rutas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.silencedaemon.seta.Consultas.ServiceInformation;
import com.silencedaemon.seta.R;

public class ListServiceAdapter extends BaseAdapter {

    private Intent IntentToServiceInformation;
    private LayoutInflater inflater = null;
    Context Contexto;
    String [] IDs;
    String [] Clientes;
    String [] TiposEscaleras;
    String [] Address;
    String [] Date;
    String [] EstadoServicio;
    String [] TipoRuta;


    public ListServiceAdapter (Context context, String[] ids, String[] clientes,
                               String[] tiposEscaleras, String[] address, String[] date, String[] estado_servicio,String[] tipo_ruta){
        this.Contexto = context;
        this.IDs = ids;
        this.Clientes = clientes;
        this.TiposEscaleras = tiposEscaleras;
        this.Address = address;
        this.Date = date;
        this.EstadoServicio = estado_servicio;
        this.TipoRuta = tipo_ruta;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.one_service, null);

        final TextView OTMService = (TextView) vista.findViewById(R.id.ViewOTMService);
        TextView Cliente = (TextView) vista.findViewById(R.id.TVCliente);
        TextView TipoEscalera = (TextView) vista.findViewById(R.id.TVTipoEscalera);
        TextView TvAddress = (TextView) vista.findViewById(R.id.TVAddress);
        TextView TvHora = (TextView) vista.findViewById(R.id.TVHora);
        TextView ViewEstadoServicio = (TextView) vista.findViewById(R.id.ViewEstadoServicio);
        TextView ViewTipoRuta = (TextView) vista.findViewById(R.id.ViewTipoRuta);
        TextView ViewEnumService = (TextView) vista.findViewById(R.id.ViewEnumService);
        LinearLayout IDServiceLL = (LinearLayout) vista.findViewById(R.id.IDServiceLL);


        OTMService.setText(IDs[position]);
        Cliente.setText(Clientes[position]);
        TipoEscalera.setText(TiposEscaleras[position]);
        TvAddress.setText(Address[position]);
        TvHora.setText(Date[position]);
        ViewEstadoServicio.setText(EstadoServicio[position]);
        ViewTipoRuta.setText(TipoRuta[position]);
        ViewEnumService.setText((position + 1) + "");
        //IDService.setTag(position);


        IDServiceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToServiceInformation = new Intent(Contexto, ServiceInformation.class);
                IntentToServiceInformation.putExtra("origen", "viewInformation");
                IntentToServiceInformation.putExtra("OTMService", OTMService.getText().toString());
                Contexto.startActivity(IntentToServiceInformation);
            }
        });





        return vista;
    }



    @Override
    public int getCount() {
        return Clientes.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
