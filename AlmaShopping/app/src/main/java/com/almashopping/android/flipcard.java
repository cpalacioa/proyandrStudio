package com.almashopping.android;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.andtinder.Model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class flipcard extends ActionBarActivity {

    private CardContainer mCardContainer;
    List<Producto> productos;
    int Id;
    SimpleCardStackAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_flipcard);
        Bundle bundle = this.getIntent().getExtras();
        Id=bundle.getInt("id");

        mCardContainer = (CardContainer) findViewById(R.id.layoutview);
        TareaWSListar listar=new TareaWSListar();
        listar.execute();

       /* CardModel cardModel = new CardModel("Title1", "Description goes here", "http://cdn2.androidhive.info/wp-content/plugins/wordpress-23-related-posts-plugin/static/thumbs/7.jpg?f8d261");

        cardModel.setOnClickListener(new CardModel.OnClickListener() {
            @Override
            public void OnClickListener() {
                Log.i("Swipeable Cards", "estoy presionando una imagen");
            }
        });*/

        /*cardModel.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
            @Override
            public void onLike() {
                Log.i("Swipeable Cards","I like the card");
            }

            @Override
            public void onDislike() {
                Log.i("Swipeable Cards","I dislike the card");
            }
        });*/

        //adapter.add(cardModel);


    }

  private void FlipCardDraw(List<Producto> productos)
  {
      adapter=new SimpleCardStackAdapter(flipcard.this.getApplicationContext());
                for(int contProd=0;contProd<productos.size();contProd++) {
                    Producto prod=(Producto)productos.get(contProd);
                    adapter.add(new CardModel(prod.titulo, prod.descripcion, prod.img_url));

                }
      mCardContainer.setAdapter(adapter);


  }
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =new HttpGet("http://www.almashopping.com/es/site/getproductsbybrand/?key=4e3ec11f9cd0ed11090dbd0b1c8ff9ee&id="+Id+"");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                productos=new ArrayList();
                JSONArray Jarray = new JSONArray(respStr);
                for(int i=0; i<Jarray.length(); i++)
                {
                    JSONObject obj = Jarray.getJSONObject(i);
                    int id = obj.getInt("pid");
                    String nombre=obj.getString("name");
                    String thumb=obj.getString("images");
                    String precio=obj.getString("price");
                    String marca=obj.getString("brand");
                    String[] fotos=thumb.split(":");
                    String imagentratada=fotos[0].replace("{","");
                    String descripcion=obj.getString("description");

                    imagentratada=imagentratada.replaceAll("\"", "");
                    String photo="http://www.almashopping.com/images/products/1/"+imagentratada+"";
                    Producto cate=new Producto(id,nombre,photo,precio,marca,descripcion);
                    productos.add(cate);
                }

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;

            }
            return  resul;
        }

        protected void onPostExecute(Boolean result) {

            if(result) {
                FlipCardDraw(productos);

            }

/*                for(int contProd=0;contProd<productos.size();contProd++) {
                    Producto prod=productos.get(contProd);

                    adapter.add(new CardModel(prod.marca, prod.titulo, prod.img_url));
                }
                mCardContainer.setAdapter(adapter);
*/
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flipcard, menu);
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
