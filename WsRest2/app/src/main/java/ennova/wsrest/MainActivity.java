package ennova.wsrest;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {

    // Definición  de variables

    private Button btnInsertar;
    private Button btnActualizar;
    private Button btnEliminar;
    private Button btnObtener;
    private Button btnListar;

    private EditText txtId;
    private EditText txtNombre;
    private EditText txtTelefono;

    private TextView lblResultado;
    private ListView lstClientes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Se obtienen referencia a los controles
        btnInsertar = (Button)findViewById(R.id.btnInsertar);
        btnActualizar = (Button)findViewById(R.id.btnActualizar);
        btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnObtener = (Button)findViewById(R.id.btnObtener);
        btnListar = (Button)findViewById(R.id.btnListar);

        txtId = (EditText)findViewById(R.id.txtId);
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtTelefono = (EditText)findViewById(R.id.txtTelefono);

        lblResultado = (TextView)findViewById(R.id.lblResultado);
        lstClientes = (ListView)findViewById(R.id.lstClientes);
            //Accion de los botones
        btnInsertar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSInsertar tarea = new TareaWSInsertar();
                tarea.execute(
                        txtNombre.getText().toString(),
                        txtTelefono.getText().toString());
            }
        });

        btnObtener.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSObtener tarea = new TareaWSObtener();
                tarea.execute(txtId.getText().toString());
            }
        });

        btnEliminar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSEliminar tarea = new TareaWSEliminar();
                tarea.execute(txtId.getText().toString());
            }
        });

        btnListar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSListar tarea = new TareaWSListar();
                tarea.execute();
            }
        });

        btnActualizar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSActualizar tarea = new TareaWSActualizar();
                tarea.execute(
                        txtId.getText().toString(),
                        txtNombre.getText().toString(),
                        txtTelefono.getText().toString());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Acciones en segundo plano
    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://172.25.4.111:52728/api/Clientes/Cliente/");
            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                //dato.put("Id", Integer.parseInt(txtId.getText().toString()));
                dato.put("Nombre", params[0]);
                dato.put("Telefono", Integer.parseInt(params[1]));

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    resul = false;
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
                lblResultado.setText("Insertado OK.");
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        private int idCli;
        private String nombCli;
        private int telefCli;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
            String id = params[0];
            HttpGet del = new HttpGet("http://172.25.4.111:52728/Api/Clientes/Cliente/" + id);
            Log.e("Url",id.toString());
            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);

                idCli = respJSON.getInt("IdCliente");
                nombCli = respJSON.getString("Nombre");
                telefCli = respJSON.getInt("Telefono");
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
                lblResultado.setText("" + idCli + "-" + nombCli + "-" + telefCli);
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de eliminación en segundo plano
    private class TareaWSEliminar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpDelete del =
                    new HttpDelete("http://172.25.4.111:52728/Api/Clientes/Cliente/" + id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    resul = false;
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
                lblResultado.setText("Eliminado OK.");
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] clientes;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =new HttpGet("http://172.25.4.111:52728/Api/Clientes/clientes");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                clientes = new String[respJSON.length()];
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    int idCli = obj.getInt("IdCliente");
                    String nombCli = obj.getString("Nombre");
                    int telefCli = obj.getInt("Telefono");
                    clientes[i] = "" + idCli + "-" + nombCli + "-" + telefCli;
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

            if (result)
            {
                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1, clientes);

                lstClientes.setAdapter(adaptador);
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de actualización en segundo plano
    private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPut put = new HttpPut("http://172.25.4.111:52728/Api/Clientes/Cliente");
            put.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("IdCliente", Integer.parseInt(params[0]));
                dato.put("Nombre", params[1]);
                dato.put("Telefono", Integer.parseInt(params[2]));

                StringEntity entity = new StringEntity(dato.toString());
                put.setEntity(entity);

                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    resul = false;
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
                lblResultado.setText("Actualizado OK.");
            }
        }
    }

}
