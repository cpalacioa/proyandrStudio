package com.almashopping.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Inicio extends ActionBarActivity {

    private UiLifecycleHelper uiHelper;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions", "publish_stream");
    private ActionBar mActionbar;
    private ViewPager mPager;


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "GCM";
    private GoogleCloudMessaging gcm;
    private String regid;
    public static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";

    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;

    private String SENDER_ID = "383216941805";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //Obtener Referencia a la actionbar
        mActionbar = getSupportActionBar();
        mActionbar.setIcon(R.drawable.ic_logo_almashopping);
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        mActionbar.setDisplayShowTitleEnabled(true);
        mActionbar.setDisplayShowCustomEnabled(true);
        mPager = (ViewPager) findViewById(R.id.pager);


        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(Inicio.this);

            //Obtenemos el Registration ID guardado
            regid = getRegistrationId(Inicio.this.getApplicationContext());

            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                TareaRegistroGCM tarea = new TareaRegistroGCM();
                tarea.execute();

            }
        } else {
            //Reemplazar por un toad
            Log.i(TAG, "No se ha encontrado Google Play Services.");
        }


        /** Manejo de tabs como viewpage**/
        FragmentManager fm = getSupportFragmentManager();
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionbar.setSelectedNavigationItem(position);

            }
        };

        mPager.setOnPageChangeListener(pageChangeListener);

        ViewPager.OnPageChangeListener cambioPagina= new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ActionBar.Tab tab=(ActionBar.Tab)mActionbar.getTabAt(position);
                mActionbar.selectTab(tab);
                mActionbar.setTitle(Integer.toString(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        AdapterFragmentsPage fragmentPagerAdapter = new AdapterFragmentsPage(fm);
        mPager.setAdapter(fragmentPagerAdapter);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
               // mActionbar.setTitle(tab.getText().toString());
               mActionbar.setTitle(tab.getContentDescription());
                //mActionbar.setTitle("ALMA");
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                //  mPager.setCurrentItem(tab.getPosition());

            }

        };
        ActionBar.Tab tab = mActionbar.newTab()
                //.setText("Catalogo")
                .setIcon(R.drawable.ic_action_business)
                .setContentDescription("Catalogo")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);

        tab = mActionbar.newTab()
                //.setText("Comunidad")
                .setIcon(R.drawable.ic_action_star_10)
                .setContentDescription("Tendencia")
                .setTabListener(tabListener);
        mActionbar.addTab(tab);


        tab=mActionbar.newTab()
                //.setText("Marcas")
                .setIcon(R.drawable.ic_action_tshirt)
                .setContentDescription("Marcas")
                .setTabListener(tabListener);
        mActionbar.addTab(tab);

        tab=mActionbar.newTab()
                .setIcon(R.drawable.ic_action_user)
                .setContentDescription("Perfil")
                .setTabListener(tabListener);
        mActionbar.addTab(tab);


        mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    }

   //Métodos del viewpager

    //Metodos para traer info del apigraph
    private void InformacionSession()
    {
        Session session = Session.getActiveSession();
        if (session != null) {
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        try {
                            Log.d("usuario", user.getId().toString());
                            Log.d("Nombre", "https://graph.facebook.com/" + user.getId() + "/picture?type=large");
                            Log.d("Locacion", user.getLocation().getProperty("name").toString());
                            Log.d("Cumpleaños", user.getBirthday().toString());
                            String email = user.getProperty("email").toString();
                            Log.d("Email", email);
                            // userInfoTextView.setText(user.getName());
                            //Picasso.with(MainFragment.this.getActivity().getApplicationContext()).load("https://graph.facebook.com/" + user.getId() + "/picture?type=large").into(profilepic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).executeAsync();
        }

    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if(session.isOpened())
            {
                if (checkPlayServices()) {
                    gcm = GoogleCloudMessaging.getInstance(Inicio.this);

                    //Obtenemos el Registration ID guardado
                    regid = getRegistrationId(Inicio.this.getApplicationContext());

                    //Si no disponemos de Registration ID comenzamos el registro
                    if (regid.equals("")) {
                        TareaRegistroGCM tarea = new TareaRegistroGCM();
                        tarea.execute();

                    }
                } else {
                    //Reemplazar por un toad
                    Log.i(TAG, "No se ha encontrado Google Play Services.");
                }

            }
        }
    };
    //Funciones para registrar Dispositivo para las notificaciones
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
                    gcm = GoogleCloudMessaging.getInstance(Inicio.this);
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);
                Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
                //Registrar en el servidor
                boolean registrado = RegistrarServidor(regid);
                if(registrado)
                {
                    setRegistrationId(Inicio.this, regid);
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
                Inicio.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putLong(PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + EXPIRATION_TIME_MS);

        editor.commit();
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
                Inicio.class.getSimpleName(),
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


    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
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
