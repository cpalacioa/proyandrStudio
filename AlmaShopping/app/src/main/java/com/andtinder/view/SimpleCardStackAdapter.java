package com.andtinder.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.almashopping.android.R;
import com.andtinder.Model.CardModel;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.security.Guard;

import bolts.Bolts;

public final class SimpleCardStackAdapter extends CardStackAdapter {

    public SimpleCardStackAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getCardView(int position, final CardModel model, View convertView, ViewGroup parent) {


            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.std_card_inner, parent, false);
                assert convertView != null;
            }


            ImageView foto = ((ImageView) convertView.findViewById(R.id.image));

            ((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());
            ((TextView) convertView.findViewById(R.id.description)).setText(
                    Html.fromHtml(model.getDescription()), TextView.BufferType.SPANNABLE);
            ((TextView) convertView.findViewById(R.id.description)).setMovementMethod(new ScrollingMovementMethod());

            Picasso.with(convertView.getContext())
                    .load(model.getCardImageDrawable())
                    .placeholder(R.drawable.ic_action_business)
                    .into(foto);


            final TextView like_dislike_text = ((TextView) convertView.findViewById(R.id.like_dislike_text));
            final ImageView likeimg=((ImageView)convertView.findViewById(R.id.like));
            final ImageView nolikeimg=((ImageView)convertView.findViewById(R.id.dislike));
/*        if(model.isLike()) {
            like_dislike_text.setText("Me gusta 1");
            likeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_up_blue));
            nolikeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_down));

        }
        else {
            like_dislike_text.setText("No Me Gusta 1");
            likeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_up));
            nolikeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_down_red));

        }*/
            ((ImageView) convertView.findViewById(R.id.like)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    model.setLike(true);
                    like_dislike_text.setText("Me gusta 2");
                    likeimg.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_up_blue));
                    nolikeimg.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_down));
                    RegistrarLike registrarLike=new RegistrarLike();
                    registrarLike.execute(model.GetId().toString(),"True");
                }
            });

            ((ImageView) convertView.findViewById(R.id.dislike)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    model.setLike(false);
                    likeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_up));
                    nolikeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_down_red));
                    like_dislike_text.setText("No me Gusta 2");
                    RegistrarLike registrarLike=new RegistrarLike();
                    registrarLike.execute(model.GetId().toString(),"False");

                }
            });
            return convertView;

        }




    private Boolean GuardarLike(int IdProducto,Boolean like)
    {
        boolean resul = false;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.mauropalacio.co/api/catalogo/Tendencia/");
        post.setHeader("content-type", "application/json");

        try
        {
            //Construimos el objeto cliente en formato JSON
            JSONObject dato = new JSONObject();

            dato.put("idProducto",IdProducto);
            dato.put("idAplicacion",  this.getContext().getResources().getInteger(R.integer.IdAPlicacion));
            dato.put("IdUsuario",1);
            dato.put("usrLike",like);

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

    private class RegistrarLike extends AsyncTask<String,Integer,Boolean> {
        Boolean result = false;

        @Override
        protected Boolean doInBackground(String... params) {
            try {

                GuardarLike(Integer.parseInt(params[0].toString()), Boolean.parseBoolean(params[1].toString()));
                result = true;
            } catch (Exception e) {

                Log.d("error", e.toString());
            }
            return result;
        }


     }
   }





