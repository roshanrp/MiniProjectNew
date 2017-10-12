package com.example.roshan.miniproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    String phoneNo, password, contact1, contact2;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (bundle == null)
            return;

        Object[] pdus = (Object[]) bundle.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage;
            if (Build.VERSION.SDK_INT < 23) {
                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            } else {
                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i], bundle.getString("format"));
            }
            String msgSender = smsMessage.getDisplayOriginatingAddress();
            String msgBody = smsMessage.getDisplayMessageBody();
            MyDBHandler myDBHandler = new MyDBHandler(context);
            try {

                password = myDBHandler.getPassword();
                contact1 = myDBHandler.getContact1();
                contact2 = myDBHandler.getContact2();

                if (msgSender.contains(contact1) | msgSender.contains(contact2)) {
                    Toast.makeText(context, "Woah", Toast.LENGTH_SHORT).show();

                    if (msgBody.equals(password)) {
                        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();

                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        final Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                        //r.play();

                    }
                }


            } catch (NullPointerException e) {
                Toast.makeText(context, (CharSequence) "Null Pointer Exception", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
