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
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CatalogoInicio extends Fragment implements  AbsListView.OnScrollListener {

    List Categorias;
    ListView gvCategorias = null;
    AdaptadorCategorias adaptadorCategorias;


    private boolean lvBusy = false;

    public CatalogoInicio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogo_inicio, container, false);

    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        gvCategorias = (ListView)getView().findViewById(R.id.listaCategoriasGrid);

        TareaWSListar tarea=new TareaWSListar();
        tarea.execute();
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                lvBusy = false;
                adaptadorCategorias.notifyDataSetChanged();
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

    //Tarea Asíncrona para llamar al WS de listado en segundo plano
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
                    String nombre=obj.getString("name");
                    String thumb=obj.getString("photo");
                    String photo="http://www.almashopping.com/images/category/1/"+thumb+"";
                    Categoria cate=new Categoria(id,nombre,"",photo);
                    //Log.d("foto",photo);
                    if(obj.getString("cidParent")=="null" && thumb.length()>0)
                    Categorias.add(cate);
                }


                //Log.d("Respuesta", Jarray.toString());
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //llenar adapter
                //lvArticulos = (ListView) findViewById( R.id.list_articulos);
                gvCategorias.setAdapter(new AdaptadorCategorias(CatalogoInicio.this,Categorias));

            }
        }
    }


}



