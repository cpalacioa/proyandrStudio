package android.com.almashopping.adapter;

import android.app.Activity;
import android.com.almashopping.R;
import android.com.almashopping.model.basket;
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

public class BasketAdapter extends ArrayAdapter<basket> {

    private Context contexto;
    List<basket> productos;
    public int ciclo;

    public BasketAdapter(Activity _contexto, List<basket> _productos) {

        super(_contexto.getApplicationContext(), R.layout.basket_item, _productos);
        contexto=_contexto;
        this.productos = _productos;
        ciclo=0;
    }


    public class ProductViewHolder {
        public ImageView img;
        public TextView title;
        public TextView mount;
        public TextView price;
        public TextView brand;

        void populate(basket p) {
            title.setText(p.producto.titulo.toUpperCase());
            mount.setText(Integer.toString(p.cantidad));
            price.setText(p.producto.valor);
            brand.setText(p.producto.marca);
            Picasso.with(contexto)
                    .load(p.producto.img_url)
                    .placeholder(R.drawable.spinner)
                    .error(R.drawable.nodisponible)
                    .into(img);
        }

        void populate(basket p, boolean isBusy) {
            title.setText(p.producto.titulo.toUpperCase());
            mount.setText(Integer.toString(p.cantidad));
            price.setText(p.producto.valor);
            brand.setText(p.producto.marca);

            if (!isBusy){
                Picasso.with(contexto)
                        .load(p.producto.img_url)
                        .placeholder(R.drawable.spinner)
                        .error(R.drawable.nodisponible)
                        .into(img);
            }
            else{
                img.setImageResource(R.drawable.ic_action_alarm);
            }
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        basket producto = getItem(position);
        ProductViewHolder holder;
        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.basket_item, parent, false);
            holder = new ProductViewHolder();

            holder.img = (ImageView)convertView.findViewById(R.id.iconbasket);
            holder.title = (TextView)convertView.findViewById(R.id.baskproducttitle);
            holder.mount=(TextView)convertView.findViewById(R.id.counterbasket);
            holder.brand=(TextView)convertView.findViewById(R.id.baskproducbrand);
            holder.price=(TextView)convertView.findViewById(R.id.baskproducprice);

            convertView.setTag(holder);
        }
        else{
            holder = (ProductViewHolder) convertView.getTag();
        }

        holder.populate(producto, false);
        return convertView;
    }


}
