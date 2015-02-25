package android.com.almashopping;

import android.app.ProgressDialog;
import android.com.almashopping.helpers.ShoppingSQLHelper;
import android.com.almashopping.model.Producto;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;



public class DetalleProducto extends ActionBarActivity {

    ImageView imageProduct;
    TextView  brandTitle;
    TextView  productTitle;
    TextView  precio;
    Producto  producto;
    TextView  description;
    TextView  cantidad;
    ImageButton btnAdd;
    ImageButton btnRemove;
    Button btnAgregar;
    ProgressDialog connectionProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setProgressStyle(R.style.Theme_Alma_ProgressDialog);
        connectionProgressDialog.show();
        connectionProgressDialog.setContentView(R.layout.loading);

        setContentView(R.layout.activity_detalle_producto);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageProduct=(ImageView)findViewById(R.id.imageProduct);
        brandTitle=(TextView)findViewById(R.id.brandTitle);
        productTitle=(TextView)findViewById(R.id.titleProduct);
        precio=(TextView)findViewById(R.id.price);
        description=(TextView)findViewById(R.id.descriptionProduct);
        cantidad=(TextView)findViewById(R.id.cantidadAdd);
        btnAdd=(ImageButton)findViewById(R.id.btnShopAdd);
        btnRemove=(ImageButton)findViewById(R.id.btnShopRemove);
        btnAgregar=(Button)findViewById(R.id.btnAgregarCart);
        btnAdd.setOnClickListener(AdicionarCantidad);
        btnRemove.setOnClickListener(SubstraerCantidad);
        btnAgregar.setOnClickListener(AgregarCarrito);

        Bundle bundle = this.getIntent().getExtras();
        int pid=bundle.getInt("Id");
        if(pid>0) {
        TasObtInfoProducto tarea=new TasObtInfoProducto();
            tarea.execute(Integer.toString(pid));
        }

    }



    public  void GraficarProducto(Producto producto)
    {
        Picasso.with(this)
                .load(producto.img_url)
                .placeholder(R.drawable.ic_action_user)
                .error(R.drawable.ic_action_user)
                .into(imageProduct);
        brandTitle.setText(producto.marca);
        productTitle.setText(producto.titulo);
        precio.setText("$ "+producto.valor+"");
        description.setText(Html.fromHtml(producto.descripcion),TextView.BufferType.SPANNABLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==android.R.id.home) {
            this.finish();
            return true;
        }
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

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener AdicionarCantidad=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           int cant=Integer.parseInt(cantidad.getText().toString());
            cant=cant+1;
            cantidad.setText(Integer.toString(cant));
        }
    };

    View.OnClickListener SubstraerCantidad=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cant=Integer.parseInt(cantidad.getText().toString());
            if(cant>0)
            cant=cant-1;

            cantidad.setText(Integer.toString(cant));

        }
    };
    View.OnClickListener AgregarCarrito=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int cantida=Integer.parseInt(cantidad.getText().toString());
            if(cantida>0)
                InsertarCarrito(producto,cantida);
            else
                Toast.makeText(DetalleProducto.this.getApplicationContext(),"Debe agregar una cantidad",Toast.LENGTH_LONG).show();
        }
    };

    public  void InsertarCarrito(Producto producto,int cantidadxprod)
    {
        try
        {
            ShoppingSQLHelper dbconnect=new ShoppingSQLHelper(DetalleProducto.this.getApplicationContext(),"DBAlma",null,1);
            SQLiteDatabase db=dbconnect.getWritableDatabase();
            if(db!=null) {
                Log.d("db", "abrio conexion");
                db.execSQL("INSERT INTO cartshop (IdProducto, Titulo,Descripcion,marca,valor,imagen,cantidad) " +
                        "VALUES (" + producto.id + ", '" + producto.titulo + "','" + producto.descripcion + "','" + producto.marca + "'," + producto.valor + ",'" + producto.img_url + "'," + cantidadxprod + ")");
                Toast.makeText(DetalleProducto.this.getApplicationContext(), "Se adiciono al carro de compras", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(DetalleProducto.this.getApplicationContext(),"No se pudo agregar al carro de compras bd",Toast.LENGTH_SHORT).show();

            db.close();
        }
        catch (Exception e)
        {
            Toast.makeText(DetalleProducto.this.getApplicationContext(),"No se pudo agregar al carro de compras",Toast.LENGTH_SHORT);
        }
    }
    private class TasObtInfoProducto extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getproductbyid/?key=851d92530079828cea8133eef93554fe&pid="+params[0]+"");
            Log.d("url",del.toString());
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray Jarray = new JSONArray(respStr);
                JSONObject obj = Jarray.getJSONObject(0);
                if(obj!=null) {

                    // producto = new Producto()
                    Integer pid=obj.getInt("pid");
                    String titulo=obj.getString("name");
                    String thumb=obj.getString("images");
                    String[] fotos=thumb.split(":");
                    String imagentratada=fotos[0].replace("{","");
                    imagentratada=imagentratada.replaceAll("\"", "");
                    String photo="http://www.almashopping.com/images/products/1/"+imagentratada+"";
                    String valor=obj.getString("price");
                    String marca=obj.getString("brand");
                    String description=obj.getString("description");
                    producto=new Producto(pid,titulo,photo,valor,marca,description);

                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //pintar producto
                //setTitle(producto.titulo);
                connectionProgressDialog.dismiss();
                GraficarProducto(producto);
            }
        }
    }
    private void InfoGeneral(String url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
