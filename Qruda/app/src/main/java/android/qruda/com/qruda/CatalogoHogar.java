package android.qruda.com.qruda;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CatalogoHogar extends Fragment {

    List productos;
    GridView gvProductos = null;
    ProductListAdapterWithCache adapterProducts;
    private boolean lvBusy = false;
    public ProgressDialog progressdialog;

    public CatalogoHogar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogo_hogar, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        productos = new ArrayList();
        productos.add(new Producto(1,"Cuaderno Mostacho","http://www.qruda.com/images/thumbs/230x337/2159/cuaderno-be-a-man-argollado-1.jpg","36.000","Infinite Design"));
        productos.add(new Producto(2,"Mug Moustache","http://www.qruda.com/images/thumbs/230x337/2159/posillo-mostacho-canada-1.jpg","21.000","Narciso & Pricila"));
        productos.add(new Producto(3,"Mug Moustache Chino","http://www.qruda.com/images/thumbs/230x337/2159/posillo-mostacho-portugal-1.jpg","21.000","Narciso & Pricila"));
        productos.add(new Producto(4,"Set Pajaros sal","http://www.qruda.com/images/thumbs/230x337/2159/sal-pimienta-aves-1.jpg","30.000","Narciso & Priscila"));
        productos.add(new Producto(5,"Cuadro Moldura","http://www.qruda.com/images/thumbs/230x337/2159/cuadro-moldura-se-diferente.jpg","92.800","Divina Diseños"));
        productos.add(new Producto(6,"Cojin Madera","http://www.qruda.com/images/thumbs/230x337/2159/cojin-madera-vintage-colores-pastel.jpg","69.000","Divina Diseños"));
        productos.add(new Producto(7,"Pizarra Pequeña","http://www.qruda.com/images/thumbs/230x337/2159/pizarra-pequena-ddpf02-recuperado.jpg","71.000","Divina Diseños"));
        productos.add(new Producto(8,"Monos Paris","http://www.qruda.com/images/thumbs/230x337/2093/06.jpg","120.000","Santiago"));


        gvProductos = (GridView)getView().findViewById(R.id.Grid_Hogar);
        adapterProducts=new ProductListAdapterWithCache(this,productos);
        gvProductos.setAdapter(adapterProducts);
        gvProductos.setOnItemClickListener(myOnItemClickListener);

    }
    OnItemClickListener myOnItemClickListener
            = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
          //  String prompt = (String)parent.getItemAtPosition(position);
            Producto producto=(Producto)productos.get(position);

            Log.d(Integer.toString(position),"");
            Toast.makeText(view.getContext(),
                    "Item Clicked: " + producto.titulo+"", Toast.LENGTH_SHORT).show();

            Intent i=new Intent(CatalogoHogar.this.getActivity().getApplicationContext(),DetalleProducto.class);
            i.putExtra("Id",producto.id);
            i.putExtra("UrlImagen",producto.img_url);
            i.putExtra("Titulo",producto.titulo);
            startActivity(i);

        }};

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
