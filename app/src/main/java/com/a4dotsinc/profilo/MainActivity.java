package com.a4dotsinc.profilo;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        ImageButton imgbtn = (ImageButton)findViewById(R.id.login_img_btn);
        select();
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_tabs);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int id = item.getItemId();

                if (id == R.id.item_one) {
                    fragment = new MapFrag();
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_place_add));
                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent tomap = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(tomap);
                        }
                    });
                } else if (id == R.id.item_two) {

                    fragment = new TimerFrag();
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));
                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogFragment timepicker = new TimePickerFrag();
                            timepicker.show(getSupportFragmentManager(), "time picker");
                        }
                    });
                } else if (id == R.id.item_three) {

                    fragment = new WishlistFrag();
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));
                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                } else if (id == R.id.item_four) {

                    fragment = new SettingsFrag();
                    floatingActionButton.setVisibility(View.GONE);
                }

                if (fragment != null){
                    FragmentTransaction ft  =  getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.theframe, fragment);
                    ft.commit();
                }
                return true;
            }
        });
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "#SOON", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minu) {
        TextView t = (TextView)findViewById(R.id.timetext);
        t.setText("Hour :"+hour+" Minute :"+minu);
    }

    private void select() {
        Fragment f = null;
        f = new MapFrag();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.theframe, f);
        ft.commit();
    }
}
