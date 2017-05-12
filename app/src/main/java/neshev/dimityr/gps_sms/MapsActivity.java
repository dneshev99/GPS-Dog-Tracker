package neshev.dimityr.gps_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private BroadcastReceiver receiver = null;
    private Marker dogMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemaps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        Intent startSmsActivity = new Intent(getApplicationContext(),SmsService.class);
        startService(startSmsActivity);

        IntentFilter filter = new IntentFilter("locationUpdates");

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String body = intent.getExtras().get("location").toString();
                String[] tokens = body.split(" ");
                LatLng location = new LatLng(Float.parseFloat(tokens[1]),Float.parseFloat(tokens[2]));
                updateDogMarker(location);
            }
        };
        registerReceiver(receiver, filter);
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Intent startSmsActivity = new Intent(getApplicationContext(),SmsService.class);
        stopService(startSmsActivity);
    }


    private void updateDogMarker(LatLng location) {
        if (dogMarker != null){
            dogMarker.remove();
        }
        MarkerOptions options = new MarkerOptions().position(location);
        dogMarker = mMap.addMarker(options);
    }

}
