package ennova.clientesws;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends ActionBarActivity {

    private EditText txtNombre;
    private EditText txtTelefono;
    private TextView txtResultado;
    private Button btnEnviar;
    private Button btnEnviar2;
    private Button btnConsultar;
    private ListView lstClientes;
    private ProgressDialog pd;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtTelefono = (EditText)findViewById(R.id.txtTelefono);
        txtResultado = (TextView)findViewById(R.id.txtResultado);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        btnEnviar2 = (Button)findViewById(R.id.btnEnviar2);
        btnConsultar = (Button)findViewById(R.id.btnConsultar);
        lstClientes = (ListView)findViewById(R.id.lstClientes);

        // Acciones de los botones

        btnEnviar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSInsercion1 tarea = new TareaWSInsercion1();
                tarea.execute();
            }
        });

        btnEnviar2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSInsercion2 tarea = new TareaWSInsercion2();
                tarea.execute();
                pd = ProgressDialog.show(context, "Por favor espere","Insertando datos", true, false);

            }
        });


        btnConsultar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSConsulta tarea = new TareaWSConsulta();
                tarea.execute();
            }
        });


    }

      //Acciones del webservice
      //Tarea Asíncrona para llamar al WS de consulta en segundo plano
      private class TareaWSInsercion1 extends AsyncTask<String,Integer,String> {

          private Cliente[] listaClientes;

          protected String doInBackground(String... params) {

              boolean resul = true;
              String resulta="";

              final String NAMESPACE = "http://tempuri.org/";
              final String URL="http://www.mauropalacio.co/WebService.asmx";
              final String METHOD_NAME = "NuevoClienteSimple";
              final String SOAP_ACTION = "http://tempuri.org/NuevoClienteSimple";

              SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

              request.addProperty("nombre", txtNombre.getText().toString());
              request.addProperty("telefono", txtTelefono.getText().toString());

              SoapSerializationEnvelope envelope =
                      new SoapSerializationEnvelope(SoapEnvelope.VER11);

              envelope.dotNet = true;

              envelope.setOutputSoapObject(request);

              HttpTransportSE transporte = new HttpTransportSE(URL);

              try
              {
                  transporte.call(SOAP_ACTION, envelope);

                  SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
                  String res = resultado_xml.toString();
                  resulta=res;
                  if(!res.equals("1"))
                      resul = false;
              }
              catch (Exception e)
              {
                  resul = false;
                  resulta=e.getMessage().toString();
              }

              return resulta;
          }

          protected void onPostExecute(String result) {

            /*  if (result)
                  txtResultado.setText("Insertado OK");
              else
                  txtResultado.setText("Error!");*/
              txtResultado.setText(result);
          }
      }

    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class TareaWSConsulta extends AsyncTask<String,Integer,Boolean> {

        private Cliente[] listaClientes;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            final String NAMESPACE = "http://tempuri.org/";
            final String URL="http://www.mauropalacio.co/WebService.asmx";
            final String METHOD_NAME = "ListadoClientes";
            final String SOAP_ACTION = "http://tempuri.org/ListadoClientes";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try
            {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap =(SoapObject)envelope.getResponse();

                listaClientes = new Cliente[resSoap.getPropertyCount()];

                for (int i = 0; i < listaClientes.length; i++)
                {
                    SoapObject ic = (SoapObject)resSoap.getProperty(i);

                    Cliente cli = new Cliente();
                    cli.IdCliente = Integer.parseInt(ic.getProperty(0).toString());
                    cli.Nombre = ic.getProperty(1).toString();
                    cli.Telefono = Integer.parseInt(ic.getProperty(2).toString());

                    listaClientes[i] = cli;
                }
            }
            catch (Exception e)
            {
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //Rellenamos la lista con los nombres de los clientes
                final String[] datos = new String[listaClientes.length];

                for(int i=0; i<listaClientes.length; i++)
                    datos[i] = listaClientes[i].Nombre;

                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1, datos);

                lstClientes.setAdapter(adaptador);
            }
            else
            {
                txtResultado.setText("Error!");
            }
        }
    }
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano
    private class TareaWSInsercion2 extends AsyncTask<String,Integer,String> {

        private Cliente[] listaClientes;

        protected String doInBackground(String... params) {

            boolean resul = true;
            String resp="";

            final String NAMESPACE = "http://tempuri.org/";
            final String URL="http://www.mauropalacio.co/WebService.asmx";
            final String METHOD_NAME = "NuevoClienteObjeto";
            final String SOAP_ACTION = "http://tempuri.org/NuevoClienteObjeto";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            Cliente cli = new Cliente();
            cli.Nombre = txtNombre.getText().toString();
            cli.Telefono = Integer.parseInt(txtTelefono.getText().toString());

            PropertyInfo pi = new PropertyInfo();
            pi.setName("cliente");
            pi.setValue(cli);
            pi.setType(cli.getClass());

            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            envelope.addMapping(NAMESPACE, "Clientes", cli.getClass());

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try
            {
                transporte.call(SOAP_ACTION, envelope);

                SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
                resp = resultado_xml.toString();

                if(!resp.equals("1"))
                    resul = false;
            }
            catch (Exception e)
            {
                resul = false;
                resp=e.getMessage().toString();
            }

            return resp;
        }

        protected void onPostExecute(String result) {

            /*if (result)
                txtResultado.setText("Insertado OK");
            else
                txtResultado.setText("Error!");*/
            pd.dismiss();
            txtResultado.setText(result);
        }
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
}
