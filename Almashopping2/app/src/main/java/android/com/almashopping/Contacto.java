package android.com.almashopping;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contacto extends Fragment {


    public Contacto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacto, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        TextView textcontac=(TextView)getView().findViewById(R.id.strContacto);
        textcontac.setText(Html.fromHtml(getString(R.string.contacto_html)));
    }


    }
