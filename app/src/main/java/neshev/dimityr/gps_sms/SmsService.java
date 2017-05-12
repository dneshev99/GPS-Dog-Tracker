package neshev.dimityr.gps_sms;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;

import com.google.android.gms.maps.model.LatLng;


public class SmsService extends Service {

    private BroadcastReceiver receiver = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                proccessSmS(intent);
            }
        };
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void proccessSmS(Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] smses = (Object[])bundle.get("pdus");
        for (int i=0;i<smses.length;i++){
            SmsMessage sms = SmsMessage.createFromPdu((byte[])smses[i]);
            String body = sms.getMessageBody();
            if(body.contains("location:")) {
                sendData(body);
            }
        }
    }

    private void sendData(String body) {
        String[] tokens = body.split(" ");
        LatLng location = new LatLng(Float.parseFloat(tokens[1]),Float.parseFloat(tokens[2]));
        Intent message = new Intent("locationUpdates");
        message.putExtra("location",body);
        sendBroadcast(message);
    }
}
