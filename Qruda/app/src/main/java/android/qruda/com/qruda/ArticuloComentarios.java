package android.qruda.com.qruda;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class ArticuloComentarios extends ActionBarActivity {

    ActionBar mActionbar;
    Button btnArticulo;
    ImageButton btnAtras;
    Button  btnAutor;

    //Datos del articulo
    int IdArticulo;
    String UrlImagen;
    String Nombre;
    String Fecha;
    String Url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo_comentarios);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayShowHomeEnabled(false);
        //displaying custom ActionBar
        View mActionBarView = getLayoutInflater().inflate(R.layout.actionbar_articulo, null);
        mActionbar.setCustomView(mActionBarView);
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Bundle bundle = this.getIntent().getExtras();
        Url=bundle.getString("Url");
        IdArticulo=bundle.getInt("Id");
        UrlImagen=bundle.getString("UrlImagen");
        Nombre=bundle.getString("Nombre");
        Fecha=bundle.getString("Fecha");

        btnArticulo=(Button)findViewById(R.id.btnArticuloComentarios);
        btnAtras=(ImageButton)findViewById(R.id.btn_atras_articulo);
        btnAutor=(Button)findViewById(R.id.btnPerfilComentarios);
        btnAtras.setOnClickListener(listenerAtras);
        btnArticulo.setOnClickListener(listenerBtnArticulo);
        btnAutor.setOnClickListener(listenerBtnAutor);

    }

    View.OnClickListener listenerBtnArticulo=new View.OnClickListener()
    {
        @Override
        public  void  onClick(View v){
            Intent intent=new Intent(ArticuloComentarios.this.getApplicationContext(),DetalleArticulo.class);
            intent.putExtra("Id",IdArticulo);
            intent.putExtra("UrlImagen",UrlImagen);
            intent.putExtra("Nombre",Nombre);
            intent.putExtra("Fecha",Fecha);
            intent.putExtra("Url",Url);
            startActivity(intent);

        }
    };
    View.OnClickListener listenerBtnAutor=new View.OnClickListener()
    {
        @Override
        public  void  onClick(View v){
            Intent intent=new Intent(ArticuloComentarios.this.getApplicationContext(),PerfilAutor.class);
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
            Intent intent=new Intent(ArticuloComentarios.this,Comunidad.class);
            startActivity(intent);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_articulo_comentarios, menu);
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
