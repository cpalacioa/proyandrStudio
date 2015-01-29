package android.qruda.com.qruda;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogoMujeres extends Fragment {

    List productos;
    GridView gvProductos = null;
    ProductListAdapterWithCache adapterProducts;

    private boolean lvBusy = false;

    public CatalogoMujeres() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogo_mujeres, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        productos = new ArrayList();
        productos.add(new Producto(1,"Camiseta Barbie Sucks","http://www.qruda.com/images/thumbs/230x337/2093/barbie-sucks.jpg","50.000","Must Have It"));
        productos.add(new Producto(2,"Vestido Cola de pato","http://www.qruda.com/images/thumbs/230x337/1/3080.jpg","86.990","Francy Leon"));
        productos.add(new Producto(3,"Vestido Bicolor","http://www.qruda.com/images/thumbs/230x337/1/1638.jpg","82.990","francy leon"));
        productos.add(new Producto(4,"Big Bang","http://www.qruda.com/images/thumbs/230x337/1/1955.jpg","20.000","Dulce Veneno"));
        productos.add(new Producto(5,"Gafas Skull","http://www.qruda.com/images/thumbs/230x337/1/skull.jpg","70.000","Little Lucia"));
        productos.add(new Producto(6,"Camiseta PTime","http://www.qruda.com/images/thumbs/230x337/2093/pussy-time-2-.jpg","50.000","Must Have It"));
        productos.add(new Producto(7,"Vestido Marinero","http://www.qruda.com/images/thumbs/230x337/2159/01front-07.jpg","130.000","Sweet Lolita"));
        productos.add(new Producto(8,"strong monk","http://www.qruda.com/images/thumbs/230x337/1/mar-7249.jpg","260.000","Gal vrs Buck"));


        gvProductos = (GridView)getView().findViewById(R.id.Grid_Mujeres);
        adapterProducts=new ProductListAdapterWithCache(this,productos);
        gvProductos.setAdapter(adapterProducts);
        gvProductos.setOnItemClickListener(listenerGridProductos);

    }

    AdapterView.OnItemClickListener listenerGridProductos =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Producto producto=(Producto)productos.get(position);
            Intent i=new Intent(CatalogoMujeres.this.getActivity().getApplicationContext(),DetalleProducto.class);
            i.putExtra("Id",producto.id);
            i.putExtra("UrlImagen",producto.img_url);
            i.putExtra("Titulo",producto.titulo);
            startActivity(i);

        }
    };

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                lvBusy = false;
                adapterProducts.notifyDataSetChanged();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                lvBusy = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                lvBusy = true;
                break;
        }
    }


    public boolean isLvBusy(){
        return lvBusy;
    }

}
