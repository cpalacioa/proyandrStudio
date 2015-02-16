package android.qruda.com.qruda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Comunidad extends ActionBarActivity {
    List articulos;
    ListView lvArticulos;
    ActionBar mActionbar;
    ImageButton btnCatalogo;

    //Campos para registro de notificaciones
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Context context;
    private GoogleCloudMessaging gcm;
    private String regid;
    public static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";

    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;

    String SENDER_ID = "383216941805";

    static final String TAG = "GCMDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunidad);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayShowHomeEnabled(false);
        View mActionBarView = getLayoutInflater().inflate(R.layout.actionbar_comunidad, null);
        mActionbar.setCustomView(mActionBarView);
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        btnCatalogo=(ImageButton)this.findViewById(R.id.btn_Catalogo);


        TareaWSListar tareasasas=new TareaWSListar();
        tareasasas.execute();


        lvArticulos = (ListView) findViewById( R.id.list_articulos);

        //Registro en servidor gcm
        context = getApplicationContext();
        //Chequemos si está instalado Google Play Services
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(Comunidad.this);

            //Obtenemos el Registration ID guardado
            regid = getRegistrationId(context);

            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                TareaRegistroGCM tarea = new TareaRegistroGCM();
                tarea.execute();
            }
        } else {
            //Reemplazar por un toad
            Log.i(TAG, "No se ha encontrado Google Play Services.");
        }

        btnCatalogo.setOnClickListener(listenerCatalogo);
        lvArticulos.setOnItemClickListener(ListenerArticulos);

    }

    AdapterView.OnItemClickListener ListenerArticulos=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                Articulo articulo = (Articulo) articulos.get(position);
                //Intent intent = new Intent(Intent.ACTION_VIEW,
                  //      Uri.parse(articulo.Link));
                Intent intent=new Intent(Comunidad.this.getApplicationContext(),DetalleArticulo.class);
                intent.putExtra("Id",articulo.id);
                intent.putExtra("UrlImagen",articulo.Imagen);
                intent.putExtra("Nombre",articulo.Nombre);
                intent.putExtra("Fecha",articulo.Fecha);
                intent.putExtra("Url",articulo.Link);
                startActivity(intent);
            }
            catch (Exception ex)
            {
                Log.d("error!!",ex.toString());
            }
        }
    };

    OnClickListener listenerCatalogo=new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent irCatalogo=new Intent(Comunidad.this.getApplicationContext(),MainActivity.class);
            startActivity(irCatalogo);
        }
    };

    //Método para validar si esta disponible el google play service en el dispositivo
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i(TAG, "Dispositivo no soportado.");
                finish();
            }
            return false;
        }
        return true;
    }
    private static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    private String getRegistrationId(Context context)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        //verifica si en las preferencias de usuario esta el reg id
        if (registrationId.length() == 0)
        {
            Log.d(TAG, "Registro GCM no encontrado.");
            return "";
        }
        // en caso de encontrarse buscamos las demas propiedades
        int registeredVersion =
                prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        long expirationTime =
                prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));

        // verificamos la versión
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion)
        {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return "";
        }
        // verificamos si ya expiro el registro
        else if (System.currentTimeMillis() > expirationTime)
        {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        }

        return registrationId;
    }
    private class TareaRegistroGCM extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            try
            {
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);
                Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
                //Registrar en el servidor
                boolean registrado = RegistrarServidor(regid);
                if(registrado)
                {
                    setRegistrationId(context, regid);
                }

            }
            catch (IOException ex)
            {
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
        }
    }
    private void setRegistrationId(Context context, String regId)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putLong(PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + EXPIRATION_TIME_MS);

        editor.commit();
    }

    private Boolean RegistrarServidor(String _serial)
    {
        boolean resul = false;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.mauropalacio.co/api/Dispositivos/dispositivo/");
        post.setHeader("content-type", "application/json");

        try
        {
            //Construimos el objeto cliente en formato JSON
            JSONObject dato = new JSONObject();

            //dato.put("Id", Integer.parseInt(txtId.getText().toString()));
            dato.put("Serial", _serial);
            dato.put("IdAplication", getResources().getInteger(R.integer.IdAPlicacion));
            dato.put("IdUsuario",getResources().getString(R.string.UserDefault));

            StringEntity entity = new StringEntity(dato.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            String respStr = EntityUtils.toString(resp.getEntity());

            if(!respStr.equals("true"))
                resul = true;
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest", "Error!", ex);
            resul = false;
        }

        return resul;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_comunidad, menu);
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


    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {


          protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =new HttpGet("http://www.qruda.com/index.php/block/ajaxretornoblockallproduct?init=0&limite=10&namewiget=32&pagina=0");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                //JSONArray respJSON = new JSONArray(respStr);
                JSONObject object = new JSONObject(respStr);
                JSONObject objecthtml=new JSONObject(object.getString("32"));
                JSONArray Jarray = objecthtml.getJSONArray("html");
                articulos=new ArrayList();
                for(int i=0; i<Jarray.length(); i++)
                {
                    JSONObject obj = Jarray.getJSONObject(i);
                    JSONObject imagenes=new JSONObject(obj.getString("images"));

                    int id = obj.getInt("pid");
                    String nombre=obj.getString("name");
                    String fecha=obj.getString("fecha");
                    String link=obj.getString("urlproblock");
                    if(!link.startsWith("http"))
                        link="http://"+link;

                    //JSONObject imagen=(JSONObject)imagenes.get(0);
                    String thumb=imagenes.getString("Thumbnail");
                    if(!thumb.startsWith("http"))
                        thumb="http://"+thumb;

                    Articulo arti=new Articulo(id,nombre,thumb,fecha,link);
                   articulos.add(arti);

                }


                Log.d("Respuesta", Jarray.toString());
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
                lvArticulos.setAdapter(new ArticleListAdapter(Comunidad.this, articulos) );

            }
        }
    }

}
