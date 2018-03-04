package com.a4dotsinc.profilo;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.kmenager.materialanimatedswitch.MaterialAnimatedSwitch;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, SharedPreferences.OnSharedPreferenceChangeListener{

    //Foreground Location

    private static final String TAG = MainActivity.class.getSimpleName();

    // The BroadcastReceiver used to listen from broadcasts from the service.
    public MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    public LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    //End Foreground Location

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    public SwitchButton switchButton;
    public MaterialAnimatedSwitch materialAnimatedSwitch;

    Dialog mapDialog;

    Toolbar toolbar;

    SpaceTabLayout spaceTabLayout;
    Activity aaa = MainActivity.this;
    private DatabaseReference mDatabase, mFlagedLoc;
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
        mFlagedLoc = FirebaseDatabase.getInstance().getReference().child("FlagedLoc").child("TestUser");
        ImageButton imgbtn = (ImageButton)findViewById(R.id.login_img_btn);

        materialAnimatedSwitch = (MaterialAnimatedSwitch)findViewById(R.id.toggle_loc);

        myReceiver = new MyReceiver();
        mapDialog = new Dialog(this);

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
        spaceTabLayout.setTabFourIcon(R.drawable.ic_wlist);
        spaceTabLayout.setTabFiveIcon(R.drawable.ic_settings);


        spaceTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (spaceTabLayout.getCurrentPosition()){
                    case 0 :  startPlacePickerActivity();
                              break;
                    case 1 :   Intent toadd = new Intent(MainActivity.this, Timepic_Activity.class);
                               startActivity(toadd);
                               break;
                    case 2 :  Toast.makeText(MainActivity.this, Html.fromHtml("<big><b>coded </b></big><small><i>with </i></small>")+"❤", Toast.LENGTH_SHORT).show();

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
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


        //switchButton = (SwitchButton) findViewById(R.id.toggle_loc);


        //switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        //        if(isChecked){
        //            mService.requestLocationUpdates();
        //       }
        //        else {
        //            mService.removeLocationUpdates();
        //        }
        //    }
        //});
        materialAnimatedSwitch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)&& (!notificationManager.isNotificationPolicyAccessGranted())){
                    Intent intent = new Intent(
                            android.provider.Settings
                                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                    startActivity(intent);}
                if(isChecked){
                                mService.requestLocationUpdates();
                           }
                            else {
                                mService.removeLocationUpdates();
                            }
            }
        });

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
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
        //final String name = placeselect.getAddress().toString();
        final String MapUrl = "https://maps.googleapis.com/maps/api/staticmap?center="+lat_val+","+lon_val+"&zoom=15&size=400x120&scale=3" +
                "&markers=color:blue%7Clabel:S%7C"+lat_val+","+lon_val+"&key=AIzaSyBVRBgrGQqX3fkEfyV3pSX_keEJbaz7Oyc";
        if (!TextUtils.isEmpty(lat_val) && !TextUtils.isEmpty(lon_val)){
            showMapPop(lat_val, lon_val, MapUrl);
        }
    }

    private void showMapPop(final String lat_Val,final String lon_Val,final String mapUrl) {
        TextView close;
        Button save;
        final EditText map_name, map_radius;
        mapDialog.setContentView(R.layout.map_selection_pop_up);
        mapDialog.show();
        close = (TextView) mapDialog.findViewById(R.id.txtclose);
        save = (Button) mapDialog.findViewById(R.id.map_save_btn);
        map_name = (EditText) mapDialog.findViewById(R.id.map_name);
        map_radius = (EditText) mapDialog.findViewById(R.id.map_radius);
        ImageView map_view = (ImageView) mapDialog.findViewById(R.id.pop_map_img);

        Picasso.with(this).load(mapUrl).into(map_view);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapRecycler mapRecycler  = new MapRecycler(lat_Val, lon_Val, map_name.getText().toString(), Float.parseFloat(map_radius.getText().toString()), mapUrl, false);
                mDatabase.push().setValue(mapRecycler);
                mFlagedLoc.child("flag").setValue("0");
                mapDialog.dismiss();
            }
        });
        mapDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minu) {

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
               // Toast.makeText(MainActivity.this, Utils.getLocationText(location),Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }
    private void setButtonsState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            materialAnimatedSwitch.setEnabled(true);
        } else {
            materialAnimatedSwitch.setEnabled(false);
        }
    }
}
