package ennova.demofragments;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ennova.demofragments.fragment_listado;


public class MainActivity extends FragmentActivity  implements fragment_listado.CorreosListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_listado frgListado
         = (fragment_listado)getSupportFragmentManager()
            .findFragmentById(R.id.FrgListado);

        frgListado.setCorreosListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //@Override
    public void onCorreoSeleccionado(Correo c) {
        boolean hayDetalle =
                (getSupportFragmentManager().findFragmentById(R.id.FrgDetalle) != null);

        if(hayDetalle) {
            ((fragment_detalle)getSupportFragmentManager()
                    .findFragmentById(R.id.FrgDetalle)).mostrarDetalle(c.getTexto());
        }
        else {
            Intent i = new Intent(this, activity_detalle.class);
            i.putExtra(activity_detalle.EXTRA_TEXTO, c.getTexto());
            startActivity(i);
        }
    }

}
