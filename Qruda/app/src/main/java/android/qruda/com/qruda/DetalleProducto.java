package android.qruda.com.qruda;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DetalleProducto extends ActionBarActivity {
    TextView titulo;
    ImageView imagen;
    ActionBar mActionbar;
    TextView tituloBarra;
    ImageButton btnAtras;
    ImageButton btnShare;
    String tituloProducto;
    TextView txtDescripcion;
    Button btnComprar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        //Obtener Referencia a la actionbar
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayShowHomeEnabled(false);
        //displaying custom ActionBar

        View mActionBarView = getLayoutInflater().inflate(R.layout.customactionbar, null);
        mActionbar.setCustomView(mActionBarView);
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        tituloBarra=(TextView)this.findViewById(R.id.BarraTitulo);
        imagen=(ImageView)this.findViewById(R.id.DetalleImagen);
        btnAtras=(ImageButton)this.findViewById(R.id.btn_slide);
        btnShare=(ImageButton)this.findViewById(R.id.btn_Share);
        btnComprar=(Button)this.findViewById(R.id.btnComprar);
        Bundle bundle = this.getIntent().getExtras();
        tituloBarra.setText(bundle.getString("Titulo"));
        tituloProducto=bundle.getString("Titulo").toString();
        txtDescripcion=(TextView)this.findViewById(R.id.txtDescripcion);
        txtDescripcion.setText(
                Html.fromHtml("<p>Esto es una <b>Descricion</b>.</p>"),
                TextView.BufferType.SPANNABLE);
        Picasso.with(this)
                .load(bundle.getString("UrlImagen"))
                .placeholder(R.drawable.spinner) // optional
                .error(R.drawable.spinner)         // optional
                .into(imagen);

        btnAtras.setOnClickListener(clickBtnAtras);
        btnShare.setOnClickListener(shareListener);
        btnComprar.setOnClickListener(comprarListener);
    }

    OnClickListener comprarListener =new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
            Uri.parse("http://www.qruda.com/es/producto/body-piezas-terciopelo/?color=000000"));
            startActivity(intent);
        }
    };

    OnClickListener shareListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            Resources resources = getResources();
            Intent emailIntent = new Intent();
            emailIntent.setAction(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<a href='http://google.com'><b>"+tituloProducto+"</b>"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Qruda te invita");
            emailIntent.setType("message/rfc822");

            PackageManager pm = getPackageManager();
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");


            Intent openInChooser = Intent.createChooser(emailIntent,"texto de prueba");

            List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
            List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
            for (int i = 0; i < resInfo.size(); i++) {
                // Extract the label, append it, and repackage it in a LabeledIntent
                ResolveInfo ri = resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                if(packageName.contains("android.email")) {
                    emailIntent.setPackage(packageName);
                } else if(packageName.contains("twitter")  || packageName.contains("mms") || packageName.contains("android.gm")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    if(packageName.contains("twitter")) {
                        intent.putExtra(Intent.EXTRA_TEXT,"texto twitter");
                    } //else if(packageName.contains("facebook")) {
                        // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                        // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                        // will show the <meta content ="..."> text from that page with our link in Facebook.
                        //intent.putExtra(Intent.EXTRA_TEXT,"Texto Facebook");
                    //}
                    else if(packageName.contains("mms")) {
                        intent.putExtra(Intent.EXTRA_TEXT, "Texto sms");
                    } else if(packageName.contains("android.gm")) {
                        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Texto Gmail"));
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Asunto");
                        intent.setType("message/rfc822");
                    }

                    intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
                }
            }

            // convert intentList to array
            LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            startActivity(openInChooser);
        }
    };
    OnClickListener clickBtnAtras=new OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent i=new Intent(v.getContext(),MainActivity.class);
            //startActivity(i);
            finish();
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_detalle_producto, menu);

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
