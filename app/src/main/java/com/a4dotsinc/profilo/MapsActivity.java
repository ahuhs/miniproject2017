package com.a4dotsinc.profilo;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.Slider;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public Boolean mLocationPermissionGranded = false;
    private static final int LOCATION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public AutoCompleteTextView search_text;
    private ImageButton getLoc, confirmLoc;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private Dialog mapDialog;
    private DatabaseReference mDatabase, mFlagedLoc;

    private GeoDataClient mGeoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );
    private String MapUrl = "";


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(mLocationPermissionGranded.toString(), "onMapReady: check");
        if (mLocationPermissionGranded) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        search_text = (AutoCompleteTextView) findViewById(R.id.map_search_text);
        getLoc = (ImageButton) findViewById(R.id.getCurrentloc);
        confirmLoc = (ImageButton)findViewById(R.id.location_tick);
        mapDialog = new Dialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("LocData").child("TestUser");
        mFlagedLoc = FirebaseDatabase.getInstance().getReference().child("FlagedLoc").child("TestUser");

        mGeoDataClient = Places.getGeoDataClient(this, null);

        getLocationPermissions();
        init();
    }

    public void init() {

        search_text.setOnItemClickListener(mAutocompleteClickListener);

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, null);

        search_text.setAdapter(placeAutocompleteAdapter);

        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    getLocation();
                    return true;
                }
                return false;
            }
        });

        getLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });

        hideKeyboard();
    }

    private void getLocation() {
        String search_string = search_text.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(search_string, 1);
        } catch (IOException e) {

        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(address.toString(), "getLocation: geolocation");
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15f, address.getAddressLine(0));
        }
    }

    private void getDeviceLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranded) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f, "My Location");
                        } else {
                            Toast.makeText(MapsActivity.this, "Unable to get Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }

    }

    private void moveCamera(final LatLng latLng, float zoom, final String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
            hideKeyboard();
        }

        confirmLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapUrl = "https://maps.googleapis.com/maps/api/staticmap?center="+latLng.latitude+","+latLng.longitude+"&zoom=15&size=500x220&scale=3" +
                        "&markers=color:blue%7Clabel:S%7C"+latLng.latitude+","+latLng.longitude+"&key=AIzaSyBVRBgrGQqX3fkEfyV3pSX_keEJbaz7Oyc";

                Log.i("MapsActivity", "Place details received: " + title);
                showMapPop(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), MapUrl);
            }
        });


    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranded = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranded = false;

        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranded = false;
                            return;
                        }
                    }
                    mLocationPermissionGranded = true;
                    //initialize Map
                    initMap();

                }

            }
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

/*
--------------------- Google Places Autocomplete API ----------------------------
 */

private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        hideKeyboard();

        final AutocompletePrediction item = placeAutocompleteAdapter.getItem(i);
        final String placeId = item.getPlaceId();

        Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
        placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

    }
};

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                // Format details of the place for display and show it in a TextView.
                moveCamera(place.getLatLng(), 15f, place.getName().toString());
                Toast.makeText(MapsActivity.this, place.getName().toString(), Toast.LENGTH_SHORT).show();

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e("MapsActivity", "Place query did not complete.", e);

            }
        }
    };

    private void showMapPop(final String lat_Val,final String lon_Val,final String mapUrl) {
        Button save, close;
        final EditText map_name;
        final Slider map_radius;
        mapDialog.setContentView(R.layout.map_selection_pop_up);
        mapDialog.show();
        save = (Button) mapDialog.findViewById(R.id.map_add);
        close = (Button) mapDialog.findViewById(R.id.map_close);
        map_name = (EditText) mapDialog.findViewById(R.id.map_name);
        map_radius = (Slider) mapDialog.findViewById(R.id.map_radius);
        CircleImageView map_view = (CircleImageView) mapDialog.findViewById(R.id.pop_map_img);

        Picasso.with(this).load(mapUrl).into(map_view);
        //map_view.setScaleType(ImageView.ScaleType.FIT_XY);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapRecycler mapRecycler  = new MapRecycler(lat_Val, lon_Val, map_name.getText().toString(), Float.parseFloat(String.valueOf(map_radius.getValue())), mapUrl, false);
                mDatabase.push().setValue(mapRecycler);
                mFlagedLoc.child("flag").setValue("0");
                mapDialog.dismiss();
                finish();
            }
        });
        mapDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}


