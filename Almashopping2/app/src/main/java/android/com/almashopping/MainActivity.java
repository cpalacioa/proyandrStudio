package android.com.almashopping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class MainActivity extends Activity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener
 {

    String[] spinnerValues = { "ARGENTINA", "COLOMBIA", "MEXICO"};
    String[] spinnerSubs = { "Disponible", "Muy pronto..", "Muy pronto..." };
    int total_images[] = { R.drawable.argentina,R.drawable.colombia,R.drawable.mexico};
    private LoginButton loginBtn;
    private UiLifecycleHelper uiHelper;
     private static final int RC_SIGN_IN = 0;
     private static final String TAG = "MainActivity";
     private GoogleApiClient mGoogleApiClient;
     private boolean mIntentInProgress;
     private boolean mSignInClicked;
     private ConnectionResult mConnectionResult;
     private SignInButton btnSignIn;


     @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

         requestWindowFeature(Window.FEATURE_NO_TITLE);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
         btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);
         setGooglePlusButtonText(btnSignIn,"Ingresar con Google");
         btnSignIn.setOnClickListener(this);


         Spinner mySpinner = (Spinner) findViewById(R.id.spinner_show);
        mySpinner.setAdapter(new AdapterPaises(this, R.layout.paises_spinner, spinnerValues));
        loginBtn = (LoginButton)findViewById(R.id.fb_login_button);
        loginBtn.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes", "email"));
        Session sesionfb = Session.getActiveSession();

        if(sesionfb.isOpened())
        {
            Intent intent=new Intent(MainActivity.this,Inicio.class);
            startActivity(intent);

        }
         if(!sesionfb.isOpened())
         {
             mGoogleApiClient = new GoogleApiClient.Builder(this)
                     .addApi(Plus.API)
                     .addConnectionCallbacks(this)
                     .addOnConnectionFailedListener(this)
                     .addScope(Plus.SCOPE_PLUS_LOGIN).build();
         }

    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView mTextView = (TextView) v;
                mTextView.setText(buttonText);
                return;
            }
        }
    }



    public class AdapterPaises extends ArrayAdapter<String> {

        public AdapterPaises(Context ctx, int txtViewResourceId, String[] objects)
        {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
        {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override public View getView(int pos, View cnvtView, ViewGroup prnt)
        {
            return getCustomView(pos, cnvtView, prnt);
        }

        private View getCustomView(int position, View convertView,ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.paises_spinner, parent, false);
            TextView main_text = (TextView) mySpinner .findViewById(R.id.text_main_seen);
            main_text.setText(spinnerValues[position]);
            TextView subSpinner = (TextView) mySpinner .findViewById(R.id.sub_text_seen);
            subSpinner.setText(spinnerSubs[position]);
            ImageView left_icon = (ImageView) mySpinner .findViewById(R.id.left_pic);
            left_icon.setImageResource(total_images[position]);
            return mySpinner;

        }


    }
    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Intent intent=new Intent(MainActivity.this,Inicio.class);
                startActivity(intent);

            } else if (state.isClosed()) {

            }
        }
    };

     @Override
     public void onClick(View v) {
         switch (v.getId()) {
             case R.id.sign_in_button:
                 // Signin button clicked
                 signInWithGplus();
                 break;
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

     private void signInWithGplus() {
         Log.d("gplus","logeo");

         if (!mGoogleApiClient.isConnecting()) {
             Log.d("gplus","isConnecting");

             mSignInClicked = true;
             resolveSignInError();
         }
         else
         {
             Log.d("gplus","ya cnecto");

         }
     }

     /**
      * Method to resolve any signin errors
      * */
     private void resolveSignInError() {
         if (mConnectionResult.hasResolution()) {
             try {
                 Log.d("gplus","try");
                 mIntentInProgress = true;
                 mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
             } catch (IntentSender.SendIntentException e) {
                 Log.d("gplus","catch");
                 mIntentInProgress = false;
                 mGoogleApiClient.connect();
             }
         }
     }
     @Override
     protected void onActivityResult(int requestCode, int responseCode,
                                     Intent intent) {
         Log.d("gplus","activity result");
         uiHelper.onActivityResult(requestCode, responseCode, intent);

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

             } else {
                 Toast.makeText(getApplicationContext(),
                         "No se pudo traer informacion googl", Toast.LENGTH_LONG).show();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     @Override
     public void onConnected(Bundle arg0) {

         mSignInClicked = false;
         Toast.makeText(this, "Usuario logueado!", Toast.LENGTH_LONG).show();

         // Get user's information
         getProfileInformation();
         Intent intent=new Intent(MainActivity.this.getApplicationContext(),Inicio.class);
         startActivity(intent);
     }

     @Override
     public void onConnectionSuspended(int arg0) {
         mGoogleApiClient.connect();
     }


     @Override
    protected void onStart() {
        super.onStart();
         if(mGoogleApiClient!=null) {
             mGoogleApiClient.connect();
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


     //Metodos para ciclo de vida de la actividad facbeook
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        Session sesionfb = Session.getActiveSession();
        if(sesionfb.isOpened())
        {
            Intent intent=new Intent(MainActivity.this,Inicio.class);
            startActivity(intent);

        }


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
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }



    @Override public void onBackPressed()
    {
        this.finish();
        super.onBackPressed();
    }
    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "android.com.almashopping",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("keyhash",e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("keyhash",e.toString());

        }
    }

}
