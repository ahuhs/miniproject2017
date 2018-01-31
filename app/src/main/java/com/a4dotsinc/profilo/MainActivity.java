package com.a4dotsinc.profilo;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    Activity aaa = MainActivity.this;
    private DatabaseReference mDatabase;
    private final int REQUEST_CODE_PLACEPICKER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("LocData").child("TestUser");
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
                        //    Intent tomap = new Intent(MainActivity.this, MapsActivity.class);
                         //   startActivity(tomap);
                            startPlacePickerActivity();
                        }
                    });
                } else if (id == R.id.item_two) {

                    fragment = new TimerFrag();
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));
                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent toadd = new Intent(MainActivity.this, Timepic_Activity.class);
                            startActivity(toadd);
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

            private void startPlacePickerActivity() {

                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

                try {
                    Intent intent = intentBuilder.build(aaa);
                    startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
                } catch (Exception e){
                    e.printStackTrace();
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_PLACEPICKER &&  resultCode == RESULT_OK){
            displaySelectedPlaceFromPlacePicker(data);
        }
    }

    private void displaySelectedPlaceFromPlacePicker(Intent data) {

        Place placeselect = PlacePicker.getPlace(data, this);
        double lat = placeselect.getLatLng().latitude;
        double lon = placeselect.getLatLng().longitude;
        //double latlonBound = placeselect.getViewport().
        final String lat_val = String.valueOf(lat);
        final String lon_val = String.valueOf(lon);
        final String name = placeselect.getAddress().toString();
        if (!TextUtils.isEmpty(lat_val) && !TextUtils.isEmpty(lon_val)){
            DatabaseReference newPost = mDatabase.push();
            newPost.child("lat").setValue(lat_val);
            newPost.child("lon").setValue(lon_val);
            newPost.child("name").setValue(name);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minu) {

    }

    private void select() {
        Fragment f = null;
        f = new MapFrag();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.theframe, f);
        ft.commit();
    }
}
