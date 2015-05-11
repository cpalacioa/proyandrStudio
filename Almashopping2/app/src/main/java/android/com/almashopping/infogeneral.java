package android.com.almashopping;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class infogeneral extends Fragment {


    public infogeneral() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_infogeneral, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        TextView textInfo=(TextView)getView().findViewById(R.id.strInfogral);
        textInfo.setText(Html.fromHtml(getString(R.string.info_html)));
        textInfo.setMovementMethod(new ScrollingMovementMethod());
    }


}
