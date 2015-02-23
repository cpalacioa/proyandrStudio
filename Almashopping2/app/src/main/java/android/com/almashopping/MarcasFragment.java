package android.com.almashopping;


import android.com.almashopping.adapter.AdaptadorProductos;
import android.com.almashopping.adapter.ExpandableListAdapter;
import android.com.almashopping.model.Categoria;
import android.com.almashopping.model.LlaveValor;
import android.com.almashopping.model.Marca;
import android.com.almashopping.model.Producto;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class MarcasFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<String> listDataChilds;
    HashMap<String, List<LlaveValor>> listDataChild;
    List<Marca>marcas;

    List<Producto> productos;
    GridView gvProductos;
    Boolean lvBusy;
    AdaptadorProductos adaptadorProductos;

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        expListView = (ExpandableListView) getView().findViewById(R.id.lvMarcas);
        TareaWSListarMarcas tarealistar=new TareaWSListarMarcas();
        tarealistar.execute();
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Categoria categoriasel;

                LlaveValor llvlr = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                if (llvlr != null) {
                    Toast.makeText(MarcasFragment.this.getActivity()
                                    .getApplicationContext(),
                            Integer.toString(llvlr.Id), Toast.LENGTH_SHORT)
                            .show();
                    LinearLayout lineal1=(LinearLayout)getView().findViewById(R.id.lsMarcas);
                    lineal1.setVisibility(View.GONE);
                    LinearLayout lineal2=(LinearLayout)getView().findViewById(R.id.lnProductosMarca);
                    lineal2.setVisibility(View.VISIBLE);

                    TextView txtMarcaNombre=(TextView) getView().findViewById(R.id.txtMarcaNombre);
                    txtMarcaNombre.setText(llvlr.Nombre);
                    gvProductos=(GridView)getView().findViewById(R.id.Grid_productos_marca);
                    TareaWSListarProductosPorMarca tarea2=new TareaWSListarProductosPorMarca();
                    tarea2.execute(Integer.toString(llvlr.Id));
                }
                return false;
            }
        });

    }

    private List<LlaveValor> ObtenerMarcasPorInicial(List<Marca>listamarcas,String inicial)
    {
     List<LlaveValor>listallavevalor=new ArrayList();
        if(listamarcas!=null) {
            //Log.d("lista", marcas.toString());
            for(int control=0;control<marcas.size();control++)
            {
                Marca mar=marcas.get(control);
                if(mar.Nombre.toUpperCase().startsWith(inicial))
                {
                    listallavevalor.add(new LlaveValor(mar.Id,mar.Nombre));
                }
            }

        }
        return  listallavevalor;

    }
    private void MarcasDAO(List<Marca>listamarcas){

        Log.d("lista",listamarcas.toString());
        listDataHeader=new ArrayList();
        List<String>letras = new ArrayList<String>(Arrays.asList(new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","V","W","X","Y","Z"}));
        listDataChild = new HashMap<String, List<LlaveValor>>();

        for(int cletra=0;cletra<letras.size();cletra++)
        {
            List<LlaveValor>llavevlrTemp=ObtenerMarcasPorInicial(listamarcas,letras.get(cletra).toUpperCase());
            if(llavevlrTemp.size()>0) {
                listDataHeader.add(letras.get(cletra));
                listDataChild.put(letras.get(cletra), llavevlrTemp);
            }

        }

        listAdapter = new ExpandableListAdapter(MarcasFragment.this.getActivity().getApplicationContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_marcas, container, false);
    }

    private class TareaWSListarMarcas extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getbrands?key=f12b8dfdb5e07d2b6806a16b9882f1a3");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray Jarray = new JSONArray(respStr);
                marcas=new ArrayList();
                for(int i=0; i<Jarray.length(); i++)
                {
                    JSONObject obj = Jarray.getJSONObject(i);

                    int id = obj.getInt("bid");
                    String nombre=obj.getString("name");
                    String description=obj.getString("description");
                    String photo=obj.getString("cover");
                    Marca marc=new Marca(id,nombre,photo,description);
                    marcas.add(marc);
                }


                //Log.d("Respuesta", Jarray.toString());
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
                Collections.sort(marcas, new Comparator<Marca>() {
                    @Override
                    public int compare(Marca lhs, Marca rhs) {
                        return lhs.Nombre.compareToIgnoreCase(rhs.Nombre);
                    }
                });

             MarcasDAO(marcas);
            }
        }
    }

    //Llamados al servicio rest
    private class TareaWSListarProductosPorMarca extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getproductsbybrand/?key=4e3ec11f9cd0ed11090dbd0b1c8ff9ee&id="+params[0]+"");
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
                    Log.d("recorte",imagentratada);

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
                gvProductos.setAdapter(new AdaptadorProductos(MarcasFragment.this.getActivity(),productos));

            }
        }
    }

}
