package com.almashopping.android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorMarcas  extends ArrayAdapter<Marca> {

    private Context contexto;
    List marcas;
    public int ciclo;
    AdaptadorMarcas adaptadorMarcas;

    public AdaptadorMarcas(Fragment _contexto, List<Marca> _marcas) {

        super(_contexto.getActivity(), R.layout.marcas_grid_item, _marcas);
        contexto=_contexto.getActivity();
        this.marcas = _marcas;
        ciclo=0;
    }

    public class MarcaViewHolder {
        public TextView inicial;
        public TextView Nombre;


        void populate(Marca _marca) {
            Nombre.setText(_marca.Nombre.toUpperCase());
            inicial.setText(_marca.Nombre.substring(0,1).toString());
        }

    }
    public View getView(int position, View convertView, ViewGroup parent) {

        Marca marca = getItem(position);
        MarcaViewHolder holder;

        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.marcas_grid_item, parent, false);
            holder = new MarcaViewHolder();
            holder.Nombre = (TextView)convertView.findViewById(R.id.txtNombreMarca);
            holder.inicial= (TextView)convertView.findViewById(R.id.txtInicialMarca);
            convertView.setTag(holder);
        }
        else{
            holder = (MarcaViewHolder) convertView.getTag();
        }
        holder.populate(marca);
        return convertView;
    }

}
