package ennova.democtrlseleccion;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Spinner cmbOpciones;
    private TextView lblMensaje;
    private ListView lstOpciones;
    private ListView lstContactos;
    private Button btnDetalle;

    Calendar calendario = GregorianCalendar.getInstance();
    private Contacto[] datos = new Contacto[]{
        new Contacto("Mauricio","Aprendiendo",calendario.getTime()),
        new Contacto("Lorena","Trabajando",calendario.getTime()),
        new Contacto("Pepe","Jugando",calendario.getTime()),
        new Contacto("Lina","Trabajando",calendario.getTime()),
        new Contacto("Lorena","Aprendiendo",calendario.getTime()),
        new Contacto("Claudia","Aprendiendo",calendario.getTime()),
        new Contacto("Jhon","Aprendiendo",calendario.getTime()),
        new Contacto("Fredy","Aprendiendo",calendario.getTime()),
        new Contacto("Nitrox","Molestando",calendario.getTime()),
        new Contacto("Cat","Acosando",calendario.getTime())

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblMensaje = (TextView)findViewById(R.id.LblMensaje);
        cmbOpciones = (Spinner)findViewById(R.id.CmbOpciones);
        lstContactos=(ListView)findViewById(R.id.listContactos);
        final String[] datos = new String[]{"Enero","Febrero","Marzo","Abril","Mayo"};

        //Alternativa 1: Array java
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, datos);
        //Alternativa 2: Recurso XML de tipo string-array
        ArrayAdapter<CharSequence> adaptador2 =
        	    ArrayAdapter.createFromResource(this,
        	        R.array.valores_array, android.R.layout.simple_spinner_item);


        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        adaptador2.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        AdaptadorContactos adaptador3=new AdaptadorContactos(this);

        cmbOpciones.setAdapter(adaptador2);
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        lstOpciones.setAdapter(adaptador);
        lstContactos.setAdapter(adaptador3);

        cmbOpciones.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        lblMensaje.setText("Seleccionado: " + datos[position]);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        lblMensaje.setText("");
                    }
                });
        //Interaccion para el boton
        btnDetalle=(Button)findViewById(R.id.btnDetalle);
        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent =
                        new Intent(MainActivity.this, DemoGrid.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
    /*    if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
*/
        switch(item.getItemId())
        {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    class AdaptadorContactos extends ArrayAdapter<Contacto> {

        Activity context;

        AdaptadorContactos(Activity context) {
            super(context, R.layout.listitem_contacto, datos);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            View item = convertView;

            if(item == null)
            {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_contacto, null);
            }

            TextView lblNombre = (TextView)item.findViewById(R.id.LblNombre);
            lblNombre.setText(datos[position].GetNombre());

            TextView lblTexto = (TextView)item.findViewById(R.id.LblTexto);
            lblTexto.setText(datos[position].GetTexto());

            TextView lblFecha=(TextView)item.findViewById(R.id.LblFecha);
            lblFecha.setText(datos[position].getFecha().toString());

            return(item);
        }
        public View getViewOld(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.listitem_contacto, null);

            TextView lblNombre = (TextView)item.findViewById(R.id.LblNombre);
            lblNombre.setText(datos[position].GetNombre());

            TextView lblTexto = (TextView)item.findViewById(R.id.LblTexto);
            lblTexto.setText(datos[position].GetTexto());

            TextView lblFecha=(TextView)item.findViewById(R.id.LblFecha);
            lblFecha.setText(datos[position].getFecha().toString());

            return(item);
        }
    }
}
