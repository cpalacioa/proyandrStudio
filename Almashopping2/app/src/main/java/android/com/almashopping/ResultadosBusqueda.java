package android.com.almashopping;


import android.com.almashopping.adapter.AdaptadorProductos;
import android.com.almashopping.model.Producto;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResultadosBusqueda extends Fragment {


     String txtaBuscar;
     TextView txtBuscado;
     TextView txtResultado;
     List<Producto> productos;
     GridView gvProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resultados_busqueda, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        SearchView mSearchView = (SearchView) getActivity().findViewById(R.id.action_search);
        txtBuscado=(TextView)getView().findViewById(R.id.txtBuscado);
        txtResultado=(TextView)getView().findViewById(R.id.txtResultado);

        gvProductos=(GridView)getView().findViewById(R.id.Grid_resultados_busqueda);
        txtBuscado.setText("Resultados para busqueda de : "+mSearchView.getQuery());
            if(txtBuscado.getText().length()>3)
            {
                TareaWSListarProductosPorKeywords buscarProductos=new TareaWSListarProductosPorKeywords();
                buscarProductos.execute(mSearchView.getQuery().toString());

            }
        }

        //Petición al servicio rest
        //Llamados al servicio rest
        private class TareaWSListarProductosPorKeywords extends AsyncTask<String,Integer,Boolean> {


            protected Boolean doInBackground(String... params) {

                boolean resul = true;
                String query=params[0];
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    query = URLEncoder.encode(query, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    query=query;
                }

                HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getproductsbykeywords/?key=ccad972df2e2225b2be247837ec1d91a&keywords="+query+"");
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
                    if(productos.size()>0) {
                        txtResultado.setText("Se encontraron "+Integer.toString(productos.size())+" productos");
                        gvProductos.setAdapter(new AdaptadorProductos(ResultadosBusqueda.this.getActivity(), productos));
                        gvProductos.setOnItemClickListener(listenerGridProductos);
                    }
                    else
                    {
                        txtResultado.setText("Tu búsqueda no arrojó ningún resultado.");
                    }

                }


            }
        }

    AdapterView.OnItemClickListener listenerGridProductos=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Producto producto=(Producto)productos.get(position);
            Intent i=new Intent(ResultadosBusqueda.this.getActivity().getApplicationContext(),DetalleProducto.class);
            i.putExtra("Id",producto.id);
            startActivity(i);
        }
    };

}

