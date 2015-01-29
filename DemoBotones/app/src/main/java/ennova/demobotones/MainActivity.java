package ennova.demobotones;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.view.View;
import android.graphics.Color;
import android.text.Editable;
import android.text.style.StyleSpan;


public class MainActivity extends ActionBarActivity {
    private TextView labelClick;
    private Button btnBasico;
    private ToggleButton btnBoton2;
    private ImageButton btnBoton3;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      btnBasico=(Button)findViewById(R.id.btnBasico);
      btnBoton2 = (ToggleButton)findViewById(R.id.toggleButton);
      btnBoton3=(ImageButton)findViewById(R.id.imageButton);
      labelClick=(TextView)findViewById(R.id.labelClick);

      //Accion del boton basico
      btnBasico.setOnClickListener(new View.OnClickListener() {
          public void onClick(View arg0)
          {
              labelClick.setText("Boton 1 pulsado!");
          }
      });
      btnBoton2.setOnClickListener(new View.OnClickListener(){
          public void onClick(View arg0)
          {

              if(btnBoton2.isChecked())
                  labelClick.setText("Botón 2: ON");
              else
                  labelClick.setText("Botón 2: OFF");
          }
      });

      btnBoton3.setOnClickListener(new View.OnClickListener(){
          public  void onClick(View arg0)
          {
              labelClick.setText("boton de imagen cickeado");
              labelClick.setBackgroundColor(Color.BLUE);
          }

      });

      //Texto Editable
      //Creamos un nuevo objeto de tipo Editabl

      Editable str = Editable.Factory.getInstance().newEditable("Esto es un simulacro.");
       //Marcamos cono fuente negrita la palabra "simulacro" (caracteres del 11-19)
      str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 11, 19,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
