package com.almashopping.android;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class productosporCategoria extends ActionBarActivity {
    List<Producto> productos;
    GridView gvProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productospor_categoria);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_productospor_categoria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Llamados Ajax
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getcategories?key=4e3ec11f9cd0ed11090dbd0b1c8ff9ee&id=1");
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

                    int id = obj.getInt("cid");
                    String nombre=obj.getString("name");
                    String thumb=obj.getString("photo");
                    String photo="http://www.almashopping.com/images/category/1/"+thumb+"";

                    Producto cate=new Producto(id,nombre,photo,"","","");
                    //Log.d("foto",photo);
                    if(obj.getString("cidParent")=="null" && thumb.length()>0)
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
                //llenar adapter
                //lvArticulos = (ListView) findViewById( R.id.list_articulos);
                gvProductos.setAdapter(new AdaptadorProductos(productosporCategoria.this,productos));

            }
        }
    }

}
