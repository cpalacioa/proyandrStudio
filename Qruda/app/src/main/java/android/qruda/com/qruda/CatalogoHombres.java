package android.qruda.com.qruda;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class CatalogoHombres extends Fragment implements  AbsListView.OnScrollListener {

    List productos;
    GridView gvProductos = null;
    ProductListAdapterWithCache adapterProducts;


    private boolean lvBusy = false;


    public CatalogoHombres() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_catalogo_hombres, container, false);

    }
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        productos = new ArrayList();
        productos.add(new Producto(1,"Bolso Cuadradp","http://www.qruda.com/images/thumbs/230x337/1/Bolso%20Cuadrado%20marron%20FRONTAL.jpg","208.800","Kico Bags"));
        productos.add(new Producto(2,"Maletín Juan","http://www.qruda.com/images/thumbs/230x337/1/Maletin%20Juan%20Marro_n%20frontal.jpg","556.800","Kico Bags"));
        productos.add(new Producto(3,"Botas Doble","http://www.qruda.com/images/thumbs/230x337/1/murdocl_botasdoble_azul%20(2).jpg","180.000","Murdock"));
        productos.add(new Producto(4,"Bajos Bota","http://www.qruda.com/images/thumbs/230x337/1/murdock_bajosbota_camufladocafe%20(2)18.jpg","180.000","Murdock"));
        productos.add(new Producto(5,"Chamarra Biker","http://www.qruda.com/images/thumbs/230x337/1/chamarra_nigga.jpg","620.000","Ducky Black"));
        productos.add(new Producto(6,"Buso","http://www.qruda.com/images/thumbs/230x337/1/_MG_5692%20copy.jpg","200.000","Feroz"));
        productos.add(new Producto(7,"Billetera Camme Azul","http://www.qruda.com/images/thumbs/230x337/1/amme-azul1.jpg","40.000","Feroz"));
        productos.add(new Producto(8,"Airport","http://www.qruda.com/images/thumbs/230x337/1/airport.jpg","20.000","Little Lucia"));


        gvProductos = (GridView)getView().findViewById(R.id.grid_Hombres);
        adapterProducts=new ProductListAdapterWithCache(this,productos);
        gvProductos.setAdapter(adapterProducts);
        gvProductos.setOnItemClickListener(listenerGridProductos);
    }

    OnItemClickListener listenerGridProductos=new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Producto producto=(Producto)productos.get(position);
            Intent i=new Intent(CatalogoHombres.this.getActivity().getApplicationContext(),DetalleProducto.class);
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
