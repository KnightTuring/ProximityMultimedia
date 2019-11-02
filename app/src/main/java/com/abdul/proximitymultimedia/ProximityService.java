package com.abdul.proximitymultimedia;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


public class ProximityService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Location location;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 2000, FASTEST_INTERVAL = 5000;

    LocationCalculator locationCalculator;

    public void onCreate() {

        Log.i("Tag", "onCreate() of proximity service");
        // build googleAPIClient
        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        locationCalculator = new LocationCalculator();
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("Tag", "ProximityLocation service started");
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            //textViewCurrentLocation.setText("Lattitude: "+location.getLatitude() + " Longitude: "+location.getLongitude());
            playMedia(location);
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location", Toast.LENGTH_SHORT);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            //textViewCurrentLocation.setText("Lattitude: "+location.getLatitude() + " Longitude: "+location.getLongitude());
            playMedia(location);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void playMedia(Location location) {
        String locationIdentifier = locationCalculator.getShortestDistance(location);
        Intent objIntent = new Intent(this, AudioPlayer.class);
        Log.i("Tag", "Location identifier: "+ locationIdentifier);
        if (locationIdentifier == null) {
            // nothing to do
            //textViewLogger.append("Not within 800m of any predefined location, no audio will be played\n");
            stopService(objIntent);
            return;
        } else {
            // play media
            Log.i("Tag", "Playing media");
            //textViewLogger.append("Within 800m of "+locationIdentifier+ ", playing audio\n");
            objIntent.putExtra("identifier", locationIdentifier);
            startService(objIntent);
        }
    }

    public void onDestroy() {
        if (googleApiClient != null & googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (com.google.android.gms.location.LocationListener) this);
            googleApiClient.disconnect();
        }
    }

}
