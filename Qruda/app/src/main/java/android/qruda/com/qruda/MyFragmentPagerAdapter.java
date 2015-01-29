package android.qruda.com.qruda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 3;

    /** Constructor of the class */
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){

            /** tab1 is selected */
            case 0:
                CatalogoHombres fragment1 = new CatalogoHombres();
                Log.d("Hombres","Hombres");

                return fragment1;

            /** tab2 is selected */
            case 1:
                CatalogoMujeres fragment2 = new CatalogoMujeres();
                return fragment2;

            case 2:
                CatalogoHogar fragment3 =new CatalogoHogar();

                return  fragment3;
        }

        return null;
    }

    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
