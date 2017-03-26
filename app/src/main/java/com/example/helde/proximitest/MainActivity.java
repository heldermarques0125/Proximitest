package com.example.helde.proximitest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import io.proximi.proximiiolibrary.ProximiioFloor;
import io.proximi.proximiiolibrary.ProximiioGoogleMapHelper;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MainActivityListener listener;
    @Nullable
    private ProximiioGoogleMapHelper mapHelper;
    private Toolbar toolbar;

    private final static String TAG = "ProximiioDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create our Proximi.io listener
        listener = new MainActivityListener(this);

        // Set toolbar buttons to change the current floor up and down
        findViewById(R.id.floorUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapHelper != null) {
                    mapHelper.floorUp();
                }
            }
        });
        findViewById(R.id.floorDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapHelper != null) {
                    mapHelper.floorDown();
                }
            }
        });

        // Set the toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(TAG);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapHelper != null) {
            mapHelper.destroy();
        }
        listener.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listener.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        listener.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Called when the map is ready to use
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapHelper = new ProximiioGoogleMapHelper.Builder(this, googleMap)
                .listener(new ProximiioGoogleMapHelper.Listener() {
                    @Override
                    public void changedFloor(@Nullable ProximiioFloor floor) {
                        toolbar.setTitle(floor != null ? floor.getName() : TAG);
                    }
                }).build();

        googleMap.setOnMyLocationButtonClickListener(mapHelper);
        googleMap.setOnMapClickListener(mapHelper);
        googleMap.setOnCameraIdleListener(mapHelper);
        googleMap.setOnMarkerClickListener(mapHelper);
        googleMap.setOnCameraMoveStartedListener(mapHelper);
    }
}
