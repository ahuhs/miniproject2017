package com.a4dotsinc.profilo;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    Toolbar toolbar;
    SpaceTabLayout spaceTabLayout;
    Activity aaa = MainActivity.this;
    private DatabaseReference mDatabase;
    private final int REQUEST_CODE_PLACEPICKER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        ActivityCompat.requestPermissions(MainActivity.this, new  String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("LocData").child("TestUser");
        ImageButton imgbtn = (ImageButton)findViewById(R.id.login_img_btn);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MapFrag());
        fragmentList.add(new TimerFrag());
        fragmentList.add(new WelcomeFrag());
        fragmentList.add(new WishlistFrag());
        fragmentList.add(new SettingsFrag());

        ViewPager viewPager = (ViewPager) findViewById(R.id.content_viewer);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainRelative);
        spaceTabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        spaceTabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);

        spaceTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (spaceTabLayout.getCurrentPosition()){
                    case 0 :  startPlacePickerActivity();
                              break;
                    case 1 :   Intent toadd = new Intent(MainActivity.this, Timepic_Activity.class);
                               startActivity(toadd);
                               break;
                    case 2 :

                    case 3 :

                    case 4 :

                    default: break;
                }


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

}
