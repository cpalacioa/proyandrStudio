package com.almashopping.android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.squareup.picasso.Picasso;


public class PerfilUsuario extends Fragment {

    ImageView profilepic;
    TextView userInfoTextView;
    public PerfilUsuario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        profilepic=(ImageView)getView().findViewById(R.id.ImageProfile);
        userInfoTextView=(TextView)getView().findViewById(R.id.NombreUsuario);
        InformacionSession();
    }

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
                             userInfoTextView.setText(user.getName());
                            Picasso.with(PerfilUsuario.this.getActivity().getApplicationContext()).load("https://graph.facebook.com/" + user.getId() + "/picture?type=large").into(profilepic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).executeAsync();
        }

    }


}
