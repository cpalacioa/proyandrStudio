package com.almashopping.android;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Marcas extends Fragment implements  AbsListView.OnScrollListener
{

    private Spinner cmbOpciones;
    private ListView lvMarcas=null;
    List<Marca> marcas;
    AdaptadorMarcas adaptadorMarcas;

    final String[]  datos =
            new String[]{"A-B-C","D-E-F","G-H-I","J-K-L","M-N-O","P-Q-R","S-T-U","V-W-X","Y-Z"};



    public Marcas() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        cmbOpciones = (Spinner) getView().findViewById(R.id.CmbOpciones);
        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(Marcas.this.getActivity().getApplicationContext(),
                        R.layout.spinner_marcas, datos);
        cmbOpciones.setAdapter(adaptador);
        lvMarcas = (ListView) getView().findViewById(R.id.listaMarcasGrid);
        TareaWSListar tarea = new TareaWSListar();
        tarea.execute();
        cmbOpciones.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        Log.d("Seleccionado",datos[position]);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.d("Seleccionado","ninguno");
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_marcas, container, false);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                adaptadorMarcas.notifyDataSetChanged();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                break;
        }
    }

    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {


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
                lvMarcas.setAdapter(new AdaptadorMarcas(Marcas.this,marcas));
            }
        }
    }



}
