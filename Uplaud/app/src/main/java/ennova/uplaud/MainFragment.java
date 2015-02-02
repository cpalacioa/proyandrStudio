package ennova.uplaud;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    //Varibles para debug
    private static final String TAG = "MainFragment";
    private UiLifecycleHelper uiHelper;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private boolean pendingPublishReauthorization = false;
    private TextView userInfoTextView;
    private ImageView profilepic;
    private Button btnShareApp;
    private Button btnShareApi;
    private String url = "http://www.qruda.com/comunidad/q-sounds-la-magia-incomparable-de-a-take-away-show/";
    private ProgressDialog mProgressDialog;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, container, false);
        userInfoTextView = (TextView) view.findViewById(R.id.userInfoTextView);
        profilepic=(ImageView)view.findViewById(R.id.ImageProfile);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        btnShareApp=(Button)view.findViewById(R.id.btnShareApp);
        btnShareApi=(Button)view.findViewById(R.id.btnShareApi);

        authButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes","user_status","email"));
        authButton.setFragment(this);
        btnShareApp.setOnClickListener(btnShareAppListener);
        btnShareApi.setOnClickListener(btnShareApiListener);
        //WebView myWebView = (WebView) view.findViewById(R.id.webView);
        //WebSettings webSettings = myWebView.getSettings();
       // webSettings.setJavaScriptEnabled(true);
        //myWebView.loadUrl(url);

        new Description().execute();

        return view;
    }


    //Acciones de botón
    View.OnClickListener btnShareAppListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(MainFragment.this.getActivity())
                    .setLink("http://www.qruda.com/")
                    .setName("QRUDA")
                    .setCaption("Qruda Colectivo")
                    .setDescription("Conoce los nuevos productos Qruda")
                    .setPicture("http://www.qruda.com/images/thumbs/113x164/1/e.jpg")
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        }
    };

    View.OnClickListener btnShareApiListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         //publishFeedDialog();
            publishStory("mensaje de prueba");
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            //cuando esta logeado mostrar ui correspondiente
            userInfoTextView.setVisibility(View.VISIBLE);

            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        try {
                            Log.d("usuario",user.getId().toString());
                            Log.d("Nombre", "https://graph.facebook.com/" + user.getId() + "/picture?type=large");
                            Log.d("Locacion",user.getLocation().getProperty("name").toString());
                            Log.d("Cumpleaños",user.getBirthday().toString());
                            String email = user.getProperty("email").toString();
                            Log.d("Email",email);

                            userInfoTextView.setText(user.getName());
                            Picasso.with(MainFragment.this.getActivity().getApplicationContext()).load("https://graph.facebook.com/"+ user.getId()+ "/picture?type=large").into(profilepic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).executeAsync();
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            //mostrar ui de no autenticado
            Log.i(TAG, "Logged out...");
            userInfoTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    //Metodos para manejo de la session de facebook en los diferentes ciclos de la aplicacion

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        //Cuando la session es diferente de null cambia la notificacion
        //para que no pase por el disparador que controla la session
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);

        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });


        //En caso de requerir nuevos permisos en especial de publciacipn
       /* Session session = Session.getActiveSession();

        if (session != null) {

            // Check for publish permissions
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
                        this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }
        }*/

    }
    private boolean isSubsetOf(Collection<String> subset,
                               Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    //manejo de permisos
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: updating the view


        }
    }

    //Publicación por webDialogo
    private void publishFeedDialog() {
        Bundle params = new Bundle();

        params.putString("name", "QRUDA");
        params.putString("caption", "Qruda Colectivo");
        params.putString("description", "Diseño independiente.");
        params.putString("link", "http://www.qruda.com/");
        params.putString("picture", "http://www.qruda.com/images/thumbs/113x164/1/e.jpg");

        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(getActivity(),
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Toast.makeText(getActivity(),
                                        "Posted story, id: " + postId,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Publish cancelled",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error posting story",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }

    private void publishStory(String mensaje) {

        if (checkPermissions()) {



            Request request = Request.newStatusUpdateRequest(
                    Session.getActiveSession(), mensaje,
                    new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            if (response.getError() == null)
                                Toast.makeText(MainFragment.this.getActivity(),
                                        "Estado Actualizado Correctamente",
                                        Toast.LENGTH_LONG).show();
                        }
                    });
            request.executeAsync();
        } else {
            requestPermissions();
        }

    }
    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }

  //Obtener un html
  // Description AsyncTask
  private class Description extends AsyncTask<Void, Void, Void> {
      String desc;

      @Override
      protected void onPreExecute() {
          super.onPreExecute();
          mProgressDialog = new ProgressDialog(MainFragment.this.getActivity());
          mProgressDialog.setTitle("Obteniendo articulo");
          mProgressDialog.setMessage("Loading...");
          mProgressDialog.setIndeterminate(false);
          mProgressDialog.show();
      }

      @Override
      protected Void doInBackground(Void... params) {
          try {
              // Connect to the web site
              Document document = Jsoup.connect(url).get();
              // Using Elements to get the Meta data
              Elements description = document
                      .getElementsByClass("content-article");
              // Locate the content attribute
              Log.d("description",description.toString());
              desc = description.html();
          } catch (IOException e) {
              e.printStackTrace();
          }
          return null;
      }

      @Override
      protected void onPostExecute(Void result) {
          // Set description into TextView
          TextView txtdesc = (TextView) getView().findViewById(R.id.desctxt);
          //txtdesc.setText(Html.fromHtml(desc));
          WebView myWebView = (WebView) getView().findViewById(R.id.webView);
          WebSettings webSettings = myWebView.getSettings();
         webSettings.setJavaScriptEnabled(true);
          myWebView.loadDataWithBaseURL(null, desc, "text/html", "UTF-8", null);
          //myWebView.loadData(desc,"text/html; charset=utf-8","UTF-8");
          mProgressDialog.dismiss();
      }
  }
}
