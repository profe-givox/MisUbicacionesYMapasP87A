package net.ivanvega.misubicacionesymapasp87a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    LocationRequest     locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    LocationSettingsRequest.Builder builder;
    TextView txt ;
    private boolean requestingLocationUpdates=true;

    public void click(View v){
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                // Update UI with location data
                // ...
                String msj = "Latitud: "+ String.valueOf(location.getLatitude())
                        + "\nLongitud: " + location.getLongitude();
                Toast.makeText(MainActivity.this, msj,
                        Toast.LENGTH_LONG).show();
                Log.i("POSICION", msj);
                txt.setText(msj);


            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txt);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            String msj = "Latitud: "+ String.valueOf(location.getLatitude())
                                    + "\nLongitud: " + location.getLongitude();
                            Toast.makeText(MainActivity.this, msj,
                                    Toast.LENGTH_LONG).show();
                            Log.i("MiUbi", msj);
                        }else {Log.i("MiUbi", "Sin ubicaciòn ");}

                    }
                });



        createLocationRequest();

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                Log.i("PRUEBA", locationSettingsResponse.toString());
                Toast.makeText(MainActivity.this, locationSettingsResponse.toString(),
                        Toast.LENGTH_SHORT).show(

                );
                LocationSettingsStates locationSettingsStates = locationSettingsResponse.getLocationSettingsStates();
                locationSettingsStates.isBlePresent();
                locationSettingsStates.isBleUsable();
                locationSettingsStates.isGpsPresent();
                locationSettingsStates.isGpsUsable();
                locationSettingsStates.isLocationUsable();
                locationSettingsStates.isNetworkLocationPresent();

                requestingLocationUpdates = true;

            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == this.REQUEST_CHECK_SETTINGS){
            Log.i("PRUEBA", String.valueOf(resultCode));
            Toast.makeText(this, String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
            if(resultCode == RESULT_OK){
                this.requestingLocationUpdates = true;
            }
        }
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onResume() {
        super.onResume();
       if (requestingLocationUpdates) {
            startLocationUpdates();
       }
    }

    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }


}
