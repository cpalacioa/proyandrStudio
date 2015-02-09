package com.almashopping.android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorCategorias  extends ArrayAdapter<Categoria> {

    private Context contexto;
    List<Categoria> categorias;
    public int ciclo;

    public AdaptadorCategorias(Fragment _contexto, List<Categoria> _categorias) {

        super(_contexto.getActivity(), R.layout.categorias_grid_item, _categorias);
        contexto=_contexto.getActivity();
        this.categorias = _categorias;
        ciclo=0;
    }


    public class CategoriaViewHolder {
        public ImageView foto;
        public TextView Nombre;

        void populate(Categoria _categoria) {
            Nombre.setText(_categoria.Nombre.toUpperCase());
            Picasso.with(contexto)
                    .load(_categoria.Foto)
                    .placeholder(R.drawable.ic_action_business)
                    .into(foto);
        }

        void populate(Categoria c, boolean isBusy) {
            Nombre.setText(c.Nombre.toUpperCase());

            if (!isBusy){
                Picasso.with(contexto)
                        .load(c.Foto)
                        .placeholder(R.drawable.ic_action_business)
                        .into(foto);

            }
            else{
                foto.setImageResource(R.drawable.ic_action_business);
            }
        }
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        Categoria categoria = getItem(position);
        CategoriaViewHolder holder;
        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.categorias_grid_item, parent, false);

            //
            holder = new CategoriaViewHolder();
            holder.foto = (ImageView)convertView.findViewById(R.id.imagenCategoria);
            holder.Nombre = (TextView)convertView.findViewById(R.id.NombreCategoria);
            convertView.setTag(holder);
        }
        else{
            holder = (CategoriaViewHolder) convertView.getTag();
        }
        holder.populate(categoria, false);

        return convertView;
    }


}
