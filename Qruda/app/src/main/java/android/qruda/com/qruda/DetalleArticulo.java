package android.qruda.com.qruda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class DetalleArticulo extends ActionBarActivity {

    private ActionBar mActionbar;
    private WebView contenido;
    private ImageView imagen;
    private ProgressDialog mProgressDialog;
    private String url;
    private ImageButton btnAtras;
    private TextView titulo;
    private Button btnPerfilAutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulo);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayShowHomeEnabled(false);
        //displaying custom ActionBar
        View mActionBarView = getLayoutInflater().inflate(R.layout.actionbar_articulo, null);
        mActionbar.setCustomView(mActionBarView);
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        imagen=(ImageView)this.findViewById(R.id.DetalleImagen);
        btnAtras=(ImageButton)this.findViewById(R.id.btn_atras_articulo);
        btnPerfilAutor=(Button)this.findViewById(R.id.btnPerfilAutor);

        titulo=(TextView)this.findViewById(R.id.tituloArticulo);
        //imagen=(ImageView)findViewById(R.id.DetalleImagenArticulo);

        Bundle bundle = this.getIntent().getExtras();
        Log.d("link",bundle.getString("UrlImagen"));
        url=bundle.getString("Url");
        titulo.setText(bundle.getString("Nombre"));
        /*Picasso.with(this)
                .load(bundle.getString("UrlImagen"))
                .placeholder(R.drawable.spinner) // optional
                .error(R.drawable.spinner)         // optional
                .into(imagen);*/
        new Description().execute();
        btnAtras.setOnClickListener(listenerAtras);
        btnPerfilAutor.setOnClickListener((listenerPerfilAutor));

    }

    View.OnClickListener listenerPerfilAutor=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i =new Intent(DetalleArticulo.this,PerfilAutor.class);
            startActivity(i);
        }
    };

    View.OnClickListener listenerAtras=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_detalle_articulo, menu);
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

    //Obtener un html
    // Description AsyncTask
    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DetalleArticulo.this);
            mProgressDialog.setTitle("Obteniendo articulo");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Log.d("Link",url);
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
            contenido=(WebView)findViewById(R.id.webViewArticulo);
            WebSettings webSettings = contenido.getSettings();
            webSettings.setJavaScriptEnabled(true);
            contenido.loadDataWithBaseURL(null, desc, "text/html", "UTF-8", null);
            //myWebView.loadData(desc,"text/html; charset=utf-8","UTF-8");
            mProgressDialog.dismiss();
        }
    }

}
