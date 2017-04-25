package neshev.dimityr.gps_sms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private BroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(checkIfAlreadyhavePermission()){
            Toast.makeText(this,"Already hame permissions",Toast.LENGTH_SHORT).show();
        }else {
                requestForSpecificPermission();
        }
    }
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permissions Granted",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,"Permissions Not Granted",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                proccessSmS(context, intent);
            }
        };
        registerReceiver(receiver, filter);
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    private void proccessSmS(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] smses = (Object[])bundle.get("pdus");
        for (int i=0;i<smses.length;i++){
            SmsMessage sms = SmsMessage.createFromPdu((byte[])smses[i]);
            String body = sms.getMessageBody();
            if(body.contains("location:")) {
                AddMarker(body);
            }
        }
    }

    private void AddMarker(String body) {
        String[] tokens = body.split(" ");
        float lat = Float.parseFloat(tokens[1]);
        float lng = Float.parseFloat(tokens[2]);
        Toast.makeText(this,"Lat:"+lat+"\n"+"Lng"+lng,Toast.LENGTH_LONG).show();
        LatLng location = new LatLng(lat,lng);
        MarkerOptions marker = new MarkerOptions().position(location);
        mMap.addMarker(marker);
    }
}
