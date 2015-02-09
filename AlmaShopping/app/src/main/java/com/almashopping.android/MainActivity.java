package com.almashopping.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends Activity {


    private LoginButton loginBtn;
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loginBtn = (LoginButton)findViewById(R.id.fb_login_button);
        loginBtn.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes", "email"));
        Session sesionfb = Session.getActiveSession();
        if(sesionfb.isOpened())
        {
            Intent intent=new Intent(MainActivity.this,Inicio.class);
            startActivity(intent);

        }

    }



    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Intent intent=new Intent(MainActivity.this,Inicio.class);
                startActivity(intent);

                Log.d("Facebook", "Facebook session opened");
            } else if (state.isClosed()) {
                Log.d("Faebook", "Facebook session closed");
            }
        }
    };

    //Metodos para ciclo de vida de la actividad
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
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
                    "ennova.uplaud",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }

}
