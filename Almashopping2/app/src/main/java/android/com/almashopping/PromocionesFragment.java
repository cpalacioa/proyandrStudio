package android.com.almashopping;


import android.com.almashopping.adapter.AdaptadorProductos;
import android.com.almashopping.model.Producto;
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


public class PromocionesFragment extends Fragment implements  AbsListView.OnScrollListener{

    List<Producto> productos;
    GridView gvProductos;
    Boolean lvBusy;
    AdaptadorProductos adaptadorProductos;
    int Id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promociones, container, false);
    }
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        gvProductos=(GridView)getView().findViewById(R.id.Grid_productosMarca);
        Id=1;
        TareaWSListar listar=new TareaWSListar();
        listar.execute();

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

    //Llamados Ajax
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getproductsbybrand/?key=4e3ec11f9cd0ed11090dbd0b1c8ff9ee&id="+Id+"");
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
                gvProductos.setAdapter(new AdaptadorProductos(PromocionesFragment.this.getActivity(),productos));

            }
        }
    }




}
