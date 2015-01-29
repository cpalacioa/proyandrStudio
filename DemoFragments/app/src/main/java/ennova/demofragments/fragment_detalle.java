package ennova.demofragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class fragment_detalle extends Fragment {

    public fragment_detalle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_detalle, container, false);

    }

    public void mostrarDetalle(String texto) {
        TextView txtDetalle =
                (TextView)getView().findViewById(R.id.TxtDetalle);

        txtDetalle.setText(texto);
    }


}
