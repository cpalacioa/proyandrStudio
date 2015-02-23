package android.com.almashopping;


import android.com.almashopping.adapter.AdaptadorProductos;
import android.com.almashopping.adapter.ExpandableListAdapter;
import android.com.almashopping.model.Categoria;
import android.com.almashopping.model.LlaveValor;
import android.com.almashopping.model.Producto;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoriasFragment extends Fragment implements  AbsListView.OnScrollListener{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<String> listDataChilds;
    HashMap<String, List<LlaveValor>> listDataChild;
    List<Categoria>Categorias;

   //Variables para listado de productos
    List<Producto> productos;
    GridView gvProductos;
    Boolean lvBusy;
    AdaptadorProductos adaptadorProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categorias, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
            expListView = (ExpandableListView) getView().findViewById(R.id.lvCategorias);
            TareaWSListar listar = new TareaWSListar();
            listar.execute();
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    Categoria categoriasel;

                    LlaveValor llvlr = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                    if (llvlr != null) {
                        Toast.makeText(CategoriasFragment.this.getActivity()
                                        .getApplicationContext(),
                                Integer.toString(llvlr.Id), Toast.LENGTH_SHORT)
                                .show();
                        LinearLayout lineal1=(LinearLayout)getView().findViewById(R.id.lnCategias);
                        lineal1.setVisibility(View.GONE);
                        LinearLayout lineal2=(LinearLayout)getView().findViewById(R.id.lnProductosCategoria);
                        lineal2.setVisibility(View.VISIBLE);
                        Log.d("aparezco","aparecer nuevo layout");
                        TextView txtCategoriaNombre=(TextView) getView().findViewById(R.id.txtCategoriaNombre);
                        txtCategoriaNombre.setText(llvlr.Nombre);
                        gvProductos=(GridView)getView().findViewById(R.id.Grid_productos_categoria);
                        TareaWSListarProductos tarea=new TareaWSListarProductos();
                        tarea.execute(Integer.toString(llvlr.Id));

                    }

                    return false;
                }
            });

            // Listview Group expanded listener
/*        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(CategoriasFragment.this.getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(CategoriasFragment.this.getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });*/

    }
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                lvBusy = false;
                adaptadorProductos.notifyDataSetChanged();
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


    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getcategories?key=65e6d67167f309a47a836a07a1e6198d");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray Jarray = new JSONArray(respStr);
                Categorias=new ArrayList();
                for(int i=0; i<Jarray.length(); i++)
                {
                    JSONObject obj = Jarray.getJSONObject(i);

                    int id = obj.getInt("cid");
                    Integer ipadre;
                    String nombre=obj.getString("name");
                    String thumb=obj.getString("photo");
                    String description=obj.getString("description");
                    String padre=obj.getString("cidParent");
                    if(padre=="null")
                        ipadre=null;
                    else
                    ipadre=Integer.parseInt(padre);

                    String photo="http://www.almashopping.com/images/category/1/"+thumb+"";
                    Categoria cate=new Categoria(id,nombre,description,photo,ipadre);
                    Categorias.add(cate);
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            Log.d("lista",Boolean.toString(result));

            if (result)
            {
                CategoriasDAO(Categorias);
                Log.d("lista2",Categorias.toString());
            }
        }
    }

    private void CategoriasDAO(List<Categoria>listacategorias){

        Log.d("lista",listacategorias.toString());
        listDataHeader = new ArrayList<String>();
        listDataChilds = new ArrayList<String>();
        listDataChild = new HashMap<String, List<LlaveValor>>();

        List<Categoria>listaPadres=new ArrayList();



        Categoria categoria=new Categoria();
        listaPadres=categoria.FiltrarPorPadres(listacategorias,null);
        for(Categoria categoriatmp:listaPadres)
        {
            listDataHeader.add(categoriatmp.Nombre);
            categoriatmp.hijas=categoria.obtenerHijas(listacategorias,categoriatmp.Id);
            listDataChild.put(categoriatmp.Nombre,categoriatmp.hijas);

        }

        listAdapter = new ExpandableListAdapter(CategoriasFragment.this.getActivity().getApplicationContext(), listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        for(int i=0; i < listAdapter.getGroupCount(); i++)
            expListView.expandGroup(i);
    }

    //Llamados Ajax
    private class TareaWSListarProductos extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getproductsbycategory/?key=43d5117d50ba57da751ff98af9bbac20&id="+params[0]+"");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray Jarray = new JSONArray(respStr);
                productos=new ArrayList();
                for(int i=0; i<Jarray.length(); i++)
                {
                    JSONObject obj = Jarray.getJSONObject(i);

                    int id = obj.getInt("pid");
                    String nombre=obj.getString("name");
                    String thumb=obj.getString("images");
                    String precio=obj.getString("price");
                    String marca=obj.getString("brand");
                    String[] fotos=thumb.split(":");
                    String imagentratada=fotos[0].replace("{","");
                    imagentratada=imagentratada.replaceAll("\"", "");
                    String photo="http://www.almashopping.com/images/products/1/"+imagentratada+"";
                    Log.d("recorte", imagentratada);

                    Producto cate=new Producto(id,nombre,photo,precio,marca,"");
                    productos.add(cate);
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                gvProductos.setAdapter(new AdaptadorProductos(CategoriasFragment.this.getActivity(),productos));

            }
        }
    }



}
