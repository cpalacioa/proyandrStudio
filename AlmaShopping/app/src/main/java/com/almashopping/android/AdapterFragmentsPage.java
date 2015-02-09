package com.almashopping.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class AdapterFragmentsPage extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;

    public AdapterFragmentsPage(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch (arg0) {

            case 0:
                CatalogoInicio fragmento1 = new CatalogoInicio();
                return fragmento1;

            case 1:
                Comunidad fragmento2= new Comunidad();
                return fragmento2;

            case 2:
                Marcas fragmento3 = new Marcas();
                return fragmento3;


        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}