package com.almashopping.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class detalleMarca extends Activity{

    private ImageView coverMarca;
    private TextView descripcionMarca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalle_marca);

        descripcionMarca=(TextView)findViewById(R.id.txtDescripcionMarca);
        coverMarca=(ImageView)findViewById(R.id.CoverMarcas);
        Bundle bundle = this.getIntent().getExtras();
        descripcionMarca.setText(Html.fromHtml(bundle.getString("descripcion")), TextView.BufferType.SPANNABLE);
        String cover="http://www.almashopping.com/images/brand/1/"+bundle.getString("cover");
        Log.d("cover", cover);

        Picasso.with(detalleMarca.this.getApplicationContext())
                .load(cover)
                .placeholder(R.drawable.ic_action_business)
                .into(coverMarca);


        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 100;
        params.width = 550;
        params.y = -10;

        this.getWindow().setAttributes(params);*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_marca, menu);
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
