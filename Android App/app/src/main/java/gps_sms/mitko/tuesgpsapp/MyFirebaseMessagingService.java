package gps_sms.mitko.tuesgpsapp;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            String[] tokens = body.split(" ");

            if (tokens[0].equals("location:") && tokens.length == 3) {
                LatLng location = new LatLng(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
                Intent message = new Intent("locationUpdates");
                message.putExtra("location", location);
                sendBroadcast(message);
            }
        }

    }
}
