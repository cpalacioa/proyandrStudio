package android.com.almashopping;

import android.app.AlertDialog;
import android.com.almashopping.adapter.BasketAdapter;
import android.com.almashopping.helpers.ShoppingSQLHelper;
import android.com.almashopping.model.Producto;
import android.com.almashopping.model.basket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

import com.squareup.picasso.Picasso;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

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
    private Session sessionfb;
    ListView mDrawerListShop;
    int optionSelectedMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        imagenPerfil=(ImageView)findViewById(R.id.imgProfilHeader);
        nombreUsuario=(TextView)findViewById(R.id.userNameHeader);

        sessionfb =Session.getActiveSession();
        if(sessionfb!=null) {
            if (sessionfb.isOpened()) {
                Log.d("sesion", "facebook");
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

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                PintarInfoPerfil(personPhotoUrl,personName);

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

        if(sessionfb.isOpened()) {
            sessionfb.closeAndClearTokenInformation();
        }
        if(!sessionfb.isOpened())
        {
            signOutFromGplus();
        }

        Intent intent = new Intent(Inicio.this.getApplicationContext(), MainActivity.class);
        startActivity(intent);


    }

    private void PintarInfoPerfil(String urlImagenProfile,String username)
    {
        imagenPerfil=(ImageView)findViewById(R.id.imgProfilHeader);
        nombreUsuario=(TextView)findViewById(R.id.userNameHeader);

        Picasso.with(Inicio.this.getApplicationContext())
                .load(urlImagenProfile)
                .error(R.drawable.ic_action_user)
                .placeholder(R.drawable.ic_action_user)
                .into(imagenPerfil);

        nombreUsuario.setText(username);
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
                            PintarInfoPerfil("https://graph.facebook.com/" + user.getId() + "/picture?type=large",user.getName());

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
            Log.d("db","abrio conexion");
            String[] campos = new String[] {"IdProducto","Titulo","Descripcion","valor","imagen","Cantidad","marca"};
            Cursor registro = db.query("cartshop", campos, null, null, null, null, null);
            //Nos aseguramos de que existe al menos un registro
            if (registro.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String valor=Integer.toString(registro.getInt(3));
                    Producto producto=new Producto(registro.getInt(0),registro.getString(1),registro.getString(4),valor,registro.getString(6),registro.getString(2));
                    basket productCart=new basket(registro.getInt(5),producto);
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

/*            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir de "+Integer.toString(optionSelectedMenu))
                    .setMessage("Estás seguro?")
                    .setNegativeButton(android.R.string.cancel, null)//sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            //Salir
                            Inicio.this.finish();
                        }
                    })
                    .show();


            return true;*/
            if(Integer.toString(optionSelectedMenu).length()>0)
            displayView(optionSelectedMenu,null);
            return true;
        }
//para las demas cosas, se reenvia el evento al listener habitual
        return  onKeyDown(keyCode,event);
    }

}
