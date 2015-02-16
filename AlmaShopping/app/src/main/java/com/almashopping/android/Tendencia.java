package com.almashopping.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Tendencia extends Fragment implements  AbsListView.OnScrollListener{

    List<Producto> productos;
    GridView gvProductos;
    Boolean lvBusy;
    AdaptadorProductos adaptadorProductos;
    Integer[]idTendencias;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tendencia, container, false);

    }
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        gvProductos = (GridView)getView().findViewById(R.id.listaTendenciaGrid);
        ListaTendencia tendencias=new ListaTendencia();
        tendencias.execute();


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



    private class ListaTendencia extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            productos=new ArrayList();
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =new HttpGet("http://www.mauropalacio.co/Api/catalogo/obtTendenciaApp?app=2");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray Jarray = new JSONArray(respStr);
                idTendencias=new Integer[Jarray.length()];
                for(int i=0; i<Jarray.length(); i++)
                {
                    JSONObject obj = Jarray.getJSONObject(i);
                    int idp = obj.getInt("IdProducto");
                    idTendencias[i]=idp;
                   // ObtenerProducto obtProducto=new ObtenerProducto();
                    //Producto prod= obtProducto.execute(Integer.toString(idp));
                    Producto prod=ObtenerInfoProducto(idp);
                    productos.add(prod);
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
/*                Log.d("idtendencias",idTendencias.toString());

                for(int i=0; i<idTendencias.length;i++)
                {
                     ObtenerProducto obtProducto=new ObtenerProducto();
                    int idPro=idTendencias[i];
                    obtProducto.execute(Integer.toString(idPro));
                }*/
                Log.d("idtendencias", productos.toString());

                gvProductos.setAdapter(new AdaptadorProductos(Tendencia.this.getActivity(),productos));

            }
        }
    }

    private Producto ObtenerInfoProducto(int id)
    {
        HttpClient httpClient = new DefaultHttpClient();
        Producto cate=null;
        HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getproductbyid/?key=851d92530079828cea8133eef93554fe&pid="+id+"");
        del.setHeader("content-type", "application/json");
        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray Jarray = new JSONArray(respStr);
            for(int i=0; i<Jarray.length(); i++)
            {
                JSONObject obj = Jarray.getJSONObject(i);

                String nombre=obj.getString("name");
                String thumb=obj.getString("images");
                String precio=obj.getString("price");
                String marca=obj.getString("brand");
                String[] fotos=thumb.split(":");
                String imagentratada=fotos[0].replace("{","");
                imagentratada=imagentratada.replaceAll("\"", "");
                String photo="http://www.almashopping.com/images/products/1/"+imagentratada+"";
                cate=new Producto(id,nombre,photo,precio,marca,"");

            }
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
        }
        return  cate;

    }
    private class ObtenerProducto extends AsyncTask<String,Integer,Boolean> {

        List<Producto>productostmp;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();

            Producto cate=null;
            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getproductbyid/?key=851d92530079828cea8133eef93554fe&pid="+params[0]+"");
            del.setHeader("content-type", "application/json");
            productostmp=new ArrayList();
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray Jarray = new JSONArray(respStr);
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
                    Log.d("recorte",imagentratada);

                    cate=new Producto(id,nombre,photo,precio,marca,"");

                    productostmp.add(cate);
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


        }
    }

}
