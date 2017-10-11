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

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

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

                String password = myDBHandler.getPassword();
                //Toast.makeText(context, "Hello " + password, Toast.LENGTH_SHORT).show();

                int contact1 = myDBHandler.getContact1();
                int contact2 = myDBHandler.getContact2();
                String string1 = Integer.toString(contact1);
                String string2 = Integer.toString(contact2);
                String string11 = ("+91") + Integer.toString(contact1);
                String string22 = ("+91") + Integer.toString(contact2);

                Toast.makeText(context, "Params" + string1 + string2 + string11 + string22, Toast.LENGTH_LONG).show();


//                if ((string1.equals(msgSender) | string11.equals(msgSender)) | (string2.equals(msgSender) | string22.equals(msgSender))) {
                    if (msgBody.equals("helo")) {
                        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();

                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        final Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                        r.play();

                    }
//                }


            } catch (NullPointerException e) {
                Toast.makeText(context, (CharSequence) "Null Pointer Exception", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
