package android.qruda.com.qruda;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PerfilAutor extends ActionBarActivity {

    ActionBar mActionbar;
    RoundImage roundedImage;
    ImageView imagencircular;
    ImageButton btnAtras;
    Button btnArticulo;
    Button btnComentarios;
    TextView NombreAutor;
    TextView DatosAutor;


    //Datos del articulo
    int IdArticulo;
    String UrlImagen;
    String Nombre;
    String Fecha;
    String Url;

     //Informacion del autor
    String username;
    String NombreUsuario;
    String Apellido;
    String Avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_autor);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayShowHomeEnabled(false);
        //displaying custom ActionBar
        View mActionBarView = getLayoutInflater().inflate(R.layout.actionbar_articulo, null);
        mActionbar.setCustomView(mActionBarView);
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        imagencircular=(ImageView)findViewById(R.id.ImagenPerfil);
        Bundle bundle = this.getIntent().getExtras();
        Url=bundle.getString("Url");
        IdArticulo=bundle.getInt("Id");
        UrlImagen=bundle.getString("UrlImagen");
        Nombre=bundle.getString("Nombre");
        Fecha=bundle.getString("Fecha");

        NombreAutor=(TextView)findViewById(R.id.NombreAutor);
        DatosAutor=(TextView)findViewById(R.id.datosAutor);


        btnAtras=(ImageButton)this.findViewById(R.id.btn_atras_articulo);
        btnArticulo=(Button)findViewById(R.id.btnArticuloAutor);
        btnComentarios=(Button)findViewById(R.id.btnComentariosAutor);

        btnAtras.setOnClickListener(listenerAtras);
        btnArticulo.setOnClickListener(listenerBtnArticulo);
        btnComentarios.setOnClickListener(listenerComentarios);



//        Picasso.with(PerfilAutor.this).load("http://kingoftheflatscreen.com/wp-content/uploads/2014/07/tumblr_mg6mrft7nI1qzpxx1o1_1280.jpg").transform(new RoundImage()).into(imagencircular);
        TraerInfoAutor informacionautor=new TraerInfoAutor();
        informacionautor.execute();

    }

   View.OnClickListener listenerBtnArticulo=new View.OnClickListener()
   {
       @Override
        public  void  onClick(View v){
           Intent intent=new Intent(PerfilAutor.this.getApplicationContext(),DetalleArticulo.class);
           intent.putExtra("Id",IdArticulo);
           intent.putExtra("UrlImagen",UrlImagen);
           intent.putExtra("Nombre",Nombre);
           intent.putExtra("Fecha",Fecha);
           intent.putExtra("Url",Url);
           startActivity(intent);

       }
   };

    View.OnClickListener listenerComentarios=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(PerfilAutor.this,ArticuloComentarios.class);
            intent.putExtra("Id",IdArticulo);
            intent.putExtra("UrlImagen",UrlImagen);
            intent.putExtra("Nombre",Nombre);
            intent.putExtra("Fecha",Fecha);
            intent.putExtra("Url",Url);
            startActivity(intent);

        }
    };


    View.OnClickListener listenerAtras=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(PerfilAutor.this,Comunidad.class);
            startActivity(intent);
        }
    };


    private class TraerInfoAutor extends AsyncTask <String,Integer,Boolean>
    {
        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =new HttpGet("http://www.qruda.com/comunidad/wp-json/posts/"+IdArticulo+"");
            Log.d("UrlConsulta",del.toString());
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                //JSONArray respJSON = new JSONArray(respStr);
                JSONObject object = new JSONObject(respStr);
                JSONObject objecthtml=new JSONObject(object.getString("author"));
                username=objecthtml.getString("nickname");
                NombreUsuario=objecthtml.getString("username");
                Apellido=objecthtml.getString("last_name");
                Avatar=objecthtml.getString("avatar");


                Log.d("Respuesta",respStr );
                Log.d("autor",objecthtml.toString());

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //llenar adapter
                //lvArticulos = (ListView) findViewById( R.id.list_articulos);
                //lvArticulos.setAdapter(new ArticleListAdapter(Comunidad.this, articulos) );
                NombreAutor.setText(username);

                String datosUsuarios=NombreUsuario+" "+Apellido;
                DatosAutor.setText(datosUsuarios);
                DatosAutor.setText(
                        Html.fromHtml("<b>PUBLICADO POR:</b> "+datosUsuarios+""),
                        TextView.BufferType.SPANNABLE);

                Picasso.with(PerfilAutor.this).load(Avatar).transform(new RoundImage()).into(imagencircular);


            }
        }

    }

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_perfil_autor, menu);
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
