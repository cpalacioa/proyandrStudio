package android.com.almashopping.adapter;

import android.app.Activity;
import android.com.almashopping.R;
import android.com.almashopping.model.Producto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorProductos extends ArrayAdapter<Producto> {

    private Context contexto;
    List<Producto> productos;
    public int ciclo;

    public AdaptadorProductos(Activity _contexto, List<Producto> _productos) {

        super(_contexto.getApplicationContext(), R.layout.productos_grid_item, _productos);
        contexto=_contexto;
        this.productos = _productos;
        ciclo=0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Producto producto = getItem(position);
        ProductViewHolder holder;
        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.productos_grid_item, parent, false);
            //
            holder = new ProductViewHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.image);
            holder.title = (TextView)convertView.findViewById(R.id.titulo);
            holder.price=(TextView)convertView.findViewById(R.id.valor);
            holder.autor=(TextView)convertView.findViewById(R.id.autor);
            //
            convertView.setTag(holder);
        }
        else{
            holder = (ProductViewHolder) convertView.getTag();
        }

        holder.populate(producto, false);
        return convertView;
    }



    public class ProductViewHolder {
        public ImageView img;
        public TextView title;
        public TextView price;
        public TextView autor;

        void populate(Producto p) {
            title.setText(p.titulo.toUpperCase());
            price.setText("$"+ p.valor.toString().toUpperCase()+"");
            autor.setText("BY " + p.marca.toUpperCase());

            Picasso.with(contexto)
                    .load(p.img_url)
                    .placeholder(R.drawable.ic_action_alarm)
                    .into(img);
        }

        void populate(Producto p, boolean isBusy) {
            title.setText(p.titulo.toUpperCase());
            price.setText("$"+ p.valor.toString().toUpperCase()+"");
            autor.setText("BY " + p.marca.toUpperCase());

            if (!isBusy){
                Picasso.with(contexto)
                        .load(p.img_url)
                        .placeholder(R.drawable.ic_action_alarm)
                        .into(img);
            }
            else{
                img.setImageResource(R.drawable.ic_action_alarm);
            }
        }
    }
}
