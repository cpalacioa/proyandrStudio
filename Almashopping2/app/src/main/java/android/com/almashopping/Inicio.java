package android.com.almashopping;

import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
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

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
    private ImageView imagenPerfil;
    private TextView nombreUsuario;
    private Session sessionfb;

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
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup header_news = (ViewGroup)inflater.inflate(R.layout.header_menu, mDrawerList, false);
        mDrawerList.addHeaderView(header_news, null, false);

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
            displayView(0);
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

        Toast.makeText(this, "Searching for " + text, Toast.LENGTH_LONG).show();

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
            displayView(position);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
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


    /**
     * Method to resolve any signin errors
     * */
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

    @Override public void onBackPressed()
    {
        this.finish();
        super.onBackPressed();
    }

}
