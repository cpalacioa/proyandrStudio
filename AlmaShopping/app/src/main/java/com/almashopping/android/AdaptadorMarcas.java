package com.almashopping.android;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

public class AdaptadorMarcas  extends ArrayAdapter<Marca> {

    private Context contexto;
    List marcas;
    public int ciclo;
    AdaptadorMarcas adaptadorMarcas;
    ListView mListView;

    public AdaptadorMarcas(Fragment _contexto, List<Marca> _marcas,ListView listView) {

        super(_contexto.getActivity(), R.layout.marcas_grid_item, _marcas);
        contexto=_contexto.getActivity();
        this.marcas = _marcas;
        this.mListView=listView;
        ciclo=0;
    }

    public class MarcaViewHolder {
        public TextView inicial;
        public TextView Nombre;
        public  ImageButton imgbtnProductos;
        public  ImageButton imgbtnDetallemarca;


        void populate(Marca _marca) {
            Nombre.setText(_marca.Nombre.toUpperCase());
            inicial.setText(_marca.Nombre.substring(0,1).toString());
        }

    }
    public View getView(int position, View convertView, ViewGroup parent) {

        Marca marca = getItem(position);
        MarcaViewHolder holder;
        mListView.setItemsCanFocus(true);
        mListView.setClickable(true);
        mListView.setFocusable(true);

        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.marcas_grid_item, parent, false);
            holder = new MarcaViewHolder();
            holder.Nombre = (TextView)convertView.findViewById(R.id.txtNombreMarca);
            holder.inicial= (TextView)convertView.findViewById(R.id.txtInicialMarca);
            holder.imgbtnProductos=(ImageButton)convertView.findViewById(R.id.btnProductosMarcas);
            holder.imgbtnProductos.setOnClickListener(verProductos);
            holder.imgbtnDetallemarca=(ImageButton)convertView.findViewById(R.id.btnInfoMarca);
            holder.imgbtnDetallemarca.setOnClickListener(verMarca);

            convertView.setTag(holder);
        }


        else{
            holder = (MarcaViewHolder) convertView.getTag();
        }
        holder.populate(marca);
        return convertView;
    }


    View.OnClickListener verProductos=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = mListView.getPositionForView((View) v.getParent());
            Marca marca =(Marca)marcas.get(position);
            Intent intent=new Intent(v.getContext(),flipcard.class);
            intent.putExtra("id",marca.Id);
            intent.putExtra("cover",marca.Cover);
            intent.putExtra("nombre",marca.Nombre);
            intent.putExtra("descripcion", marca.Descripcion);
            v.getContext().startActivity(intent);

        }
    };

    View.OnClickListener verMarca=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = mListView.getPositionForView((View) v.getParent());
            Marca marca =(Marca)marcas.get(position);
            Intent intent=new Intent(v.getContext(),detalleMarca.class);
            intent.putExtra("id",marca.Id);
            intent.putExtra("cover",marca.Cover);
            intent.putExtra("nombre",marca.Nombre);
            intent.putExtra("descripcion", marca.Descripcion);
            v.getContext().startActivity(intent);
        }
    };
}
