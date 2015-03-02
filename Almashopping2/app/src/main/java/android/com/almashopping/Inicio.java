package android.com.almashopping;

import android.com.almashopping.adapter.BasketAdapter;
import android.com.almashopping.helpers.Ennovva.Core;
import android.com.almashopping.helpers.ShoppingSQLHelper;
import android.com.almashopping.model.Producto;
import android.com.almashopping.model.Usuario;
import android.com.almashopping.model.basket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.com.almashopping.adapter.NavDrawerListAdapter;
import android.com.almashopping.model.NavDrawerItem;
import android.content.res.TypedArray;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.SQLData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;




public class Inicio extends ActionBarActivity implements
 SearchView.OnQueryTextListener ,ConnectionCallbacks, OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "gplus";
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private RelativeLayout mDrawerShop;
    private ActionBarDrawerToggle mDrawerToggle;
	// nav drawer title
    private SearchView mSearchView;

    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private List<basket> productos;
    private BasketAdapter adapterbasket;
    private ImageView imagenPerfil;
    private TextView nombreUsuario;
    private TextView userLastName;
    private Session sessionfb;
    ListView mDrawerListShop;
    int optionSelectedMenu;

    /* variables para registro en google cloud message*/
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleCloudMessaging gcm;
    private String regid;
    public static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    private String SENDER_ID = "1031648470286";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        imagenPerfil=(ImageView)findViewById(R.id.imgProfilHeader);
        nombreUsuario=(TextView)findViewById(R.id.userNameHeader);
        userLastName=(TextView)findViewById(R.id.userLastName);

        sessionfb =Session.getActiveSession();
        if(sessionfb!=null) {
            if (sessionfb.isOpened()) {
                ObtenerInfoFacebook();

            }


            if (!sessionfb.isOpened()) {
                // Initializing google plus api client
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(Plus.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addScope(Plus.SCOPE_PLUS_LOGIN).build();

            }
        }
        else
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Plus.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        }
        mTitle = mDrawerTitle = getTitle();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        LayoutInflater inflater = getLayoutInflater();

        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
         ViewGroup header_news = (ViewGroup)inflater.inflate(R.layout.header_menu, mDrawerList, false);
        mDrawerList.addHeaderView(header_news, null, false);

        mDrawerShop=(RelativeLayout)findViewById(R.id.drawer_shop);
        mDrawerListShop = (ListView)findViewById(R.id.drawer_shop2);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);


        LoadCartShop();

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        RelativeLayout relative = new RelativeLayout(getSupportActionBar().getThemedContext());
        ImageView imLogo = new ImageView(Inicio.this.getApplicationContext());
        RelativeLayout.LayoutParams imParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        imLogo.setImageResource(R.drawable.logo_almashopping);
        imParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imLogo.setLayoutParams(imParams);
        relative.addView(imLogo,imParams);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(relative);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_action_list, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {

               // getSupportActionBar().setTitle(mTitle);

                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {

                //getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            displayView(0,null);
        }


    }


    @Override
    public boolean onQueryTextChange(String newText) {

        if(newText.length()>3)
        Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();

        return false;
    }
    @Override
    public boolean onQueryTextSubmit(String text) {

        Toast.makeText(this, "Buscando " + text, Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        bundle.putString("txtBuscar", text);
        displayView(6,bundle);
        return false;
    }


    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            optionSelectedMenu=position;
            displayView(position,null);
        }
    }

    private void displayView(int position,Bundle bundle) {
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new MarcasFragment();
                break;

            case 3:
                fragment= new CategoriasFragment();
                break;

            case 4:
                fragment=new PromocionesFragment();
                break;

            case 5:
                CloseSession();
                break;

            case 6:
                fragment=new ResultadosBusqueda();
                break;


            default:
                fragment=new HomeFragment();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager =getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            //setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sessionfb!=null) {
            if (!sessionfb.isOpened()) {
                mGoogleApiClient.connect();
            }
        }
    }

    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient!=null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);


                PintarInfoPerfil(personPhotoUrl,personName,currentPerson.getCurrentLocation());
                Core core=new Core();
                core.RegistrarUsuario(personName,email,2,Inicio.this);
                Usuario usuario=core.ObtUsuarioRegistrado(email,getResources().getInteger(R.integer.IdAPlicacion),2,Inicio.this);
                if(usuario!=null)
                {
                    RegistrarGCM(usuario.Id, 2);
                }


            } else {
                Toast.makeText(getApplicationContext(),
                        "Error trayendo informacion de google", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        //Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        getProfileInformation();
    }
    private void signOutFromGplus() {
        if(mGoogleApiClient!=null)

        {
            if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    private void CloseSession()
    {
        sessionfb =Session.getActiveSession();
         if(sessionfb!=null) {
             if (sessionfb.isOpened()) {
                 sessionfb.closeAndClearTokenInformation();
             }
             if (!sessionfb.isOpened()) {
                 if(mGoogleApiClient!=null) {
                  if(mGoogleApiClient.isConnected())
                     signOutFromGplus();
                 }
             }
         }

        if(mGoogleApiClient!=null)
        {
            if(mGoogleApiClient.isConnecting())
                signOutFromGplus();
        }


        Intent intent = new Intent(Inicio.this.getApplicationContext(), MainActivity.class);
        startActivity(intent);


    }

    private void PintarInfoPerfil(String urlImagenProfile,String username,String lastname)
    {
        imagenPerfil=(ImageView)findViewById(R.id.imgProfilHeader);
        nombreUsuario=(TextView)findViewById(R.id.userNameHeader);
        userLastName=(TextView)findViewById(R.id.userLastName);

        Picasso.with(Inicio.this.getApplicationContext())
                .load(urlImagenProfile)
                .error(R.drawable.ic_action_user)
                .placeholder(R.drawable.ic_action_user)
                .into(imagenPerfil);

        nombreUsuario.setText(username);
        userLastName.setText(lastname);
    }
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
                Inicio.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        //verifica si en las preferencias de usuario esta el reg id
        if (registrationId.length() == 0)
        {
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
            return "";
        }
        // verificamos si ya expiro el registro
        else if (System.currentTimeMillis() > expirationTime)
        {
            return "";
        }

        return registrationId;
    }



    private class TareaRegistroGCM extends AsyncTask<Integer,Integer,String>
    {
        Core core=new Core();

        @Override
        protected String doInBackground(Integer... params)
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
                //Registrar en el servidor
                Log.d(TAG,Integer.toString(params[0]));
                boolean registrado= core.RegistrarDispositivo(Inicio.this, regid, params[0]);
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

    private void RegistrarGCM(int IdUsuario,int provider)
    {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(Inicio.this);

            //Obtenemos el Registration ID guardado
            regid = getRegistrationId(Inicio.this.getApplicationContext());

            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                TareaRegistroGCM tarea = new TareaRegistroGCM();
                tarea.execute(IdUsuario);

            }
        } else {
            //Reemplazar por un toad
            Log.i(TAG, "No se ha encontrado Google Play Services.");
        }

    }
    private void ObtenerInfoFacebook()
    {

        if (sessionfb != null) {
            Request.newMeRequest(sessionfb, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        try {
                            String email = user.getProperty("email").toString();
                            PintarInfoPerfil("https://graph.facebook.com/" + user.getId() + "/picture?type=large",user.getFirstName(),user.getLastName());
                            Core core=new Core();
                            core.RegistrarUsuario(user.getName(),email,1,Inicio.this);
                            Usuario usuario=core.ObtUsuarioRegistrado(email,getResources().getInteger(R.integer.IdAPlicacion),1,Inicio.this);
                            if(usuario!=null)
                            {
                                RegistrarGCM(usuario.Id, 1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).executeAsync();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if(mSearchView!=null) {
            mSearchView.setQueryHint("Buscar...");
            mSearchView.setOnQueryTextListener(this);
        }
        return true;
    }

    View.OnClickListener carroCompra=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mDrawerLayout.openDrawer(mDrawerShop);
        }
    };
    private void InfoGeneral(String url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if(id==R.id.btnInfoAlma) {
            InfoGeneral("http://www.almashopping.com.co/es/site/quienessomos");
            return true;
        }

        if(id==R.id.btnCartshop)
        {
            if(mDrawerLayout.isDrawerOpen(mDrawerShop))
                mDrawerLayout.closeDrawer(mDrawerShop);
            else {
                LoadCartShop();
                mDrawerLayout.openDrawer(mDrawerShop);
            }
            return true;
        }

        if(id==R.id.btnContacto){
            InfoGeneral("http://www.almashopping.com/es/site/contacto");
            return  true;
        }

        if(id==R.id.btnTerminos){
            InfoGeneral("http://www.almashopping.com.co/es/site/terminos");
            return  true;
        }

        if(id==R.id.btnLogout){
            CloseSession();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.btnLogout).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    public void onResume() {
        super.onResume();
          //  ObtenerInfoGoogle();
        }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void LoadCartShop()
    {
        //SQL LITE
        productos=new ArrayList();
        ShoppingSQLHelper dbconnect=new ShoppingSQLHelper(Inicio.this.getApplicationContext(),"DBAlma",null,1);
        SQLiteDatabase db=dbconnect.getWritableDatabase();
        if(db!=null)
        {
            String[] campos = new String[] {"Id,IdProducto","Titulo","Descripcion","valor","imagen","Cantidad","marca"};
            Cursor registro = db.query("cartshop", campos, null, null, null, null, null);
            //Nos aseguramos de que existe al menos un registro
            if (registro.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String valor=Integer.toString(registro.getInt(4));
                    Producto producto=new Producto(registro.getInt(1),registro.getString(2),registro.getString(5),valor,registro.getString(7),registro.getString(3));
                    basket productCart=new basket(registro.getInt(0),registro.getInt(6),producto);
                    productos.add(productCart);

                } while(registro.moveToNext());
            }
            db.close();
            adapterbasket=new BasketAdapter(Inicio.this,productos);
            mDrawerListShop.setAdapter(adapterbasket);

        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(Integer.toString(optionSelectedMenu).length()>0)
            displayView(optionSelectedMenu,null);
            return true;
        }
        return  onKeyDown(keyCode,event);
    }



}
