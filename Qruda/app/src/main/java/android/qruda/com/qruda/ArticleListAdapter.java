package android.qruda.com.qruda;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleListAdapter extends ArrayAdapter<Articulo> {

    List<Articulo> articulos;

    public ArticleListAdapter(Context _context, List<Articulo> _articulos) {
        super(_context, R.layout.lista_articulos,_articulos);
        this.articulos = _articulos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
        convertView = vi.inflate(R.layout.lista_articulos, parent, false);


        Articulo articulo = getItem(position);


        TextView txtTitle = (TextView) convertView.findViewById(R.id.NombreArticulo);
        txtTitle.setText(articulo.Nombre);

        ImageView img = (ImageView)convertView.findViewById(R.id.imagenArticulo);
        TextView txtFecha=(TextView)convertView.findViewById(R.id.FechaArticulo);
         txtFecha.setText(articulo.Fecha);

        // download image
        Picasso.with(getContext())
                .load(articulo.Imagen)
                .placeholder(R.drawable.spinner)
                .into(img);


        return convertView;
    }
}
