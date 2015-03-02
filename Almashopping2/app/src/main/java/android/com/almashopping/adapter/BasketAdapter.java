package android.com.almashopping.adapter;

import android.app.Activity;
import android.com.almashopping.R;
import android.com.almashopping.helpers.ShoppingSQLHelper;
import android.com.almashopping.model.basket;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        public ImageButton btnEliminar;
        public Integer idsql;

        void populate(basket p) {
            title.setText(p.producto.titulo.toUpperCase());
            mount.setText(Integer.toString(p.cantidad));
            price.setText(p.producto.valor);
            brand.setText(p.producto.marca);
            idsql=p.Id;
            final int id=p.producto.id;

            Picasso.with(contexto)
                    .load(p.producto.img_url)
                    .placeholder(R.drawable.spinner)
                    .error(R.drawable.nodisponible)
                    .into(img);
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "eliminar2");

                    Toast.makeText(v.getContext(),Integer.toString(id),Toast.LENGTH_SHORT);
                }
            });

        }

        void populate(basket p, boolean isBusy) {
            title.setText(p.producto.titulo.toUpperCase());
            mount.setText(Integer.toString(p.cantidad));
            price.setText("$ "+p.producto.valor+"");
            brand.setText(p.producto.marca);
            final int id=p.producto.id;
            idsql=p.Id;

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

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "eliminar3");
                    Toast.makeText(v.getContext(),Integer.toString(id),Toast.LENGTH_SHORT);
                }
            });
        }

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final basket producto = getItem(position);
        final ProductViewHolder holder;
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
            holder.btnEliminar=(ImageButton)convertView.findViewById(R.id.btnEliminarbasket);
            convertView.setTag(holder);
        }
        else{
            holder = (ProductViewHolder) convertView.getTag();
        }

        holder.populate(producto, false);
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click ", "" + position);

                String eliminar = Integer.toString(producto.producto.id);
                Toast.makeText(v.getContext(), "Se eliminara :" + producto.producto.id + "", Toast.LENGTH_LONG);
                ElminarItem(holder.idsql,v.getContext());
                remove(getItem(position));
            }
        });


        return convertView;
    }

   public void ElminarItem(int Id,Context context)
   {
       Log.d("click",Integer.toString(Id));
       ShoppingSQLHelper dbconnect=new ShoppingSQLHelper(context,"DBAlma",null,1);
       SQLiteDatabase db=dbconnect.getWritableDatabase();
       db.delete("cartshop","Id="+Id+"",null);

   }
}
