package ennova.demopush;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import  android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {


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

    Button btnRegistrar;
    EditText txtUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegistrar = (Button) findViewById(R.id.btnGuadar);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);

        //Acciones del boton
        btnRegistrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context = getApplicationContext();
                //Chequemos si está instalado Google Play Services
                if (checkPlayServices()) {
                    gcm = GoogleCloudMessaging.getInstance(MainActivity.this);

                    //Obtenemos el Registration ID guardado
                    regid = getRegistrationId(context);

                    //Si no disponemos de Registration ID comenzamos el registro
                    if (regid.equals("")) {
                        TareaRegistroGCM tarea = new TareaRegistroGCM();
                        tarea.execute(txtUsuario.getText().toString());
                    }
                } else {
                    //Reemplazar por un toad
                    Log.i(TAG, "No se ha encontrado Google Play Services.");
                }

            }
        });
    }


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

    //Manejador de el registracion id si ya existe si no ha vencido y si no ha cambiado
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
            String registeredUser =
                    prefs.getString(PROPERTY_USER, "user");

            int registeredVersion =
                    prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

            long expirationTime =
                    prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String expirationDate = sdf.format(new Date(expirationTime));

            Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                    ", version=" + registeredVersion +
                    ", expira=" + expirationDate + ")");
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
            // verificamos el nombre de usuario
            else if (!txtUsuario.getText().toString().equals(registeredUser))
            {
                Log.d(TAG, "Nuevo nombre de usuario.");
                return "";
            }

            return registrationId;
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
                //Nos registramos en nuestro servidor
                boolean registrado = registroServidor(params[0], regid);
                //Guardamos los datos del registro
                if(registrado)
                {
                    setRegistrationId(context, params[0], regid);
                }
            }
            catch (IOException ex)
            {
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
        }
    }

    // Método que registra en el servidor utilizandp webservices
    private boolean registroServidor(String usuario, String regId)
    {
        boolean reg = false;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL="http://www.mauropalacio.co/WebService.asmx";
        final String METHOD_NAME = "RegistrarCliente";
        final String SOAP_ACTION = "http://tempuri.org/RegistrarCliente";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("usuario", usuario);
        request.addProperty("regGCM", regId);
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

            if(res.toUpperCase().equals("TRUE"))
            {
                Log.d(TAG, "Registrado en mi servidor.");
                reg = true;
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
        }
        return reg;
    }
    // Método para guardar las preferencias de la aplicación
    private void setRegistrationId(Context context, String user, String regId)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER, user);
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putLong(PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + EXPIRATION_TIME_MS);

        editor.commit();
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
