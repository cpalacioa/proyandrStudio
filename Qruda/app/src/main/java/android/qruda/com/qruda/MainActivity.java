package android.qruda.com.qruda;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    ActionBar mActionbar;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setiar logo en el action bar

        //Obtener Referencia a la actionbar
        mActionbar = getSupportActionBar();
        mActionbar.setIcon(R.drawable.ic_launcher);
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        mActionbar.setDisplayShowTitleEnabled(false);
        mActionbar.setTitle("  QRUDA COLECTIVO");

        mPager = (ViewPager) findViewById(R.id.pager);


        /** Set tab navigation mode */
        mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        FragmentManager fm = getSupportFragmentManager();
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionbar.setSelectedNavigationItem(position);

            }
        };

        mPager.setOnPageChangeListener(pageChangeListener);

        ViewPager.OnPageChangeListener cambioPagina= new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Tab tab=(Tab)mActionbar.getTabAt(position);
               mActionbar.selectTab(tab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        /** Creating an instance of FragmentPagerAdapter */
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(fm);

        /** Setting the FragmentPagerAdapter object to the viewPager object */
        mPager.setAdapter(fragmentPagerAdapter);
        /** Defining tab listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
              //  mPager.setCurrentItem(tab.getPosition());

            }

        };
        /** Creating fragment1 Tab */
        Tab tab = mActionbar.newTab()
                .setText("Hombres")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);

        /** Creating fragment2 Tab */
        tab = mActionbar.newTab()
                .setText("Mujeres")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);

        tab=mActionbar.newTab()
                .setText("Hogar")
                .setTabListener(tabListener);
        mActionbar.addTab(tab);

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
