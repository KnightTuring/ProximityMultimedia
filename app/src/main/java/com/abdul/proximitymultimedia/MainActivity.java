package com.abdul.proximitymultimedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView textViewCurrentLocation;
    TextView textViewLogger;

    private Location location;
    private GoogleApiClient googleApiClient = null;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 2000, FASTEST_INTERVAL = 5000;

    private LocationCalculator locationCalculator;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private static final int ALL_PERMISSIONS_RESULT = 1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCurrentLocation = findViewById(R.id.textViewCurrentLocation);
        textViewCurrentLocation.setTypeface(null, Typeface.BOLD);
        textViewLogger = findViewById(R.id.textViewLocationLog);
        textViewLogger.setMovementMethod(new ScrollingMovementMethod());
        Button buttonLocation = findViewById(R.id.buttonExit);
        locationCalculator = new LocationCalculator();
        textViewLogger.append("\nLogging begins\n");

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.i("Tag", "in onCreate() of main activity");
        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        Log.i("Tag", "request permissions done");
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });

        Log.i("Tag", "Starting proximity service");
        Intent proximityIntent = new Intent(this, ProximityService.class);
        startService(proximityIntent);
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> permissions) {
        ArrayList<String> result = new ArrayList<>();

        for(String perm: permissions) {
            if(!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String perm) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!checkPlayServices()) {
            // install google play services
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this );

        if (resultCode != ConnectionResult.SUCCESS) {
            if(apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }
            return false;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm: permissionsToRequest) {
                    if(!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }
                if(permissionsRejected.size() > 0) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            (new AlertDialog.Builder(MainActivity.this))
                                    .setMessage("These permissions are mandatory to get your location, You need to allow them.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String [permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .create()
                                    .show();
                        }
                    }
                } else {
                    Intent objIntent = new Intent(this, ProximityService.class);
                    startService(objIntent);
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
