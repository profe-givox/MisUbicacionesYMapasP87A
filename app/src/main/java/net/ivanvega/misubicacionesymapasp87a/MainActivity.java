package net.ivanvega.misubicacionesymapasp87a;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }   
}
