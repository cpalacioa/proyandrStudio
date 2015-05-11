package android.com.almashopping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class terminos extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terminos, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        TextView textTerm=(TextView)getView().findViewById(R.id.strTerminos);
        textTerm.setText(Html.fromHtml(getString(R.string.terminos_html)));
        textTerm.setMovementMethod(new ScrollingMovementMethod());
    }

}
