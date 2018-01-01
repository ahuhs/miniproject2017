package com.a4dotsinc.profilo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        select();
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_tabs);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int id = item.getItemId();

                if (id == R.id.item_one) {
                    fragment = new MapFrag();
                } else if (id == R.id.item_two) {

                    fragment = new TimerFrag();
                } else if (id == R.id.item_three) {

                    fragment = new WishlistFrag();
                } else if (id == R.id.item_four) {

                    fragment = new SettingsFrag();
                }

                if (fragment != null){
                    FragmentTransaction ft  =  getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.theframe, fragment);
                    ft.commit();
                }
                return true;
            }
        });
    }

    private void select() {
        Fragment f = null;
        f = new MapFrag();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.theframe, f);
        ft.commit();
    }
}
